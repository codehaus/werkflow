package com.werken.werkflow.syntax.petri;

import org.apache.commons.jelly.TagLibrary;

public class PetriTagLibrary
    extends TagLibrary
{
    public static final String NS_URI = "werkflow:petri";

    public PetriTagLibrary()
    {
        registerTag( "net",
                     NetTag.class );

        registerTag( "place",
                     PlaceTag.class );

        registerTag( "transition",
                     TransitionTag.class );

        registerTag( "input",
                     InputTag.class );

        registerTag( "output",
                     OutputTag.class );

        registerTag( "task",
                     TaskTag.class );

        registerTag( "message",
                     MessageTag.class );
    }
}
