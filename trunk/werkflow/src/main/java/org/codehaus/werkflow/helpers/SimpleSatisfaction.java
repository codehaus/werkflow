package org.codehaus.werkflow.helpers;

import org.codehaus.werkflow.spi.Instance;
import org.codehaus.werkflow.spi.Path;
import org.codehaus.werkflow.spi.Component;
import org.codehaus.werkflow.spi.Satisfaction;
import org.codehaus.werkflow.spi.SatisfactionSpec;
import org.codehaus.werkflow.spi.Satisfier;

public class SimpleSatisfaction
    implements Satisfaction
{
    private String id;
    private Satisfier satisfier;

    public SimpleSatisfaction(String id)
    {
        this.id = id;
        this.satisfier = new ValueCopySatisfier( id );
    }

    public SimpleSatisfaction(String id,
                              Satisfier satisfier)
    {
        this.id = id;
        this.satisfier = satisfier;
    }

    public String getId()
    {
        return this.id;
    }

    public Path[] begin(Instance instance,
                        Path thisPath)
    {
        if ( satisfier != null )
        {
            return new Path[]
                {
                    thisPath.childPath( 0 )
                };
        }

        return null;
    }

    public Path endChild(Instance instance,
                         Path childPath)
    {
        return NONE;
    }

    public Component[] getChildren()
    {
        if ( satisfier != null )
        {
            return new Component[]
                {
                    satisfier
                };
        }

        return new Component[0];
    }

    public SatisfactionSpec getSatisfactionSpec()
    {
        return new SimpleSatisfactionSpec( getId() );
    }
}
