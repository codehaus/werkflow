/*
 * $Id$
 */

package org.codehaus.werkflow.tagalog;

import org.codehaus.tagalog.Attributes;
import org.codehaus.tagalog.TagException;
import org.codehaus.tagalog.TagalogParseException;
import org.codehaus.werkflow.idioms.Sequence;

/**
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class ElseTag
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

    public Object end(String elementName)
        throws TagException
    {
        Sequence sequence = (Sequence) super.end( elementName );

        IfTag ifTag = (IfTag) getParent();

        ifTag.setFalseBody( sequence );

        return sequence;
    }
}
