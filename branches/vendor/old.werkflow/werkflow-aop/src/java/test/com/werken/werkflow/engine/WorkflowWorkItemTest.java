package com.werken.werkflow.engine;

import junit.framework.TestCase;

public class WorkflowWorkItemTest
    extends TestCase
{
    public void testConstruct()
    {
        WorkflowWorkItem workItem = new WorkflowWorkItem( "the case",
                                                          "the transition" );

        assertEquals( "the case",
                      workItem.getCaseId() );

        assertEquals( "the transition",
                      workItem.getTransitionId() );
    }
}
