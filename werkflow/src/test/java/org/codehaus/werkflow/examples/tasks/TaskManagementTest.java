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
package org.codehaus.werkflow.examples.tasks;

import java.util.Properties;

import junit.framework.TestCase;

import org.codehaus.werkflow.Engine;
import org.codehaus.werkflow.InitialContext;
import org.codehaus.werkflow.Transaction;
import org.codehaus.werkflow.Workflow;
import org.codehaus.werkflow.idioms.interactive.Task;
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
import org.codehaus.werkflow.spi.SatisfactionValues;
import org.codehaus.werkflow.spi.Component;

public class TaskManagementTest extends TestCase
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

        Workflow w = engine.getWorkflowManager().getWorkflow("article");
        assertEquals(w, workflow);
    }


    public void testStartNewWorkflow()
            throws Exception
    {
        Workflow w = engine.getWorkflowManager().getWorkflow("article");

        InitialContext context = new InitialContext();
        context.set("subject", "Open source meals");

        // new user action creating a new instance
        Transaction tx = engine.beginTransaction(w.getId(), "request1", context);
        RobustInstance i = engine.getInstanceManager().getInstance(tx.getInstanceId());
        tx.commit();

        assertEquals("request1", i.getId());
        assertEquals("Open source meals", i.get("subject"));
    }

    public void testContinuation()
            throws Exception
    {
        RobustInstance i = engine.getInstanceManager().getInstance("request1");
        DefaultSatisfactionValues userInput;

        showWaitingTasks(i);

        userInput = new DefaultSatisfactionValues();
        userInput.setValue("articleId", "32131231");
        userInput.setValue("author", "bart");
        finishTask(i, "WriteArticle", userInput);

        showWaitingTasks(i);

        userInput = new DefaultSatisfactionValues();
        userInput.setValue("advice", "reject");
        userInput.setValue("comment", "need more information");
        userInput.setValue("reviewer", "bob");
        finishTask(i, "ReviewArticle1", userInput);

        showWaitingTasks(i);

        userInput = new DefaultSatisfactionValues();
        userInput.setValue("advice", "proceed");
        userInput.setValue("comment", "good piece!");
        userInput.setValue("reviewer", "klaas");
        finishTask(i, "ReviewArticle2", userInput);

        showWaitingTasks(i);

        userInput = new DefaultSatisfactionValues();
        userInput.setValue("approved", "false");
        userInput.setValue("approver", "piet");
        finishTask(i, "ApproveArticle", userInput);

        showWaitingTasks(i);
    }

    public void testWorkflowIsFinished()
            throws Exception
    {
        RobustInstance i = engine.getInstanceManager().getInstance("request1");
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

    private void finishTask(RobustInstance i, String taskId, SatisfactionValues sv) throws Exception
    {
        System.out.println("User finishes task " + taskId + " and gives as input: ");
        String names[] = sv.getNames();
        for (int j = 0; j < names.length; j++)
        {
            String name = names[j];
            System.out.println( " - " + name + "=" + sv.getValue(name));
        }

        Transaction tx = engine.beginTransaction(i.getId());
        tx.satisfy(taskId, sv);
        tx.commit();
    }

    private void showWaitingTasks(RobustInstance i)
    {
        SatisfactionSpec[] specs = i.getEligibleSatisfactions();

        System.out.println(specs.length + " task item(s) waiting");

        for (int j = 0; j < specs.length; j++)
        {
            SatisfactionSpec spec = specs[j];
            System.out.println(" - " + spec.getId());

            Path path = i.getWorkflow().getSatisfactionPath(spec.getId());
            Component comp = i.getWorkflow().getComponent(path);
            if(Task.class.isAssignableFrom(comp.getClass()))
            {
                Task task = (Task)i.getWorkflow().getComponent(path);
                System.out.println("   task description = " + task.getTaskDescription());
                System.out.println("   assignee = " + task.getAssignee());
            }
        }
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
