package org.codehaus.werkflow.messaging;

import org.codehaus.werkflow.Workflow;
import org.codehaus.werkflow.Instance;
import org.codehaus.werkflow.InitialContext;
import org.codehaus.werkflow.AutomaticEngine;
import org.codehaus.werkflow.simple.SimpleWorkflowReader;
import org.codehaus.werkflow.simple.ExpressionFactory;
import org.codehaus.werkflow.simple.ActionManager;
import org.codehaus.werkflow.nonpersistent.NonPersistentInstanceManager;

import org.drools.RuleBase;
import org.drools.io.RuleBaseBuilder;

import java.util.List;
import java.util.ArrayList;

public class MessagingSatisfactionManagerTest
    extends MessagingTestBase
    implements ActionManager
{
    private List performed;

    public void setUp()
        throws Exception
    {
        super.setUp();
        this.performed = new ArrayList();
    }

    public void tearDown()
        throws Exception
    {
        super.tearDown();
        this.performed = null;
    }

    public void testCorrelation()
        throws Exception
    {
        Workflow workflow = SimpleWorkflowReader.read( this,
                                                       (ExpressionFactory) null,
                                                       getClass().getResource( "MessagingSatisfactionManagerTest-workflow.xml" ) );

        RuleBase ruleBase = RuleBaseBuilder.buildFromUrl( getClass().getResource( "MessagingSatisfactionManagerTest-rules.xml" ) );

        MessagingSatisfactionManager satisfactionManager = new MessagingSatisfactionManager( ruleBase );

        AutomaticEngine engine = new AutomaticEngine( new NonPersistentInstanceManager(),
                                                      satisfactionManager );

        engine.addWorkflow( workflow );

        Instance cheddar = engine.newInstance( "MessagingSatisfactionManagerTest-workflow",
                                               "cheddar",
                                               new InitialContext() );

        System.err.println( "A" );
        Thread.sleep( 1000 );

        assertLength( 1,
                      getPerformed() );

        assertContains( "cheddar/before message",
                        getPerformed() );
        
        satisfactionManager.acceptMessage( "velveeta" );

        Thread.sleep( 1000 );

        assertLength( 1,
                      getPerformed() );

        assertContains( "cheddar/before message",
                        getPerformed() );

        satisfactionManager.acceptMessage( "cheddar" );

        Thread.sleep( 1000 );

        assertLength( 3,
                      getPerformed() );

        assertContains( "cheddar/before message",
                        getPerformed() );

        assertContains( "cheddar/with message",
                        getPerformed() );

        assertContains( "cheddar/after message",
                        getPerformed() );

        Instance velveeta = engine.newInstance( "MessagingSatisfactionManagerTest-workflow",
                                                "velveeta",
                                                new InitialContext() );

        Thread.sleep( 1000 );
        
        assertLength( 6,
                      getPerformed() );

        assertContains( "cheddar/before message",
                        getPerformed() );

        assertContains( "cheddar/with message",
                        getPerformed() );

        assertContains( "cheddar/after message",
                        getPerformed() );

        assertContains( "velveeta/before message",
                        getPerformed() );

        assertContains( "velveeta/with message",
                        getPerformed() );

        assertContains( "velveeta/after message",
                        getPerformed() );
    }

    public void perform(String actionId,
                        Instance instance)
        throws Exception
    {
        this.performed.add( instance.getId() + "/" + actionId );
    }

    public String[] getPerformed()
    {
        return (String[]) this.performed.toArray( new String[ this.performed.size() ] );
    }
}
