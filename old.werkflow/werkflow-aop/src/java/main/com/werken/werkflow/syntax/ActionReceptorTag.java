package com.werken.werkflow.syntax;

import com.werken.werkflow.work.Action;
import com.werken.werkflow.jelly.MiscTagSupport;
import com.werken.werkflow.syntax.fundamental.ActionReceptor;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class ActionReceptorTag
    extends MiscTagSupport
    implements ActionReceptor
{
    private String var;

    public ActionReceptorTag()
    {

    }

    public String getVar()
    {
        return this.var;
    }

    public void setVar(String var)
    {
        this.var = var;
    }

    public void receiveAction(Action action)
        throws JellyTagException
    {
        getContext().setVariable( getVar(),
                                  action );
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        requireStringAttribute( "var",
                                getVar() );

        pushObject( ActionReceptor.class,
                    this );

        invokeBody( output );

        popObject( ActionReceptor.class );
    }
}
