package org.codehaus.werkflow.core;

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

import org.codehaus.werkflow.SimpleAttributes;
import org.codehaus.werkflow.MutableAttributes;
import org.codehaus.werkflow.ProcessInfo;
import org.codehaus.werkflow.work.ActionInvocation;

import java.util.Map;

class CoreActionInvocation
    implements ActionInvocation
{
    private CoreActivity activity;

    private MutableAttributes caseAttrs;
    private MutableAttributes otherAttrs;

    private Completion completion;

    CoreActionInvocation(CoreActivity activity,
                         Map otherAttrsMap,
                         Completion completion)
    {
        this.activity   = activity;
        this.completion = completion;

        CoreProcessCase processCase = getCase();

        // make defensive copies of the attributes so that the
        // action acts in isolation.  Changes to the case
        // attributes are only loaded back upon successfully
        // completion, and within the token-producing tx.

        this.caseAttrs  = new SimpleAttributes( processCase );
        this.otherAttrs = new SimpleAttributes( otherAttrsMap );


        ProcessInfo processInfo = processCase.getProcessInfo();

        this.otherAttrs.setAttribute( "packageId",
                                      getPackageId() );

        this.otherAttrs.setAttribute( "processId",
                                      getProcessId() );

        this.otherAttrs.setAttribute( "caseId",
                                      getCaseId() );

        this.otherAttrs.setAttribute( "transitionId",
                                      activity.getWorkItem().getTransition().getId() );
    }

    CoreActivity getActivity()
    {
        return this.activity;
    }

    CoreWorkItem getWorkItem()
    {
        return getActivity().getWorkItem();
    }

    CoreProcessCase getCase()
    {
        return getWorkItem().getCase();
    }

    public String getPackageId()
    {
        return getCase().getProcessInfo().getPackageId();
    }

    public String getProcessId()
    {
        return getCase().getProcessInfo().getId();
    }

    public String getCaseId()
    {
        return getCase().getId();
    }

    public MutableAttributes getCaseAttributes()
    {
        return this.caseAttrs;
    }

    public MutableAttributes getOtherAttributes()
    {
        return this.otherAttrs;
    }

    public void complete()
    {
        this.completion.complete( caseAttrs );
    }

    public void completeWithError(Throwable error)
    {
        this.completion.completeWithError( error );
    }
}
