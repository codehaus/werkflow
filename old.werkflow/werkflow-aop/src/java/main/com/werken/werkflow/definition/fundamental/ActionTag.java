package com.werken.werkflow.definition.fundamental;

import com.werken.werkflow.action.Action;
import com.werken.werkflow.jelly.JellyAction;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyException;

public class ActionTag
    extends FundamentalTagSupport
{
    private String className;

    public ActionTag()
    {

    }

    public void setClass(String className)
    {
        this.className = className;
    }

    public void doTag(XMLOutput output)
        throws Exception
    {
        TaskTag task = (TaskTag) requiredAncestor( "task",
                                                   TaskTag.class );

        Action action = null;

        if ( this.className == null )
        {
            action = new JellyAction( getBody() );
        }
        else
        {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();

            if ( cl == null )
            {
                cl = getClass().getClassLoader();
            }

            Class actionClass = cl.loadClass( this.className );

            if ( ! Action.class.isAssignableFrom( actionClass ) )
            {
                throw new JellyException( this.className + " does not implement com.werken.werkflow.action.Action" );
            }

            action = (Action) actionClass.newInstance();
        }

        task.setAction( action );
        
        this.className = null;
    }
}

