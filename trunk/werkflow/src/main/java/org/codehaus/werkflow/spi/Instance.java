package org.codehaus.werkflow.spi;

import org.codehaus.werkflow.Context;

public interface Instance
    extends Context
{
    public String getId();

    void put(String name,
             Object value);

    Path[] getActiveChildren(Path path);

    public SatisfactionSpec[] getBlockedSatisfactions();
}
