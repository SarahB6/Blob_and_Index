public class TreeEntry
{
    String type;
    String fileName;
    public TreeEntry (String type, String fileName)
    {
        this.type = type;
        this.fileName = fileName;
    }
    public String getType ()
    {
        return type;
    }
    public String getFileName ()
    {
        return fileName;
    }
}