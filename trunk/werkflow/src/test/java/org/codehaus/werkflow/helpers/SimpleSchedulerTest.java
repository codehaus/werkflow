package org.codehaus.werkflow.helpers;

import junit.framework.TestCase;
import org.codehaus.werkflow.simple.ActionManager;
import org.codehaus.werkflow.simple.ExpressionFactory;
import org.codehaus.werkflow.simple.SimpleWorkflowReader;
import org.codehaus.werkflow.spi.*;
import org.codehaus.werkflow.expressions.False;
import org.codehaus.werkflow.Engine;
import org.codehaus.werkflow.Workflow;
import org.codehaus.werkflow.InitialContext;
import org.codehaus.werkflow.Transaction;

import java.util.Properties;
import java.util.Arrays;

/**
 * @author <a href="mailto:saimonmoore@yahoo.co.uk">Saimon Moore</a>
 * @version $Id$
 */
public class SimpleSchedulerTest extends TestCase
        implements ActionManager, ExpressionFactory
{

    //  ------------------------------------------------------------
    //  C O N S T A N T S
    //  ------------------------------------------------------------
    
    public static final String CLASS_VERSION = "$Id$";
    
    //  ------------------------------------------------------------
    //  C L A S S  F I E L D S
    //  ------------------------------------------------------------
    
    //  ------------------------------------------------------------
    //  I N S T A N C E  F I E L D S
    //  ------------------------------------------------------------

    SimpleScheduler scheduler;

    //  ------------------------------------------------------------
    //  C O N S T R U C T O R S
    //  ------------------------------------------------------------
    
    //  ------------------------------------------------------------
    //  I M P L E M E N T A T I O N
    //  ------------------------------------------------------------

    public Engine createEngine()
    {
        PersistenceManager pm = new SimplePersistenceManager();
        WorkflowManager wm = new SimpleWorkflowManager();
        SatisfactionManager sm = new SimpleSatisfactionManager();
        InstanceManager im = new SimpleInstanceManager();


        Engine engine = new Engine();
        this.scheduler = new SimpleScheduler(engine);

        engine.setScheduler(this.scheduler);

        engine.setPersistenceManager(pm);
        engine.setSatisfactionManager(sm);
        engine.setWorkflowManager(wm);
        engine.setInstanceManager(im);

        engine.start();

        return engine;
    }

    public void testSimpleScheduler()
            throws Exception
    {

        Engine engine = createEngine();
        Workflow workflow = SimpleWorkflowReader.read(this,this,getClass().getResource("workflow.xml"));
        assertNotNull(workflow);
        engine.getWorkflowManager().addWorkflow(workflow);

        InitialContext context = new InitialContext();
        context.set("party", new Boolean(true));
        context.set("animal", "horse");

        // new user action creating a new instance
        Transaction transaction = engine.beginTransaction(workflow.getId(), "een", context);
        RobustInstance instance = engine.getInstanceManager().getInstance(transaction.getInstanceId());
        transaction.commit();


        assertEquals("een", instance.getId());
        assertEquals("horse", instance.get("animal"));
        assertTrue(((Boolean) instance.get("party")).booleanValue());

        showInstance(instance);

        while (instance.getEligibleSatisfactions().length == 0 && !instance.isComplete())
        {
            this.scheduler.runTask();
        }

        showInstance(instance);
        System.out.println("Eligible satisfactions: " + Arrays.asList(instance.getEligibleSatisfactions()));

        RobustInstance i = engine.getInstanceManager().getInstance("een");

        // new user action
        Transaction tx = engine.beginTransaction(i.getId());
        // provide user choices
        DefaultSatisfactionValues sv = new DefaultSatisfactionValues();
        sv.setValue("choice", "green");
        sv.setValue("input", "more reasons");

        System.out.println("About to satisfy pick_color...");
        tx.satisfy("pick_color", sv);
        tx.commit();
        showInstance(i);

        while (i.getEligibleSatisfactions().length == 0 && !instance.isComplete())
        {
            this.scheduler.runTask();
        }

        System.out.println("Eligible satisfactions: " + Arrays.asList(instance.getEligibleSatisfactions()));

        showInstance(i);

        // new user action
        tx = engine.beginTransaction(i.getId());
        // provide more user choices
        sv = new DefaultSatisfactionValues();
        sv.setValue("choice", "reject");
        sv.setValue("input", "more information");

        System.out.println("About to satisfy approval...");
        tx.satisfy("approval", sv);
        tx.commit();

        showInstance(i);

        while (i.getEligibleSatisfactions().length == 0 && !instance.isComplete())
        {
            this.scheduler.runTask();
        }

        showInstance(i);

        RobustInstance ii = engine.getInstanceManager().getInstance("een");
        assertTrue(ii.isComplete());

        engine.stop();
    }

    private void showInstance(RobustInstance i)
    {
        System.out.println(i);
        System.out.println(i.getState());
    }

    //  ------------------------------------------------------------
    //  A C C E S S O R S / M U T A T O R S
    //  ------------------------------------------------------------
    
    //  ------------------------------------------------------------
    //  N E S T E D  C L A S S E S
    //  ------------------------------------------------------------

    public void perform(String actionId,
                        Instance instance,
                        Properties props)
            throws Exception
    {
        System.out.println("Performing action (" + actionId + ") :" + props);
    }


    public Expression newExpression(String text)
            throws Exception
    {
        return False.INSTANCE;
    }

}
