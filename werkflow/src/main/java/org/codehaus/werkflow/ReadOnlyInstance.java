package org.codehaus.werkflow;

import org.codehaus.werkflow.spi.SatisfactionSpec;

public interface ReadOnlyInstance
    extends Context
{
    String getWorkflowId();
    boolean isComplete();

    SatisfactionSpec[] getBlockedSatisfactions();
    SatisfactionSpec getBlockedSatisfaction(String id);

    String[] getPendingSatisfactionIds();

    SatisfactionSpec[] getEligibleSatisfactions();
}
