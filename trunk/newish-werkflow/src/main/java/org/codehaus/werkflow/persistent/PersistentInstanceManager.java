package org.codehaus.werkflow.persistent;

import org.codehaus.werkflow.*;
import org.codehaus.werkflow.spi.*;
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
                                          InitialContext initialContext)
        throws Exception
    {
        return new PersistentInstance( new File( getRoot(),
                                                 id ),
                                       new DefaultInstance( workflow,
                                                            id,
                                                            initialContext ) );
    }

    public RobustInstance getInstance(String id)
        throws NoSuchInstanceException, Exception
    {
        try
        {
            return super.getInstance( id );
        }
        catch (NoSuchInstanceException e)
        {
            File instanceFile = new File( getRoot(),
                                          id );

            if ( ! instanceFile.exists() )
            {
                throw new NoSuchInstanceException( id );
            }

            return new PersistentInstance( instanceFile );
        }
    }
}
