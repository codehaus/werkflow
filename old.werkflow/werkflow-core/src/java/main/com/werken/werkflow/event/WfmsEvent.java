package com.werken.werkflow.event;

import com.werken.werkflow.Wfms;

import java.util.EventObject;

public class WfmsEvent
    extends EventObject
{
    public WfmsEvent(Wfms wfms)
    {
        super( wfms );
    }

    public Wfms getWfms()
    {
        return (Wfms) getSource();
    }
}
