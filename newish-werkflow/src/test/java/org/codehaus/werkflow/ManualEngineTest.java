package org.codehaus.werkflow;

import java.util.Arrays;

public class ManualEngineTest
    extends WerkflowTestBase
{
    public void testWorkflowManagement()
        throws Exception
    {
        Workflow workflow = new Workflow( "ted" );

        Engine engine = new ManualEngine();

        engine.addWorkflow( workflow );

        assertSame( "able to get workflow by id",
                    workflow,
                    engine.getWorkflow( "ted" ) );

        try
        {
            engine.addWorkflow( workflow );
            fail( "should have thrown DuplicateWorkflowException" );
        }
        catch (DuplicateWorkflowException e)
        {
            // expected and correct
            assertEquals( workflow,
                          e.getWorkflow() );
        }

        try
        {
            engine.getWorkflow( "larry" );
            fail( "should have thrown NoSuchWorkflowException" );
        }
        catch (NoSuchWorkflowException e)
        {
            // expected and correct
            assertEquals( "larry",
                          e.getId() );
        }
    }

    public void testRunSequence()
        throws Exception
    {
        Sequence sequence = new Sequence();

        MockSyncComponent step1 = new MockSyncComponent();
        MockSyncComponent step2 = new MockSyncComponent();
        MockSyncComponent step3 = new MockSyncComponent();

        sequence.addStep( step1 );
        sequence.addStep( step2 );
        sequence.addStep( step3 );

        Workflow workflow = new Workflow( "ted",
                                          sequence );


        ManualEngine engine = new ManualEngine();

        engine.addWorkflow( workflow );

        Instance instance = engine.newInstance( "ted",
                                                "larry" );
        
        assertEquals( "sequence enqueued",
                      new Path(),
                      engine.getEnqueued( instance )[ 0 ] );
        
        engine.step( instance );
        
        assertEquals( "step1 enqueued",
                      new Path().childPath( 0 ),
                      engine.getEnqueued( instance )[ 0 ] );
        
        engine.step( instance );
        
        assertEquals( "step2 enqueued",
                      new Path().childPath( 1 ),
                      engine.getEnqueued( instance )[ 0 ] );

        engine.step( instance );

        assertEquals( "step3 enqueued",
                      new Path().childPath( 2 ),
                      engine.getEnqueued( instance )[ 0 ] );
        
        engine.step( instance );

        assertEmpty( "nothing enqueued",
                     engine.getEnqueued( instance ) );
    }

    public void testRunParallel()
        throws Exception
    {
        Parallel parallel = new Parallel();

        MockSyncComponent branch1 = new MockSyncComponent();
        MockSyncComponent branch2 = new MockSyncComponent();
        MockSyncComponent branch3 = new MockSyncComponent();

        parallel.addBranch( branch1 );
        parallel.addBranch( branch2 );
        parallel.addBranch( branch3 );

        Workflow workflow = new Workflow( "ted",
                                          parallel );


        ManualEngine engine = new ManualEngine();

        engine.addWorkflow( workflow );

        Instance instance = engine.newInstance( "ted",
                                                "larry" );
        
        assertEquals( "parallel enqueued",
                      new Path(),
                      engine.getEnqueued( instance )[ 0 ] );
        
        engine.step( instance );

        assertLength( "3 things enqueued",
                      3,
                      engine.getEnqueued( instance ) );


        assertContains( "branch1 enqueued",
                        new Path().childPath( 0 ),
                        engine.getEnqueued( instance ) );

        assertContains( "branch2 enqueued",
                        new Path().childPath( 1 ),
                        engine.getEnqueued( instance ) );

        assertContains( "branch3 enqueued",
                        new Path().childPath( 2 ),
                        engine.getEnqueued( instance ) );

        engine.step( instance );

        assertLength( "2 things enqueued",
                      2,
                      engine.getEnqueued( instance ) );

        engine.step( instance );

        assertLength( "1 thing enqueued",
                      1,
                      engine.getEnqueued( instance ) );

        engine.step( instance );

        assertEmpty( "nothing enqueued",
                     engine.getEnqueued( instance ) );
    }

    public void testRunParallelInSequence()
        throws Exception
    {
        Sequence sequence = new Sequence();

        Parallel parallel = new Parallel();

        MockSyncComponent branch1 = new MockSyncComponent();
        MockSyncComponent branch2 = new MockSyncComponent();
        MockSyncComponent branch3 = new MockSyncComponent();

        parallel.addBranch( branch1 );
        parallel.addBranch( branch2 );
        parallel.addBranch( branch3 );

        MockSyncComponent postParallel = new MockSyncComponent();

        sequence.addStep( parallel );
        sequence.addStep( postParallel );

        Workflow workflow = new Workflow( "ted",
                                          sequence );


        ManualEngine engine = new ManualEngine();

        engine.addWorkflow( workflow );

        Instance instance = engine.newInstance( "ted",
                                                "larry" );
        
        assertEquals( "sequence enqueued",
                      new Path(),
                      engine.getEnqueued( instance )[ 0 ] );
        
        engine.step( instance );

        assertEquals( "parallel enqueued",
                      new Path().childPath( 0 ),
                      engine.getEnqueued( instance )[ 0 ] );

        engine.step( instance );

        assertLength( "3 things enqueued",
                      3,
                      engine.getEnqueued( instance ) );

        assertContains( "branch1 enqueued",
                        new Path().childPath( 0 ).childPath( 0 ),
                        engine.getEnqueued( instance ) );

        assertContains( "branch2 enqueued",
                        new Path().childPath( 0 ).childPath( 1 ),
                        engine.getEnqueued( instance ) );

        assertContains( "branch3 enqueued",
                        new Path().childPath( 0 ).childPath( 2 ),
                        engine.getEnqueued( instance ) );

        engine.step( instance );

        assertLength( "2 things enqueued",
                      2,
                      engine.getEnqueued( instance ) );

        engine.step( instance );

        assertLength( "1 thing enqueued",
                      1,
                      engine.getEnqueued( instance ) );

        engine.step( instance );

        assertContains( "postParallel enqueued",
                        new Path().childPath( 1 ),
                        engine.getEnqueued( instance ) );

        engine.step( instance );

        assertEmpty( "nothing enqueued",
                     engine.getEnqueued( instance ) );
    }

    public void testRunWhileSequence()
        throws Exception
    {
        Sequence sequence = new Sequence();

        While whileComp = new While( new Once() );

        MockSyncComponent body = new MockSyncComponent();

        whileComp.setBody( body );

        MockSyncComponent postWhile = new MockSyncComponent();

        sequence.addStep( whileComp );
        sequence.addStep( postWhile );

        Workflow workflow = new Workflow( "ted",
                                          sequence );

        ManualEngine engine = new ManualEngine();

        engine.addWorkflow( workflow );

        Instance instance = engine.newInstance( "ted",
                                                "larry" );

        assertEquals( "sequence.enqueued",
                      new Path(),
                      engine.getEnqueued( instance )[ 0 ] );

        engine.step( instance );
        
        assertEquals( "while.enqueued",
                      new Path().childPath( 0 ),
                      engine.getEnqueued( instance )[ 0 ] );
        
        engine.step( instance );

        assertEquals( "body.enqueued",
                      new Path().childPath( 0 ).childPath( 0 ),
                      engine.getEnqueued( instance )[ 0 ] );

        engine.step( instance );
        
        assertEquals( "while.enqueued",
                      new Path().childPath( 0 ),
                      engine.getEnqueued( instance )[ 0 ] );

        engine.step( instance );

        assertEquals( "postWhile enqueued",
                      new Path().childPath( 1 ),
                      engine.getEnqueued( instance)[ 0 ] );
        
    }

    public void testRunIfElseSequence_TrueIf()
        throws Exception
    {
        Sequence sequence = new Sequence();

        IfElse trueIf = new IfElse( True.INSTANCE );

        MockSyncComponent trueBody = new MockSyncComponent();

        MockSyncComponent postIf = new MockSyncComponent();

        trueIf.setTrueBody( trueBody );

        sequence.addStep( trueIf );
        sequence.addStep( postIf );
                   
        Workflow workflow = new Workflow( "ted",
                                          sequence );

        ManualEngine engine = new ManualEngine();

        engine.addWorkflow( workflow );

        Instance instance = engine.newInstance( "ted",
                                                "larry" );

        assertEquals( "sequence.enqueued",
                      new Path(),
                      engine.getEnqueued( instance )[ 0 ] );

        engine.step( instance );
        
        assertEquals( "if.enqueued",
                      new Path().childPath( 0 ),
                      engine.getEnqueued( instance )[ 0 ] );
        
        engine.step( instance );

        assertEquals( "body.enqueued",
                      new Path().childPath( 0 ).childPath( 0 ),
                      engine.getEnqueued( instance )[ 0 ] );

        engine.step( instance );

        assertEquals( "postIf.enqueued",
                      new Path().childPath( 1 ),
                      engine.getEnqueued( instance )[ 0 ] );
    }
}
