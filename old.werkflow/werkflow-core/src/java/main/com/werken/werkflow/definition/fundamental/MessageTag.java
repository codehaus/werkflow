package com.werken.werkflow.definition.fundamental;

import com.werken.werkflow.definition.MessageCorrelator;
import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.definition.NoSuchMessageTypeException;
import com.werken.werkflow.definition.petri.DefaultNet;
import com.werken.werkflow.definition.petri.DefaultPlace;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class MessageTag
    extends FundamentalTagSupport
{
    private String messageTypeId;
    private String id;
    private MessageCorrelator correlator;

    public MessageTag()
    {

    }

    public void setType(String messageTypeId)
    {
        this.messageTypeId = messageTypeId;
    }

    public String getType()
    {
        return this.messageTypeId;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public void setMessageCorrelator(MessageCorrelator correlator)
    {
        this.correlator = correlator;
    }

    public MessageCorrelator getMessageCorrelator()
    {
        return this.correlator;
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        requireStringAttribute( "id",
                                getId() );

        requireStringAttribute( "type",
                                getType() );

        TransitionTag transition = (TransitionTag) requiredAncestor( "transition",
                                                                     TransitionTag.class );

        MessageType msgType = null;

        try
        {
            msgType = getMessageTypeLibrary().getMessageType( getType() );
        }
        catch (NoSuchMessageTypeException e)
        {
            throw new JellyTagException( e );
        }

        MessageWaiter waiter = new MessageWaiter( msgType,
                                                  getId() );

        transition.setMessageWaiter( waiter );

        invokeBody( output );

        if ( getMessageCorrelator() != null )
        {
            waiter.setMessageCorrelator( getMessageCorrelator() );
        }
    }
}
