package com.werken.werkflow.syntax.bpel4ws;

import org.apache.commons.jelly.TagLibrary;

public class Bpel4wsTagLibrary
    extends TagLibrary
{
    public Bpel4wsTagLibrary()
    {
        registerTag( "process",
                     ProcessTag.class );

        registerTag( "sequence",
                     SequenceTag.class );

        registerTag( "flow",
                     FlowTag.class );

        registerTag( "empty",
                     EmptyTag.class );

        /*
        registerTag( "while",
                     WhileTag.class );
        */
    }
}
