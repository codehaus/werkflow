/*
 * $Id$
 */

package org.codehaus.werkflow.messaging.tagalog;

import org.codehaus.tagalog.AbstractTagLibrary;
import org.codehaus.tagalog.TagBinding;

/**
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class MessagingTagLibrary
    extends AbstractTagLibrary
{
    public static final String NS_URI = "http://werkflow.codehaus.org/messaging";

    public static final TagBinding RECEIVE = new TagBinding( "receive", ReceiveTag.class );

    public MessagingTagLibrary()
    {
        registerTagBinding( RECEIVE );
    }
}
