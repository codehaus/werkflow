package com.werken.werkflow.service.caserepo;

import com.werken.werkflow.Attributes;

import java.util.Map;
import java.util.HashMap;

public abstract class AbstractCaseRepository
    implements CaseRepository
{
    public AbstractCaseRepository()
    {
    }

    public CaseState newCaseState(String processId,
                                  Attributes attributes)
    {
        String caseId = nextCaseId();

        CaseState state = new DefaultCaseState( caseId,
                                                processId,
                                                this );

        String[] attrNames = attributes.getAttributeNames();

        for ( int i = 0 ; i < attrNames.length ; ++i )
        {
            state.setAttribute( attrNames[i],
                                attributes.getAttribute( attrNames[i] ) );
        }

        state.addMark( "in" );

        state.store();

        return new DefaultCaseState( state,
                                     this );
    }

    public CaseState getCaseState(String caseId)
    {
        CaseState state = retrieve( caseId );

        if ( state == null )
        {
            return null;
        }

        return new DefaultCaseState( state,
                                     this );
    }

    protected abstract String nextCaseId();

    protected abstract void store(CaseState state);

    protected abstract CaseState retrieve(String caseId);
}
