package com.werken.werkflow.service.caserepo;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class DefaultCaseState
    implements CaseState
{
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    
    private transient AbstractCaseRepository repo;
    private String caseId;
    private String processId;

    private Map attributes;
    private Set marks;
    
    public DefaultCaseState(String caseId,
                            String processId,
                            AbstractCaseRepository repo)
    {
        this.caseId     = caseId;
        this.processId  = processId;
        this.repo       = repo;
        this.attributes = new HashMap(); 
        this.marks      = new HashSet();
    }

    public DefaultCaseState(CaseState state,
                            AbstractCaseRepository repo)
    {
        this( state.getCaseId(),
              state.getProcessId(),
              repo );

        String[] attrNames = state.getAttributeNames();
        
        for ( int i = 0 ; i < attrNames.length ; ++i )
        {
            this.attributes.put( attrNames[i],
                                 state.getAttribute( attrNames[i] ) );
        }

        String[] marks = state.getMarks();

        for ( int i = 0 ; i < marks.length ; ++i )
        {
            this.marks.add( marks[i] );
        }
    }

    public String getCaseId()
    {
        return this.caseId;
    }

    public String getProcessId()
    {
        return this.processId;
    }

    public void setAttribute(String key,
                             Object value)
    {
        this.attributes.put( key,
                             value );
    }

    public Object getAttribute(String key)
    {
        return this.attributes.get( key );
    }

    public String[] getAttributeNames()
    {
        return (String[]) this.attributes.keySet().toArray( EMPTY_STRING_ARRAY );
    }

    public boolean hasAttribute(String key)
    {
        return this.attributes.containsKey( key );
    }

    public void clearAttribute(String key)
    {
        this.attributes.remove( key );
    }

    public void addMark(String placeId)
    {
        this.marks.add( placeId );
    }

    public void removeMark(String placeId)
    {
        this.marks.remove( placeId );
    }

    public String[] getMarks()
    {
        return (String[]) this.marks.toArray( EMPTY_STRING_ARRAY );
    }

    public boolean hasMark(String placeId)
    {
        return this.marks.contains( placeId );
    }

    protected AbstractCaseRepository getRepository()
    {
        return this.repo;
    }

    public void store()
    {
        getRepository().store( this );
    }
}
