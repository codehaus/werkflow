package com.werken.werkflow.core;

import com.werken.werkflow.task.Task;
import com.werken.werkflow.service.messaging.Message;

class CoreActivity
{
    static final CoreActivity[] EMPTY_ARRAY = new CoreActivity[0];
    
    private CoreWorkItem workItem;
    private Message message;

    CoreActivity(CoreWorkItem workItem,
                 Message message)
    {
        this.workItem = workItem;
        this.message  = message;
    }

    CoreWorkItem getWorkItem()
    {
        return this.workItem;
    }

    Message getMessage()
    {
        return this.message;
    }

    void perform(CoreActionInvocation invocation)
    {
        Task task = getWorkItem().getTransition().getTask();

        if ( task == null )
        {
            invocation.complete();
            return;
        }

        System.err.println( "WorkItem: " + getWorkItem() );
        System.err.println( "WorkItem.Transition: " + getWorkItem().getTransition() );
        System.err.println( "WorkItem.Transition.Task: " + getWorkItem().getTransition().getTask() );
        System.err.println( "WorkItem.Transition.Task.Action: " + getWorkItem().getTransition().getTask().getAction() );
        System.err.println( "action to perform: " + getWorkItem().getTransition().getTask().getAction() );
        getWorkItem().getTransition().getTask().getAction().perform( invocation );
    }
}
