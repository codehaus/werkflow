package com.werken.werkflow.log;

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

/** Simple <code>Log</code> implementation.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class SimpleLog
    implements Log
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Log name. */
    private String name;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     *
     *  @param name Log name.
     */
    public SimpleLog(String name)
    {
        this.name = name;
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Retrieve the log name.
     *
     *  @return The name.
     */
    public String getName()
    {
        return this.name;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** @see Log
     */
    public void debug(String msg)
    {
        synchronized ( SimpleLog.class )
        {
            System.err.println( "[debug] [" + getName() + "] " + msg );
        }
    }

    /** @see Log
     */
    public void info(String msg)
    {
        synchronized ( SimpleLog.class )
        {
            System.err.println( "[info]  [" + getName() + "] " + msg );
        }
    }

    /** @see Log
     */
    public void warn(String msg)
    {
        synchronized ( SimpleLog.class )
        {
            System.err.println( "[warn]  [" + getName() + "] " + msg );
        }
    }

    /** @see Log
     */
    public void error(String msg)
    {
        synchronized ( SimpleLog.class )
        {
            System.err.println( "[error] [" + getName() + "] " + msg );
        }
    }

    /** @see Log
     */
    public void error(String msg,
                      Throwable err)
    {
        synchronized ( SimpleLog.class )
        {
            System.err.println( "[error] [" + getName() + "] " + msg );
            err.printStackTrace( System.err );
        }
    }

    /** @see Log
     */
    public void fatal(String msg)
    {
        synchronized ( SimpleLog.class )
        {
            System.err.println( "[fatal] [" + getName() + "] " + msg );
        }
    }
    
    /** @see Log
     */
    public void fatal(String msg,
                      Throwable err)
    {
        synchronized ( SimpleLog.class )
        {
            System.err.println( "[fatal] [" + getName() + "] " + msg );
            err.printStackTrace( System.err );
        }
    }

    /** @see Log
     */
    public Log getChildLog(String postfix)
    {
        return new SimpleLog( getName() + "." + postfix );
    }
}
