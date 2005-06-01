package org.codehaus.werkflow;

import org.codehaus.werkflow.helpers.SimpleInstanceManager;
import org.codehaus.werkflow.helpers.SimplePersistenceManager;
import org.codehaus.werkflow.helpers.SimpleSatisfactionManager;
import org.codehaus.werkflow.helpers.SimpleWorkflowManager;
import org.codehaus.werkflow.helpers.ThreadPoolScheduler;
import org.codehaus.werkflow.spi.AsyncComponent;
import org.codehaus.werkflow.spi.Component;
import org.codehaus.werkflow.spi.InstanceManager;
import org.codehaus.werkflow.spi.Path;
import org.codehaus.werkflow.spi.PersistenceManager;
import org.codehaus.werkflow.spi.RobustInstance;
import org.codehaus.werkflow.spi.RobustTransaction;
import org.codehaus.werkflow.spi.Satisfaction;
import org.codehaus.werkflow.spi.SatisfactionManager;
import org.codehaus.werkflow.spi.SatisfactionValues;
import org.codehaus.werkflow.spi.Satisfier;
import org.codehaus.werkflow.spi.Scheduler;
import org.codehaus.werkflow.spi.SyncComponent;
import org.codehaus.werkflow.spi.WorkflowManager;
import org.codehaus.werkflow.spi.ErrorHandler;

public class Engine
{
    private static final ThreadLocal threadEngine = new ThreadLocal();

    private PersistenceManager persistenceManager;

    private WorkflowManager workflowManager;

    private SatisfactionManager satisfactionManager;

    private InstanceManager instanceManager;

    private Locker locker;

    private Scheduler scheduler;

    private ErrorHandler errorHandler;

    private boolean started = false;

    private int transactionCounter;

    // ----------------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------------

    public Engine()
    {
        this.locker = new Locker();
    }

    // ----------------------------------------------------------------------
    // Public interface for wiring up components
    // ----------------------------------------------------------------------

    public void setPersistenceManager(PersistenceManager persistenceManager)
    {
        assertNotStarted();

        this.persistenceManager = persistenceManager;
    }

    public PersistenceManager getPersistenceManager()
    {
        return this.persistenceManager;
    }

    public void setWorkflowManager(WorkflowManager workflowManager)
    {
        assertNotStarted();

        this.workflowManager = workflowManager;
    }

    public WorkflowManager getWorkflowManager()
    {
        return this.workflowManager;
    }

    public void setSatisfactionManager(SatisfactionManager satisfactionManager)
    {
        assertNotStarted();

        this.satisfactionManager = satisfactionManager;
    }

    public SatisfactionManager getSatisfactionManager()
    {
        return this.satisfactionManager;
    }

    public void setInstanceManager(InstanceManager instanceManager)
    {
        assertNotStarted();

        this.instanceManager = instanceManager;
    }

    public InstanceManager getInstanceManager()
    {
        return this.instanceManager;
    }

    private Locker getLocker()
    {
        return this.locker;
    }

    public void setScheduler(Scheduler scheduler)
    {
        assertNotStarted();

        this.scheduler = scheduler;
    }

    public Scheduler getScheduler()
    {
        return this.scheduler;
    }

    public ErrorHandler getErrorHandler()
    {
        return errorHandler;
    }

    public void setErrorHandler( ErrorHandler errorHandler )
    {
        this.errorHandler = errorHandler;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public void start()
    {
        if ( this.persistenceManager == null )
        {
            this.persistenceManager = new SimplePersistenceManager();
        }

        if ( this.workflowManager == null )
        {
            this.workflowManager = new SimpleWorkflowManager();
        }

        if ( this.satisfactionManager == null )
        {
            this.satisfactionManager = new SimpleSatisfactionManager();
        }

        if ( this.instanceManager == null )
        {
            this.instanceManager = new SimpleInstanceManager();
        }

        if ( this.scheduler == null )
        {
            this.scheduler = new ThreadPoolScheduler( 4 );
        }

        this.scheduler.start( this );

        started = true;
    }

    public boolean isStarted()
    {
        return started;
    }

    public void stop()
    {
        this.scheduler.stop( this );

        started = false;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private void assertNotStarted()
    {
        if ( isStarted() )
        {
            throw new IllegalStateException( "engine started" );
        }
    }

    void newInstance(RobustTransaction transaction,
                     String workflowId,
                     String instanceId,
                     InitialContext initialContext)
        throws NoSuchWorkflowException, DuplicateInstanceException, Exception
    {
        if ( getInstanceManager().hasInstance( instanceId ) )
        {
            throw new DuplicateInstanceException( instanceId );
        }

        Workflow workflow = getWorkflowManager().getWorkflow( workflowId );

        RobustInstance instance = getInstanceManager().newInstance( workflow,
                                                                    instanceId,
                                                                    initialContext );

        start( instance );

        transaction.addNewInstanceId( instance.getId() );
    }

    void evaluateInstance(String id)
        throws NoSuchInstanceException, Exception
    {
        if ( ! getInstanceManager().hasInstance( id ) )
        {
            throw new NoSuchInstanceException( id );
        }

        // This is okay to be non-transactional
        //
        // We're only inspecting recently-added instances to
        // see if we can't get'em into the quee to do some work.

        RobustInstance instance = getInstanceManager().getInstance( id );

        Path nextPath = instance.getNextPath();

        if ( nextPath != null )
        {
            enqueue( instance.getId(),
                     nextPath );
        }

        /*
        Path[] leaves = instance.getLeaves();

        for ( int i = 0 ; i < leaves.length ; ++i )
        {
            Component component = instance.getWorkflow().getComponent( leaves[ i ] );

            if ( component instanceof Satisfaction )
            {
                enqueue( instance,
                         leaves[ i ] );
            }
        }
        */
    }

    void enqueue(String instanceId,
                 Path path)
    {
        this.scheduler.enqueue( new InstanceTask( instanceId,
                                                  path ) );
    }

    public void run(InstanceTask task)
        throws NoSuchInstanceException, InterruptedException
    {
        initializeThread();

        RobustTransaction transaction = beginInternalTransaction( task.getInstanceId() );

        try
        {
            transaction.run( task.getPath() );

            getPersistenceManager().commitTransaction();
            //getEngine().getPersistenceManager().beginTransaction();

            transaction.commit();

            //getEngine().getPersistenceManager().commitTransaction();
        }
        catch (Exception e)
        {
            handleError( e );
        }
        finally
        {
            if ( getPersistenceManager().isTransactionActive() )
            {
                getPersistenceManager().rollbackTransaction();
            }

            if ( transaction.isOpen() )
            {
                transaction.rollback();
            }
        }
    }

    void handleError( Throwable throwable )
    {
        if ( errorHandler != null )
        {
            errorHandler.handle( throwable );
        }
        else
        {
            // ----------------------------------------------------------------------
            // At least throw the error out on the console
            // ----------------------------------------------------------------------

            throwable.printStackTrace();
        }
    }

    void run(RobustTransaction transaction,
             String instanceId,
             Path path)
        throws Exception
    {
        RobustInstance instance = getInstanceManager().getInstance( instanceId );

        Workflow workflow = instance.getWorkflow();

        Component component = workflow.getComponent( path );

        if ( component instanceof Workflow )
        {
            runWorkflow( transaction,
                         instance,
                         path );
        }
        else if ( component instanceof Satisfaction )
        {
            runSatisfaction( transaction,
                             instance,
                             (Satisfaction) component );
        }
        else if ( component instanceof Satisfier )
        {
            runSatisfier( transaction,
                          instance,
                          path,
                          (Satisfier) component );
        }
        else if ( component instanceof AsyncComponent )
        {
            runAsyncComponent( transaction,
                               instance,
                               path,
                               (AsyncComponent) component );
        }
        else if ( component instanceof SyncComponent )
        {

            runSyncComponent( transaction,
                              instance,
                              path,
                              (SyncComponent) component );
        }

        instance.dequeue( path );
    }

    void runWorkflow(RobustTransaction transaction,
                     RobustInstance instance,
                     Path path)
    {
        start( instance,
               path.childPath( 0 ) );

    }

    void runSatisfaction(RobustTransaction transaction,
                         RobustInstance instance,
                         Satisfaction satisfaction)
        throws NoSuchInstanceException, Exception
    {
        if ( getSatisfactionManager().isSatisfied( transaction,
                                                   satisfaction.getId(),
                                                   instance,
                                                   new EngineSatisfactionCallback( this,
                                                                                   instance.getId(),
                                                                                   satisfaction.getId() ) ) )
        {
            enqueueSatisfier( transaction,
                              instance.getId(),
                              satisfaction.getId() );
        }
    }

    void runSatisfier(RobustTransaction transaction,
                      RobustInstance instance,
                      Path path,
                      Satisfier satisfier)
        throws NoSuchInstanceException, Exception
    {
        Workflow workflow = instance.getWorkflow();

        Satisfaction satisfaction = (Satisfaction) workflow.getComponent( path.parentPath() );

        SatisfactionValues values = getSatisfactionManager().getSatisfactionValues( satisfaction.getId(),
                                                                                    instance );
        try
        {
            satisfier.perform( instance,
                               values );
        }
        catch (Exception e)
        {
            handleError( e );
        }

        end( instance,
             path );
    }

    void runAsyncComponent(RobustTransaction transaction,
                           RobustInstance instance,
                           Path path,
                           AsyncComponent asyncComponent)
    {
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

    void runSyncComponent(RobustTransaction transaction,
                          RobustInstance instance,
                          Path path,
                          SyncComponent syncComponent)
        throws Exception
    {
            syncComponent.perform( instance );

        end( instance,
             path );
    }

    void satisfy(EngineTransaction transaction,
                 String instanceId,
                 String satisfactionId,
                 SatisfactionValues values)
        throws Exception
    {
        getSatisfactionManager().satisfy( transaction,
                                          instanceId,
                                          satisfactionId,
                                          values );
    }

    void enqueueSatisfier(RobustTransaction transaction,
                          String instanceId,
                          String satisfactionId)
        throws NoSuchInstanceException, Exception
    {
        if ( ! getInstanceManager().hasInstance( instanceId ) )
        {
            throw new NoSuchInstanceException( instanceId );
        }

        RobustInstance instance = getInstanceManager().getInstance( instanceId );

        //System.err.println( "added pending: " + satisfactionId );
        instance.addPendingSatisfactionId( satisfactionId );

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

        transaction.addSatisfiedInstanceId( instanceId );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    void start(RobustInstance instance)
    {
        start( instance,
               new Path() );
    }

    void start(RobustInstance instance,
               Path path)
    {
        instance.push( path );
        instance.enqueue( path );
    }

    void start(RobustInstance instance,
               Path[] paths)
    {
        instance.push( paths );

        for ( int i = 0 ; i < paths.length ; ++i )
        {
            instance.enqueue( paths[ i ] );
        }
    }

    void end(RobustInstance instance,
             Path path)
        //throws InterruptedException
    {
        Workflow workflow = instance.getWorkflow();

        Path parentPath = path.parentPath();

        if ( parentPath == null )
        {
            instance.setComplete( true );
            return;
        }

        Component self = workflow.getComponent( path );

        if ( self instanceof Satisfaction )
        {
            //System.err.println( "remove pending: " + ((Satisfaction)self).getId() );
            instance.removePendingSatisfactionId( ((Satisfaction)self).getId() );
        }

        Component parent = (AsyncComponent) workflow.getComponent( parentPath );

        if ( parent instanceof AsyncComponent )
        {
            Path nextPath = ((AsyncComponent)parent).endChild( instance,
                                                               path );

            while ( nextPath.equals( AsyncComponent.NONE ) )
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
            if ( nextPath.equals( AsyncComponent.SELF ) )
            {
                instance.pop( path );
                instance.pop( path.parentPath() );
                start( instance,
                       path.parentPath() );
            }
            else if ( nextPath.equals( AsyncComponent.DEFER) )
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
            else if ( nextPath.equals( AsyncComponent.NONE ) )
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

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public synchronized Transaction beginTransaction(String workflowId,
                                                     String instanceId,
                                                     InitialContext initialContext)
        throws NoSuchWorkflowException, DuplicateInstanceException, InterruptedException, Exception
    {
        if ( getInstanceManager().hasInstance( instanceId ) )
        {
            throw new DuplicateInstanceException( instanceId );
        }

        RobustTransaction transaction = newTransaction( instanceId );

        try
        {
            transaction.newInstance( workflowId,
                                     instanceId,
                                     initialContext );
        }
        catch (Exception e)
        {
            transaction.rollback();
            throw e;
        }

        return transaction;
    }

    public synchronized Transaction beginTransaction(String instanceId)
        throws NoSuchInstanceException, InterruptedException, Exception
    {
        if ( ! getInstanceManager().hasInstance( instanceId ) )
        {
            throw new NoSuchInstanceException( instanceId );
        }

        RobustTransaction transaction = newTransaction( instanceId );

        return transaction;
    }

    synchronized RobustTransaction beginInternalTransaction(String instanceId)
        throws NoSuchInstanceException, InterruptedException
    {
        if ( ! getInstanceManager().hasInstance( instanceId ) )
        {
            throw new NoSuchInstanceException( instanceId );
        }

        return newTransaction( instanceId );
    }

    protected synchronized RobustTransaction instantiateTransaction(String transactionId,
                                                                    String instanceId)
    {
        return new EngineTransaction( this,
                                      transactionId,
                                      instanceId );
    }

    synchronized RobustTransaction newTransaction(String instanceId)
        throws InterruptedException
    {
        RobustTransaction transaction = instantiateTransaction( ++this.transactionCounter + "",
                                                                instanceId );

        transaction.begin();

        return transaction;
    }

    void begin(RobustTransaction transaction)
        throws InterruptedException
    {
        acquireLock( transaction );
    }

    void commit(RobustTransaction transaction)
    {
        try
        {
            evaluateInstance( transaction.getInstanceId() );

            String[] newInstanceIds = transaction.getNewInstanceIds();

            for ( int i = 0 ; i < newInstanceIds.length ; ++i )
            {
                try
                {
                    if ( ! newInstanceIds[ i ].equals( transaction.getInstanceId() ) )
                    {
                        evaluateInstance( newInstanceIds[ i ] );
                    }

                }
                catch (Exception e)
                {
                    handleError( e );
                }
            }

            String[] satisfiedInstanceIds = transaction.getSatisfiedInstanceIds();

            for ( int i = 0 ; i < satisfiedInstanceIds.length ; ++i )
            {
                try
                {
                    if ( ! satisfiedInstanceIds[ i ].equals( transaction.getInstanceId() ) )
                    {
                        evaluateInstance( satisfiedInstanceIds[ i ] );
                    }

                }
                catch (Exception e)
                {
                    handleError( e );
                }
            }
        }
        catch (Exception e)
        {
            handleError( e );
        }
        finally
        {
            dropLock( transaction );
        }
    }

    void rollback(RobustTransaction transaction)
    {
        try
        {
            // nothing
        }
        finally
        {
            dropLock( transaction );
        }
    }

    void acquireLock(Transaction transaction)
        throws InterruptedException
    {
        getLocker().acquireLock( transaction.getId(),
                                 transaction.getInstanceId() );
    }

    void dropLock(Transaction transaction)
    {
        getLocker().dropLock( transaction.getId(),
                              transaction.getInstanceId() );
    }

    public void initializeThread()
    {
        setThreadEngine( this );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static Engine getThreadEngine()
    {
        return (Engine) threadEngine.get();
    }

    static void setThreadEngine(Engine engine)
    {
        threadEngine.set( engine );
    }
}
