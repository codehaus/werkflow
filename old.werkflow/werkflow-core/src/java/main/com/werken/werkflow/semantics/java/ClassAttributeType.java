package com.werken.werkflow.semantics.java;

import com.werken.werkflow.AttributeType;

public class ClassAttributeType
    implements AttributeType
{
    private Class type;

    public ClassAttributeType(Class type)
    {
        this.type = type;
    }

    public boolean isInstance(Object value)
    {
        return this.type.isInstance( value );
    }

    public String getDocumentation()
    {
        return "Java class: " + this.type.getName();
    }
}
