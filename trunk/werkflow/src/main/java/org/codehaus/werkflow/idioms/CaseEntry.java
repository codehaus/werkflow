package org.codehaus.werkflow.idioms;

import org.codehaus.werkflow.spi.Component;
import org.codehaus.werkflow.spi.Expression;

class CaseEntry
{
    private Expression caseExpr;
    private Component branch;

    CaseEntry(Expression caseExpr,
              Component branch)
    {
        this.caseExpr = caseExpr;
        this.branch = branch;
    }

    Expression getCaseExpression()
    {
        return this.caseExpr;
    }

    Component getBranch()
    {
        return this.branch;
    }
}
