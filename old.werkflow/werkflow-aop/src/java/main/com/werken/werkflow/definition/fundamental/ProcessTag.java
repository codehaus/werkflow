package com.werken.werkflow.definition.fundamental;

import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.definition.petri.DefaultNet;

import org.apache.commons.jelly.XMLOutput;

import java.util.List;

public class ProcessTag
    extends FundamentalTagSupport
    implements DocumentableTag
{
    private String id;
    private String documentation;
    private DefaultNet net;

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

    public DefaultNet getNet()
    {
        return this.net;
    }

    public void setDocumentation(String documentation)
    {
        this.documentation = documentation;
    }

    public String getDocumentation()
    {
        return this.documentation;
    }

    public void doTag(XMLOutput output)
        throws Exception
    {
        requireStringAttribute( "id",
                                getId() );

        this.net = new DefaultNet();

        setDocumentation( null );

        invokeBody( output );

        ProcessDefinition def = new ProcessDefinition( getId(),
                                                       this.net );

        def.setDocumentation( getDocumentation() );

        List defList = (List) getContext().getVariable( FundamentalDefinitionLoader.FUNDAMENTAL_DEFINITION_LIST );

        defList.add( def );

        this.net = null;
    }
}
