package com.werken.werkflow.definition.petri;

import java.util.Map;
import java.util.HashMap;

public class IdiomDefinitionLibrary
{
    private Map idiomDefs;

    public IdiomDefinitionLibrary()
    {
        this.idiomDefs = new HashMap();
    }

    public void addIdiomDefinition(IdiomDefinition idiomDef)
        throws DuplicateIdiomDefinitionException
    {
        if ( this.idiomDefs.containsKey( idiomDef.getId() ) )
        {
            throw new DuplicateIdiomDefinitionException( idiomDef );
        }

        this.idiomDefs.put( idiomDef.getId(),
                            idiomDef );
    }

    public IdiomDefinition getIdiomDefinition(String id)
        throws NoSuchIdiomDefinitionException
    {
        if ( ! this.idiomDefs.containsKey( id ) )
        {
            throw new NoSuchIdiomDefinitionException( id );
        }

        return (IdiomDefinition) this.idiomDefs.get( id );
    }

    public IdiomDefinition[] getIdiomDefinitions()
    {
        return (IdiomDefinition[]) this.idiomDefs.values().toArray( IdiomDefinition.EMPTY_ARRAY );
    }
}
