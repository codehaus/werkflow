package org.codehaus.werkflow.spi;

import org.codehaus.werkflow.Context;

public interface Instance
    extends Context
{
    void put(String name,
             Object value);

    Path[] getActiveChildren(Path path);
}
