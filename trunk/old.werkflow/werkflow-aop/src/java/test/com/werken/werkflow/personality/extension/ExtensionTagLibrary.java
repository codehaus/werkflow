package com.werken.werkflow.personality.extension;

import org.apache.commons.jelly.TagLibrary;

/**
 * Test tag library.
 *
 * @author Mark Wilkinson
 */
public class ExtensionTagLibrary extends TagLibrary
{
    public static final String NAMESPACE_URI = "werkflow:test";

    public ExtensionTagLibrary()
    {
        registerTag( "foo", FooTag.class );
    }
}
