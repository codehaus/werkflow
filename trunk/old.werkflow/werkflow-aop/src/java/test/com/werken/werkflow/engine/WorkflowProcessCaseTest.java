package com.werken.werkflow.engine;

import com.werken.werkflow.SimpleAttributes;
import com.werken.werkflow.MockProcessInfo;
import com.werken.werkflow.service.caserepo.CaseState;
import com.werken.werkflow.service.caserepo.InMemoryCaseRepository;

import junit.framework.TestCase;

import java.util.Arrays;

public class WorkflowProcessCaseTest
    extends TestCase
{
    private InMemoryCaseRepository caseRepo;

    public void setUp()
    {
        this.caseRepo = new InMemoryCaseRepository();
    }

    public void testConstruct()
    {
        MockProcessInfo  info  = new MockProcessInfo( "the package",
                                                      "the process",
                                                      "the docs" );

        CaseState state = this.caseRepo.newCaseState( "the package",
                                                      "the process",
                                                      new SimpleAttributes()  );

        WorkflowProcessCase processCase = new WorkflowProcessCase( info,
                                                                   state );

        assertSame( state,
                    processCase.getState() );

        assertSame( info,
                    processCase.getProcessInfo() );

        assertEquals( 0,
                      processCase.getAttributeNames().length );

        assertEquals( 1,
                      processCase.getMarks().length );

        assertEquals( "in",
                      processCase.getMarks()[0] );
        
        assertEquals( 0,
                      processCase.getEnabledTransitions().length );

        assertNotNull( processCase.getId() );
    }

    public void testAttributes()
    {
        MockProcessInfo  info  = new MockProcessInfo( "the package",
                                                      "the process",
                                                      "the docs" );

        CaseState state = this.caseRepo.newCaseState( "the package",
                                                      "the process",
                                                      new SimpleAttributes()  );

        WorkflowProcessCase processCase = new WorkflowProcessCase( info,
                                                                   state );

        assertEquals( 0,
                      processCase.getAttributeNames().length );

        processCase.setAttribute( "foo",
                                  "fooValue" );

        assertEquals( 1,
                      processCase.getAttributeNames().length );

        assertContains( "foo",
                        processCase.getAttributeNames() );

        assertEquals( "fooValue",
                      processCase.getAttribute( "foo" ) );

        processCase.setAttribute( "foo",
                                  "newFooValue" );

        assertEquals( 1,
                      processCase.getAttributeNames().length );

        assertContains( "foo",
                        processCase.getAttributeNames() );

        assertEquals( "newFooValue",
                      processCase.getAttribute( "foo" ) );

        processCase.setAttribute( "bar",
                                  null );

        assertEquals( 2,
                      processCase.getAttributeNames().length );

        assertContains( "foo",
                        processCase.getAttributeNames() );

        assertContains( "bar",
                        processCase.getAttributeNames() );

        assertNull( processCase.getAttribute( "bar" ) );

        processCase.clearAttribute( "bar" );

        assertEquals( 1,
                      processCase.getAttributeNames().length );

        assertContains( "foo",
                        processCase.getAttributeNames() );
    }

    public void testMarks()
    {
        MockProcessInfo  info  = new MockProcessInfo( "the package",
                                                      "the process",
                                                      "the docs" );

        CaseState state = this.caseRepo.newCaseState( "the package",
                                                      "the process",
                                                      new SimpleAttributes()  );

        WorkflowProcessCase processCase = new WorkflowProcessCase( info,
                                                                   state );

        assertEquals( 1,
                      processCase.getMarks().length );

        processCase.addMark( "foo" );

        assertEquals( 2,
                      processCase.getMarks().length );

        assertContains( "foo",
                        processCase.getMarks() );

        processCase.addMark( "bar" );

        assertEquals( 3,
                      processCase.getMarks().length );

        assertContains( "foo",
                        processCase.getMarks() );

        assertContains( "bar",
                        processCase.getMarks() );

        processCase.removeMark( "foo" );

        assertEquals( 2,
                      processCase.getMarks().length );

        assertContains( "bar",
                        processCase.getMarks() );
    }

    public void assertContains(Object obj,
                               Object[] array)
    {
        for ( int i = 0 ; i < array.length ; ++i )
        {
            if ( array[i].equals( obj ) )
            {
                return;
            }
        }

        fail( obj + " not in " + Arrays.asList( array ) );
    }
}
