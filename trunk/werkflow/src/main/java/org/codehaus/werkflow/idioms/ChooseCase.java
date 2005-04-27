package org.codehaus.werkflow.idioms;

import org.codehaus.werkflow.spi.Instance;
import org.codehaus.werkflow.spi.Component;
import org.codehaus.werkflow.spi.AsyncComponent;
import org.codehaus.werkflow.spi.Expression;
import org.codehaus.werkflow.spi.Path;

import java.util.List;
import java.util.ArrayList;

/**
 * A <code>choose...when</code> style of switch statement, similar
 * to that provided by the JSP standard tag library. Chooses the
 * first of a list of alternatives whose guard condition evaluates
 * to <code>true</code>.
 *
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class ChooseCase
    implements AsyncComponent
{
    private List cases;
    private Component defaultBranch;

    public ChooseCase()
    {
        this.cases = new ArrayList();
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
        CaseEntry[] cases = getCases();

        for ( int i = 0 ; i < cases.length ; ++i )
        {
            Object caseValue = cases[ i ].getCaseExpression().evaluate( instance );

            if ( Boolean.TRUE.equals( caseValue ) )
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
