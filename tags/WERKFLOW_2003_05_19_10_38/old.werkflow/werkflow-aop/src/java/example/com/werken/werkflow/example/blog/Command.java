package com.werken.werkflow.example.blog;

public class Command
{
    private String type;
    private String title;

    public Command(String type,
                   String title)
    {
        this.type  = type;
        this.title = title;
    }

    public String getType()
    {
        return this.type;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String toString()
    {
        return "[Command: " + getType() + " // " + getTitle() + "]";
    }
}
