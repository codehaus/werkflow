package com.werken.werkflow;

public interface ProcessInfo
{
    static final ProcessInfo[] EMPTY_ARRAY = new ProcessInfo[0];
    
    String getId();

    String getDocumentation();
}
