package com.werken.werkflow.syntax.idiomatic;

import com.werken.werkflow.definition.petri.IdiomDefinition;
import com.werken.werkflow.definition.petri.IdiomDefinitionLibrary;

import org.apache.commons.jelly.impl.DynamicTagLibrary;

public class IdiomaticTagLibrary
    extends DynamicTagLibrary
{
    private IdiomDefinitionLibrary idiomDefLib;

    public IdiomaticTagLibrary(IdiomDefinitionLibrary idiomDefLib)
    {
        this.idiomDefLib = idiomDefLib;

        registerIdioms();
    }

    public IdiomDefinitionLibrary getIdiomDefinitionLibrary()
    {
        return this.idiomDefLib;
    }

    private void registerIdioms()
    {
        IdiomDefinition[] idiomDefs = getIdiomDefinitionLibrary().getIdiomDefinitions();

        for ( int i = 0 ; i < idiomDefs.length ; ++i )
        {
            registerIdiom( idiomDefs[i] );
        }
    }

    protected void registerIdiom(IdiomDefinition idiomDef)
    {
        registerBeanTag( idiomDef.getId(),
                         new IdiomTagFactory( idiomDef ) );
    }
}
