/*
 * $Id$
 */

package org.codehaus.werkflow.tagalog;

import org.codehaus.tagalog.AbstractTag;
import org.codehaus.tagalog.Attributes;
import org.codehaus.tagalog.Tag;
import org.codehaus.tagalog.TagBinding;
import org.codehaus.tagalog.TagException;
import org.codehaus.tagalog.TagalogParseException;

import org.codehaus.werkflow.idioms.Sequence;
import org.codehaus.werkflow.spi.Component;

/**
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class SequenceTag
    extends AbstractTag
    implements Tag
{
    private Sequence sequence;

    public void begin(String elementName,
                      Attributes attributes)
        throws TagException, TagalogParseException
    {
        sequence = new Sequence();
    }

    public void child(TagBinding childType,
                      Object child)
        throws TagException, TagalogParseException
    {
        if ( child instanceof Component )
        {
            if ( sequence != null )
            {
                sequence.addStep( (Component) child );
            }
        }
        else
        {
            throw new TagException( "child element must be a Component" );
        }
    }

    public Object end(String elementName)
        throws TagException
    {
        return sequence;
    }

    public boolean recycle()
    {
        sequence = null;

        return super.recycle();
    }
}
