package com.werken.werkflow.engine;

import com.werken.werkflow.definition.MessageType;

public class MessageBlocker
{
    private WorkflowProcessCase processCase;
    private MessageType messageType;
    
    public MessageBlocker(WorkflowProcessCase processCase,
                          MessageType messageType)
    {
        this.processCase   = processCase;
        this.messageType = messageType;
    }

    public WorkflowProcessCase getProcessCase()
    {
        return this.processCase;
    }

    public MessageType getMessageType()
    {
        return this.messageType;
    }
}
