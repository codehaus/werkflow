package com.werken.werkflow.resource;

public interface Resource
{
    static final Resource[] EMPTY_ARRAY = new Resource[0];

    String getId();

    String getDocumentation();

    String[] getResourceClassIds();
}
