package com.werken.werkflow.log;

public class SimpleLog
    implements Log
{
    private String name;

    public SimpleLog(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void debug(String msg)
    {
        synchronized ( SimpleLog.class )
        {
            System.err.println( "[debug] [" + getName() + "] " + msg );
        }
    }

    public void info(String msg)
    {
        synchronized ( SimpleLog.class )
        {
            System.err.println( "[info]  [" + getName() + "] " + msg );
        }
    }

    public void warn(String msg)
    {
        synchronized ( SimpleLog.class )
        {
            System.err.println( "[warn]  [" + getName() + "] " + msg );
        }
    }

    public void error(String msg)
    {
        synchronized ( SimpleLog.class )
        {
            System.err.println( "[error] [" + getName() + "] " + msg );
        }
    }

    public void error(String msg,
                      Throwable err)
    {
        synchronized ( SimpleLog.class )
        {
            System.err.println( "[error] [" + getName() + "] " + msg );
            err.printStackTrace( System.err );
        }
    }

    public void fatal(String msg)
    {
        synchronized ( SimpleLog.class )
        {
            System.err.println( "[fatal] [" + getName() + "] " + msg );
        }
    }
    
    public void fatal(String msg,
                      Throwable err)
    {
        synchronized ( SimpleLog.class )
        {
            System.err.println( "[fatal] [" + getName() + "] " + msg );
            err.printStackTrace( System.err );
        }
    }

    public Log getChildLog(String postfix)
    {
        return new SimpleLog( getName() + "." + postfix );
    }
}
