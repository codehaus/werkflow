package org.codehaus.werkflow;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

import java.util.Timer;
import java.util.TimerTask;

public abstract class Engine
{
    private Timer timer;
    private Map workflows;
    private Map instances;
    private SatisfactionManager satisfactionManager;

    public Engine()
    {
        this.workflows = new HashMap();
        this.instances = new HashMap();
        this.timer     = new Timer();
    }

    public Engine(SatisfactionManager satisfactionManager)
    {
        this();
        setSatisfactionManager( satisfactionManager );
    }

    public void addWorkflow(Workflow workflow)
        throws DuplicateWorkflowException
    {
        if ( this.workflows.containsKey( workflow.getId() ) )
        {
            throw new DuplicateWorkflowException( workflow );
        }

        this.workflows.put( workflow.getId(),
                            workflow );
    }

    public Workflow getWorkflow(String id)
        throws NoSuchWorkflowException
    {
        if ( this.workflows.containsKey( id ) )
        {
            return (Workflow) this.workflows.get( id );
        }

        throw new NoSuchWorkflowException( id );
    }

    public Instance getInstance(String id)
        throws NoSuchInstanceException
    {
        if ( ! this.instances.containsKey( id ) )
        {
            throw new NoSuchInstanceException( id );
        }

        return (Instance) this.instances.get( id );
    }

    public Instance newInstance(String workflowId,
                                String id)
        throws NoSuchWorkflowException, InterruptedException
    {
        Workflow workflow = getWorkflow( workflowId );

        Instance instance = new Instance( this,
                                          workflow,
                                          id );

        this.instances.put( id,
                            instance );

        start( instance,
               new Path() );

        return instance;
    }

    protected abstract void enqueue(Instance instance,
                                    Path path)
        throws InterruptedException;

    protected void run(Instance instance,
                       Path path)
        throws InterruptedException
    {
        //System.err.println( "run(" + path + ")" );
        Workflow workflow = instance.getWorkflow();

        Component component = workflow.getComponent( path );

        if ( component instanceof Satisfaction )
        {
            Satisfaction satisfaction = (Satisfaction) component;
            //System.err.println( "## Satisfaction " + satisfaction.getId() );

            if ( getSatisfactionManager().isSatisfied( satisfaction.getId(),
                                                       instance ) ) 
            {
                try
                {
                    satisfy( satisfaction.getId(),
                             instance.getId() );
                }
                catch (NoSuchInstanceException e)
                {
                    throw new AssumptionViolationError( "instance does not have its own id" );
                }
            }
            else
            {
                if ( component instanceof PolledSatisfaction )
                {
                    PolledSatisfaction polledSatisfaction = (PolledSatisfaction) satisfaction;


                    setupSatisfactionPoll( polledSatisfaction,
                                           instance.getId() );
                }
            }
        }
        else if ( component instanceof AsyncComponent )
        {
            //System.err.println( "## AsyncComponent" );

            AsyncComponent asyncComponent = (AsyncComponent) component;

            Path[] nextPaths = asyncComponent.begin( instance,
                                                     path );

            if ( nextPaths != null
                 &&
                 nextPaths.length > 0 )
            {
                start( instance,
                       nextPaths );
            }
            else 
            {
                end( instance,
                     path );
            }
        }
        else if ( component instanceof SyncComponent )
        {
            //System.err.println( "## SyncComponent" );
            SyncComponent syncComponent = (SyncComponent) component;

            try
            {
                syncComponent.perform( instance );
            }
            catch (Exception e)
            {
                // handle error;
            }

            end( instance,
                 path );
        }
        else
        {
            throw new RuntimeException( "Unknown component type <" + component.getClass().getName() + ">" );
        }

        //System.err.println( "end run(" + path + ")" );
    }

    void start(Instance instance,
               Path path)
        throws InterruptedException
    {
        //System.err.println( "start(" + path + ")" );
        instance.push( path );
        enqueue( instance,
                 path );
    }

    void start(Instance instance,
               Path[] paths)
        throws InterruptedException
    {
        //System.err.println( "start(" + Arrays.asList( paths ) + ")" );
        instance.push( paths );

        for ( int i = 0 ; i < paths.length ; ++i )
        {
            enqueue( instance,
                     paths[ i ] );
        }
    }

    void end(Instance instance,
             Path path)
        throws InterruptedException
    {
        //System.err.println( "end " + path );
        Workflow workflow = instance.getWorkflow();

        Path parentPath = path.parentPath();

        if ( parentPath == null )
        {
            instance.setComplete( true );
        }

        Component parent = (AsyncComponent) workflow.getComponent( path.parentPath() );
        
        if ( parent instanceof AsyncComponent )
        {
            Path nextPath = ((AsyncComponent)parent).endChild( instance,
                                                               path );
            
            //System.err.println( parent + " says next path is " + nextPath + " after " + path );

            while ( nextPath == AsyncComponent.NONE )
            {
                instance.pop( path );
                
                path = path.parentPath();

                if ( path == null )
                {
                    return;
                }

                parentPath = path.parentPath();

                if ( parentPath == null )
                {
                    instance.setComplete( true );
                    break;
                }
                
                parent = workflow.getComponent( parentPath );
                
                if ( parent instanceof AsyncComponent )
                {
                    nextPath = ((AsyncComponent)parent).endChild( instance,
                                                                  path );
                }
                else
                {
                    break;
                }
            }
            if ( nextPath == AsyncComponent.SELF )
            {
                instance.pop( path );
                instance.pop( path.parentPath() );
                start( instance,
                       path.parentPath() );
            }
            else if ( nextPath == AsyncComponent.DEFER)
            {
                instance.pop( path );
            }
            else if ( nextPath.parentPath() != null
                      &&
                      nextPath.parentPath().equals( path.parentPath() ) )
            {
                instance.pop( path );
                start( instance,
                       nextPath );
            }
            else if ( nextPath == AsyncComponent.NONE )
            {
                // nothing in particular
            }
            else
            {
                start( instance,
                       nextPath );
            }
        }
    }

    public void satisfy(String satisfactionId,
                        String instanceId)
        throws NoSuchInstanceException, InterruptedException
    {
        Instance instance = getInstance( instanceId );

        Workflow workflow = instance.getWorkflow();

        Path satisfactionPath = workflow.getSatisfactionPath( satisfactionId );

        Satisfaction satisfaction = (Satisfaction) workflow.getComponent( satisfactionPath );

        Path[] nextPaths = satisfaction.begin( instance,
                                               satisfactionPath );

        if ( nextPaths != null
             &&
             nextPaths.length > 0 )
        {
            for ( int i = 0 ; i < nextPaths.length ; ++i )
            {
                start( instance,
                       nextPaths[ i ] );
            }
        }
        else
        {
            end( instance,
                 satisfactionPath );
        }
    }

    protected abstract void setupSatisfactionPoll(final PolledSatisfaction satisfaction,
                                                  final String instanceId);

    public void setSatisfactionManager(SatisfactionManager satisfactionManager)
    {
        this.satisfactionManager = satisfactionManager;
    }

    public SatisfactionManager getSatisfactionManager()
    {
        return this.satisfactionManager;
    }
    
}
