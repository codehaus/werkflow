package com.werken.werkflow.syntax.idiom;

import com.werken.werkflow.definition.petri.IdiomDefinition;
import com.werken.werkflow.definition.petri.IdiomDefinitionLibrary;

import org.apache.commons.jelly.impl.DynamicTagLibrary;

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

    private void registerIdioms()
    {
        for ( int i = 0 ; i < this.idiomDefs.length ; ++i )
        {
            registerIdiom( this.idiomDefs[i] );
        }
    }

    protected void registerIdiom(IdiomDefinition idiomDef)
    {
        registerBeanTag( idiomDef.getId(),
                         new IdiomTagFactory( idiomDef ) );
    }
}
