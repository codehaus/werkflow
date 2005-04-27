/*
 * $Id$
 */

package org.codehaus.werkflow.tagalog;

import org.codehaus.tagalog.Attributes;
import org.codehaus.tagalog.TagException;
import org.codehaus.tagalog.TagalogParseException;

/**
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class ThenElseTag
    extends SequenceTag
{
    public void begin(String elementName,
                      Attributes attributes)
        throws TagException, TagalogParseException
    {
        requireParent( elementName,
                       "if",
                       IfTag.class );

        super.begin( elementName,
                     attributes );
    }
}
