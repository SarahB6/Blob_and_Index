import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Scanner;

public class Tree {
    /*
    File myFile;
    String oldSHA1;

    public Tree () throws IOException
    {
        myFile = new File("./oldFile");
        myFile.createNewFile ();
        //oldSHA1 = 
        
        myFile = new File("./oldFile");
        myFile.createNewFile ();
        FileWriter writer = new FileWriter("./oldFile",false);
        PrintWriter out = new PrintWriter(writer);
        out.print ("shmokeee");
        writer.close ();
        out.close ();
        
    }
    

    public void save () throws IOException
    {
        //Path oldFilePath = Paths.get ("./oldFile");
        Scanner scanner = new Scanner(new File("./oldFile"));
        String myString = scanner.useDelimiter("\\A").next();
        scanner.close();
        myFile = new File("./objects/temp");
        myFile.createNewFile ();
        FileWriter writer = new FileWriter("./objects/temp",false);
        PrintWriter out = new PrintWriter(writer);
        out.print (myString);
        writer.close ();
        out.close ();
    }

    public void addToTree (String typeAndCount)
    {
        
    }

    public void remove (String SHA1)
    {

    } */
    
    HashMap <String, TreeEntry> myMap;

    public Tree ()
    {
        myMap = new HashMap <String, TreeEntry> ();
    }

    public void save () throws IOException
    {
        String myString;
        //File myFile = new File ("./objects/temp");
        //Scanner scanner = new Scanner(myFile);
        //String myString = scanner.useDelimiter("\\A").next();
        //scanner.close();
        //FileWriter writer = new FileWriter (new File ("./objects/" + SHA1Name(myString)));
        FileWriter writer = new FileWriter ("./objects/temp");
        File myFile = new File("./objects/temp");
        PrintWriter out = new PrintWriter (writer);
        for (String key : myMap.keySet ())
        {
            if (myMap.get (key).getType ().equals ("blob"))
            {
                out.println ("blob : " + key + " : " + myMap.get (key).getFileName ());
            }
            else
            {
                out.println ("tree : " + key);
            }
        }
        writer.close();
        out.close ();
        File file2 = new File ("./objects/" + SHA1Name ("./objects/temp"));
        System.out.println (SHA1Name ("./objects/temp"));
        myFile.renameTo (file2);
        
    }

    
    public void addToTree (String typeAndContent)
    {
        if (typeAndContent.substring (0,4).equals ("blob"))
        {
            myMap.put (typeAndContent.substring (7,47), new TreeEntry ("blob", typeAndContent.substring (50)));
        }
        else
        {
            myMap.put (typeAndContent.substring (7,47), new TreeEntry ("tree", ""));
        }
    }
    

    public void removeLine (String SHA1)
    {
        myMap.remove (SHA1);
    }
    

    //codes the input and returns a sha
    public String SHA1Name(String input) throws IOException
    {
        BufferedReader ogReader= new BufferedReader(new BufferedReader(new FileReader(input))); 
        StringBuilder sb = new StringBuilder();
        while(ogReader.ready())
        {
            sb.append((char)ogReader.read());
        }
        ogReader.close();

        String dataAsString = sb.toString();

        String sha1 = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(dataAsString.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return sha1;
    }

    //helper for above method
    private static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
