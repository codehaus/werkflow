/*
 * $Id$
 */

package org.codehaus.werkflow.tagalog;

import org.codehaus.tagalog.Attributes;
import org.codehaus.tagalog.TagException;
import org.codehaus.tagalog.TagalogParseException;
import org.codehaus.tagalog.el.ExpressionParseException;

import org.codehaus.werkflow.spi.Component;
import org.codehaus.werkflow.spi.Expression;

/**
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class WhenTag
    extends SequenceTag
{
    private Expression test;

    public void begin(String elementName, Attributes attributes)
        throws TagException, TagalogParseException
    {
        requireParent( elementName,
                       "choose",
                       ChooseTag.class);

        String testText = requireAttribute( attributes,
                                            elementName,
                                            "test" );

        try
        {
            test = ExpressionWrapper.newExpression( testText );
        }
        catch (ExpressionParseException e)
        {
            throw new TagException( "could not parse test",
                                    e );
        }

        super.begin(elementName, attributes);
    }

    public Object end(String elementName)
        throws TagException
    {
        Component body = (Component) super.end(elementName);

        ChooseTag parent = (ChooseTag) getParent();

        parent.addCase( test, body );

        return null;
    }
}
