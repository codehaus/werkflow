package com.werken.werkflow.personality;

import com.werken.werkflow.definition.ProcessDefinition;

import java.io.IOException;
import java.net.URL;

public interface Personality
{
    ProcessDefinition[] load(URL url)
        throws IOException, Exception;
}
