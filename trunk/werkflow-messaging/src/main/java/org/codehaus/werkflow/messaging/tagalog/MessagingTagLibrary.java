/*
 * $Id$
 */

package org.codehaus.werkflow.messaging.tagalog;

import org.codehaus.tagalog.AbstractTagLibrary;

/**
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class MessagingTagLibrary
    extends AbstractTagLibrary
{
    public static final String NS_URI = "http://werkflow.codehaus.org/messaging";

    public MessagingTagLibrary()
    {
        registerTag( "receive", ReceiveTag.class );
    }
}
