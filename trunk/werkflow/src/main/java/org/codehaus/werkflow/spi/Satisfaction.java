package org.codehaus.werkflow.spi;

public interface Satisfaction
    extends AsyncComponent
{
    String getId();

    SatisfactionSpec getSatisfactionSpec();
}
