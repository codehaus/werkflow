package com.werken.werkflow.work;

public interface Action
{
    void perform(ActionInvocation invocation)
        throws Exception;
}
