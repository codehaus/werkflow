package com.werken.werkflow.syntax.idiomatic;

import com.werken.werkflow.action.Action;
import com.werken.werkflow.jelly.MiscTagSupport;
import com.werken.werkflow.syntax.fundamental.ActionReceptor;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class ReceiverTag
    extends MiscTagSupport
    implements ActionReceptor
{
    public ReceiverTag()
    {

    }

    public void receiveAction(Action action)
        throws JellyTagException
    {
        OnMessageTag tag = (OnMessageTag) findAncestorWithClass( OnMessageTag.class );

        if ( tag == null )
        {
            throw new JellyTagException( "receive not allowed in this context" );
        }

        tag.setReceiverAction( action );
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        
        invokeBody( output );
    }
}
