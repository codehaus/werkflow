package org.codehaus.werkflow;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

import java.util.Timer;
import java.util.TimerTask;

public abstract class Engine
{
    private static ThreadLocal threadEngine = new ThreadLocal();

    private Timer timer;
    private Map workflows;
    private SatisfactionManager satisfactionManager;
    private InstanceManager instanceManager;

    public Engine()
    {
        this.workflows = new HashMap();
        this.timer     = new Timer();
    }

    public Engine(InstanceManager instanceManager,
                  SatisfactionManager satisfactionManager)
    {
        this();
        setInstanceManager( instanceManager );
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

    public synchronized Instance getInstance(String id)
        throws NoSuchInstanceException, Exception
    {
        RobustInstance instance = getInstanceManager().getInstance( id );


        return instance;
    }

    public synchronized Instance newInstance(String workflowId,
                                             String id,
                                             InitialContext context)
        throws NoSuchWorkflowException, DuplicateInstanceException, Exception
    {
        Workflow workflow = getWorkflow( workflowId );

        RobustInstance instance = getInstanceManager().newInstance( workflow,
                                                                    id,
                                                                    context );

        start( instance,
               new Path() );

        return instance;
    }

    protected abstract void enqueue(RobustInstance instance,
                                    Path path)
        throws InterruptedException;

    protected void run(RobustInstance instance,
                       Path path)
        throws Exception
    {
        //System.err.println( "run(" + path + ")" );

        synchronized ( instance )
        {
            setThreadEngine( this );
            boolean threw = false;

            try
            {
                getInstanceManager().startTransaction( instance );

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
            catch (Error e)
            {
                threw = true;
                throw e;
            }
            catch (Exception e)
            {
                threw = true;
                throw e;
            }
            finally
            {
                if ( threw )
                {
                    getInstanceManager().abortTransaction( instance );
                }
                else
                {
                    instance.dequeue( path );
                    getInstanceManager().commitTransaction( instance );
                }
            }
        }
    }

    void start(RobustInstance instance,
               Path path)
        throws InterruptedException
    {
        //System.err.println( "start(" + path + ")" );
        instance.push( path );
        enqueue( instance,
                 path );
    }

    void start(RobustInstance instance,
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

    void end(RobustInstance instance,
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

    public void satisfy(String satisfactionId,
                        String instanceId)
        throws NoSuchInstanceException, InterruptedException, Exception
    {
        RobustInstance instance = (RobustInstance) getInstance( instanceId );

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

    public void setInstanceManager(InstanceManager instanceManager)
    {
        this.instanceManager = instanceManager;
    }

    public InstanceManager getInstanceManager()
    {
        return this.instanceManager;
    }

    public void setSatisfactionManager(SatisfactionManager satisfactionManager)
    {
        this.satisfactionManager = satisfactionManager;
    }

    public SatisfactionManager getSatisfactionManager()
    {
        return this.satisfactionManager;
    }

    public static void setThreadEngine(Engine engine)
    {
        threadEngine.set( engine );
    }

    public static Engine getThreadEngine()
    {
        return (Engine) threadEngine.get();
    }
}
