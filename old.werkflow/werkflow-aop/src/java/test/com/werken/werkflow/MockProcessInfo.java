package com.werken.werkflow;

public class MockProcessInfo
    implements ProcessInfo
{
    private String pkgId;
    private String id;
    private String docs;

    public MockProcessInfo(String pkgId,
                           String id,
                           String docs)
    {
        this.pkgId = pkgId;
        this.id = id;
        this.docs = docs;
    }

    public String getPackageId()
    {
        return this.pkgId;
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
