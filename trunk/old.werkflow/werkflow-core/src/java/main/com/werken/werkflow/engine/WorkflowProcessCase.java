package com.werken.werkflow.engine;

import com.werken.werkflow.ProcessCase;
import com.werken.werkflow.ProcessInfo;
import com.werken.werkflow.definition.petri.Parameters;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.service.caserepo.CaseState;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class WorkflowProcessCase
    implements ProcessCase, Parameters
{
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private ProcessInfo info;
    private CaseState state;
    private Transition[] enabledTransitions;

    public WorkflowProcessCase(ProcessInfo info,
                               CaseState state)
    {
        this.info  = info;
        this.state = state;
        this.enabledTransitions = Transition.EMPTY_ARRAY;
    }

    protected CaseState getState()
    {
        return this.state;
    }

    public String getId()
    {
        return getState().getCaseId();
    }

    public ProcessInfo getProcessInfo()
    {
        return this.info;
    }

    public Object getAttribute(String name)
    {
        return getState().getAttribute( name );
    }

    public void setAttribute(String name,
                             Object value)
    {
        getState().setAttribute( name,
                                 value );
    }

    public void clearAttribute(String name)
    {
        getState().clearAttribute( name );
    }

    public String[] getAttributeNames()
    {
        return getState().getAttributeNames();
    }

    public boolean hasAttribute(String name)
    {
        return getState().hasAttribute( name );
    }

    public boolean hasMark(String placeId)
    {
        return getState().hasMark( placeId );
    }

    public String[] getMarks()
    {
        return getState().getMarks();
    }

    public void addMark(String placeId)
    {
        getState().addMark( placeId );
    }

    public void removeMark(String placeId)
    {
        getState().removeMark( placeId );
    }

    public Object getParameter(String name)
    {
        return getAttribute( name );
    }

    public String[] getParameterNames()
    {
        return getAttributeNames();
    }

    public void setEnabledTransitions(Transition[] enabledTransitions)
    {
        this.enabledTransitions = enabledTransitions;
    }

    public Transition[] getEnabledTransitions()
    {
        return this.enabledTransitions;
    }

    void setCaseAttributes(Map caseAttrs)
    {
        String[] origAttrNames = getAttributeNames();

        for ( int i = 0 ; i < origAttrNames.length ; ++i )
        {
            if ( ! caseAttrs.containsKey( origAttrNames[i] ) )
            {
                clearAttribute( origAttrNames[i] );
            }
        }

        Iterator attrNames   = caseAttrs.keySet().iterator();
        String  eachAttrName = null;

        while ( attrNames.hasNext() )
        {
            eachAttrName = (String) attrNames.next();

            setAttribute( eachAttrName,
                          caseAttrs.get( eachAttrName ) );
        }
    }

    Map getCaseAttributes()
    {
        Map caseAttrs = new HashMap();

        String[] attrNames = getAttributeNames();

        for ( int i = 0 ; i < attrNames.length ; ++i )
        {
            caseAttrs.put( attrNames[i],
                           getAttribute( attrNames[i] ) );
        }

        return caseAttrs;
    }
}
