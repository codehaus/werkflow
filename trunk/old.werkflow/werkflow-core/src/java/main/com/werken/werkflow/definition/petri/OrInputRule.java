package com.werken.werkflow.definition.petri;

import com.werken.werkflow.engine.WorkflowProcessCase;

public class OrInputRule
    implements ActivationRule
{
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private static final OrInputRule INSTANCE = new OrInputRule();

    public static ActivationRule getInstance()
    {
        return INSTANCE;
    }
    
    private OrInputRule()
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

            if ( processCase.hasMark( eachPlace.getId() ) )
            {
                return true;
            }
        }

        return false;
    }

    public String[] satisfy(Transition transition,
                            WorkflowProcessCase processCase)
    {
        Arc[] arcs = transition.getArcsFromPlaces();
        Place eachPlace = null;

        for ( int i = 0 ; i < arcs.length ; ++i )
        {
            eachPlace = arcs[i].getPlace();

            if ( processCase.hasMark( eachPlace.getId() ) )
            {
                processCase.removeMark( eachPlace.getId() ); 

                return new String[]
                    {
                        eachPlace.getId()
                    };
            }
        }

        return EMPTY_STRING_ARRAY;
    }
}
