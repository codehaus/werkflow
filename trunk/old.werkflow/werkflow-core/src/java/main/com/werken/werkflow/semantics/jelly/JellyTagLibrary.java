package com.werken.werkflow.semantics.jelly;

import org.apache.commons.jelly.TagLibrary;

public class JellyTagLibrary
extends TagLibrary
{
    public JellyTagLibrary()
    {
        registerTag( "correlator",
                     JellyMessageCorrelatorTag.class );

        registerTag( "action",
                     JellyActionTag.class );
    }
}

