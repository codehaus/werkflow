/*
 * $Id$
 */

package org.codehaus.werkflow.tagalog;

import org.codehaus.tagalog.AbstractTag;
import org.codehaus.tagalog.Attributes;
import org.codehaus.tagalog.TagBinding;
import org.codehaus.tagalog.TagException;
import org.codehaus.tagalog.TagalogParseException;
import org.codehaus.tagalog.el.ExpressionParseException;
import org.codehaus.werkflow.idioms.IfElse;
import org.codehaus.werkflow.spi.Component;
import org.codehaus.werkflow.spi.Expression;

/**
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class IfTag
    extends AbstractTag
{
    private IfElse ifElse;

    public void begin(String elementName,
                      Attributes attributes)
        throws TagException
    {
        String test = requireAttribute( attributes,
                                        elementName,
                                        "test" );

        try
        {
            Expression expression = ExpressionWrapper.newExpression( test );

            ifElse = new IfElse( expression );
        }
        catch (ExpressionParseException e)
        {
            throw new TagException( "could not parse test",
                                    e );
        }
    }

    public void child(TagBinding childType, Object child)
        throws TagException, TagalogParseException
    {
        if ( childType == SimpleWerkflowTagLibrary.THEN )
        {
            if ( ifElse.getTrueBody() != null )
            {
                throw new TagException( "<then> may be specified at most once" );
            }

            ifElse.setTrueBody( (Component) child );
        }
        else if ( childType == SimpleWerkflowTagLibrary.ELSE )
        {
            if ( ifElse.getTrueBody() == null )
            {
                throw new TagException( "<then> must come before <else>" );
            }

            if ( ifElse.getFalseBody() != null )
            {
                throw new TagException( "<else> may be specified at most once" );
            }

            ifElse.setFalseBody( (Component) child );
        }
    }

    public Object end(String elementName)
        throws TagException
    {
        return ifElse;
    }

    public boolean recycle()
    {
        ifElse = null;

        return super.recycle();
    }
}
