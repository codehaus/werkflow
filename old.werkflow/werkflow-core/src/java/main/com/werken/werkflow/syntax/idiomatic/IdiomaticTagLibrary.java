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
    }
}
