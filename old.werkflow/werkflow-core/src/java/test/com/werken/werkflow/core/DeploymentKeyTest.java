package com.werken.werkflow.core;

import com.werken.werkflow.WerkflowTestCase;

public class DeploymentKeyTest
    extends WerkflowTestCase
{
    public void testConstruct()
    {
        DeploymentKey key = new DeploymentKey( "the.package",
                                               "the.id" );

        assertEquals( "the.package",
                      key.getPackageId() );

        assertEquals( "the.id",
                      key.getId() );
    }

    public void testHashCode()
    {
        DeploymentKey keyA = new DeploymentKey( "the.package",
                                                "the.id" );

        DeploymentKey keyB = new DeploymentKey( "the.package",
                                                "the.id" );

        DeploymentKey keyC = new DeploymentKey( "the.package",
                                                "the.other.id" );

        assertTrue( keyA.hashCode() == keyB.hashCode() );

        assertFalse( keyA.hashCode() == keyC.hashCode() );
        assertFalse( keyB.hashCode() == keyC.hashCode() );
    }

    public void testEquals()
    {
        DeploymentKey keyA = new DeploymentKey( "the.package",
                                                "the.id" );

        DeploymentKey keyB = new DeploymentKey( "the.package",
                                                "the.id" );

        DeploymentKey keyC = new DeploymentKey( "the.package",
                                                "the.other.id" );


        assertTrue( keyA.equals( keyA ) );
        assertTrue( keyB.equals( keyB ) );
        assertTrue( keyC.equals( keyC ) );

        assertTrue( keyA.equals( keyB ) );
        assertTrue( keyB.equals( keyA ) );

        assertFalse( keyA.equals( keyC ) );
        assertFalse( keyC.equals( keyA ) );

        assertFalse( keyB.equals( keyC ) );
        assertFalse( keyC.equals( keyB ) );

        assertFalse( keyA.equals( new Object() ) );
    }
}
