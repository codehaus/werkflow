package com.werken.werkflow.syntax.idiom;

import org.apache.commons.jelly.TagLibrary;

public class IdiomTagLibrary
    extends TagLibrary
{
    public IdiomTagLibrary()
    {
        registerTag( "idiom",
                     IdiomTag.class );

        registerTag( "parameter",
                     ParameterTag.class );

        registerTag( "template",
                     TemplateTag.class );

        registerTag( "place",
                     PlaceTag.class );

        registerTag( "transition",
                     TransitionTag.class );

        registerTag( "input",
                     InputTag.class );

        registerTag( "output",
                     OutputTag.class );
    }
}
