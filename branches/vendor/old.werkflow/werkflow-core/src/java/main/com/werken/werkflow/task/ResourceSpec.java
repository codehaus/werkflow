package com.werken.werkflow.task;

import java.util.Set;
import java.util.HashSet;

public class ResourceSpec
{
    public static final ResourceSpec[] EMPTY_ARRAY = new ResourceSpec[0];
    
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private Set resourceClassIds;

    public ResourceSpec()
    {
        this.resourceClassIds = new HashSet();
    }

    public void addResourceClassId(String resourceClassId)
    {
        this.resourceClassIds.add( resourceClassId );
    }

    public String[] getResourceClassIds()
    {
        return (String[]) this.resourceClassIds.toArray( EMPTY_STRING_ARRAY );
    }
}
