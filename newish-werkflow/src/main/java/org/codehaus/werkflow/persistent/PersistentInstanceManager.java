package org.codehaus.werkflow.persistent;

import org.codehaus.werkflow.*;
import org.codehaus.werkflow.nonpersistent.NonPersistentInstanceManager;

import java.io.File;

public class PersistentInstanceManager
    extends NonPersistentInstanceManager
{
    private File root;

    public PersistentInstanceManager(File root)
    {
        this.root = root;
    }

    public File getRoot()
    {
        return this.root;
    }

    protected RobustInstance makeInstance(Workflow workflow,
                                          String id,
                                          Context initialContext)
        throws Exception
    {
        return new PersistentInstance( new File( getRoot(),
                                                 "" + id.hashCode()),
                                       new DefaultInstance( workflow,
                                                            id ) );
    }
}
