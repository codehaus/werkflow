package com.werken.werkflow.action;

import com.werken.werkflow.Attributes;
import com.werken.werkflow.SimpleAttributes;
import com.werken.werkflow.activity.Activity;

import java.util.Map;

public class DefaultCallAction
    implements CallAction
{
    private String processId;
    private Attributes attributes;

    public DefaultCallAction(String processId)
    {
        this.processId = processId;
        this.attributes = new SimpleAttributes();
    }

    public String getProcessId()
    {
        return this.processId;
    }

    public void setAttributes(Attributes attributes)
    {
        this.attributes = attributes;
    }

    public Attributes getAttributes()
    {
        return this.attributes;
    }

    public void perform(Activity activity,
                        Map caseAttrs,
                        Map otherAttrs)
    {
        // nothing
    }
}
