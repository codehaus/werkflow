package com.werken.werkflow.syntax.idiomatic;

import org.apache.commons.jelly.TagLibrary;

public class IdiomaticTagLibrary
    extends TagLibrary
{
    public static final String NS_URI = "werkflow:idiomatic";

    public IdiomaticTagLibrary()
    {
        registerTag( "process",
                     ProcessTag.class );

        registerTag( "sequence",
                     SequenceTag.class );

        registerTag( "parallel",
                     ParallelTag.class );

        registerTag( "while",
                     WhileTag.class );

        registerTag( "if",
                     IfTag.class );

        registerTag( "switch",
                     SwitchTag.class );

        registerTag( "case",
                     CaseTag.class );

        registerTag( "pick",
                     PickTag.class );

        registerTag( "on-message",
                     OnMessageTag.class );

        registerTag( "terminate",
                     TerminateTag.class );
    }
}
