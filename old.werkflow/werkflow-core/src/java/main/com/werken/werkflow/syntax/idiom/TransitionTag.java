package com.werken.werkflow.syntax.idiom;

import com.werken.werkflow.definition.petri.TransitionDefinition;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class TransitionTag
    extends IdiomTagSupport
{
    private String id;
    private String documentation;

    public TransitionTag()
    {
        // intentionally left blank
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public void setDocumentation(String documentation)
    {
        this.documentation = documentation;
    }

    public String getDocumentation()
    {
        return this.documentation;
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        invokeBody( output );

        TransitionDefinition transition = new TransitionDefinition( getId(),
                                                                    getDocumentation() );
    }
}
