package org.codehaus.werkflow.nonpersistent;

import org.codehaus.werkflow.*;
import org.codehaus.werkflow.spi.*;

import java.util.Map;
import java.util.HashMap;

public class NonPersistentInstanceManager
    implements InstanceManager
{
    private Map instances;

    public NonPersistentInstanceManager()
    {
        this.instances = new HashMap();
    }

    public synchronized RobustInstance newInstance(Workflow workflow,
                                                   String id,
                                                   InitialContext initialContext)
        throws DuplicateInstanceException, Exception
    {
        RobustInstance instance = (RobustInstance) this.instances.get( id );

        if ( instance != null )
        {
            throw new DuplicateInstanceException( instance );
        }

        instance = makeInstance( workflow,
                                 id,
                                 initialContext );


        this.instances.put( id,
                            instance );

        return instance;
    }

    protected RobustInstance makeInstance(Workflow workflow,
                                          String id,
                                          InitialContext initialContext)
        throws Exception
    {
        return new NonPersistentInstance( new DefaultInstance( workflow,
                                                               id,
                                                               initialContext ) );
    }

    public RobustInstance getInstance(String id)
        throws NoSuchInstanceException, Exception
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

        nonPersistentInstance.startTransaction();
    }

    public void commitTransaction(RobustInstance instance)
        throws Exception
    {
        NonPersistentInstance nonPersistentInstance = (NonPersistentInstance) instance;

        nonPersistentInstance.commitTransaction();
    }

    public void abortTransaction(RobustInstance instance)
        throws Exception
    {
        NonPersistentInstance nonPersistentInstance = (NonPersistentInstance) instance;

        nonPersistentInstance.abortTransaction();
    }
}
