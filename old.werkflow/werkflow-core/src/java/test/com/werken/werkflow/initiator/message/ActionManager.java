package com.werken.werkflow.initiator.message;

import java.util.Map;

/**
 *
 *
 * @author <a href="mailto:jason@maven.org">Jason van Zyl</a>
 *
 * @version $Id$
 */
public class ActionManager
{
    public void executeAction( Map caseAttributes, Map otherAttributes )
        throws Exception
    {
        System.out.println( "ACTION-MANAGER: caseAttributes = " + caseAttributes );
        System.out.println( "ACTION-MANAGER: otherAttributes = " + otherAttributes );

        Entity entity = (Entity) otherAttributes.get( "message" );

        System.out.println( "ACTION-MANAGER: entity = " + entity );

        // Now we decide what we want to do with the entity based on
        // the action id.
        String actionId = (String) otherAttributes.get( "actionId" );

        if ( actionId == null )
        {
            System.out.println( "ACTION-MANAGER: We have a problem: the actionId is null." );
        }
        else
        {
            System.out.println( "ACTION-MANAGER: actionId = " + actionId );
        }
    }
}
