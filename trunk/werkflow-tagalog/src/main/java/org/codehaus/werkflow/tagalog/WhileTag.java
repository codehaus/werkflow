/*
 * $Id$
 */

package org.codehaus.werkflow.tagalog;

import org.codehaus.tagalog.Attributes;
import org.codehaus.tagalog.Tag;
import org.codehaus.tagalog.TagException;
import org.codehaus.tagalog.TagalogParseException;
import org.codehaus.tagalog.el.ExpressionParseException;
import org.codehaus.werkflow.idioms.Sequence;
import org.codehaus.werkflow.idioms.While;
import org.codehaus.werkflow.spi.Expression;

/**
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class WhileTag
    extends SequenceTag
    implements Tag
{
    private While whileComponent;

    public void begin(String elementName,
                      Attributes attributes)
        throws TagException, TagalogParseException
    {
        String test = requireAttribute( attributes,
                                        elementName,
                                        "test" );

        try
        {
            Expression expression = ExpressionWrapper.newExpression( test );

            whileComponent = new While( expression );
        }
        catch (ExpressionParseException e)
        {
            throw new TagException( "could not parse test",
                                    e );
        }

        super.begin( elementName,
                     attributes );
    }

    public Object end(String elementName)
        throws TagException
    {
        Sequence sequence = (Sequence) super.end( elementName );

        if ( whileComponent != null )
        {
            whileComponent.setBody( sequence );
        }

        return whileComponent;
    }

    public boolean recycle()
    {
        whileComponent = null;

        return super.recycle();
    }
}
