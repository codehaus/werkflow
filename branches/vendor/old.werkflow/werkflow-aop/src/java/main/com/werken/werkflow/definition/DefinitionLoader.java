package com.werken.werkflow.definition;

import java.io.IOException;
import java.net.URL;

public interface DefinitionLoader
{
    ProcessDefinition[] load(URL url)
        throws Exception;
}
