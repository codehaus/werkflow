package com.werken.werkflow.syntax.idiom;

import com.werken.werkflow.definition.petri.ArcDefinition;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class InputTag
    extends IdiomTagSupport
{
    private String from;
    private String filter;

    public InputTag()
    {
        // intentionally left blank
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getFrom()
    {
        return this.from;
    }

    public void setFilter(String filter)
    {
        this.filter = filter;
    }

    public String getFilter()
    {
        return this.filter;
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        requireStringAttribute( "from",
                                getFrom() );

        TransitionTag tag = (TransitionTag) findAncestorWithClass( TransitionTag.class );

        if ( tag == null )
        {
            throw new JellyTagException( "input only valid within a transition" );
        }

        String transId = tag.getId();

        ArcDefinition arcDef = ArcDefinition.newArcFromPlaceToTransition( getFrom(),
                                                                          transId,
                                                                          getFilter() );

        invokeBody( output );
    }
}
