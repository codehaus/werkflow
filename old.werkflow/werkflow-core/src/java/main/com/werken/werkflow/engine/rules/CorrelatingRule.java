package com.werken.werkflow.engine.rules;

import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.service.messaging.MessageSelector;

import org.drools.rule.Rule;
import org.drools.rule.Declaration;

public class CorrelatingRule
    extends Rule
{
    public static final String MESSAGE_DECL_ID = "werkflow.message";

    private MessageType messageType;

    private Declaration messageDecl;

    public CorrelatingRule(MessageType messageType,
                           Transition transition)
    {
        super( "correlating." + transition.getId() + "." + messageType.getId() );

        this.messageType = messageType;

        addDecls();
    }

    protected void addDecls()
    {
        this.messageDecl = new Declaration( new MessageTypeObjectType( getMessageType() ),
                                            MESSAGE_DECL_ID );

        addParameterDeclaration( this.messageDecl );
    }

    public MessageType getMessageType()
    {
        return this.messageType;
    }

    protected Declaration getMessageDeclaration()
    {
        return this.messageDecl;
    }
}
