/*
 * $Id$
 */

package org.codehaus.werkflow.tagalog;

import org.codehaus.tagalog.AbstractTag;
import org.codehaus.tagalog.Attributes;
import org.codehaus.tagalog.TagException;
import org.codehaus.werkflow.idioms.Parallel;
import org.codehaus.werkflow.spi.Component;

/**
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class ParallelTag
    extends AbstractTag
{
    private Parallel parallel;

    public void begin(String elementName,
                      Attributes attributes)
        throws TagException
    {
        parallel = new Parallel();
    }

    public void child(Object child)
        throws TagException
    {
        if ( child instanceof Component )
        {
            parallel.addBranch( (Component) child );
        }
        else
        {
            throw new TagException( "child element must be a Component" );
        }
    }

    public Object end(String elementName)
        throws TagException
    {
        return parallel;
    }

    public boolean recycle()
    {
        parallel = null;

        return super.recycle();
    }
}
