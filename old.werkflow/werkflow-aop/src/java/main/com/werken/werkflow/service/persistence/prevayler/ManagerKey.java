/*
 * Created on Apr 5, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.werken.werkflow.service.persistence.prevayler;

import java.io.Serializable;

import com.werken.werkflow.ProcessInfo;

/**
 * @author kevin
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
class ManagerKey implements Serializable
{

    ManagerKey(ProcessInfo info)
    {
        this(info.getPackageId(), info.getId());
    }

    ManagerKey(String packageId, String processId)
    {
        if (null == packageId || null == processId
            || processId.length() == 0 || packageId.length() == 0)
        {
            throw new IllegalArgumentException("The package and process id's can not be null or empty");            
        }
        
        _package = packageId;
        _process = processId;
    }

    private String _process;
    private String _package;
    
    String getProcessId()
    {
        return _process;
    }
    
    String getPackageId()
    {
        return _package;
    }
    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        if (obj instanceof ManagerKey)
        {
            ManagerKey other = (ManagerKey) obj;
            
            return _process.equals(other._process) && _package.equals(other._package);
        }
        
        return false;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return _process.hashCode() + _package.hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "[ManagerKey] Package: " + _package + " Process: " + _process;
    }

}
