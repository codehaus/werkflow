/*
 * $Id$
 */

package org.codehaus.werkflow.helpers;

import org.codehaus.werkflow.spi.Instance;
import org.codehaus.werkflow.spi.SatisfactionValues;
import org.codehaus.werkflow.spi.Satisfier;

/**
 * {@link Satisfier} that copies all the satisfaction values into the
 * instance.
 *
 * The names of the satisfaction values are prefixed with the
 * satisfaction Id (passed as an argument to
 * {@link #ValueCopySatisfier(String)}.
 * So, for example, a value with the name <code>cheese</code> provided
 * to satisfy a satisfaction <code>consume</code> will be copied into
 * the instance with the name <code>consume.cheese</code>.
 *
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class ValueCopySatisfier
    implements Satisfier
{
    private final String satisfactionId;

    public ValueCopySatisfier(String satisfactionId)
    {
        this.satisfactionId = satisfactionId;
    }

    public void perform(Instance instance,
                        SatisfactionValues values)
        throws Exception
    {
        String[] valueNames = values.getNames();
        
        for ( int i = 0 ; i < valueNames.length ; ++i )
        {
            instance.put( satisfactionId + "." + valueNames[ i ],
                          values.getValue( valueNames[ i ] ) );
        }
    }
}
