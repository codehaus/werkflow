package com.werken.werkflow.definition.fundamental;

import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.definition.petri.DefaultNet;
import com.werken.werkflow.definition.petri.DefaultPlace;
import com.werken.werkflow.service.messaging.MessageSelector;

import org.apache.commons.jelly.XMLOutput;

public class MessageTypeTag
    extends FundamentalTagSupport
    implements DocumentableTag
{
    private String id;
    private String documentation;

    private MessageSelector selector;

    public MessageTypeTag()
    {

    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public void setDocumentation(String documentation)
    {
        this.documentation = documentation;
    }

    public String getDocumentation()
    {
        return this.documentation;
    }

    public void setMessageSelector(MessageSelector selector)
    {
        this.selector = selector;
    }

    public MessageSelector getMessageSelector()
    {
        return this.selector;
    }

    public void doTag(XMLOutput output)
        throws Exception
    {
        requireStringAttribute( "id",
                                getId() );

        ProcessTag process = (ProcessTag) requiredAncestor( "process",
                                                            ProcessTag.class );

        MessageType messageType = new MessageType( getId() );

        setDocumentation( null );

        invokeBody( output );

        messageType.setDocumentation( getDocumentation() );
        messageType.setMessageSelector( getMessageSelector() );

        process.addMessageType( messageType );
    }
}
