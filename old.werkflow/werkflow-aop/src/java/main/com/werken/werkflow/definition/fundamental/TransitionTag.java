package com.werken.werkflow.definition.fundamental;

import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.definition.petri.DefaultNet;
import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.definition.petri.AndInputRule;
import com.werken.werkflow.definition.petri.OrInputRule;
import com.werken.werkflow.semantics.jelly.JellyExpression;
import com.werken.werkflow.task.Task;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.expression.Expression;

public class TransitionTag
    extends FundamentalTagSupport
    implements DocumentableTag
{
    private String id;

    private String type;

    private String documentation;

    private Expression expression;

    private Task task;

    private MessageWaiter messageWaiter;

    public TransitionTag()
    {

    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public void setDocumentation(String documentation)
    {
        this.documentation = documentation;
    }

    public String getDocumentation()
    {
        return this.documentation;
    }

    public void setTest(Expression expression)
    {
        this.expression = expression;
    }

    public Expression getTest()
    {
        return this.expression;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return this.type;
    }

    public void setTask(Task task)
    {
        this.task = task;
    }

    public Task getTask()
    {
        return this.task;
    }

    public void setMessageWaiter(MessageWaiter messageWaiter)
    {
        this.messageWaiter = messageWaiter;
    }

    public MessageWaiter getMessageWaiter()
    {
        return this.messageWaiter;
    }

    public void doTag(XMLOutput output)
        throws Exception
    {
        ProcessTag process = (ProcessTag) requiredAncestor( "process",
                                                            ProcessTag.class );

        requireStringAttribute( "id",
                                getId() );

        DefaultNet net = process.getNet();

        DefaultTransition transition = net.addTransition( getId() );

        if ( getType() == null
             ||
             getType().equals( "and" ) )
        {
            transition.setActivationRule( AndInputRule.getInstance() );
        }
        else if ( getType().equals( "or" ) )
        {
            transition.setActivationRule( OrInputRule.getInstance() );
        }
        else
        {
            throw new JellyException( "invalid 'type' attribute; must be one of: 'and' 'or'" );
        }

        setDocumentation( null );

        invokeBody( output );

        transition.setDocumentation( getDocumentation() );

        if ( getTest() != null )
        {
            transition.setExpression( new JellyExpression( getTest() ) );
        }

        if ( getTask() != null )
        {
            transition.setTask( getTask() );
        }

        if ( getMessageWaiter() != null )
        {
            transition.setMessageWaiter( getMessageWaiter() );
        }

        this.expression = null;
        this.task       = null;
    }
}
