package com.werken.werkflow.core;

import com.werken.werkflow.SimpleAttributes;
import com.werken.werkflow.MutableAttributes;
import com.werken.werkflow.ProcessInfo;
import com.werken.werkflow.work.ActionInvocation;

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
        System.err.println( "complete()" );
        System.err.println( "completion -->" + this.completion );
        this.completion.complete( caseAttrs );
    }

    public void completeWithError(Throwable error)
    {
        System.err.println( "completeWithError(" + error + ")" );
        this.completion.completeWithError( error );
    }
}
