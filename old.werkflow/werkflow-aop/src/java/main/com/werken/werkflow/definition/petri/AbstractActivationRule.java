package com.werken.werkflow.definition.petri;

public abstract class AbstractActivationRule
    implements ActivationRule
{
    public AbstractActivationRule()
    {

    }

    public boolean contains(String mark,
                            String[] availMarks)
    {
        for ( int i = 0 ; i < availMarks.length ; ++i )
        {
            if ( availMarks[i].equals( mark ) )
            {
                return true;
            }
        }

        return false;
    }
}
