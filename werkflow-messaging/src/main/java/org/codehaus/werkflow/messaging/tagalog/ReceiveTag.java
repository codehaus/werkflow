/*
 * $Id$
 */

package org.codehaus.werkflow.messaging.tagalog;

import org.codehaus.tagalog.AbstractTag;
import org.codehaus.tagalog.Attributes;
import org.codehaus.tagalog.TagException;
import org.codehaus.tagalog.TagalogParseException;

import org.codehaus.werkflow.messaging.Receive;
import org.codehaus.werkflow.spi.Component;

/**
 * @author Mark H. Wilkinson
 * @version $Revision$
 */
public class ReceiveTag
    extends AbstractTag
{
    private Receive satisfaction;

    public void begin(String elementName,
                      Attributes attributes)
        throws TagException, TagalogParseException
    {
        String id = requireAttribute( attributes,
                                      elementName,
                                      "id" );

        satisfaction = new Receive( id );

        super.begin( elementName,
                     attributes );
    }

    public void child(Object child)
        throws TagException, TagalogParseException
    {
        if ( child instanceof Component )
        {
            satisfaction.addStep( (Component) child );
        }
        else
        {
            throw new TagException( "child element must be a Component" );
        }
    }

    public Object end(String elementName)
        throws TagException
    {
        return satisfaction;
    }

    public boolean recycle()
    {
        satisfaction = null;

        return super.recycle();
    }
}
