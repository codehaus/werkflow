package com.werken.werkflow.core;

import com.werken.werkflow.WerkflowTestCase;
import com.werken.werkflow.SimpleAttributes;

public class CoreCompletionTest
    extends WerkflowTestCase
{
    public void testComplete()
    {
        MockActivityCompleter completer = new MockActivityCompleter();

        CoreActivity activity = new CoreActivity( null,
                                                  null );

        CoreCompletion completion = new CoreCompletion( completer,
                                                        activity );

        SimpleAttributes caseAttrs = new SimpleAttributes();

        completion.complete( caseAttrs );

        assertSame( activity,
                    completer.getActivity() );

        assertSame( caseAttrs,
                    completer.getCaseAttributes() );
    }

    public void testCompleteWithError()
    {
        MockActivityCompleter completer = new MockActivityCompleter();

        CoreActivity activity = new CoreActivity( null,
                                                  null );

        CoreCompletion completion = new CoreCompletion( completer,
                                                        activity );
        
        Throwable error = new Throwable();

        completion.completeWithError( error );

        assertSame( activity,
                    completer.getActivity() );

        assertSame( error,
                    completer.getError() );
    }
}
