package com.werken.werkflow.definition.fundamental;

import com.werken.werkflow.definition.petri.DefaultNet;

import org.apache.commons.jelly.XMLOutput;

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
        throws Exception
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
}
