package org.codehaus.werkflow.examples.blog;

import java.util.Properties;

import junit.framework.TestCase;

import org.codehaus.werkflow.Engine;
import org.codehaus.werkflow.InitialContext;
import org.codehaus.werkflow.Transaction;
import org.codehaus.werkflow.Workflow;
import org.codehaus.werkflow.expressions.False;
import org.codehaus.werkflow.helpers.SimpleInstanceManager;
import org.codehaus.werkflow.helpers.SimplePersistenceManager;
import org.codehaus.werkflow.helpers.SimpleSatisfactionManager;
import org.codehaus.werkflow.helpers.SimpleWorkflowManager;
import org.codehaus.werkflow.simple.ActionManager;
import org.codehaus.werkflow.simple.ExpressionFactory;
import org.codehaus.werkflow.simple.SimpleWorkflowReader;
import org.codehaus.werkflow.spi.DefaultSatisfactionValues;
import org.codehaus.werkflow.spi.Expression;
import org.codehaus.werkflow.spi.Instance;
import org.codehaus.werkflow.spi.InstanceManager;
import org.codehaus.werkflow.spi.Path;
import org.codehaus.werkflow.spi.PersistenceManager;
import org.codehaus.werkflow.spi.RobustInstance;
import org.codehaus.werkflow.spi.SatisfactionManager;
import org.codehaus.werkflow.spi.SatisfactionSpec;
import org.codehaus.werkflow.spi.WorkflowManager;

public class BlogWorkflowTest extends TestCase
        implements ActionManager, ExpressionFactory
{
    private Engine engine;
    private Workflow workflow;


    protected void setUp() throws Exception
    {
        this.engine = createEngine();

        this.workflow = SimpleWorkflowReader.read(this,this,getClass().getResource("workflow.xml"));

        assertNotNull(this.workflow);

        this.engine.getWorkflowManager().addWorkflow(this.workflow);
    }

    public Engine createEngine()
    {
        PersistenceManager pm = new SimplePersistenceManager();
        WorkflowManager wm = new SimpleWorkflowManager();
        SatisfactionManager sm = new SimpleSatisfactionManager();
        InstanceManager im = new SimpleInstanceManager();

        Engine engine = new Engine();
        engine.setPersistenceManager(pm);
        engine.setSatisfactionManager(sm);
        engine.setWorkflowManager(wm);
        engine.setInstanceManager(im);

        engine.start();

        return engine;
    }

    public void testCreateWorkflowFromXML()
            throws Exception
    {
        Workflow bloggieWorkflow = this.engine.getWorkflowManager().getWorkflow("bloggie");
        assertEquals(bloggieWorkflow, this.workflow);
    }


    public void testStartNewWorkflow()
            throws Exception
    {
        InitialContext context = new InitialContext();
        context.set("party", new Boolean(true));
        context.set("animal", "horse");

        // new user action creating a new instance
        Transaction transaction = this.engine.beginTransaction(this.workflow.getId(), "een", context);
        RobustInstance instance = this.engine.getInstanceManager().getInstance(transaction.getInstanceId());
        transaction.commit();

        assertEquals("een", instance.getId());
        assertEquals("horse", instance.get("animal"));
        assertTrue(((Boolean) instance.get("party")).booleanValue());

        showInstance(instance);

        RobustInstance i = engine.getInstanceManager().getInstance("een");

        // new user action
        Transaction tx = engine.beginTransaction(i.getId());
        // provide user choices
        DefaultSatisfactionValues sv = new DefaultSatisfactionValues();
        sv.setValue("choice", "green");
        sv.setValue("input", "more reasons");
        tx.satisfy("pick_color", sv);
        tx.commit();

        showInstance(i);

        // new user action
        tx = engine.beginTransaction(i.getId());
        // provide more user choices
        sv = new DefaultSatisfactionValues();
        sv.setValue("choice", "reject");
        sv.setValue("input", "more information");
        tx.satisfy("approval", sv);
        tx.commit();

        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException ie)
        {

        }        

        showInstance(i);

        RobustInstance ii = engine.getInstanceManager().getInstance("een");
        assertTrue(ii.isComplete());

        engine.stop();
    }


    // ********************************

    private void showInstance(RobustInstance i)
    {
        System.out.println(i);
        System.out.println(i.getState());
    }

    // ********************************

    public void perform(String actionId,
                        Instance instance,
                        Properties props)
            throws Exception
    {

    }


    public Expression newExpression(String text)
            throws Exception
    {
        return False.INSTANCE;
    }

}
