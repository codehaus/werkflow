package com.werken.werkflow.syntax.idiomatic;

import com.werken.werkflow.definition.ProcessDefinition;
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
    implements DefinitionRoot
{
    private String id;
    private String initiation;

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

    public void setInitiation(String initiation)
    {
        this.initiation = initiation;
    }

    public String getInitiation()
    {
        return this.initiation;
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        requireStringAttribute( "id",
                                getId() );

        requireStringAttribute( "initiation",
                                getInitiation() );

        ProcessDefinition.InitiationType initiationType = null;

        if ( getInitiation().equals( "message" ) )
        {
            initiationType = ProcessDefinition.InitiationType.MESSAGE;
        }
        else if ( getInitiation().equals( "call" ) )
        {
            initiationType = ProcessDefinition.InitiationType.CALL;
        }
        else
        {
            throw new JellyTagException( "initiation attribute must be 'message' or 'other'" );
        } 
                                
        this.segment = new SequenceSegment();

        ProcessDefinition processDef = new ProcessDefinition( getId(),
                                                              initiationType );

        setCurrentProcess( processDef );

        pushSegment( this.segment );

        invokeBody( output );

        popSegment();

        try
        {
            NetBuilder builder = new NetBuilder();

            Net net = builder.build( this.segment );

            processDef.setNet( net );

            addProcessDefinition( processDef );
        }
        catch (PetriException e)
        {
            throw new JellyTagException( e );
        }
    }
}
