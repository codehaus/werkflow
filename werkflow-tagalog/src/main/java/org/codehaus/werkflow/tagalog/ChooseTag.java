/*
 * $Id$
 */

package org.codehaus.werkflow.tagalog;

import org.codehaus.tagalog.AbstractTag;
import org.codehaus.tagalog.Attributes;
import org.codehaus.tagalog.TagBinding;
import org.codehaus.tagalog.TagException;
import org.codehaus.tagalog.TagalogParseException;

import org.codehaus.werkflow.idioms.ChooseCase;
import org.codehaus.werkflow.spi.Component;
import org.codehaus.werkflow.spi.Expression;

/**
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class ChooseTag
    extends AbstractTag
{
    private ChooseCase chooseCase;

    public void begin(String elementName,
                      Attributes attributes)
        throws TagException
    {
        chooseCase = new ChooseCase();
    }

    public void addCase(Expression expression,
                        Component body)
    {
            chooseCase.addCase( expression,
                                body );
    }

    public void child(TagBinding childType,
                      Object child)
        throws TagException, TagalogParseException
    {
        if ( childType == SimpleWerkflowTagLibrary.OTHERWISE )
        {
            chooseCase.setDefaultBranch( (Component) child );
        }
    }

    public Object end(String elementName)
        throws TagException
    {
        return chooseCase;
    }

    public boolean recycle()
    {
        chooseCase = null;

        return super.recycle();
    }
}
