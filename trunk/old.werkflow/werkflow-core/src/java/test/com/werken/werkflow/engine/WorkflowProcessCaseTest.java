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
        MockProcessInfo  info  = new MockProcessInfo( "the process",
                                                      "the docs" );

        CaseState state = this.caseRepo.newCaseState( "the case",
                                                      new SimpleAttributes()  );

        WorkflowProcessCase processCase = new WorkflowProcessCase( info,
                                                                   state );

        assertSame( state,
                    processCase.getState() );

        assertSame( info,
                    processCase.getProcessInfo() );

        assertEquals( 0,
                      processCase.getAttributeNames().length );

        assertEquals( 0,
                      processCase.getParameterNames().length );

        assertEquals( 0,
                      processCase.getMarks().length );
        
        assertEquals( 0,
                      processCase.getEnabledTransitions().length );

        assertNotNull( processCase.getId() );
    }

    public void testAttributesParameters()
    {
        MockProcessInfo  info  = new MockProcessInfo( "the process",
                                                      "the docs" );

        CaseState state = this.caseRepo.newCaseState( "the case",
                                                      new SimpleAttributes()  );

        WorkflowProcessCase processCase = new WorkflowProcessCase( info,
                                                                   state );

        assertEquals( 0,
                      processCase.getAttributeNames().length );

        processCase.setAttribute( "foo",
                                  "fooValue" );

        assertEquals( 1,
                      processCase.getAttributeNames().length );

        assertEquals( 1,
                      processCase.getParameterNames().length );

        assertContains( "foo",
                        processCase.getAttributeNames() );

        assertContains( "foo",
                        processCase.getParameterNames() );

        assertEquals( "fooValue",
                      processCase.getAttribute( "foo" ) );

        assertEquals( "fooValue",
                      processCase.getParameter( "foo" ) );

        processCase.setAttribute( "foo",
                                  "newFooValue" );

        assertEquals( 1,
                      processCase.getAttributeNames().length );

        assertEquals( 1,
                      processCase.getParameterNames().length );

        assertContains( "foo",
                        processCase.getAttributeNames() );

        assertContains( "foo",
                        processCase.getParameterNames() );

        assertEquals( "newFooValue",
                      processCase.getAttribute( "foo" ) );

        assertEquals( "newFooValue",
                      processCase.getParameter( "foo" ) );

        processCase.setAttribute( "bar",
                                  null );

        assertEquals( 2,
                      processCase.getAttributeNames().length );

        assertEquals( 2,
                      processCase.getParameterNames().length );

        assertContains( "foo",
                        processCase.getAttributeNames() );

        assertContains( "foo",
                        processCase.getParameterNames() );

        assertContains( "bar",
                        processCase.getAttributeNames() );

        assertContains( "bar",
                        processCase.getParameterNames() );

        assertNull( processCase.getAttribute( "bar" ) );

        assertNull( processCase.getParameter( "bar" ) );

        processCase.clearAttribute( "bar" );

        assertEquals( 1,
                      processCase.getAttributeNames().length );

        assertEquals( 1,
                      processCase.getParameterNames().length );

        assertContains( "foo",
                        processCase.getAttributeNames() );

        assertContains( "foo",
                        processCase.getParameterNames() );
    }

    public void testMarks()
    {
        MockProcessInfo  info  = new MockProcessInfo( "the process",
                                                      "the docs" );

        CaseState state = this.caseRepo.newCaseState( "the case",
                                                      new SimpleAttributes()  );

        WorkflowProcessCase processCase = new WorkflowProcessCase( info,
                                                                   state );

        assertEquals( 0,
                      processCase.getMarks().length );

        processCase.addMark( "foo" );

        assertEquals( 1,
                      processCase.getMarks().length );

        assertContains( "foo",
                        processCase.getMarks() );

        processCase.addMark( "bar" );

        assertEquals( 2,
                      processCase.getMarks().length );

        assertContains( "foo",
                        processCase.getMarks() );

        assertContains( "bar",
                        processCase.getMarks() );

        processCase.removeMark( "foo" );

        assertEquals( 1,
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
