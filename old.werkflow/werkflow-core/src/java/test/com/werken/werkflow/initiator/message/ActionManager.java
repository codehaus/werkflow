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
    }
}
