package com.werken.werkflow.definition.fundamental;

import com.werken.werkflow.definition.MessageCorrelator;
import com.werken.werkflow.service.messaging.MessageSelector;

import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.JellyException;

public abstract class AbstractMessageCorrelatorTag
    extends TagSupport
{
    public AbstractMessageCorrelatorTag()
    {
    }

    public void setMessageCorrelator(MessageCorrelator correlator)
        throws Exception
    {
        MessageTag message = (MessageTag) findAncestorWithClass( MessageTag.class );

        if ( message == null )
        {
            throw new JellyException( "not within <message>" );
        }

        message.setMessageCorrelator( correlator );
    }

    public String getMessageId()
        throws JellyException
    {
        MessageTag message = (MessageTag) findAncestorWithClass( MessageTag.class );

        if ( message == null )
        {
            throw new JellyException( "not within <message>" );
        }

        return message.getId();
    }
}
