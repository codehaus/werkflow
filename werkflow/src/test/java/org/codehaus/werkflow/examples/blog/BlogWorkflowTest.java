/* ====================================================================
 * iBanx Software License, Version 1.0
 *
 * Copyright (c) 2002 iBanx B.V.  All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * iBanx. You shall not disclose such confidential information and
 * shall use it only in accordance with the terms of the license
 * agreement you entered into with iBanx.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL IBANX BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * For more information on iBanx, please see <http://www.ibanx.nl/>.
 */
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
    static Engine engine;

    public void testEngine()
    {
        PersistenceManager pm = new SimplePersistenceManager();
        WorkflowManager wm = new SimpleWorkflowManager();
        SatisfactionManager sm = new SimpleSatisfactionManager();
        InstanceManager im = new SimpleInstanceManager();

        engine = new Engine();
        engine.setPersistenceManager(pm);
        engine.setSatisfactionManager(sm);
        engine.setWorkflowManager(wm);
        engine.setInstanceManager(im);

        engine.start();
    }

    public void testCreateWorkflow()
            throws Exception
    {
        Workflow workflow = SimpleWorkflowReader.read(this,
                                                      this,
                                                      getClass().getResource("workflow.xml"));

        assertNotNull(workflow);

        engine.getWorkflowManager().addWorkflow(workflow);

        Workflow w = engine.getWorkflowManager().getWorkflow("bloggie");
        assertEquals(w, workflow);
    }


    public void testStartNewWorkflow()
            throws Exception
    {
        Workflow w = engine.getWorkflowManager().getWorkflow("bloggie");

        InitialContext context = new InitialContext();
        context.set("party", new Boolean(true));
        context.set("animal", "horse");

        // new user action creating a new instance
        Transaction tx = engine.beginTransaction(w.getId(), "een", context);
        RobustInstance i = engine.getInstanceManager().getInstance(tx.getInstanceId());
        tx.commit();

        assertEquals("een", i.getId());
        assertEquals("horse", i.get("animal"));
        assertTrue(((Boolean) i.get("party")).booleanValue());

        showInstance(i);        
    }

    public void testContinuation()
            throws Exception
    {
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

        showInstance(i);
    }

    public void testWorkflowIsFinished()
            throws Exception
    {
        RobustInstance i = engine.getInstanceManager().getInstance("een");
        assertTrue(i.isComplete());
    }


    public void testEngineShutdown()
    {
        // lets stop the engine
        engine.stop();

        // make sure we cannot use the engine anymore
        // todo add test to see that engine is really stopped
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
