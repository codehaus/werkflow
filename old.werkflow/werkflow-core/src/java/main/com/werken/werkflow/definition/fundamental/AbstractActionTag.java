package com.werken.werkflow.definition.fundamental;

import com.werken.werkflow.action.Action;

import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.JellyException;

public abstract class AbstractActionTag
    extends TagSupport
{
    private String id;

    public AbstractActionTag()
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

    public void setAction(Action action)
        throws Exception
    {
        ActionReceptor receptor = (ActionReceptor) findAncestorWithClass( ActionReceptor.class );

        if ( receptor == null )
        {
            throw new JellyException( "invalid context for <action>" );
        }

        receptor.setAction( action );
    }
}
