package com.werken.werkflow.syntax.bpel4ws;

import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.definition.MessageInitiator;
import com.werken.werkflow.definition.idiomatic.NetBuilder;
import com.werken.werkflow.definition.idiomatic.Segment;
import com.werken.werkflow.definition.idiomatic.UnsupportedIdiomException;
import com.werken.werkflow.definition.petri.Net;
import com.werken.werkflow.definition.petri.PetriException;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class ProcessTag
    extends Bpel4wsTagSupport
    implements SegmentReceptor
{
    private String targetNamespace;
    private String name;

    private Segment segment;

    public ProcessTag()
    {

    }

    public void setTargetNamespace(String targetNamespace)
    {
        this.targetNamespace = targetNamespace;
    }

    public String getTargetNamespace()
    {
        return this.targetNamespace;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void receiveSegment(Segment segment)
        throws UnsupportedIdiomException
    {
        this.segment = segment;
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        invokeBody( output );

        NetBuilder builder = new NetBuilder();

        try
        {
            Net net = builder.build( this.segment );

            ProcessDefinition processDef = new ProcessDefinition( getTargetNamespace() + "#" + getName(),
                                                                  net,
                                                                  MessageInitiator.EMPTY_ARRAY );
        }
        catch (PetriException e)
        {
            throw new JellyTagException( e );
        }

    }
}
