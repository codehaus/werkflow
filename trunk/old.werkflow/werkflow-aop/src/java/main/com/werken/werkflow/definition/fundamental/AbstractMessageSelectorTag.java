package com.werken.werkflow.definition.fundamental;

import com.werken.werkflow.service.messaging.MessageSelector;

import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.JellyException;

public abstract class AbstractMessageSelectorTag
    extends TagSupport
{
    public AbstractMessageSelectorTag()
    {
    }

    public void setMessageSelector(MessageSelector selector)
        throws Exception
    {
        MessageTypeTag messageType = (MessageTypeTag) findAncestorWithClass( MessageTypeTag.class );

        if ( messageType == null )
        {
            throw new JellyException( "not within <message-type>" );
        }

        messageType.setMessageSelector( selector );
    }

}
