package com.werken.werkflow.action;

import com.werken.werkflow.Attributes;
import com.werken.werkflow.SimpleAttributes;
import com.werken.werkflow.ProcessCase;
import com.werken.werkflow.activity.Activity;
import com.werken.werkflow.expr.Expression;
import com.werken.werkflow.expr.ExpressionContext;
import com.werken.werkflow.expr.AttributesExpressionContext;
import com.werken.werkflow.semantics.jelly.CaseJellyContext;

import java.util.Map;
import java.util.Iterator;

public class DefaultCallAction
    implements CallAction
{
    private String packageId;
    private String processId;
    private Map attrExprs;

    public DefaultCallAction(String packageId,
                             String processId)
    {
        this.packageId = packageId;
        this.processId = processId;
    }

    public String getPackageId()
    {
        return this.packageId;
    }

    public String getProcessId()
    {
        return this.processId;
    }

    public void setAttributeExpressions(Map attrExprs)
    {
        this.attrExprs = attrExprs;
    }

    public Attributes getAttributes(ProcessCase parentCase)
        throws Exception
    {
        ExpressionContext context = new AttributesExpressionContext( parentCase );

        SimpleAttributes attrs = new SimpleAttributes();

        Iterator nameIter = this.attrExprs.keySet().iterator();
        String   eachName = null;

        Expression eachExpr = null;

        while ( nameIter.hasNext() )
        {
            eachName = (String) nameIter.next();
            eachExpr = (Expression) this.attrExprs.get( eachName );

            attrs.setAttribute( eachName,
                                eachExpr.evaluate( context ) );
        }

        return attrs;
    }

    public void perform(Activity activity,
                        Map caseAttrs,
                        Map otherAttrs)
    {
        // nothing
    }
}
