package com.werken.werkflow.syntax.idiomatic;

import org.apache.commons.jelly.TagLibrary;

public class IdiomaticTagLibrary
    extends TagLibrary
{
    public static final String NS_URI = "werkflow:idiomatic";

    public IdiomaticTagLibrary()
    {
        registerTag( "package",
                     com.werken.werkflow.syntax.fundamental.PackageTag.class );

        registerTag( "action-definition",
                     com.werken.werkflow.syntax.fundamental.ActionDefinitionTag.class );

        registerTag( "message-types",
                     com.werken.werkflow.syntax.fundamental.MessageTypesTag.class );

        registerTag( "message-type",
                     com.werken.werkflow.syntax.fundamental.MessageTypeTag.class );

        registerTag( "import",
                     com.werken.werkflow.syntax.fundamental.ImportTag.class );

        registerTag( "documentation",
                     com.werken.werkflow.syntax.fundamental.DocumentationTag.class );

        registerTag( "action",
                     com.werken.werkflow.syntax.fundamental.ActionTag.class );

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

        registerTag( "otherwise",
                     OtherwiseTag.class );

        registerTag( "pick",
                     PickTag.class );

        registerTag( "on-message",
                     OnMessageTag.class );

        registerTag( "terminate",
                     TerminateTag.class );
    }
}
