package org.codehaus.werkflow;

import org.codehaus.werkflow.spi.*;
import org.codehaus.werkflow.idioms.*;
import org.codehaus.werkflow.nonpersistent.*;

import java.util.List;

public class AutomaticEngineTest
    extends WerkflowTestBase
    implements SatisfactionManager
{
    private long start;

    public void setUp()
        throws Exception
    {
        super.setUp();
        this.start = System.currentTimeMillis();
    }

    public void tearDown()
        throws Exception
    {
        super.tearDown();
    }

    public void testComplex()
        throws Exception
    {
        Sequence root = new Sequence()
            .addStep( new Touch( "step 1" ) )
            .addStep( new Touch( "step 2" ) )
            .addStep( new Touch( "step 3" ) )
            .addStep( new IfElse( True.INSTANCE,
                                  new Touch( "if 1 true" ),
                                  new Touch( "if 1 false" ) ) )
            .addStep( new Touch( "step 4" ) )
            .addStep( new IfElse( False.INSTANCE,
                                  new Touch( "if 2 true" ),
                                  new Touch( "if 2 false" ) ) )
            .addStep( new Touch( "step 5" ) )
            .addStep( new IfElse( False.INSTANCE,
                                  new Touch( "if 3 true" ) ) )
            .addStep( new Touch( "step 6" ) )
            .addStep( new While( new Counter( 2 ),
                                 new Touch( "while body" ) ) )
            .addStep( new Touch( "step 7" ) )
            .addStep( new Parallel()
                      .addBranch( new Sequence()
                                  .addStep( new Touch( "branch 1 step 1" ) )
                                  .addStep( new Touch( "branch 1 step 2" ) ) )
                      .addBranch( new Sequence()
                                  .addStep( new Touch( "branch 2 step 1" ) )
                                  .addStep( new Touch( "branch 2 step 2" ) ) ) )
            .addStep( new Touch( "step 8" ) );
        
        Workflow workflow = new Workflow( "ted",
                                          root );

        AutomaticEngine engine = new AutomaticEngine( new NonPersistentInstanceManager() );

        engine.addWorkflow( workflow );

        Instance instance = engine.newInstance( "ted",
                                                "larry",
                                                new InitialContext()  );

        //instance.waitFor();

        Thread.sleep( 2000 );

        List touches = (List) instance.get( "touches" );

        assertNotNull( touches );

        assertLength( "16 touches",
                      16,
                      touches );

        assertEquals( "touches[0]: step 1",
                      "step 1",
                      touches.get( 0 ) );

        assertEquals( "touches[1]: step 2",
                      "step 2",
                      touches.get( 1 ) );

        assertEquals( "touches[2]: step 3",
                      "step 3",
                      touches.get( 2 ) );

        assertEquals( "touches[3]: if 1 true",
                      "if 1 true",
                      touches.get( 3 ) );

        assertEquals( "touches[4]: step 4",
                      "step 4",
                      touches.get( 4 ) );

        assertEquals( "touches[5]: if 2 false",
                      "if 2 false",
                      touches.get( 5 ) );

        assertEquals( "touches[6]: step 5",
                      "step 5",
                      touches.get( 6 ) );

        assertEquals( "touches[7]: step 6",
                      "step 6",
                      touches.get( 7 ) );

        assertEquals( "touches[8]: while body",
                      "while body",
                      touches.get( 8 ) );

        assertEquals( "touches[9]: while body",
                      "while body",
                      touches.get( 9 ) );

        assertEquals( "touches[10]: step 7",
                      "step 7",
                      touches.get( 10 ) );

        assertContains( "touches[11-14]: branch 1 step 1",
                        "branch 1 step 1",
                        touches.subList( 11, 15 ) );

        assertContains( "touches[11-14]: branch 1 step 2",
                        "branch 1 step 2",
                        touches.subList( 11, 15 ) );

        assertContains( "touches[11-14]: branch 2 step 1",
                        "branch 2 step 1",
                        touches.subList( 11, 15 ) );

        assertContains( "touches[11-14]: branch 2 step 2",
                        "branch 2 step 2",
                        touches.subList( 11, 15 ) );

        assertEquals( "touches[15]: step 8",
                      "step 8",
                      touches.get( 15 ) );
    }

    /*
    public void testSatisfaction()
        throws Exception
    {
        Sequence root = new Sequence()
            .addStep( new Touch( "step 1" ) )
            .addStep( new PolledSatisfaction( "4000",
                                              500 ) )
            .addStep( new Touch( "step 2" ) );

        Workflow workflow = new Workflow( "ted",
                                          root );

        AutomaticEngine engine = new AutomaticEngine( this );

        engine.addWorkflow( workflow );

        Instance instance = engine.newInstance( "ted",
                                                "larry" ,
                                                new InitialContext() );

        Thread.sleep( 1000 );

        List touches = (List) instance.get( "touches" );

        assertNotNull( touches );

        assertLength( "1 touch",
                      1,
                      touches );

        Thread.sleep( 4000 );

        assertLength( "2 touches",
                      2,
                      touches );
    }
    */

    //  --

    public boolean isSatisfied(String satisfactionId,
                               Context context)
    {
        long delay = Long.parseLong( satisfactionId );

        long now = System.currentTimeMillis();

        boolean result = ( this.start + delay <= now );

        return result;
    }

    public boolean isSatisfied(String satisfactionId,
                               Context context,
                               SatisfactionCallback callback)
    {
        long delay = Long.parseLong( satisfactionId );

        long now = System.currentTimeMillis();

        boolean result = ( this.start + delay <= now );

        return result;
    }
}
