package com.werken.werkflow.definition.fundamental;

import com.werken.werkflow.definition.petri.DefaultNet;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class OutputTag
    extends FundamentalTagSupport
{
    private String to;

    public OutputTag()
    {

    }

    public void setTo(String to)
    {
        this.to = to;
    }

    public String getTo()
    {
        return this.to;
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        try
        {
            ProcessTag process = (ProcessTag) requiredAncestor( "process",
                                                                ProcessTag.class );
            
            TransitionTag transition = (TransitionTag) requiredAncestor( "transition",
                                                                         TransitionTag.class );
            
            requireStringAttribute( "to",
                                    getTo() );
            
            DefaultNet net = process.getNet();
            
            net.connectTransitionToPlace( transition.getId(),
                                          getTo() );
            
            invokeBody( output );
        }
        catch (Exception e)
        {
            throw new JellyTagException( e );
        }
    }
}
