package org.codehaus.werkflow.jdo;

import org.codehaus.werkflow.Workflow;
import org.codehaus.werkflow.Engine;
import org.codehaus.werkflow.ReadOnlyInstance;
import org.codehaus.werkflow.InitialContext;
import org.codehaus.werkflow.DuplicateInstanceException;
import org.codehaus.werkflow.NoSuchInstanceException;
import org.codehaus.werkflow.spi.InstanceManager;
import org.codehaus.werkflow.spi.RobustInstance;
import org.codehaus.werkflow.spi.RobustInstanceState;
import org.codehaus.werkflow.spi.DefaultReadOnlyInstance;

import java.util.Collection;
import java.util.Iterator;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class JdoInstanceManager
    implements InstanceManager
{
    public JdoInstanceManager()
    {

    }

    public RobustInstance newInstance(Workflow workflow,
                                      String id,
                                      InitialContext initialContext)
        throws DuplicateInstanceException, Exception
    {
        if ( hasInstance( id ) )
        {
            throw new DuplicateInstanceException( id );
        }

        RobustInstance instance = new RobustInstance( workflow,
                                                      id,
                                                      initialContext );

        PersistenceManager pm = JdoEngine.getThreadPersistenceManager();

        pm.makePersistent( instance.getState() );

        return instance;
    }

    public boolean hasInstance(String id)
    {
        PersistenceManager pm = JdoEngine.getThreadPersistenceManager();

        Query query = pm.newQuery( RobustInstanceState.class );

        query.setIgnoreCache( true );

        query.setFilter( "this.id == \"" + id + "\"" );

        Collection results = (Collection) query.execute();

        return ( ! results.isEmpty() );
    }

    public RobustInstance getInstance(String id)
        throws NoSuchInstanceException, Exception
    {
        PersistenceManager pm = JdoEngine.getThreadPersistenceManager();

        Query query = pm.newQuery( RobustInstanceState.class );

        query.setIgnoreCache( true );

        query.setFilter( "this.id == \"" + id + "\"" );

        Collection results = (Collection) query.execute();

        if ( results.isEmpty() )
        {
            throw new NoSuchInstanceException( id );
        }

        RobustInstanceState state = (RobustInstanceState) results.iterator().next();

        if ( pm.currentTransaction().isActive() )
        {
            pm.makeTransactional( state );
        }
        pm.refresh( state );

        state.setWorkflow( Engine.getThreadEngine().getWorkflowManager().getWorkflow( state.getWorkflowId() ) );

        return new RobustInstance( state );
    }

    public ReadOnlyInstance getReadOnlyInstance(String id)
        throws NoSuchInstanceException, Exception
    {
        PersistenceManager pm = JdoEngine.getThreadPersistenceManager();

        Query query = pm.newQuery( RobustInstanceState.class );

        query.setIgnoreCache( true );

        query.setFilter( "this.id == \"" + id + "\"" );

        Collection results = (Collection) query.execute();

        if ( results.isEmpty() )
        {
            throw new NoSuchInstanceException( id );
        }

        RobustInstanceState state = (RobustInstanceState) results.iterator().next();

        if ( pm.currentTransaction().isActive() )
        {
            pm.makeTransactional( state );
        }

        pm.refresh( state );

        state.setWorkflow( Engine.getThreadEngine().getWorkflowManager().getWorkflow( state.getWorkflowId() ) );

        RobustInstance instance = new RobustInstance( state );

        return new DefaultReadOnlyInstance( instance );
    }

    public ReadOnlyInstance[] getReadOnlyInstances(String workflowId)
        throws Exception
    {
        PersistenceManager pm = JdoEngine.getThreadPersistenceManager();

        Query query = pm.newQuery( RobustInstanceState.class );

        query.setIgnoreCache( true );

        if ( workflowId != null )
        {
            query.setFilter( "this.workflowId == \"" + workflowId + "\"" );
        }

        Collection results = (Collection) query.execute();

        ReadOnlyInstance[] instances = new ReadOnlyInstance[ results.size() ];

        Iterator resultIter = results.iterator();
        int      i          = 0;

        while ( resultIter.hasNext() )
        {
            RobustInstanceState state = (RobustInstanceState) resultIter.next();

            if ( pm.currentTransaction().isActive() )
            {
                pm.makeTransactional( state );
            }
            pm.refresh( state );

            state.setWorkflow( Engine.getThreadEngine().getWorkflowManager().getWorkflow( state.getWorkflowId() ) );

            RobustInstance instance = new RobustInstance( state );

            instances[ i ] = new DefaultReadOnlyInstance( instance );

            ++i;
        }

        return instances;
    }
}
