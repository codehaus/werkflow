package org.codehaus.werkflow.drools;

public interface Request
{
    String getId();
    Object get(String id);
}
