package org.codehaus.werkflow.helpers;

import org.codehaus.werkflow.spi.SatisfactionSpec;

public class SimpleSatisfactionSpec
    implements SatisfactionSpec
{
    private String id;

    public SimpleSatisfactionSpec(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }
}
