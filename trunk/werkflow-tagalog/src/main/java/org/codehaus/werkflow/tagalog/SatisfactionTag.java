/*
 * $Id$
 */

package org.codehaus.werkflow.tagalog;

import org.codehaus.tagalog.AbstractTag;
import org.codehaus.tagalog.Attributes;
import org.codehaus.tagalog.TagException;
import org.codehaus.tagalog.TagalogParseException;
import org.codehaus.werkflow.helpers.SimpleSatisfaction;

/**
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class SatisfactionTag
    extends AbstractTag
{
    private SimpleSatisfaction satisfaction;

    public void begin(String elementName,
                      Attributes attributes)
        throws TagException, TagalogParseException
    {
        String id = requireAttribute( attributes,
                                      elementName,
                                      "id" );

        satisfaction = new SimpleSatisfaction( id );

        super.begin( elementName,
                     attributes );
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
