package org.codehaus.werkflow;

public class While
    implements AsyncComponent
{
    private Expression test;
    private Component body;

    public While()
    {

    }

    public While(Expression test)
    {
        this();
        setTest( test );
    }

    public While(Expression test,
                 Component body)
    {
        this( test );
        setBody( body );
    }

    public void setTest(Expression test)
    {
        this.test = test;
    }

    public Expression getTest()
    {
        return this.test;
    }

    public void setBody(Component body)
    {
        this.body = body;
    }

    public Component getBody()
    {
        return this.body;
    }

    public void accept(Instance instance)
    {

    }

    public Path[] begin(Instance instance,
                        Path thisPath)
    {
        if ( getTest().evaluate( instance ) )
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
        return SELF;
    }

    public Component[] getChildren()
    {
        return new Component[]
            {
                getBody()
            };
    }
}
