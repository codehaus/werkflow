package com.werken.werkflow;

public class MockProcessInfo
    implements ProcessInfo
{
    private String id;
    private String docs;

    public MockProcessInfo(String id,
                           String docs)
    {
        this.id = id;
        this.docs = docs;
    }

    public String getId()
    {
        return this.id;
    }

    public String getDocumentation()
    {
        return this.docs;
    }
}
