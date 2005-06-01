package org.codehaus.werkflow.spi;

/**
 * @author <a href="mailto:jason@maven.org">Jason van Zyl</a>
 * @version $Id$
 */
public interface ErrorHandler
{
    void handle( Throwable throwable );
}
