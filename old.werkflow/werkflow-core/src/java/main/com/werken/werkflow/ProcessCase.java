package com.werken.werkflow;

public interface ProcessCase
{
    String getId();

    ProcessInfo getProcessInfo();

    Object getAttribute(String name);

    String[] getAttributeNames();

    boolean hasAttribute(String key);
}
