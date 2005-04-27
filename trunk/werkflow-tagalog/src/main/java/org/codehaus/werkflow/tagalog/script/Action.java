/*
 * $Id$
 */

package org.codehaus.werkflow.tagalog.script;

import org.codehaus.werkflow.spi.Instance;
import org.codehaus.werkflow.spi.SyncComponent;

import org.codehaus.tagalog.script.Statement;

/**
 * Wrap a tagalog script as a werkflow action.
 *
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class Action
    implements SyncComponent
{
    private final Statement statement;

    public Action(Statement statement)
    {
        this.statement = statement;
    }

    public void perform(Instance instance)
        throws Exception
    {
        this.statement.execute( instance.getContextMap() );
    }
}
