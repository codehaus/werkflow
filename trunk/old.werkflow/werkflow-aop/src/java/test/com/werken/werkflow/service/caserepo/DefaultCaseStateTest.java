package com.werken.werkflow.service.caserepo;

import junit.framework.TestCase;

public class DefaultCaseStateTest
    extends TestCase
{
    public void testConstruct()
    {
        MockCaseRepository repo = new MockCaseRepository();

        DefaultCaseState state = new DefaultCaseState( "case id",
                                                       "package id",
                                                       "process id",
                                                       repo );


        assertEquals( "case id",
                      state.getCaseId() );

        assertEquals( "package id",
                      state.getPackageId() );

        assertEquals( "process id",
                      state.getProcessId() );

        assertEquals( repo,
                      state.getRepository() );

        assertEquals( 0,
                      state.getAttributeNames().length );

        assertEquals( 0,
                      state.getMarks().length );
    }

    public void testMarks()
    {
        MockCaseRepository repo = new MockCaseRepository();

        DefaultCaseState state = new DefaultCaseState( "case id",
                                                       "package id",
                                                       "process id",
                                                       repo );
        
        assertEquals( 0,
                      state.getMarks().length );

        state.addMark( "mark one" );

        assertEquals( 1,
                      state.getMarks().length );

        assertEquals( "mark one",
                      state.getMarks()[0] );

        state.addMark( "mark one" );

        assertEquals( 1,
                      state.getMarks().length );

        assertEquals( "mark one",
                      state.getMarks()[0] );

        state.removeMark( "mark one" );

        assertEquals( 0,
                      state.getMarks().length );

        state.removeMark( "mark one" );

        assertEquals( 0,
                      state.getMarks().length );
    }

    public void testAttributes()
    {
        MockCaseRepository repo = new MockCaseRepository();

        DefaultCaseState state = new DefaultCaseState( "case id",
                                                       "package id",
                                                       "process id",
                                                       repo );

        assertEquals( 0,
                      state.getAttributeNames().length );

        state.setAttribute( "the key",
                            "the value" );
 
        assertEquals( 1,
                      state.getAttributeNames().length );

        assertEquals( "the key",
                      state.getAttributeNames()[0] );

        assertEquals( "the value",
                      state.getAttribute( "the key" ) );

        state.setAttribute( "the key",
                            "the new value" );

        assertEquals( 1,
                      state.getAttributeNames().length );

        assertEquals( "the key",
                      state.getAttributeNames()[0] );

        assertEquals( "the new value",
                      state.getAttribute( "the key" ) );
    }

    public void testStore()
    {
        MockCaseRepository repo = new MockCaseRepository();

        DefaultCaseState state = new DefaultCaseState( "case id",
                                                       "package id",
                                                       "process id",
                                                       repo );

        state.store();

        assertSame( state,
                    repo.getStored() );
    }
}
