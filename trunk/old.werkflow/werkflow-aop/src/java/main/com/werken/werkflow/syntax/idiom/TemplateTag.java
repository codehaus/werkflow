package com.werken.werkflow.syntax.idiom;

import com.werken.werkflow.definition.petri.IdiomDefinition;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class TemplateTag
    extends IdiomTagSupport
{
    public TemplateTag()
    {
        // intentionally left blank
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        invokeBody( output );
    }
}
