package com.werken.werkflow.syntax.idiom;

import com.werken.werkflow.definition.petri.IdiomDefinition;
import com.werken.werkflow.definition.petri.TransitionDefinition;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class MessageTag
    extends IdiomTagSupport
{
    private String type;

    public MessageTag()
    {
        // intentionally left blank
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return this.type;
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        IdiomDefinition idiomDef = getCurrentIdiomDefinition();

        requireStringAttribute( "type",
                                getType() );

        TransitionTag tag = (TransitionTag) findAncestorWithClass( TransitionTag.class );

        if ( tag == null )
        {
            throw new JellyTagException( "message only valid within a transition" );
        }

        invokeBody( output );
    }
}
