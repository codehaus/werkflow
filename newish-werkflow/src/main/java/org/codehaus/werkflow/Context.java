package org.codehaus.werkflow;

public interface Context
{
    String getId();
    Object get(String id);
}
