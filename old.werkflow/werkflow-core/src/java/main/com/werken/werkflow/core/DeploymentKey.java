package com.werken.werkflow.core;

class DeploymentKey
{
    private String packageId;
    private String id;

    DeploymentKey(String packageId,
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

        if ( thatObj instanceof DeploymentKey )
        {
            DeploymentKey that = (DeploymentKey) thatObj;

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

    public String toString()
    {
        return "[key: pkg=" + this.packageId + " id=" + this.id + "]";
    }
}
