package com.werken.werkflow.syntax.fundamental;

import com.werken.werkflow.definition.MessageCorrelator;

public interface MessageCorrelatorReceptor
{
    void receiveMessageCorrelator(MessageCorrelator correlator);
    String getMessageId();
}
