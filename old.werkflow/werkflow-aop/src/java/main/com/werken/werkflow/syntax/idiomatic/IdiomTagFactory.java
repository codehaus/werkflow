package com.werken.werkflow.syntax.idiomatic;

import com.werken.werkflow.definition.petri.IdiomDefinition;

import org.apache.commons.jelly.Tag;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.impl.TagFactory;

import org.xml.sax.Attributes;

public class IdiomTagFactory
    implements TagFactory
{
    private IdiomDefinition idiomDef;

    public IdiomTagFactory(IdiomDefinition idiomDef)
    {
        this.idiomDef = idiomDef;
    }

    public IdiomDefinition getIdiomDefinition()
    {
        return this.idiomDef;
    }

    public Tag createTag(String name,
                         Attributes attributes)
        throws JellyException
    {
        return new IdiomTag( getIdiomDefinition() );
    }
}
