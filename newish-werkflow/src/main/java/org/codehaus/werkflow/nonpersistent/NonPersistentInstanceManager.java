package org.codehaus.werkflow.nonpersistent;

import org.codehaus.werkflow.*;

import java.util.Map;
import java.util.HashMap;

public abstract class NonPersistentInstanceManager
    implements InstanceManager
{
    private Map instances;

    public NonPersistentInstanceManager()
    {
        this.instances = new HashMap();
    }

    public synchronized RobustInstance newInstance(Engine engine,
                                                   Workflow workflow,
                                                   String id,
                                                   Context initialContext)
        throws DuplicateInstanceException
    {
        RobustInstance instance = (RobustInstance) this.instances.get( id );

        if ( instance != null )
        {
            throw new DuplicateInstanceException( instance );
        }

        instance = new NonPersistentInstance( engine,
                                              workflow,
                                              id );

        this.instances.put( id,
                            instance );

        return instance;
    }

    public RobustInstance getInstance(String id)
        throws NoSuchInstanceException
    {
        if ( this.instances.containsKey( id ) )
        {
            return (RobustInstance) this.instances.get( id );
        }

        throw new NoSuchInstanceException( id );
    }

    public void startTransaction(RobustInstance instance)
        throws Exception
    {
        NonPersistentInstance nonPersistentInstance = (NonPersistentInstance) instance;

        //nonPersistentInstance.checkpointTransaction();
    }

    public void commitTransaction(RobustInstance instance)
        throws Exception
    {
        NonPersistentInstance nonPersistentInstance = (NonPersistentInstance) instance;

        //nonPersistentInstance.commitTransaction();
    }

    public void abortTransaction(RobustInstance instance)
        throws Exception
    {
        NonPersistentInstance nonPersistentInstance = (NonPersistentInstance) instance;

        //nonPersistentInstance.abortTransaction();
    }
}
