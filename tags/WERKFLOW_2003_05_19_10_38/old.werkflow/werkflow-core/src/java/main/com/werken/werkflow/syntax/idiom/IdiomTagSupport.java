package com.werken.werkflow.syntax.idiom;

import com.werken.werkflow.definition.petri.IdiomDefinition;

import com.werken.werkflow.jelly.MiscTagSupport;

import org.apache.commons.jelly.JellyTagException;

public abstract class IdiomTagSupport
    extends MiscTagSupport
{
    public static final String CURRENT_IDIOM_DEFINITION_KEY = "werkflow.idiom.definition";

    public IdiomTagSupport()
    {
        // intentionally left blank
    }

    public IdiomDefinition getCurrentIdiomDefinition()
        throws JellyTagException
    {
        IdiomDefinition idiomDef = (IdiomDefinition) getContext().getVariable( CURRENT_IDIOM_DEFINITION_KEY );

        if ( idiomDef == null )
        {
            throw new JellyTagException( "no current idiom definition" );
        }

        return idiomDef;
    }

    public void setCurrentIdiomDefinition(IdiomDefinition idiomDef)
    {
        getContext().setVariable( CURRENT_IDIOM_DEFINITION_KEY,
                                  idiomDef );
    }
}
