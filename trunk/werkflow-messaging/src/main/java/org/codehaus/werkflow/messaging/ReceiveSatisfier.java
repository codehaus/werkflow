/*
 * $Id$
 */

package org.codehaus.werkflow.messaging;

import org.codehaus.werkflow.spi.Instance;
import org.codehaus.werkflow.spi.SatisfactionValues;
import org.codehaus.werkflow.spi.Satisfier;

/**
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class ReceiveSatisfier
    implements Satisfier
{
    public void perform(Instance instance,
                        SatisfactionValues values)
        throws Exception
    {
        String[] valueNames = values.getNames();
        
        for ( int i = 0 ; i < valueNames.length ; ++i )
        {
            instance.put( valueNames[ i ],
                          values.getValue( valueNames[ i ] ) );
        }
    }
}
