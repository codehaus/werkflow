package com.werken.werkflow.service.caserepo;

public interface CaseState
{
    String getCaseId();

    String getProcessId();

    void setAttribute(String key,
                      Object value);

    void clearAttribute(String key);

    Object getAttribute(String key);

    String[] getAttributeNames();

    void addMark(String placeId);

    void removeMark(String placeId);

    String[] getMarks();

    boolean hasMark(String placeId);

    void store();
}
