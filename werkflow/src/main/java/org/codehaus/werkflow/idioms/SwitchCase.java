package org.codehaus.werkflow.idioms;

import org.codehaus.werkflow.spi.Instance;
import org.codehaus.werkflow.spi.Component;
import org.codehaus.werkflow.spi.AsyncComponent;
import org.codehaus.werkflow.spi.Expression;
import org.codehaus.werkflow.spi.Path;

import java.util.List;
import java.util.ArrayList;

public class SwitchCase
    implements AsyncComponent
{
    private Expression switchExpr;
    private List cases;
    private Component defaultBranch;

    public SwitchCase()
    {
        this.cases = new ArrayList();
    }

    public SwitchCase(Expression switchExpr)
    {
        this();
        setSwitchExpression( switchExpr );
    }

    public void setSwitchExpression(Expression expr)
    {
        this.switchExpr = expr;
    }

    public Expression getSwitchExpression()
    {
        return this.switchExpr;
    }

    public void addCase(Expression caseExpr,
                        Component branch)
    {
        this.cases.add( new CaseEntry( caseExpr,
                                       branch ) );
    }

    CaseEntry[] getCases()
    {
        return (CaseEntry[]) this.cases.toArray( new CaseEntry[ this.cases.size() ] );
    }

    public void setDefaultBranch(Component branch)
    {
        this.defaultBranch = branch;
    }

    public Component getDefaultBranch()
    {
        return this.defaultBranch;
    }

    public Path[] begin(Instance instance,
                        Path thisPath)
    {
        Object switchValue = getSwitchExpression().evaluate( instance );

        CaseEntry[] cases = getCases();

        for ( int i = 0 ; i < cases.length ; ++i )
        {
            Object caseValue = cases[ i ].getCaseExpression().evaluate( instance );

            if ( switchValue.equals( caseValue ) )
            {
                return new Path[] { thisPath.childPath( i ) };
            }
        }

        if ( getDefaultBranch() != null )
        {
            return new Path[] { thisPath.childPath( cases.length ) };
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
        Component[] children = null;

        CaseEntry[] cases = getCases();
        Component defaultBranch = getDefaultBranch();

        if ( defaultBranch != null )
        {
            children = new Component[ cases.length + 1 ];
        }
        else
        {
            children = new Component[ cases.length ];
        }

        for ( int i = 0 ; i < cases.length ; ++i )
        {
            children[ i ] = cases[ i ].getBranch();
        }


        if ( defaultBranch != null )
        {
            children[ children.length - 1 ] = defaultBranch;
        }

        return children;
    }
}
