package com.werken.werkflow.syntax.idiom;

import com.werken.werkflow.definition.petri.ArcDefinition;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class OutputTag
    extends IdiomTagSupport
{
    private String to;
    private String test;

    public OutputTag()
    {
        // intentionally left blank
    }

    public void setTo(String to)
    {
        this.to = to;
    }

    public String getTo()
    {
        return this.to;
    }

    public void setTest(String test)
    {
        this.test = test;
    }

    public String getTest()
    {
        return this.test;
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        requireStringAttribute( "to",
                                getTo() );

        TransitionTag tag = (TransitionTag) findAncestorWithClass( TransitionTag.class );

        if ( tag == null )
        {
            throw new JellyTagException( "input only valid within a transition" );
        }

        String transId = tag.getId();

        ArcDefinition arcDef = ArcDefinition.newArcFromTransitionToPlace( getTo(),
                                                                          transId,
                                                                          getTest() );

        invokeBody( output );
    }
}
