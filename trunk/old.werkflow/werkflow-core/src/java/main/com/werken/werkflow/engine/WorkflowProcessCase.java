package com.werken.werkflow.engine;

import com.werken.werkflow.ProcessCase;
import com.werken.werkflow.ProcessInfo;
import com.werken.werkflow.action.MutableProcessCase;
import com.werken.werkflow.definition.petri.Parameters;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.service.caserepo.CaseState;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class WorkflowProcessCase
    implements MutableProcessCase, Parameters
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

    public boolean isCorrelated(String transitionId)
    {
        return false;
    }

    public void addCorrelation(String transitionId,
                               String messageId)
    {

    }
}
