package com.werken.werkflow.action;

import com.werken.werkflow.ProcessCase;

public interface MutableProcessCase
    extends ProcessCase
{
    void setAttribute(String id,
                      Object value);

    void clearAttribute(String id);
}
