package com.werken.werkflow.example.blog;

public class BlogEntry
{
    private String title;
    private String author;
    private String content;

    public BlogEntry()
    {

    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return this.title;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getAuthor()
    {
        return this.author;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return this.content;
    }

    public String toString()
    {
        return "[BlogEntry: title=" + this.title
            + "; author=" + this.author
            + "; content=" + this.content + "]";
    }

}
