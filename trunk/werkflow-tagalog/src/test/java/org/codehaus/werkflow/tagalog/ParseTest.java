/*
 * $Id$
 */

package org.codehaus.werkflow.tagalog;

import org.codehaus.werkflow.Workflow;

/**
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class ParseTest
    extends TagalogTestBase
{
    public void testParse()
        throws Exception
    {
        Workflow w = parseWorkflow();

        assertNotNull( w );
    }
}
