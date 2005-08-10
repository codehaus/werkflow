package org.codehaus.werkflow.helpers;

import junit.framework.TestCase;
import org.codehaus.werkflow.Engine;
import org.codehaus.werkflow.InitialContext;
import org.codehaus.werkflow.Transaction;
import org.codehaus.werkflow.Workflow;
import org.codehaus.werkflow.simple.SimpleWorkflowReader;
import org.codehaus.werkflow.spi.DefaultSatisfactionValues;
import org.codehaus.werkflow.spi.RobustInstance;

/**
 * @author <a href="mailto:saimonmoore@yahoo.co.uk">Saimon Moore</a>
 * @version $Id$
 */
public class SimpleSchedulerTest extends TestCase
{

    private Engine engine;
    private Workflow workflow;

    protected void setUp() throws Exception {

        engine = new Engine();
        engine.setScheduler( new SimpleScheduler( engine ) );

        engine.start();

        workflow = SimpleWorkflowReader.read(
            new MockActionManager(),
            new MockExpressionFactory(),
            getClass().getResource( "workflow.xml" ) );

        assertNotNull( workflow );

        engine.getWorkflowManager().addWorkflow( workflow );

    }

    protected void tearDown() throws Exception {

        engine.stop();
        engine = null;

    }

    public void testSimpleScheduler()
            throws Exception
    {

        SimpleScheduler scheduler = (SimpleScheduler)engine.getScheduler();

        InitialContext context = new InitialContext();
        context.set("party", Boolean.TRUE );
        context.set("animal", "horse");

        // new user action creating a new instance
        Transaction transaction = engine.beginTransaction(workflow.getId(), "een", context);
        RobustInstance instance = engine.getInstanceManager().getInstance(transaction.getInstanceId());
        transaction.commit();

        assertEquals("een", instance.getId());
        assertEquals("horse", instance.get("animal"));
        assertTrue(((Boolean) instance.get("party")).booleanValue());

        while (scheduler.runTask())
        {

        }

        // new user action
        Transaction tx = engine.beginTransaction(instance.getId());

        // provide user choices
        DefaultSatisfactionValues sv = new DefaultSatisfactionValues();
        sv.setValue("choice", "green");
        sv.setValue("input", "more reasons");

        tx.satisfy("pick_color", sv);
        tx.commit();

        while (scheduler.runTask())
        {

        }

        // new user action
        tx = engine.beginTransaction(instance.getId());

        // provide more user choices
        sv = new DefaultSatisfactionValues();
        sv.setValue("choice", "reject");
        sv.setValue("input", "more information");

        tx.satisfy("approval", sv);
        tx.commit();

        while (scheduler.runTask())
        {

        }

        assertTrue(instance.isComplete());

        engine.stop();
    }

}
