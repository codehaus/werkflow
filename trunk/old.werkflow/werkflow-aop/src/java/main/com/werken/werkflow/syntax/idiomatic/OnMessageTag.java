package com.werken.werkflow.syntax.idiomatic;

import com.werken.werkflow.definition.MessageCorrelator;
import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.definition.NoSuchMessageTypeException;
import com.werken.werkflow.definition.idiomatic.Segment;
import com.werken.werkflow.definition.idiomatic.MessageConditionalSegment;
import com.werken.werkflow.definition.idiomatic.SequenceSegment;
import com.werken.werkflow.semantics.jelly.JellyExpression;

import org.apache.commons.jelly.expression.Expression;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class OnMessageTag
    extends ComplexActionTagSupport
{
    private String type;
    private String var;
    private MessageCorrelator messageCorrelator;

    public OnMessageTag()
    {

    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return this.type;
    }

    public void setVar(String var)
    {
        this.var = var;
    }

    public String getVar()
    {
        return this.var;
    }

    public void receiveMessageCorrelator(MessageCorrelator messageCorrelator)
    {
        this.messageCorrelator = messageCorrelator;
    }

    public MessageCorrelator getMessageCorrelator()
    {
        return this.messageCorrelator;
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        requireStringAttribute( "var",
                                getVar() );

        requireStringAttribute( "type",
                                getType() );

        try
        {
            MessageType msgType = getCurrentScope().getMessageType( getType() );
            
            MessageWaiter msgWaiter = new MessageWaiter( msgType,
                                                         getVar() );
            
            msgWaiter.setMessageCorrelator( getMessageCorrelator() );
            
            MessageConditionalSegment msgSegment = new MessageConditionalSegment( msgWaiter );
            
            pushSegment( msgSegment );
            
            invokeBody( output );
            
            popSegment();
        }
        catch (NoSuchMessageTypeException e)
        {
            throw new JellyTagException( e );
        }
    }
}
