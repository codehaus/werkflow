package com.werken.werkflow.definition.petri;

import com.werken.werkflow.definition.Expression;
import com.werken.werkflow.definition.MockExpression;

import junit.framework.TestCase;

public class DefaultTransitionTest
    extends TestCase
{
    public void testConstruct()
    {
        DefaultTransition transition = new DefaultTransition( "the id" );

        assertEquals( "the id",
                      transition.getId() );

        assertNull( transition.getExpression() );
    }

    public void testExpression()
    {
        DefaultTransition transition = new DefaultTransition( "the id" );

        assertNull( transition.getExpression() );

        Expression expr = new MockExpression( true );

        transition.setExpression( expr );

        assertSame( expr,
                    transition.getExpression() );
    }
}
