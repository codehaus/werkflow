package com.werken.werkflow.definition.fundamental;


import org.apache.commons.jelly.XMLOutput;

public class DocumentationTag
    extends FundamentalTagSupport
{
    public DocumentationTag()
    {

    }

    public void doTag(XMLOutput output)
        throws Exception
    {

        DocumentableTag documentable = (DocumentableTag) requiredAncestor( "documentable",
                                                                           DocumentableTag.class );


        documentable.setDocumentation( getBodyText() );
    }
}
