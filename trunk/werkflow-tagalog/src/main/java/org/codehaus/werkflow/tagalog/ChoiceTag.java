/*
 * $Id$
 */

package org.codehaus.werkflow.tagalog;

import org.codehaus.tagalog.AbstractTag;
import org.codehaus.tagalog.Attributes;
import org.codehaus.tagalog.TagException;
import org.codehaus.werkflow.idioms.interactive.Choice;
import org.codehaus.werkflow.idioms.interactive.MultipleChoice;

/**
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class ChoiceTag
    extends AbstractTag
{
    private MultipleChoice multipleChoice;

    public void begin(String elementName,
                      Attributes attributes)
        throws TagException
    {
        String id = requireAttribute( attributes,
                                      elementName,
                                      "id" );

        multipleChoice = new MultipleChoice( id );
    }

    public void addChoice(Choice choice)
    {
        multipleChoice.addChoice( choice );
    }

    public Object end(String elementName)
        throws TagException
    {
        return multipleChoice;
    }

    public boolean recycle()
    {
        multipleChoice = null;

        return super.recycle();
    }
}
