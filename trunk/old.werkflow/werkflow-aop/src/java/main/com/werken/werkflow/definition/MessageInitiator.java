package com.werken.werkflow.definition;

public class MessageInitiator
{
    public static final MessageInitiator[] EMPTY_ARRAY = new MessageInitiator[0];

    private String messageTypeId;
    private String bindingVar;
    private String documentation;

    public MessageInitiator(String messageTypeId,
                            String bindingVar)
    {
        this.messageTypeId = messageTypeId;
        this.bindingVar = bindingVar;
    }

    public void setDocumentation(String documentation)
    {
        this.documentation = documentation;
    }

    public String getDocumentation()
    {
        return this.documentation;
    }

    public String getMessageTypeId()
    {
        return this.messageTypeId;
    }

    public String getBindingVar()
    {
        return this.bindingVar;
    }
}
    
