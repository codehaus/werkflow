package com.werken.werkflow;

public class MockProcessCase
    extends SimpleAttributes
    implements ProcessCase
{
    private String id;
    private ProcessInfo processInfo;

    public MockProcessCase(String id,
                           ProcessInfo processInfo)
    {
        this.id          = id;
        this.processInfo = processInfo;
    }

    public String getId()
    {
        return this.id;
    }

    public ProcessInfo getProcessInfo()
    {
        return this.processInfo;
    }
}
