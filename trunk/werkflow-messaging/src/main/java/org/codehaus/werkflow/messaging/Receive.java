/*
 * $Id$
 */

package org.codehaus.werkflow.messaging;

import org.codehaus.werkflow.helpers.SimpleSatisfaction;
import org.codehaus.werkflow.helpers.SimpleSatisfactionSpec;
import org.codehaus.werkflow.idioms.Sequence;
import org.codehaus.werkflow.spi.AsyncComponent;
import org.codehaus.werkflow.spi.SatisfactionSpec;

/**
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class Receive
    extends Sequence
    implements AsyncComponent
{
    private String id;

    public Receive(String id)
    {
        this.id = id;

        addStep( new SimpleSatisfaction( id,
                                         new ReceiveSatisfier() ) );
    }

    public String getId()
    {
        return this.id;
    }

    public SatisfactionSpec getSatisfactionSpec()
    {
        return new SimpleSatisfactionSpec( getId() );
    }
}
