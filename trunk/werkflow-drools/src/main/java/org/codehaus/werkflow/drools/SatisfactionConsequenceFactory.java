package org.codehaus.werkflow.drools;

import org.drools.rule.Declaration;
import org.drools.smf.Configuration;
import org.drools.smf.ConsequenceFactory;
import org.drools.smf.FactoryException;
import org.drools.spi.Consequence;

public class SatisfactionConsequenceFactory
    implements ConsequenceFactory
{
    public SatisfactionConsequenceFactory()
    {
        // intentionaly left blank
    }

    public Consequence newConsequence(Configuration config,
                                      Declaration availDecls[])
        throws FactoryException
    {
        for ( int i = 0 ; i < availDecls.length ; ++i )
        {
            if ( availDecls[ i ].getIdentifier().equals( "context" ) )
            {
                return new SatisfactionConsequence( availDecls[ i ] );
            }
        }

        throw new FactoryException( "no declaration for <context>" );
    }
}
