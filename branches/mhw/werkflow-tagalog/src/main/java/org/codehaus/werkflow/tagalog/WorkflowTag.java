/*
 * $Id$
 */

package org.codehaus.werkflow.tagalog;

import org.codehaus.tagalog.Attributes;
import org.codehaus.tagalog.TagException;
import org.codehaus.tagalog.TagalogParseException;
import org.codehaus.werkflow.Workflow;
import org.codehaus.werkflow.idioms.Sequence;

/**
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class WorkflowTag
    extends SequenceTag
{
    private Workflow workflow;

    public void begin(String elementName,
                      Attributes attributes)
        throws TagException, TagalogParseException
    {
        if ( getParent() != null )
        {
            throw new TagException( "<workflow> can only be root element" );
        }

        String id = requireAttribute( attributes,
                                      elementName,
                                      "id" );

        workflow = new Workflow( id );

        super.begin( elementName,
                     attributes );
    }

    public Object end(String elementName)
        throws TagException
    {
        Sequence sequence = (Sequence) super.end( elementName );

        workflow.setBody( sequence );

        return workflow;
    }

    public boolean recycle()
    {
        workflow = null;

        return super.recycle();
    }
}
