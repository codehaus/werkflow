package com.werken.werkflow;

public interface AttributeType
{
    boolean isInstance(Object value);

    String getDocumentation();
}
