package softagi.firebase;

public class Khalod
{
    private static Khalod khalod;

    private Khalod (){}

    public static Khalod audi ()
    {
        if (khalod == null)
        {
            khalod = new Khalod();
        }
        return khalod;
    }
}
