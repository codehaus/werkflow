package com.werken.werkflow.definition.fundamental;

import com.werken.werkflow.definition.petri.DefaultNet;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class InputTag
    extends FundamentalTagSupport
{
    private String from;

    public InputTag()
    {

    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getFrom()
    {
        return this.from;
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
            
            requireStringAttribute( "from",
                                    getFrom() );
            
            DefaultNet net = process.getNet();
            
            net.connectPlaceToTransition( getFrom(),
                                          transition.getId() );
            
            
            invokeBody( output );
        }
        catch (Exception e)
        {
            throw new JellyTagException( e );
        }
    }
}
