package com.werken.werkflow.semantics.java;

/*
 $Id$

 Copyright 2003 (C) The Werken Company. All Rights Reserved.
 
 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.
 
 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.
 
 3. The name "werkflow" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Werken Company.  For written permission,
    please contact bob@werken.com.
 
 4. Products derived from this Software may not be called "werkflow"
    nor may "werkflow" appear in their names without prior written
    permission of The Werken Company. "werkflow" is a registered
    trademark of The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://werkflow.werken.com/).
 
 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.
 
 */

import com.werken.werkflow.action.Action;
import com.werken.werkflow.activity.Activity;

import org.apache.commons.beanutils.MethodUtils;

import java.util.Map;

/** Adaptor to wrap most any generic JavaBean into a werkflow <code>Action</code>.
 *
 *  <p>
 *  The only constraint is that the JavaBean must have a method with any name that
 *  takes a single <code>java.util.Map</code> as a parameter.  The process-case attributes
 *  will be copied in and out of the <code>Map</code>.
 *  </p>
 *
 *  <p>
 *  The following is a helpful example:
 *  </p>
 *  
 *  <pre>
 *  public class MyAction
 *  {
 *      public void doSomeWork(Map caseAttrs)
 *          throws Exception
 *      {
 *        // logic to perform some work.
 *      }
 *  }
 *
 *  --
 *
 *    DefaultTask task = new DefaultTask();
 *
 *    task.setAction( new JavaBeanAction( myActionInstance, "doSomeWork" ) );
 *
 *    transition.setTask( task );
 *  </pre>
 *
 *  <p>
 *  The xml syntax has yet to be determined.
 *  </p>
 *
 *  @see Action
 *  @see Task#getAction
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class JavaAction
    implements Action
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Wrapped bean. */
    private Object bean;

    /** Method to invoke. */
    private String methodName;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     *
     *  @param bean The JavaBean to wrap.
     *  @param methodName The name of the work-performing method.
     */
    public JavaAction(Object bean,
                      String methodName)
    {
        this.bean       = bean;
        this.methodName = methodName;
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Retrieve the wrapped JavaBean.
     *
     *  @return The wrapped JavaBean.
     */
    public Object getJavaBean()
    {
        return this.bean;
    }

    /** Retrieve the name of the work-perfoming method.
     *
     *  @return The method name.
     */
    public String getMethodName()
    {
        return this.methodName;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** @see Action
     */
    public void perform(Activity activity,
                        Map caseAttrs,
                        Map otherAttrs)
    {
        try
        {
            MethodUtils.invokeMethod( getJavaBean(),
                                      getMethodName(),
                                      new Object[] { caseAttrs,
                                                     otherAttrs } );
        }
        catch (NoSuchMethodException e)
        {
            try
            {
                MethodUtils.invokeMethod( getJavaBean(),
                                          getMethodName(),
                                          caseAttrs );
            }
            catch (Exception e2)
            {
                activity.completeWithError( e2 );
                return;
            }
        }
        catch (Exception e)
        {
            activity.completeWithError( e );
            return;
        }
        
        activity.complete();
    }
}
