package com.werken.werkflow;

public interface MutableAttributes
    extends Attributes
{
    void setAttribute(String id,
                      Object value);
}
