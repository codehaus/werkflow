package com.werken.werkflow.definition.fundamental;

import com.werken.werkflow.action.Action;

import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.JellyException;

public abstract class AbstractActionTag
    extends TagSupport
{
    public AbstractActionTag()
    {
    }

    public void setAction(Action action)
        throws Exception
    {
        TaskTag task = (TaskTag) findAncestorWithClass( TaskTag.class );

        if ( task == null )
        {
            throw new JellyException( "not within <task>" );
        }

        task.setAction( action );
    }
}
