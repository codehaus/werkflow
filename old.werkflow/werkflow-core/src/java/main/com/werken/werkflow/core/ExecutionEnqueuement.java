package com.werken.werkflow.core;

import com.werken.werkflow.service.messaging.Message;

import java.util.Map;
import java.util.HashMap;

class ExecutionEnqueuement
    implements Runnable
{
    private Executor executor;
    private CoreActivity activity;

    ExecutionEnqueuement( Executor executor,
                          CoreActivity activity )
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

    /*
    public void run()
    {
        System.err.println( "RUNNING " + this );
        getExecutor().execute( getActivity() );
    }
    */

    public void run()
    {
        Map otherAttrs = new HashMap();

        Message message = getActivity().getMessage();

        if ( message != null )
        {
            otherAttrs.put( "message",
                            message.getMessage() );
        }

        Completion completion = new CoreCompletion( getExecutor(),
                                                    getActivity() );

        CoreActionInvocation invocation = new CoreActionInvocation( getActivity(),
                                                                    otherAttrs,
                                                                    completion );
        activity.perform( invocation );
    }

}
