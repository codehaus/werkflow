package org.codehaus.werkflow.spi;

public interface SatisfactionValues
{
    String[] getNames();

    Object getValue(String name);
}
