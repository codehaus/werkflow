package org.codehaus.werkflow;

public class SingleStateInstance
    extends DefaultInstance
{
    protected SingleStateInstance(Engine engine,
                                  Workflow workflow,
                                  String id)
    {
        super( engine,
               workflow,
               id );
    }
}
