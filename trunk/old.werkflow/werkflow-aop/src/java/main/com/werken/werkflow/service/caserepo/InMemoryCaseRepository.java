package com.werken.werkflow.service.caserepo;

import com.werken.werkflow.QueryException;

import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class InMemoryCaseRepository
    extends AbstractCaseRepository
{
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private long counter;
    private Map cases;
    
    public InMemoryCaseRepository()
    {
        this.counter = 0;
        this.cases   = new HashMap();
    }

    protected synchronized String nextCaseId()
    {
        return "case." + (++counter);
    }

    protected void store(CaseState state)
    {
        this.cases.put( state.getCaseId(),
                        state );
    }

    protected CaseState retrieve(String caseId)
    {
        return (CaseState) this.cases.get( caseId );
    }

    public String[] selectCases(String processId,
                                String placeId)
        throws QueryException
    {
        List caseIds = new ArrayList();
        
        Iterator  caseIter = this.cases.values().iterator();
        CaseState eachCase = null;

        while ( caseIter.hasNext() )
        {
            eachCase = (CaseState) caseIter.next();

            if ( eachCase.getProcessId().equals( processId ) )
            {
                if ( eachCase.hasMark( placeId ) )
                {
                    caseIds.add( eachCase.getCaseId() );
                }
            }
        }

        return (String[]) caseIds.toArray( EMPTY_STRING_ARRAY );
    }

    public String[] selectCases(String processId,
                                Map qbeAttrs)
    {
        List caseIds = new ArrayList();
        
        Iterator  caseIter = this.cases.values().iterator();
        CaseState eachCase = null;

        while ( caseIter.hasNext() )
        {
            eachCase = (CaseState) caseIter.next();

            if ( eachCase.getProcessId().equals( processId ) )
            {
            }
        }

        return (String[]) caseIds.toArray( EMPTY_STRING_ARRAY );
    }

    protected boolean evalQueryByExample(CaseState caseState,
                                         Map qbeAttrs)
    {
        Iterator attrNameIter = qbeAttrs.keySet().iterator();
        String   eachAttrName = null;

        while ( attrNameIter.hasNext() )
        {
            eachAttrName = (String) attrNameIter.next();

            if ( ! caseState.hasAttribute( eachAttrName ) )
            {
                return false;
            }

            if ( ! caseState.getAttribute( eachAttrName ).equals( qbeAttrs.get( eachAttrName ) ) )
            {
                return false;
            }
        }

        return true;
    }
}
