package com.werken.werkflow.engine;

import com.werken.werkflow.NoSuchCaseException;
import com.werken.werkflow.NoSuchProcessException;
import com.werken.werkflow.action.Action;
import com.werken.werkflow.activity.Activity;
import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.engine.ProcessDeployment;
import com.werken.werkflow.definition.petri.Place;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.definition.petri.Arc;
import com.werken.werkflow.definition.petri.Expression;
import com.werken.werkflow.definition.petri.NoSuchTransitionException;
import com.werken.werkflow.task.Task;
import com.werken.werkflow.task.ResourceSpec;
import com.werken.werkflow.resource.ResourceClass;
import com.werken.werkflow.resource.NoSuchResourceClassException;
import com.werken.werkflow.work.WorkItem;

import EDU.oswego.cs.dl.util.concurrent.LinkedQueue;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class ActivityManager
{
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private WorkflowEngine engine;
    private Set activities;

    private PooledExecutor pool;
    private LinkedQueue queue;

    public ActivityManager(WorkflowEngine engine)
    {
        this.engine     = engine;
        // this.cases      = new LinkedList();
        this.activities = new HashSet();

        this.queue = new LinkedQueue();

        this.pool = new PooledExecutor( this.queue );
        this.pool.setMinimumPoolSize( 2 );
        this.pool.setKeepAliveTime( -1 );
        this.pool.waitWhenBlocked();
        this.pool.createThreads( 2 );
    }

    private WorkflowEngine getEngine()
    {
        return this.engine;
    }

    public void scheduleCase(final WorkflowProcessCase processCase)
    {
        Transition[] transitions = processCase.getEnabledTransitions();

        boolean schedulable = false;

      TASKS_LOOP:
        for ( int i = 0 ; i < transitions.length ; ++i )
        {
            Task task = transitions[i].getTask();

            ResourceSpec[] resourceSpecs = task.getResourceSpecs();

          SPECS_LOOP:
            for ( int j = 0 ; j < resourceSpecs.length ; ++i )
            {
                String[] resourceClassIds = resourceSpecs[i].getResourceClassIds();

              CLASSES_LOOP:
                for ( int k = 0 ; k < resourceClassIds.length ; ++k )
                {
                    try
                    {
                        if ( engine.getResourceClass( resourceClassIds[k] ).isOffline() )
                        {
                            // requires an offline resource, triggerable
                            // at some later point via a WorkItem.
                            continue TASKS_LOOP;
                        }
                    }
                    catch (NoSuchResourceClassException e)
                    {
                        // FIXME: log it
                        continue TASKS_LOOP;
                    }
                } 
            } 

            // requires no offline resources, so may be scheduled
            // to possibly be executed automatically.
            schedulable = true;
            break;
        } 

        if ( ! schedulable )
        {
            return;
        }

        try
        {
            this.queue.put( new Runnable()
                {
                    public void run()
                    {
                        Transition[] enabledTrans = processCase.getEnabledTransitions();
                        
                        if ( enabledTrans.length == 0 )
                        {
                            return;
                        }
                        
                        Transition nextTrans = enabledTrans[0];
                        
                        fire( processCase,
                              nextTrans );
                    }
                }
                            
                            );
        }
        catch (InterruptedException e)
        {
            // swallow
        }

        /*
        synchronized ( this.cases )
        {
            this.cases.addLast( processCase );
            notifyAll();
        }
        */
    }

    /*
    public void caseExecutorLoop()
        throws InterruptedException
    {
        WorkflowProcessCase processCase = getNextCase();

        Transition[] enabledTrans = processCase.getEnabledTransitions();

        if ( enabledTrans.length == 0 )
        {
            return;
        }

        Transition nextTrans = enabledTrans[0];

        fire( processCase,
              nextTrans );
    }
    */

    protected void verify(WorkflowProcessCase processCase,
                          Transition transition)
    {
        // FIXME: implement
    }

    protected String[] satisfy(WorkflowProcessCase processCase,
                               Transition transition)
    {
        return transition.getActivationRule().satisfy( transition,
                                                       processCase );
    }

    protected Activity fire(WorkflowProcessCase processCase,
                            Transition transition)
    {
        verify( processCase,
                transition);

        String[] placeIds = satisfy( processCase,
                                     transition );
        
        return fire( processCase,
                     transition,
                     placeIds );
    }

    protected Activity fire(WorkItem workItem)
        throws Exception
    {
        WorkflowProcessCase processCase = getEngine().getProcessCase( workItem.getCaseId() );

        Transition[] enabledTrans = processCase.getEnabledTransitions();

        String transId = workItem.getTransitionId();

        for ( int i = 0 ; i < enabledTrans.length ; ++i )
        {
            if ( enabledTrans[i].getId().equals( transId ) )
            {
                return fire( processCase,
                             enabledTrans[i] );
            }
        }

        return null;
    }

    protected Activity fire(WorkflowProcessCase processCase,
                            Transition transition,
                            String[] placeIds)
    {
        Task task = transition.getTask();
        
        WorkflowActivity activity = newActivity( processCase.getId(),
                                                 transition.getId(),
                                                 placeIds );
        
        if ( task == null )
        {
            complete( activity );
        }
        else
        {
            fire( activity,
                  processCase,
                  task );
        }
            
        try
        {
            getEngine().evaluateCase( processCase );
        }
        catch (NoSuchProcessException e)
        {
            // FIXME
            e.printStackTrace();
        }

        return activity;
    }

    protected void fire(WorkflowActivity activity,
                        WorkflowProcessCase processCase,
                        Task task)
    {
        Action action = task.getAction();

        try
        {
            action.perform( activity,
                            processCase );

            activity.complete();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            activity.completeWithError( e );
        }
    }


    protected void complete(WorkflowActivity activity)
    {
        try
        {
            WorkflowProcessCase processCase = getEngine().getProcessCase( activity.getCaseId() );
            
            ProcessDeployment deployment = getEngine().getProcessDeployment( processCase.getProcessInfo().getId() );
            
            ProcessDefinition processDef = deployment.getProcessDefinition();
            
            Transition transition = processDef.getNet().getTransitionById( activity.getTransitionId() );
            
            Arc[] arcs = transition.getArcsToPlaces();
            
            for ( int i = 0 ; i < arcs.length ; ++i )
            {
                Expression expr = arcs[i].getExpression();
                
                if ( expr != null )
                {
                    try
                    {
                        if ( ! expr.evaluate( processCase ) )
                        {
                            continue;
                        }
                    }
                    catch (Exception e)
                    {
                        // FIXME
                        e.printStackTrace();
                    }
                }
                
                Place place = arcs[i].getPlace();
                
                processCase.addMark( place.getId() );
            }

            this.activities.remove( activity );

            getEngine().evaluateCase( processCase );
        }
        catch (NoSuchTransitionException e)
        {
            // FIXME
            e.printStackTrace();
        }
        catch (NoSuchProcessException e)
        {
            // FIXME
            e.printStackTrace();
        }
        catch (NoSuchCaseException e)
        {
            // FIXME
            e.printStackTrace();
        }
    }

    protected void completeWithError(WorkflowActivity activity,
                                     Throwable error)
    {
        try
        {
            WorkflowProcessCase processCase = getEngine().getProcessCase( activity.getCaseId() );
            
            this.activities.remove( activity );
            
            String[] placeIds = activity.getPlaceIds();
            
            
            for ( int i = 0 ; i < placeIds.length ; ++i )
            {
                processCase.addMark( placeIds[i] );
            }

            getEngine().evaluateCase( processCase );
        }
        catch (NoSuchCaseException e)
        {
            // FIXME
            e.printStackTrace();
        }
        catch (NoSuchProcessException e)
        {
            // FIXME
            e.printStackTrace();
        }
    }

    protected WorkflowActivity newActivity(String caseId,
                                           String transitionId,
                                           String[] placeIds)
    {
        WorkflowActivity activity = new WorkflowActivity( this,
                                                          caseId,
                                                          transitionId,
                                                          placeIds );

        this.activities.add( activity );

        return activity;
    }

    protected WorkflowActivity getActivity(String caseId,
                                           String transitionId)
    {
        Iterator activityIter = this.activities.iterator();
        WorkflowActivity eachActivity = null;

        while ( activityIter.hasNext() )
        {
            eachActivity = (WorkflowActivity) activityIter.next();

            if ( eachActivity.getCaseId().equals( caseId )
                 &&
                 eachActivity.getTransitionId().equals( transitionId ) )
            {
                return eachActivity;
            }
        }

        return null;
    }

    /*
    protected WorkflowProcessCase getNextCase()
        throws InterruptedException
    {
        synchronized ( this.cases )
        {
            while ( this.cases.isEmpty() )
            {
                this.cases.wait();
            }

            return (WorkflowProcessCase) this.cases.removeFirst();
        }
    }
    */

    Activity[] getActivitiesForProcessCase(String caseId)
    {
        List caseActivities = new ArrayList();

        Iterator activityIter = this.activities.iterator();
        WorkflowActivity eachActivity = null;

        while ( activityIter.hasNext() )
        {
            eachActivity = (WorkflowActivity) activityIter.next();

            if ( eachActivity.getCaseId().equals( caseId ) )
            {
                caseActivities.add( eachActivity );
            }
        }

        return (Activity[]) caseActivities.toArray( Activity.EMPTY_ARRAY );
    }
}
