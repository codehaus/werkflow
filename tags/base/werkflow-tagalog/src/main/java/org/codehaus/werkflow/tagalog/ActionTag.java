/*
 * $Id$
 */

package org.codehaus.werkflow.tagalog;

import java.util.Properties;

import org.codehaus.tagalog.AbstractTag;
import org.codehaus.tagalog.Attributes;
import org.codehaus.tagalog.TagException;
import org.codehaus.tagalog.TagalogParseException;
import org.codehaus.werkflow.simple.Action;
import org.codehaus.werkflow.simple.ActionManager;

/**
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class ActionTag
    extends AbstractTag
{
    private Action action;

    public void begin(String elementName,
                      Attributes attributes)
        throws TagException, TagalogParseException
    {
        String id = requireAttribute( attributes,
                                      elementName,
                                      "id" );

        int len = attributes.getAttributeCount();

        Properties properties = new Properties();

        for ( int i = 0 ; i < len ; ++i )
        {
            String name = attributes.getName( i );

            if ( ! "id".equals( name ) )
            {
                String value = attributes.getValue( i );
                properties.setProperty( name,
                                        value );
            }
        }

        ActionManager actionManager = (ActionManager) getContext().get( "actionManager" );

        if ( actionManager == null )
        {
            throw new TagException( "no action manager in parse context" );
        }

        action = new Action( actionManager,
                             id,
                             properties );

        super.begin( elementName,
                     attributes );
    }

    public Object end(String elementName)
        throws TagException
    {
        return action;
    }

    public boolean recycle()
    {
        action = null;

        return super.recycle();
    }
}
