package org.codehaus.werkflow.helpers;

import org.codehaus.werkflow.spi.Instance;
import org.codehaus.werkflow.spi.Path;
import org.codehaus.werkflow.spi.Component;
import org.codehaus.werkflow.spi.Satisfaction;
import org.codehaus.werkflow.spi.SatisfactionSpec;

public class SimpleSatisfaction
    implements Satisfaction
{
    private String id;

    public SimpleSatisfaction(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public Path[] begin(Instance instance,
                        Path thisPath)
    {
        return null;
    }

    public Path endChild(Instance instance,
                         Path childPath)
    {
        return NONE;
    }

    public Component[] getChildren()
    {
        return new Component[0];
    }

    public SatisfactionSpec getSatisfactionSpec()
    {
        return new SimpleSatisfactionSpec( getId() );
    }
}
