package com.werken.werkflow.core;

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

import com.werken.werkflow.WerkflowTestCase;

public class ActivityKeyTest
    extends WerkflowTestCase
{
    public void testConstruct()
    {
        ActivityKey key = new ActivityKey( "the.package",
                                           "the.process",
                                           "the.case",
                                           "the.transition" );

        assertEquals( "the.package",
                      key.getPackageId() );

        assertEquals( "the.process",
                      key.getProcessId() );

        assertEquals( "the.case",
                      key.getCaseId() );

        assertEquals( "the.transition",
                      key.getTransitionId() );

        assertTrue( key.equals( key ) );
    }

    public void testHashCode()
    {
        ActivityKey keyA = new ActivityKey( "the.package",
                                            "the.process",
                                            "the.case",
                                            "the.transition" );

        ActivityKey keyB = new ActivityKey( "the.package",
                                            "the.process",
                                            "the.case",
                                            "the.transition" );

        ActivityKey keyC = new ActivityKey( "the.package",
                                            "the.process",
                                            "the.other.case",
                                            "the.transition" );

        assertTrue( keyA.hashCode() == keyB.hashCode() );
        assertFalse( keyA.hashCode() == keyC.hashCode() );
        assertFalse( keyB.hashCode() == keyC.hashCode() );
    }

    public void testEquals()
    {
        ActivityKey keyA = new ActivityKey( "the.package",
                                            "the.process",
                                            "the.case",
                                            "the.transition" );

        ActivityKey keyB = new ActivityKey( "the.package",
                                            "the.process",
                                            "the.case",
                                            "the.transition" );

        ActivityKey keyC = new ActivityKey( "the.package",
                                            "the.process",
                                            "the.other.case",
                                            "the.transition" );

        assertTrue( keyA.equals( keyA ) );
        assertTrue( keyB.equals( keyB ) );
        assertTrue( keyC.equals( keyC ) );

        assertTrue( keyA.equals( keyB ) );
        assertTrue( keyB.equals( keyA ) );

        assertFalse( keyA.equals( keyC ) );
        assertFalse( keyC.equals( keyA ) );

        assertFalse( keyA.equals( new Object() ) );
    }
}
