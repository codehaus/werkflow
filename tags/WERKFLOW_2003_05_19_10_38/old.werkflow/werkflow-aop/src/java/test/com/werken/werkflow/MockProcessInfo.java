package com.werken.werkflow;

import java.util.Map;
import java.util.HashMap;

public class MockProcessInfo
    implements ProcessInfo
{
    private String pkgId;
    private String id;
    private String docs;

    private Map attrDecls;

    public MockProcessInfo(String pkgId,
                           String id,
                           String docs)
    {
        this.pkgId = pkgId;
        this.id = id;
        this.docs = docs;
        this.attrDecls = new HashMap();
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

    public void addAttributeDeclaration(AttributeDeclaration attrDecl)
    {
        this.attrDecls.put( attrDecl.getId(),
                            attrDecl );
    }

    public AttributeDeclaration[] getAttributeDeclarations()
    {
        return (AttributeDeclaration[]) this.attrDecls.values().toArray( AttributeDeclaration.EMPTY_ARRAY );
    }

    public AttributeDeclaration getAttributeDeclaration(String id)
    {
        return (AttributeDeclaration) this.attrDecls.get( id );
    }
}
