package org.codehaus.werkflow;

import java.util.Map;

public interface Context
{
    String getId();
    Object get(String id);
    Map getContextMap();
}
