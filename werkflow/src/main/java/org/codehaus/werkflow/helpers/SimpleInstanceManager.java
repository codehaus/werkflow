package org.codehaus.werkflow.helpers;

import org.codehaus.werkflow.Workflow;
import org.codehaus.werkflow.InitialContext;
import org.codehaus.werkflow.ReadOnlyInstance;
import org.codehaus.werkflow.NoSuchInstanceException;
import org.codehaus.werkflow.DuplicateInstanceException;
import org.codehaus.werkflow.spi.InstanceManager;
import org.codehaus.werkflow.spi.RobustInstance;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class SimpleInstanceManager
    implements InstanceManager
{
    private Map instances;

    public RobustInstance newInstance(Workflow workflow,
                                      String id,
                                      InitialContext initialContext)
        throws DuplicateInstanceException, Exception
    {
        RobustInstance instance = new RobustInstance( workflow,
                                                      id,
                                                      initialContext );

        this.instances.put( id,
                            instance );

        return instance;
    }

    public RobustInstance getInstance(String id)
        throws NoSuchInstanceException, Exception
    {
        return (RobustInstance) this.instances.get( id );
    }

    public boolean hasInstance(String id)
    {
        return this.instances.containsKey( id );
    }

    public ReadOnlyInstance getReadOnlyInstance(String id)
        throws NoSuchInstanceException, Exception
    {
        return (ReadOnlyInstance) this.instances.get( id );
    }

    public ReadOnlyInstance[] getReadOnlyInstances(String workflowId)
        throws Exception
    {
        Set instances = new HashSet();

        for ( Iterator instanceIter = this.instances.values().iterator();
              instanceIter.hasNext(); )
        {
            RobustInstance instance = (RobustInstance) instanceIter.next();

            if ( instance.getWorkflowId().equals( workflowId ) )
            {
                instances.add( instance );
            }
        }

        return (ReadOnlyInstance[]) instances.toArray( new ReadOnlyInstance[ instances.size() ] );
    }
    

}
