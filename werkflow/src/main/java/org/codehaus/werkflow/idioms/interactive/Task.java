package org.codehaus.werkflow.idioms.interactive;

import org.codehaus.werkflow.spi.SatisfactionSpec;
import org.codehaus.werkflow.helpers.SimpleSatisfaction;

public class Task
        extends SimpleSatisfaction implements SatisfactionSpec
{
    private String taskDescription;
    private String assignee;

    public Task(String id, String taskDescription, String assignee)
    {
        super(id);
        this.taskDescription = taskDescription;
        this.assignee = assignee;
    }

    public String getAssignee()
    {
        return assignee;
    }

    public void setAssignee(String assignee)
    {
        this.assignee = assignee;
    }

    public String getTaskDescription()
    {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription)
    {
        this.taskDescription = taskDescription;
    }

    public SatisfactionSpec getSatisfactionSpec()
    {
       return this;
    }

}
