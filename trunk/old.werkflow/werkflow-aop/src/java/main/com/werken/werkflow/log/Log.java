package com.werken.werkflow.log;

public interface Log
{
    void debug(String msg);

    void info(String msg);

    void warn(String msg);

    void error(String msg);

    void error(String msg,
               Throwable throwable);

    void fatal(String msg);

    void fatal(String msg,
               Throwable throwable);

    Log getChildLog(String postfix);
}
