package org.codehaus.werkflow;

public class Satisfaction
    implements AsyncComponent
{
    private String id;

    private Component satisfier;

    public Satisfaction(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public void setSatisfier(Component satisfier)
    {
        this.satisfier = satisfier;
    }

    public Component getSatisfier()
    {
        return this.satisfier;
    }

    public Path[] begin(Instance instance,
                        Path thisPath)
    {
        if ( getSatisfier() != null )
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
        if ( getSatisfier() != null )
        {
            return new Component[]
                {
                    getSatisfier()
                };
        }

        return new Component[0];
    }
}
