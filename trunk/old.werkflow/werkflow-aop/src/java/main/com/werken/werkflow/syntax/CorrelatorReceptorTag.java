package com.werken.werkflow.syntax;

import com.werken.werkflow.definition.MessageCorrelator;
import com.werken.werkflow.jelly.MiscTagSupport;
import com.werken.werkflow.syntax.fundamental.MessageCorrelatorReceptor;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class CorrelatorReceptorTag
    extends MiscTagSupport
    implements MessageCorrelatorReceptor
{
    private String var;
    private String bind;

    public CorrelatorReceptorTag()
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

    public void setBind(String bind)
    {
        this.bind = bind;
    }

    public String getBind()
    {
        return this.bind;
    }

    public String getMessageId()
    {
        return getBind();
    }

    public void receiveMessageCorrelator(MessageCorrelator correlator)
    {
        getContext().setVariable( getVar(),
                                  correlator );
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        requireStringAttribute( "var",
                                getVar() );

        pushObject( MessageCorrelatorReceptor.class,
                    this );

        invokeBody( output );

        popObject( MessageCorrelatorReceptor.class );
    }
}
