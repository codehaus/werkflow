package com.werken.werkflow.personality;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import com.werken.werkflow.definition.ProcessDefinition;

public interface Personality
{
    ProcessDefinition[] load(URL url)
        throws IOException, Exception;

    ProcessDefinition[] load(URL url, Map beans)
        throws IOException, Exception;
}
