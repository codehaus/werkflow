package com.werken.werkflow.definition;

public class MessageInitiator
{
    public static final MessageInitiator[] EMPTY_ARRAY = new MessageInitiator[0];

    private MessageType messageType;
    private String bindingVar;
    private String documentation;

    public MessageInitiator(MessageType messageType,
                            String bindingVar)
    {
        this.messageType = messageType;
        this.bindingVar  = bindingVar;
    }

    public void setDocumentation(String documentation)
    {
        this.documentation = documentation;
    }

    public String getDocumentation()
    {
        return this.documentation;
    }

    public MessageType getMessageType()
    {
        return this.messageType;
    }

    public String getBindingVar()
    {
        return this.bindingVar;
    }
}
    
