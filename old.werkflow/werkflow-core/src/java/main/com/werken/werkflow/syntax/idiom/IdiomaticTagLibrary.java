package com.werken.werkflow.syntax.idiom;

import com.werken.werkflow.definition.petri.IdiomDefinition;
import com.werken.werkflow.definition.petri.IdiomDefinitionLibrary;

import org.apache.commons.jelly.Tag;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.expression.ExpressionFactory;
import org.apache.commons.jelly.impl.DynamicTagLibrary;

import org.xml.sax.Attributes;

public class IdiomaticTagLibrary
    extends DynamicTagLibrary
{
    public static final String NAMESPACE_URI = "werkflow:idiomatic";

    private IdiomDefinition[] idiomDefs;
    private ExpressionFactory exprFactory;

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
        registerBeanTag( idiomDef.getId(),
                         new IdiomTagFactory( idiomDef ) );
    }

    public void setExpressionFactory(ExpressionFactory exprFactory)
    {
        this.exprFactory = exprFactory;
    }

    public ExpressionFactory getExpressionFactory()
    {
        return this.exprFactory;
    }
}
