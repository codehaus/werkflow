package com.werken.werkflow.core;

class ExecutionEnqueuement
    implements Runnable
{
    private Executor executor;
    private CoreActivity activity;

    ExecutionEnqueuement(Executor executor,
                         CoreActivity activity)
    {
        this.executor = executor;
        this.activity = activity;
    }

    Executor getExecutor()
    {
        return this.executor;
    }

    CoreActivity getActivity()
    {
        return this.activity;
    }

    public void run()
    {
        getExecutor().execute( getActivity() );
    }
}
