package com.werken.werkflow.syntax;

import org.apache.commons.jelly.TagLibrary;
import org.apache.commons.jelly.tags.define.InvokeBodyTag;

public class SyntaxTagLibrary
    extends TagLibrary
{
    public static final String NAMESPACE_URI = "werkflow:syntax";

    public SyntaxTagLibrary()
    {
        registerTag( "syntax",
                     SyntaxTag.class );

        registerTag( "tag",
                     TagTag.class );

        registerTag( "invokeBody",
                     InvokeBodyTag.class );

        registerTag( "import",
                     ImportTag.class );

        registerTag( "expr-factory",
                     ExprFactoryTag.class );
    }
}
