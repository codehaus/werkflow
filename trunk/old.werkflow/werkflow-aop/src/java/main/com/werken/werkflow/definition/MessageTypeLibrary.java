package com.werken.werkflow.definition;

import java.util.Map;
import java.util.HashMap;

public class MessageTypeLibrary
{
    public static final String MESSAGE_TYPE_LIBRARY_KEY = "werkflow.msg.type.lib";

    private Map messageTypes;

    public MessageTypeLibrary()
    {
        this.messageTypes = new HashMap();
    }

    public void addMessageType(MessageType messageType)
        throws DuplicateMessageTypeException
    {
        if ( this.messageTypes.containsKey( messageType.getId() ) )
        {
            throw new DuplicateMessageTypeException( messageType );
        }

        this.messageTypes.put( messageType.getId(),
                               messageType );
    }

    public MessageType getMessageType(String id)
        throws NoSuchMessageTypeException
    {
        if ( ! this.messageTypes.containsKey( id ) )
        {
            throw new NoSuchMessageTypeException( id );
        }

        return (MessageType) this.messageTypes.get( id );
    }

        
}
