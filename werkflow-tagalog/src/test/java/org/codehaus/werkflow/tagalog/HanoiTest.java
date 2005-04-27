/*
 * $Id$
 */

package org.codehaus.werkflow.tagalog;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.codehaus.tagalog.script.Script;

import org.codehaus.werkflow.Engine;
import org.codehaus.werkflow.InitialContext;
import org.codehaus.werkflow.Transaction;
import org.codehaus.werkflow.Workflow;
import org.codehaus.werkflow.helpers.SimpleScheduler;

/**
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class HanoiTest
    extends TagalogTestBase
{
    public void testParseAndExecute()
        throws Exception
    {
        Workflow w = parseWorkflow();

        assertNotNull( w );

        Engine engine = new Engine();

        SimpleScheduler scheduler = new SimpleScheduler( engine );

        engine.setScheduler( scheduler );

        engine.start();

        engine.getWorkflowManager().addWorkflow( w );

        InitialContext context = new InitialContext();

        context.set( "height",
                     new Integer( 5 ) );

        context.set( Script.TAGALOG_OUT,
                     new PrintWriter( new OutputStreamWriter( System.out ), true ) );

        Transaction tx = engine.beginTransaction( "hanoi-solver",
                                                  "hanoi-instance",
                                                  context );

        tx.commit();

        while ( scheduler.runTask() )
        {

        }
    }
}
