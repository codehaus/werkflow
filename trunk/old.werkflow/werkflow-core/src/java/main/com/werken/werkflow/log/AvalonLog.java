package com.werken.werkflow.log;

import org.apache.avalon.framework.logger.Logger;

public class AvalonLog
    implements Log
{
    private Logger logger;

    public AvalonLog(Logger logger)
    {
        this.logger = logger;
    }

    public Logger getLogger()
    {
        return this.logger;
    }

    public void debug(String msg)
    {
        getLogger().debug( msg );
    }

    public void info(String msg)
    {
        getLogger().info( msg );
    }

    public void warn(String msg)
    {
        getLogger().warn( msg );
    }

    public void error(String msg)
    {
        getLogger().error( msg );
    }

    public void error(String msg,
                      Throwable err)
    {
        getLogger().error( msg,
                           err );
    }

    public void fatal(String msg)
    {
        getLogger().fatalError( msg );
    }
    
    public void fatal(String msg,
                      Throwable err)
    {
        getLogger().fatalError( msg,
                                err );
    }

    public Log getChildLog(String postfix)
    {
        return new AvalonLog( getLogger().getChildLogger( postfix ) );
    }
}
