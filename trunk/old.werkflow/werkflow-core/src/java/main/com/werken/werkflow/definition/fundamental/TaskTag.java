package com.werken.werkflow.definition.fundamental;

import com.werken.werkflow.action.Action;
import com.werken.werkflow.task.DefaultTask;

import org.apache.commons.jelly.XMLOutput;

public class TaskTag
    extends FundamentalTagSupport
{
    private Action action;

    public TaskTag()
    {

    }

    public void setAction(Action action)
    {
        this.action = action;
    }

    public Action getAction()
    {
        return this.action;
    }

    public void doTag(XMLOutput output)
        throws Exception
    {
        TransitionTag transition = (TransitionTag) requiredAncestor( "transition",
                                                                     TransitionTag.class );

        DefaultTask task = new DefaultTask();

        invokeBody( output );

        if ( getAction() != null )
        {
            task.setAction( getAction() );

            transition.setTask( task );
        }

        this.action = null;
    }
}
