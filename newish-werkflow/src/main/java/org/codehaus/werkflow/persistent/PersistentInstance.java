package org.codehaus.werkflow.persistent;

import org.codehaus.werkflow.spi.*;
import org.codehaus.werkflow.nonpersistent.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class PersistentInstance
    extends NonPersistentInstance
{
    private File file;

    PersistentInstance(File file)
        throws Exception
    {
        super( readInstance( file ) );
        this.file = file;
    }

    PersistentInstance(File file,
                       DefaultInstance instance)
        throws Exception
    {
        super( instance );
        this.file = file;
        writeInstance( instance,
                       file );
    }

    public File getFile()
    {
        return this.file;
    }

    public void startTransaction()
        throws Exception
    {
        super.startTransaction();
    }

    public void commitTransaction()
        throws Exception
    {
        writeInstance( (DefaultInstance) getInstance(),
                       getFile() );
        super.commitTransaction();
    }

    public void abortTransaction()
        throws Exception
    {
        super.abortTransaction();
    }

    static void writeInstance(DefaultInstance instance,
                              File file)
        throws Exception
    {
        FileOutputStream fileOut = new FileOutputStream( file );
        ObjectOutputStream objOut = new ObjectOutputStream( fileOut );
        
        try
        {
            objOut.writeObject( instance );
        }
        finally
        {
            fileOut.close();
        }
    }

    static DefaultInstance readInstance(File file)
        throws Exception
    {
        FileInputStream fileIn = new FileInputStream( file );
        ObjectInputStream objIn = new ObjectInputStream( fileIn );

        try
        {
            return (DefaultInstance) objIn.readObject();
        }
        finally
        {
            fileIn.close();
        }
    }
}
