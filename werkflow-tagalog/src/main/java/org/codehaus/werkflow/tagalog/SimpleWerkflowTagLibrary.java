/*
 * $Id$
 */

package org.codehaus.werkflow.tagalog;

import org.codehaus.tagalog.AbstractTagLibrary;
import org.codehaus.tagalog.TagBinding;

/**
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class SimpleWerkflowTagLibrary
    extends AbstractTagLibrary
{
    public static final String NS_URI = "http://werkflow.codehaus.org/simple";

    public static final TagBinding WORKFLOW     = new TagBinding( "workflow",     WorkflowTag.class );
    public static final TagBinding SEQUENCE     = new TagBinding( "sequence",     SequenceTag.class );
    public static final TagBinding PARALLEL     = new TagBinding( "parallel",     ParallelTag.class );
    public static final TagBinding IF           = new TagBinding( "if",           IfTag.class );
    public static final TagBinding THEN         = new TagBinding( "then",         ThenElseTag.class );
    public static final TagBinding ELSE         = new TagBinding( "else",         ThenElseTag.class );
    public static final TagBinding WHILE        = new TagBinding( "while",        WhileTag.class );
    public static final TagBinding CHOOSE       = new TagBinding( "choose",       ChooseTag.class );
    public static final TagBinding WHEN         = new TagBinding( "when",         WhenTag.class );
    public static final TagBinding OTHERWISE    = new TagBinding( "otherwise",    SequenceTag.class );
    public static final TagBinding SATISFACTION = new TagBinding( "satisfaction", SatisfactionTag.class );
    public static final TagBinding ACTION       = new TagBinding( "action",       ActionTag.class );
    public static final TagBinding CHOICE       = new TagBinding( "choice",       ChoiceTag.class );
    public static final TagBinding OPTION       = new TagBinding( "option",       OptionTag.class );

    public SimpleWerkflowTagLibrary()
    {
        registerTagBinding( WORKFLOW );
        registerTagBinding( SEQUENCE );
        registerTagBinding( PARALLEL );
        registerTagBinding( IF );
        registerTagBinding( THEN );
        registerTagBinding( ELSE );
        registerTagBinding( WHILE );
        registerTagBinding( CHOOSE );
        registerTagBinding( WHEN );
        registerTagBinding( OTHERWISE );
        registerTagBinding( SATISFACTION );
        registerTagBinding( ACTION );
        registerTagBinding( CHOICE );
        registerTagBinding( OPTION );
    }
}
