package com.werken.werkflow.definition.fundamental;

import com.werken.werkflow.definition.petri.DefaultNet;
import com.werken.werkflow.definition.petri.DefaultPlace;

import org.apache.commons.jelly.XMLOutput;

public class MessageTypesTag
    extends FundamentalTagSupport
{
    public MessageTypesTag()
    {
    }

    public void doTag(XMLOutput output)
        throws Exception
    {
        invokeBody( output );
    }
}
