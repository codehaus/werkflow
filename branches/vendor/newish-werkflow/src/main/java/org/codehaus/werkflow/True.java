package org.codehaus.werkflow;

public class True
    implements Expression
{
    public static final True INSTANCE = new True();

    public boolean evaluate(Context context)
    {
        return true;
    }

    public String toString()
    {
        return "true";
    }
}
