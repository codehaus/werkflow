package com.werken.werkflow.core;

import com.werken.werkflow.WerkflowTestCase;
import com.werken.werkflow.service.messaging.Message;
import com.werken.werkflow.service.messaging.DefaultMessage;

public class CoreActivityTest
    extends WerkflowTestCase
{
    public void testConstruct()
    {
        Message message = new DefaultMessage( "the.message",
                                              null,
                                              new Object() );

        CoreWorkItem workItem = new CoreWorkItem( null,
                                                  null,
                                                  null );

        CoreActivity activity = new CoreActivity( workItem,
                                                  message );

        assertSame( workItem,
                    activity.getWorkItem() );

        assertSame( message,
                    activity.getMessage() );
    }
}
