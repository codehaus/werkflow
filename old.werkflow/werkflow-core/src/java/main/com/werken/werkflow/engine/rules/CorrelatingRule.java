package com.werken.werkflow.engine.rules;

import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.service.messaging.MessageSelector;
import com.werken.werkflow.engine.MessageBlocker;

import org.drools.rule.Rule;
import org.drools.rule.Declaration;

public class CorrelatingRule
    extends Rule
{
    public static final String MESSAGE_DECL_ID = "werkflow.message";
    public static final String BLOCKER_DECL_ID = "werkflow.blocker";

    private Transition transition;
    private MessageType messageType;

    private Declaration messageDecl;
    private Declaration blockerDecl;

    public CorrelatingRule(Transition transition,
                           MessageType messageType)
    {
        super( "correlating." + transition.getId() + "." + messageType.getId() );

        this.messageType = messageType;
        this.transition  = transition;

        addDecls();
    }

    protected void addDecls()
    {
        this.messageDecl = new Declaration( new MessageTypeObjectType( getMessageType() ),
                                            MESSAGE_DECL_ID );

        addParameterDeclaration( this.messageDecl );

        this.blockerDecl = new Declaration( new MessageBlockerObjectType( getMessageType() ),
                                            BLOCKER_DECL_ID );

        addParameterDeclaration( this.blockerDecl );
    }

    public MessageType getMessageType()
    {
        return this.messageType;
    }

    public Transition getTransition()
    {
        return this.transition;
    }

    protected Declaration getMessageDeclaration()
    {
        return this.messageDecl;
    }
}
