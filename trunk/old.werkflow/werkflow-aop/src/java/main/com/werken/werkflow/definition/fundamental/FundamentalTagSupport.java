package com.werken.werkflow.definition.fundamental;

import com.werken.werkflow.definition.MessageTypeLibrary;
import com.werken.werkflow.jelly.MiscTagSupport;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyException;

public abstract class FundamentalTagSupport
    extends MiscTagSupport
{
    public FundamentalTagSupport()
    {
    }

    public MessageTypeLibrary getMessageTypeLibrary()
        throws JellyException
    {
        JellyContext context = getContext();

        MessageTypeLibrary msgTypeLib = (MessageTypeLibrary) context.getVariable( FundamentalDefinitionLoader.MESSAGE_TYPE_LIBRARY_KEY );
        
        if ( msgTypeLib == null )
        {
            throw new JellyException( "no message-type library" );
        }

        return msgTypeLib;
    }
}
