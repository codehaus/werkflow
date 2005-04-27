package org.codehaus.werkflow.idioms.interactive;

import org.codehaus.werkflow.spi.Component;

public class Choice
    implements Component
{
    private String id;
    private Component body;

    public Choice(String id)
    {
        this.id   = id;
    }

    public String getId()
    {
        return this.id;
    }

    public void setBody(Component body)
    {
        this.body = body;
    }

    public Component getBody()
    {
        return this.body;
    }
}
