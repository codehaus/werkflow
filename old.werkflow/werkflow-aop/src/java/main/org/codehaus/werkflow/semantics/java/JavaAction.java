package org.codehaus.werkflow.semantics.java;

/*
 $Id$

 Copyright 2003 (C) The Codehaus. All Rights Reserved.

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
    permission of The Codehaus.  For written permission,
    please contact info@codehaus.org.

 4. Products derived from this Software may not be called "werkflow"
    nor may "werkflow" appear in their names without prior written
    permission of The Codehaus. "werkflow" is a registered
    trademark of The Codehaus.

 5. Due credit should be given to The Codehaus -
    http://werkflow.codehaus.org/

 THIS SOFTWARE IS PROVIDED BY THE CODEHAUS AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE CODEHAUS OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 */

import org.codehaus.werkflow.Attributes;
import org.codehaus.werkflow.MutableAttributes;
import org.codehaus.werkflow.work.Action;
import org.codehaus.werkflow.work.ActionInvocation;

import org.apache.commons.beanutils.MethodUtils;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

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
    public void perform(ActionInvocation invocation)
    {
        MutableAttributes caseAttrs  = invocation.getCaseAttributes();
        MutableAttributes otherAttrs = invocation.getOtherAttributes();

        Map caseAttrsMap  = new HashMap();
        Map otherAttrsMap = new HashMap();

        copy( caseAttrs,
              caseAttrsMap );

        copy( otherAttrs,
              otherAttrsMap );

        try
        {
            MethodUtils.invokeMethod( getJavaBean(),
                                      getMethodName(),
                                      new Object[] { caseAttrsMap,
                                                     otherAttrsMap } );
        }
        catch (NoSuchMethodException e)
        {
            try
            {
                MethodUtils.invokeMethod( getJavaBean(),
                                          getMethodName(),
                                          caseAttrsMap );
            }
            catch (Exception e2)
            {
                invocation.completeWithError( e2 );
                return;
            }
        }
        catch (Exception e)
        {
            invocation.completeWithError( e );
            return;
        }

        copy( caseAttrsMap,
              caseAttrs );

        invocation.complete();
    }

    void copy(Attributes attrs,
              Map attrsMap)
    {
        String[] names = attrs.getAttributeNames();

        for ( int i = 0 ; i < names.length ; ++i )
        {
            attrsMap.put( names[i],
                          attrs.getAttribute( names[i] ) );
        }
    }

    void copy(Map attrsMap,
              MutableAttributes attrs)
    {
        Iterator nameIter = attrsMap.keySet().iterator();
        String   eachName = null;

        while ( nameIter.hasNext() )
        {
            eachName = (String) nameIter.next();

            attrs.setAttribute( eachName,
                                attrsMap.get( eachName ) );
        }
    }
}
