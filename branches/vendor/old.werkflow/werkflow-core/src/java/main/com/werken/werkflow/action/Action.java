package com.werken.werkflow.action;

import com.werken.werkflow.activity.Activity;

public interface Action
{
    void perform(Activity activity,
                 MutableProcessCase processCase)
        throws Exception;
}
