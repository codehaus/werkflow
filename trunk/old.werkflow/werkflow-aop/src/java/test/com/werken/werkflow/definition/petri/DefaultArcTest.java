package com.werken.werkflow.definition.petri;

import com.werken.werkflow.expr.Expression;
import com.werken.werkflow.expr.MockExpression;

import junit.framework.TestCase;

public class DefaultArcTest
    extends TestCase
{
    public void testConstruct()
    {
        DefaultPlace place = new DefaultPlace( "the place" );

        DefaultTransition transition = new DefaultTransition( "the transition" );

        DefaultArc arc = new DefaultArc( place,
                                         transition );

        assertSame( place,
                    arc.getPlace() );

        assertSame( transition,
                    arc.getTransition() );

        assertNull( arc.getDocumentation() );

        assertNull( arc.getExpression() );
    }

    public void testExpression()
    {
        DefaultPlace place = new DefaultPlace( "the place" );

        DefaultTransition transition = new DefaultTransition( "the transition" );

        DefaultArc arc = new DefaultArc( place,
                                         transition );

        assertNull( arc.getExpression() );

        Expression expr = new MockExpression( true );

        arc.setExpression( expr );

        assertSame( expr,
                    arc.getExpression() );
    }
}
