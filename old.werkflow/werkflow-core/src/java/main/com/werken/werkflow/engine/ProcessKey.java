package com.werken.werkflow.engine;

class ProcessKey
{
    private String packageId;
    private String id;

    ProcessKey(String packageId,
               String id)
    {
        this.packageId = packageId;
        this.id = id;
    }

    String getPackageId()
    {
        return this.packageId;
    }

    String getId()
    {
        return this.id;
    }

    public boolean equals(Object thatObj)
    {
        if ( thatObj == this )
        {
            return true;
        }

        if ( thatObj instanceof ProcessKey )
        {
            ProcessKey that = (ProcessKey) thatObj;

            return ( getPackageId().equals( that.getPackageId() )
                     &&
                     getId().equals( that.getId() ) );
        }

        return false;
    }
    
    public int hashCode()
    {
        return getPackageId().hashCode() + getId().hashCode();
    }
}
