/*
 * $Id$
 */

package org.codehaus.werkflow.tagalog;

import org.codehaus.tagalog.Attributes;
import org.codehaus.tagalog.TagException;
import org.codehaus.tagalog.TagalogParseException;
import org.codehaus.werkflow.idioms.Sequence;
import org.codehaus.werkflow.idioms.interactive.Choice;

/**
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class OptionTag
    extends SequenceTag
{
    private Choice choice;

    public void begin(String elementName,
                      Attributes attributes)
        throws TagException, TagalogParseException
    {
        requireParent( elementName,
                       "choice",
                       ChoiceTag.class );

        String id = requireAttribute( attributes,
                                      elementName,
                                      "id" );

        choice = new Choice( id );

        super.begin( elementName,
                     attributes );
    }

    public Object end(String elementName)
        throws TagException
    {
        Sequence sequence = (Sequence) super.end( elementName );

        choice.setBody( sequence );

        ChoiceTag choiceTag = (ChoiceTag) getParent();

        choiceTag.addChoice( choice );

        return choice;
    }
}
