package com.werken.werkflow.syntax.idiom;

import com.werken.werkflow.definition.petri.IdiomDefinition;
import com.werken.werkflow.definition.petri.TransitionDefinition;
import com.werken.werkflow.definition.petri.MessageWaiterDefinition;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class MessageTag
    extends IdiomTagSupport
{
    private String type;
    private String correlator;
    private String bind;

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

    public void setCorrelator(String correlator)
    {
        this.correlator = correlator;
    }

    public String getCorrelator()
    {
        return this.correlator;
    }

    public void setBind(String bind)
    {
        this.bind = bind;
    }

    public String getBind()
    {
        return this.bind;
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

        MessageWaiterDefinition msgWaiter = new MessageWaiterDefinition( getType(),
                                                                         getCorrelator(),
                                                                         getBind() );
        tag.setWaiter( msgWaiter );
    }
}
