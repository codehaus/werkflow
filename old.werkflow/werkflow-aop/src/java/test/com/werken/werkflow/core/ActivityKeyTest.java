package com.werken.werkflow.core;

import com.werken.werkflow.WerkflowTestCase;

public class ActivityKeyTest
    extends WerkflowTestCase
{
    public void testConstruct()
    {
        ActivityKey key = new ActivityKey( "the.package",
                                           "the.process",
                                           "the.case",
                                           "the.transition" );

        assertEquals( "the.package",
                      key.getPackageId() );

        assertEquals( "the.process",
                      key.getProcessId() );

        assertEquals( "the.case",
                      key.getCaseId() );

        assertEquals( "the.transition",
                      key.getTransitionId() );

        assertTrue( key.equals( key ) );
    }

    public void testHashCode()
    {
        ActivityKey keyA = new ActivityKey( "the.package",
                                            "the.process",
                                            "the.case",
                                            "the.transition" );

        ActivityKey keyB = new ActivityKey( "the.package",
                                            "the.process",
                                            "the.case",
                                            "the.transition" );

        ActivityKey keyC = new ActivityKey( "the.package",
                                            "the.process",
                                            "the.other.case",
                                            "the.transition" );

        assertTrue( keyA.hashCode() == keyB.hashCode() );
        assertFalse( keyA.hashCode() == keyC.hashCode() );
        assertFalse( keyB.hashCode() == keyC.hashCode() );
    }

    public void testEquals()
    {
        ActivityKey keyA = new ActivityKey( "the.package",
                                            "the.process",
                                            "the.case",
                                            "the.transition" );

        ActivityKey keyB = new ActivityKey( "the.package",
                                            "the.process",
                                            "the.case",
                                            "the.transition" );

        ActivityKey keyC = new ActivityKey( "the.package",
                                            "the.process",
                                            "the.other.case",
                                            "the.transition" );

        assertTrue( keyA.equals( keyA ) );
        assertTrue( keyB.equals( keyB ) );
        assertTrue( keyC.equals( keyC ) );

        assertTrue( keyA.equals( keyB ) );
        assertTrue( keyB.equals( keyA ) );

        assertFalse( keyA.equals( keyC ) );
        assertFalse( keyC.equals( keyA ) );

        assertFalse( keyA.equals( new Object() ) );
    }
}
