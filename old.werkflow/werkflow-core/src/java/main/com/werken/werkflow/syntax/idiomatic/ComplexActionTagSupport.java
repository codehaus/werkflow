package com.werken.werkflow.syntax.idiomatic;

import com.werken.werkflow.action.Action;
import com.werken.werkflow.definition.idiomatic.ActionSegment;
import com.werken.werkflow.syntax.fundamental.ActionReceptor;

import org.apache.commons.jelly.JellyTagException;

public abstract class ComplexActionTagSupport
    extends IdiomaticTagSupport
    implements ActionReceptor
{
    public ComplexActionTagSupport()
    {

    }

    public void receiveAction(Action action)
        throws JellyTagException
    {
        pushSegment( new ActionSegment( action ) );
        popSegment();
    }
}
