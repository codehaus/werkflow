package com.werken.werkflow.definition.fundamental;

import com.werken.werkflow.definition.petri.DefaultNet;
import com.werken.werkflow.definition.petri.DefaultPlace;

import org.apache.commons.jelly.XMLOutput;

public class PlaceTag
    extends FundamentalTagSupport
    implements DocumentableTag
{
    private String id;
    private String documentation;

    public PlaceTag()
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

        ProcessTag process = (ProcessTag) requiredAncestor( "process",
                                                            ProcessTag.class );

        DefaultNet net = process.getNet();

        DefaultPlace place = net.addPlace( getId() );

        setDocumentation( null );

        invokeBody( output );

        place.setDocumentation( getDocumentation() );

    }
}
