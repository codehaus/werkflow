package com.werken.werkflow.syntax.idiom;

import com.werken.werkflow.definition.petri.IdiomDefinition;
import com.werken.werkflow.definition.petri.IdiomDefinitionLibrary;

import org.apache.commons.jelly.Tag;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.impl.DynamicTagLibrary;

import org.xml.sax.Attributes;

public class IdiomaticTagLibrary
    extends DynamicTagLibrary
{
    public static final String NAMESPACE_URI = "werkflow:idiomatic";

    private IdiomDefinition[] idiomDefs;

    public IdiomaticTagLibrary(IdiomDefinitionLibrary idiomDefLib)
    {
        this( idiomDefLib.getIdiomDefinitions() );
    }

    public IdiomaticTagLibrary(IdiomDefinition[] idiomDefs)
    {
        this.idiomDefs = idiomDefs;

        registerIdioms();
    }
    public Tag createTag(String name,
                         Attributes attributes)
        throws JellyException
    {
        System.err.println( "idiom taglib -> " + name );

        return super.createTag( name,
                                attributes );
    }
    private void registerIdioms()
    {
        for ( int i = 0 ; i < this.idiomDefs.length ; ++i )
        {
            registerIdiom( this.idiomDefs[i] );
        }
    }

    protected void registerIdiom(IdiomDefinition idiomDef)
    {
        System.err.println( "register tag: " + idiomDef.getId() );
        registerBeanTag( idiomDef.getId(),
                         new IdiomTagFactory( idiomDef ) );
    }
}
