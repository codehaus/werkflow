package com.werken.werkflow.syntax;

import com.werken.werkflow.jelly.MiscTagSupport;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

import java.net.URL;

public class ImportTag
    extends MiscTagSupport
{
    private String url;

    public ImportTag()
    {

    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUrl()
    {
        return this.url;
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        requireStringAttribute( "url",
                                getUrl() );

        try
        {
            URL baseUrl = getContext().getCurrentURL();

            URL scriptUrl = new URL( baseUrl,
                                     getUrl() );

            getContext().runScript( scriptUrl,
                                    output,
                                    true,
                                    true );
        }
        catch (Exception e)
        {
            throw new JellyTagException( e );
        }
    }
}
