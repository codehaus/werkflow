package org.codehaus.werkflow.drools;

import org.codehaus.werkflow.Context;
import org.codehaus.werkflow.spi.SatisfactionCallback;

public interface Request
{
    String getId();
    Object get(String id);
}
