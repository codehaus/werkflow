package com.werken.werkflow.engine;

import com.werken.werkflow.resource.ResourceClass;
import com.werken.werkflow.resource.DuplicateResourceClassException;
import com.werken.werkflow.resource.NoSuchResourceClassException;

import java.util.Map;
import java.util.HashMap;

public class ResourceManager
{
    private WorkflowEngine engine;
    private Map resourceClasses;

    public ResourceManager(WorkflowEngine engine)
    {
        this.engine = engine;
        this.resourceClasses = new HashMap();
    }

    private WorkflowEngine getEngine()
    {
        return this.engine;
    }

    void addResourceClass(ResourceClass resourceClass)
        throws DuplicateResourceClassException
    {
        if ( this.resourceClasses.containsKey( resourceClass.getId() ) )
        {
            throw new DuplicateResourceClassException( resourceClass.getId() );
        }
        
        this.resourceClasses.put( resourceClass.getId(),
                                  resourceClass );
    }

    ResourceClass getResourceClass(String id)
        throws NoSuchResourceClassException
    {
        ResourceClass resourceClass = (ResourceClass) this.resourceClasses.get( id );

        if ( resourceClass == null )
        {
            throw new NoSuchResourceClassException( id );
        }

        return resourceClass;
    }
}
