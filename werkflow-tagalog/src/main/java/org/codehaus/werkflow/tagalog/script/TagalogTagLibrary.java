/*
 * $Id$
 */

package org.codehaus.werkflow.tagalog.script;

import org.codehaus.tagalog.AbstractTagLibrary;
import org.codehaus.tagalog.TagBinding;

/**
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class TagalogTagLibrary
    extends AbstractTagLibrary
{
    public static final String NS_URI = "http://werkflow.codehaus.org/tagalog";

    public static final TagBinding ACTION = new TagBinding( "action",
                                                            ActionTag.class );

    public TagalogTagLibrary()
    {
        registerTagBinding( ACTION );
    }
}
