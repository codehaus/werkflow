package org.codehaus.werkflow.messaging;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.codehaus.tagalog.ParserConfiguration;
import org.codehaus.tagalog.TagalogParser;
import org.codehaus.tagalog.sax.TagalogSAXParserFactory;

import org.codehaus.werkflow.Engine;
import org.codehaus.werkflow.InitialContext;
import org.codehaus.werkflow.Transaction;
import org.codehaus.werkflow.Workflow;
import org.codehaus.werkflow.helpers.SimplePersistenceManager;
import org.codehaus.werkflow.messaging.tagalog.MessagingTagLibrary;
import org.codehaus.werkflow.simple.ActionManager;
import org.codehaus.werkflow.spi.Instance;
import org.codehaus.werkflow.tagalog.SimpleWerkflowTagLibrary;

import org.drools.RuleBase;
import org.drools.io.RuleBaseBuilder;

public class MessagingSatisfactionManagerTest
    extends MessagingTestBase
    implements ActionManager
{
    private TagalogSAXParserFactory factory;

    private List performed;

    public void setUp()
        throws Exception
    {
        super.setUp();

        ParserConfiguration config = new ParserConfiguration();

        config.addTagLibrary( SimpleWerkflowTagLibrary.NS_URI,
                              new SimpleWerkflowTagLibrary() );
        config.addTagLibrary( MessagingTagLibrary.NS_URI,
                              new MessagingTagLibrary() );

        this.factory = new TagalogSAXParserFactory(config);

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
        InputStream stream = getClass().getResourceAsStream( "MessagingSatisfactionManagerTest-workflow.xml" );

        TagalogParser parser = factory.createParser( stream );

        Map context = new java.util.HashMap();

        context.put("actionManager", this);

        Object o = parser.parse(context);

        assertEquals(0, parser.parseErrors().length);

        Workflow workflow = (Workflow) o;

        RuleBase ruleBase = RuleBaseBuilder.buildFromUrl( getClass().getResource( "MessagingSatisfactionManagerTest-rules.xml" ) );

        Engine engine = new Engine();

        MessagingSatisfactionManager satisfactionManager = new MessagingSatisfactionManager( ruleBase, engine );

        engine.setSatisfactionManager( satisfactionManager );

        engine.setPersistenceManager( new SimplePersistenceManager() );

        engine.start();

        engine.getWorkflowManager().addWorkflow( workflow );

        Transaction tx = engine.beginTransaction( "MessagingSatisfactionManagerTest-workflow",
                                                  "cheddar",
                                                  new InitialContext() );
        tx.commit();

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

        tx = engine.beginTransaction( "MessagingSatisfactionManagerTest-workflow",
                                      "velveeta",
                                      new InitialContext() );
        tx.commit();

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
                        Instance instance,
                        Properties properties)
        throws Exception
    {
        this.performed.add( instance.getId() + "/" + actionId );
    }

    public String[] getPerformed()
    {
        return (String[]) this.performed.toArray( new String[ this.performed.size() ] );
    }
}
