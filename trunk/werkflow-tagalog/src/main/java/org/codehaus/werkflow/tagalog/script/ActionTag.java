/*
 * $Id$
 */

package org.codehaus.werkflow.tagalog.script;

import org.codehaus.tagalog.TagException;
import org.codehaus.tagalog.TagalogParseException;
import org.codehaus.tagalog.script.Statement;
import org.codehaus.tagalog.script.tags.AbstractCompoundStatementTag;

/**
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class ActionTag
    extends AbstractCompoundStatementTag
{
    public Object end(String elementName)
        throws TagException, TagalogParseException
    {
        Statement stmt = (Statement) super.end( elementName );

        return new Action( stmt );
    }
}
