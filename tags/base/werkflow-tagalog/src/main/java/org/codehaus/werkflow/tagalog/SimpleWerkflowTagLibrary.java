/*
 * $Id$
 */

package org.codehaus.werkflow.tagalog;

import org.codehaus.tagalog.AbstractTagLibrary;

/**
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class SimpleWerkflowTagLibrary
    extends AbstractTagLibrary
{
    public static final String NS_URI = "http://werkflow.codehaus.org/simple";

    public SimpleWerkflowTagLibrary()
    {
        registerTag( "workflow",     WorkflowTag.class );
        registerTag( "sequence",     SequenceTag.class );
        registerTag( "parallel",     ParallelTag.class );
        registerTag( "if",           IfTag.class );
        registerTag( "then",         ThenTag.class );
        registerTag( "else",         ElseTag.class );
        registerTag( "while",        WhileTag.class );
        registerTag( "satisfaction", SatisfactionTag.class );
        registerTag( "action",       ActionTag.class );
        registerTag( "choice",       ChoiceTag.class );
        registerTag( "option",       OptionTag.class );
    }
}
