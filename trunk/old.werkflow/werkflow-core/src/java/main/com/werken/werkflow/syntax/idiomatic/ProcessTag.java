package com.werken.werkflow.syntax.idiomatic;

import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.definition.MessageInitiator;
import com.werken.werkflow.definition.idiomatic.NetBuilder;
import com.werken.werkflow.definition.idiomatic.Segment;
import com.werken.werkflow.definition.idiomatic.SequenceSegment;
import com.werken.werkflow.definition.idiomatic.UnsupportedIdiomException;
import com.werken.werkflow.definition.petri.Net;
import com.werken.werkflow.definition.petri.PetriException;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class ProcessTag
    extends ComplexActionTagSupport
    implements SegmentReceptor, DefinitionRoot
{
    private String id;

    private Segment segment;

    public ProcessTag()
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

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        this.segment = new SequenceSegment();

        pushSegment( this.segment );

        invokeBody( output );

        popSegment();

        try
        {
            NetBuilder builder = new NetBuilder();

            Net net = builder.build( this.segment );

            ProcessDefinition processDef = new ProcessDefinition( getId(),
                                                                  net,
                                                                  MessageInitiator.EMPTY_ARRAY );
        }
        catch (PetriException e)
        {
            throw new JellyTagException( e );
        }
    }
}
