package org.codehaus.werkflow.drools;

import org.codehaus.werkflow.Context;

public class ContextRequest
    implements Request
{
    private Context context;

    public ContextRequest(Context context)
    {
        this.context = context;
    }

    public Context getContext()
    {
        return this.context;
    }

    public String getId()
    {
        return getContext().getId();
    }

    public Object get(String id)
    {
        return getContext().get( id );
    }
}
