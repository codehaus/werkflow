package org.codehaus.werkflow.idioms;

import org.codehaus.werkflow.Instance;
import org.codehaus.werkflow.spi.Component;
import org.codehaus.werkflow.spi.AsyncComponent;
import org.codehaus.werkflow.spi.Expression;
import org.codehaus.werkflow.spi.Path;

public class IfElse
    implements AsyncComponent
{
    private Expression test;
    private Component trueBody;
    private Component falseBody;

    public IfElse()
    {
    }

    public IfElse(Expression test)
    {
        this();
        setTest( test );
    }

    public IfElse(Expression test,
                  Component trueBody)
    {
        this( test );
        setTrueBody( trueBody );
    }

    public IfElse(Expression test,
                  Component trueBody,
                  Component falseBody)
    {
        this( test,
              trueBody );
        setFalseBody( falseBody );
    }

    public void setTest(Expression test)
    {
        this.test = test;
    }

    public Expression getTest()
    {
        return this.test;
    }

    public void setTrueBody(Component trueBody)
    {
        this.trueBody = trueBody;
    }

    public Component getTrueBody()
    {
        return this.trueBody;
    }

    public void setFalseBody(Component falseBody)
    {
        this.falseBody = falseBody;
    }

    public Component getFalseBody()
    {
        return this.falseBody;
    }

    public Path[] begin(Instance instance,
                        Path thisPath)
    {
        if ( getTest().evaluate( instance ) )
        {
            return new Path[] { thisPath.childPath( 0 ) };
        }

        if ( getFalseBody() != null )
        {
            return new Path[] { thisPath.childPath( 1 ) };
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
        if ( getFalseBody() != null )
        {
            return new Component[]
                {
                    getTrueBody(),
                    getFalseBody()
                };
        }

        return new Component[] { getTrueBody() };
    }
}
