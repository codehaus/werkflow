package com.werken.werkflow.example.blog;

import com.werken.werkflow.Wfms;
import com.werken.werkflow.SimpleAttributes;
import com.werken.werkflow.example.BaseExample;

public class Example
    extends BaseExample
{
    public Example()
    {

    }

    public void defaultExample()
        throws Exception
    {
        deploy( "blog.xml" );

        BlogEntry entry = new BlogEntry();

        entry.setTitle( "A Tale of Two Cheeses" );
        entry.setAuthor( "bob@werken.com" );
        entry.setContent( "It was a balmy night when Gouda first considered the proposition..." );

        getMessagingManager().acceptMessage( entry );

        entry = new BlogEntry();

        entry.setTitle( "Bob Considered Harmful" );
        entry.setAuthor( "bob@werken.com" );
        entry.setContent( "While used by many folks, the practice of Bob has many disadvantages..." );

        getMessagingManager().acceptMessage( entry );

        Command command = new Command( "publish",
                                       "A Tale of Two Cheeses" );

        getMessagingManager().acceptMessage( command );

        command = new Command( "reject",
                               "Bob Considered Harmful" );

        getMessagingManager().acceptMessage( command );
    }
}
