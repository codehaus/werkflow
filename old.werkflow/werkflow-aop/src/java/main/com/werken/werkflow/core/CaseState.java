package com.werken.werkflow.core;

import com.werken.werkflow.service.persistence.CaseTransfer;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

class CaseState
{
    static final String[] EMPTY_STRING_ARRAY = new String[0];

    private Set tokens;
    private Map attributes;

    CaseState()
    {
        this.tokens     = new HashSet();
        this.attributes = new HashMap();
    }

    CaseState(CaseTransfer caseTransfer)
    {
        this.attributes = new HashMap( caseTransfer.getAttributes() );
        this.tokens     = new HashSet();

        String[] transferTokens = caseTransfer.getTokens();

        for ( int i = 0 ; i < transferTokens.length ; ++i )
        {
            this.tokens.add( transferTokens[i] );
        }
    }

    void addToken(String token)
    {
        this.tokens.add( token );
    }

    void consumeTokens(String[] consumed)
    {
        for ( int i = 0 ; i < consumed.length ; ++i )
        {
            if ( ! this.tokens.remove( consumed[i] ) )
            {
                throw new ConsistencyException( "attempt to consume non-existant token: " + consumed[i] );
            }
        }
    }

    boolean hasTokens(String[] tokens)
    {
        for ( int i = 0 ; i < tokens.length ; ++i )
        {
            if ( ! this.tokens.contains( tokens[i] ) )
            {
                return false;
            }
        }

        return true;
    }

    String[] getTokens()
    {
        return (String[]) this.tokens.toArray( EMPTY_STRING_ARRAY );
    }

    void setAttribute(String name,
                      Object value)
    {
        this.attributes.put( name,
                             value );
    }

    String[] getAttributeNames()
    {
        return (String[]) this.attributes.keySet().toArray( EMPTY_STRING_ARRAY );
    }

    Object getAttribute(String name)
    {
        return this.attributes.get( name );
    }

    boolean hasAttribute(String name)
    {
        return this.attributes.containsKey( name );
    }
}
