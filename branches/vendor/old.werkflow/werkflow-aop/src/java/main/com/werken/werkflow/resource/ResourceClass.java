package com.werken.werkflow.resource;

public class ResourceClass
{
    public static final class Accessibility
    {
        public static final Accessibility ONLINE = new Accessibility( "online" );
        public static final Accessibility OFFLINE = new Accessibility( "offline" );

        private String label;

        private Accessibility(String label)
        {
            this.label = label;
        }

        public String toString()
        {
            return this.label;
        }
    }

    public static final class Category
    {
        public static final Category ORGANIZATIONAL = new Category( "organizational" );
        public static final Category CAPABILITY = new Category( "capability" );

        private String label;

        private Category(String label)
        {
            this.label = label;
        }

        public String toString()
        {
            return this.label;
        }
    }

    private String id;
    private String documentation;
    private Accessibility accessibility;
    private Category category;

    public ResourceClass(String id,
                         Category category,
                         Accessibility accessibility)
    {
        this.id            = id;
        this.category      = category;
        this.accessibility = accessibility;
    }

    public String getId()
    {
        return this.id;
    }

    public Accessibility getAccessibility()
    {
        return this.accessibility;
    }

    public boolean isOnline()
    {
        return getAccessibility() == Accessibility.ONLINE;
    }

    public boolean isOffline()
    {
        return getAccessibility() == Accessibility.OFFLINE;
    }

    public Category getCategory()
    {
        return this.category;
    }

    public void setDocumentation(String documentation)
    {
        this.documentation = documentation;
    }

    public String getDocumentation()
    {
        return this.documentation;
    }
}
