package com.werken.werkflow.definition.fundamental;

import org.apache.commons.jelly.TagLibrary;

public class FundamentalTagLibrary
    extends TagLibrary
{
    public FundamentalTagLibrary()
    {
        registerTag( "process",
                     ProcessTag.class );

        registerTag( "place",
                     PlaceTag.class );

        registerTag( "transition",
                     TransitionTag.class );

        registerTag( "input",
                     InputTag.class );

        registerTag( "output",
                     OutputTag.class );

        registerTag( "documentation",
                     DocumentationTag.class );

        registerTag( "task",
                     TaskTag.class );

        registerTag( "action",
                     ActionTag.class );

        registerTag( "message-type",
                     MessageTypeTag.class );
    }
}
