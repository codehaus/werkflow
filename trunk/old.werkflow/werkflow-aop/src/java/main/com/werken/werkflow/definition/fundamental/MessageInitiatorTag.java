package com.werken.werkflow.definition.fundamental;

import com.werken.werkflow.action.Action;
import com.werken.werkflow.definition.MessageInitiator;
import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.definition.NoSuchMessageTypeException;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class MessageInitiatorTag
    extends FundamentalTagSupport
    implements DocumentableTag, ActionReceptor
{
    private String id;
    private String type;
    private String documentation;

    private Action action;

    public MessageInitiatorTag()
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

    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return this.type;
    }

    public void setDocumentation(String documentation)
    {
        this.documentation = documentation;
    }

    public String getDocumentation()
    {
        return this.documentation;
    }

    public void setAction(Action action)
    {
        this.action = action;
    }

    public Action getAction()
    {
        return this.action;
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        requireStringAttribute( "id",
                                getId() );

        requireStringAttribute( "type",
                                getType() );

        ProcessTag process = (ProcessTag) requiredAncestor( "process",
                                                            ProcessTag.class );

        setDocumentation( null );

        invokeBody( output );

        MessageType messageType = null;

        try
        {
            messageType = getMessageTypeLibrary().getMessageType( getType() );
        }
        catch (NoSuchMessageTypeException e)
        {
            throw new JellyTagException( e );
        }

        MessageInitiator initiator = new MessageInitiator( messageType,
                                                           getId() );

        initiator.setDocumentation( getDocumentation() );

        if ( this.action != null )
        {
            initiator.setAction( this.action );
        }

        process.addMessageInitiator( initiator );
    }
}
