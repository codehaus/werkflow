package com.werken.werkflow.definition.petri;

import com.werken.werkflow.engine.WorkflowProcessCase;

import java.util.List;
import java.util.ArrayList;

public class AndInputRule
    implements ActivationRule
{
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private static final AndInputRule INSTANCE = new AndInputRule();

    public static ActivationRule getInstance()
    {
        return INSTANCE;
    }
    
    private AndInputRule()
    {
        // intentionally left blank
    }

    public boolean isSatisfied(Transition transition,
                               WorkflowProcessCase processCase)
        throws Exception
    {
        Arc[] arcs = transition.getArcsFromPlaces();
        Place eachPlace = null;

        for ( int i = 0 ; i < arcs.length ; ++i )
        {
            eachPlace = arcs[i].getPlace();

            if ( ! processCase.hasMark( eachPlace.getId() ) )
            {
                return false;
            }
        }
        
        return true;
    }

    public String[] satisfy(Transition transition,
                        WorkflowProcessCase processCase)
    {
        List placeIds = new ArrayList();

        Arc[] arcs = transition.getArcsFromPlaces();
        Place eachPlace = null;

        for ( int i = 0 ; i < arcs.length ; ++i )
        {
            eachPlace = arcs[i].getPlace();

            processCase.removeMark( eachPlace.getId() );
            placeIds.add( eachPlace.getId() );
        }

        return (String[]) placeIds.toArray( EMPTY_STRING_ARRAY );
    }
}
