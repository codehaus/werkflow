package org.codehaus.werkflow;

import java.util.List;
import java.util.ArrayList;

public class Touch
    implements SyncComponent
{
    private String id;

    public Touch(String id)
    {
        this.id = id;
    }

    public void perform(Instance instance)
    {
        List touches = (List) instance.get( "touches" );

        if ( touches == null )
        {
            touches = new ArrayList();
            instance.put( "touches",
                          touches );
        }

        touches.add( this.id );
    }
}
