package org.codehaus.werkflow.persistent;

import org.codehaus.werkflow.*;
import org.codehaus.werkflow.spi.*;
import org.codehaus.werkflow.idioms.*;

import java.io.File;
import java.util.List;

public class PersistentTest
    extends PersistentTestBase
{
    public void testNothing()
        throws Exception
    {
        Sequence sequence = new Sequence()
            .addStep( new Touch( "one" ) )
            .addStep( new Touch( "two" ) )
            .addStep( new Touch( "three" ) )
            .addStep( new Touch( "four" ) );
                      
        Workflow workflow = new Workflow( "ted",
                                          sequence );

        File basedir = new File( new File( System.getProperty( "basedir" ),
                                           "target" ),
                                 "werkflow" );

        basedir.mkdirs();

        InstanceManager instanceManager = new PersistentInstanceManager( basedir );

        ManualEngine engine = new ManualEngine( instanceManager );

        engine.addWorkflow( workflow );

        Instance instance = engine.newInstance( "ted",
                                                "larry",
                                                new InitialContext() );

        engine.step( instance );
        System.err.println( "a: " + instance.get( "touches" ) );
        engine.step( instance );
        System.err.println( "b: " + instance.get( "touches" ) );
        engine.step( instance );
        System.err.println( "c: " + instance.get( "touches" ) );

        Thread.sleep( 1000 );

        System.err.println( "=====" );
        

        instanceManager = new PersistentInstanceManager( basedir );

        engine = new ManualEngine( instanceManager );

        engine.addWorkflow( workflow );

        instance = engine.getInstance( "larry" );

        System.err.println( "d: " + instance.get( "touches" ) );
        engine.step( instance );
        System.err.println( "e: " + instance.get( "touches" ) );
        engine.step( instance );
        System.err.println( "f: " + instance.get( "touches" ) );
        
    }
}
