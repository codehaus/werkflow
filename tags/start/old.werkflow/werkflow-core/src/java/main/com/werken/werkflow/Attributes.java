package com.werken.werkflow;

public interface Attributes
{
    void setAttribute(String key,
                      Object value);

    Object getAttribute(String key);

    String[] getAttributeNames();
}
