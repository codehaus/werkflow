package com.werken.werkflow.personality.extension;

import com.werken.werkflow.work.Action;
import com.werken.werkflow.work.ActionInvocation;

/**
 * Implementation of <code>Action</code> to go with <code>FooTag</code>.
 *
 * @author Mark Wilkinson
 */
public class FooAction implements Action
{
    private static String checkValue;

    public FooAction(String value)
    {
        FooAction.checkValue = value;
    }

    public static String getCheckValue()
    {
        return checkValue;
    }

    public void perform(ActionInvocation invocation)
    {
        // do nothing
    }
}
