package com.werken.werkflow.definition;

import java.util.Map;
import java.util.HashMap;

public class MessageTypeLibrary
{
    public static final String MESSAGE_TYPE_LIBRARY_KEY = "werkflow.msg.type.lib";

    private MessageTypeLibrary parent;
    private Map messageTypes;

    public MessageTypeLibrary()
    {
        this( null );
    }
    
    public MessageTypeLibrary(MessageTypeLibrary parent)
    {
        this.parent = parent;
        this.messageTypes = new HashMap();
    }

    public MessageTypeLibrary getParent()
    {
        return this.parent;
    }

    public boolean containsMessageType(MessageType messageType)
    {
        if ( this.messageTypes.containsKey( messageType.getId() ) )
        {
            return true;
        }

        if ( this.parent != null )
        {
            return this.parent.containsMessageType( messageType );
        }

        return false;
    }

    public void addMessageType(MessageType messageType)
        throws DuplicateMessageTypeException
    {
        if ( containsMessageType( messageType ) )
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
            if ( this.parent == null )
            {
                throw new NoSuchMessageTypeException( id );
            }

            return this.parent.getMessageType( id );
        }
        
        return (MessageType) this.messageTypes.get( id );
    }
}
