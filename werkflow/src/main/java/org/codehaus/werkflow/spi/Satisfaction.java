package org.codehaus.werkflow.spi;

public interface Satisfaction
    extends AsyncComponent
{
    public String getId();

    public abstract SatisfactionSpec getSatisfactionSpec();
}
