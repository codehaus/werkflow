package com.werken.werkflow.syntax;

import org.apache.commons.jelly.Tag;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.impl.DynamicTagLibrary;

import org.xml.sax.Attributes;

public class Syntax
{
    public static final Syntax[] EMPTY_ARRAY = new Syntax[0];

    private String namespaceUri;
    private DynamicTagLibrary tagLibrary;

    public Syntax(String namespaceUri,
                  DynamicTagLibrary tagLibrary)
    {
        this.namespaceUri = namespaceUri;
        this.tagLibrary   = tagLibrary;
    }

    public Syntax(final String namespaceUri)
    {
        this( namespaceUri,
              new DynamicTagLibrary() );
    }

    public String getNamespaceUri()
    {
        return this.namespaceUri;
    }

    public DynamicTagLibrary getTagLibrary()
    {
        return this.tagLibrary;
    }
}
