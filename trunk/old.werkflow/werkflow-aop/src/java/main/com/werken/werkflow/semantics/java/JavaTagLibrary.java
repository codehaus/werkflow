package com.werken.werkflow.semantics.java;

import org.apache.commons.jelly.TagLibrary;

public class JavaTagLibrary
    extends TagLibrary
{
    public JavaTagLibrary()
    {
        registerTag( "selector",
                     ClassMessageSelectorTag.class );
    }
}
