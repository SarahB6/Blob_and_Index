import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.InvalidPathException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;

public class Tree {
    private String sha1;
    HashMap <String, TreeEntry> myMap;

    public Tree ()
    {
        myMap = new HashMap <String, TreeEntry> ();
    }

    public String addDirectory (String name) throws IOException, InvalidPathException
    {
        
        File ogFile = new File(name);
        if(!ogFile.exists())
        {
            throw new java.nio.file.InvalidPathException(name, "Invalid path reason");
        }
        String[] list = ogFile.list();
        for(int i = 0; i<list.length; i++)
        {
            File thisFile = new File(list[i]);
         if(!thisFile.isDirectory())
            {
                String shaOfThisBlob = SHA1FilePath(list[i]);
                Blob b = new Blob(list[i]);
                this.addToTree("blob : " + shaOfThisBlob + " : "  + list[1]);
                
            }
        }
        this.save();
        return sha1;
       // StringBuilder s = addDirectoryReccursive(list, list[0], sb, 0);
        //need to return something
        
    }

    private StringBuilder addDirectoryReccursive (String[] list, String currentFile, StringBuilder currentInfo, int i) throws IOException
    {
        File thisFile = new File(currentFile);
        if(!thisFile.isDirectory())
        {
            String shaOfThisBlob = SHA1FilePath(list[i]);
            this.addToTree("blob : " + shaOfThisBlob + " : "  + list[1]);
            currentInfo.append("blob : " + shaOfThisBlob + " : "  + list[1]); 
            
        }
        else
        {
            if(insideTreeIsAllBlobs(thisFile))
            {
                currentInfo.append(justBlobs(thisFile));
            }
            else
            {
                StringBuilder sub = new StringBuilder();
                StringBuilder thisTree = addDirectoryReccursive(thisFile.list(), thisFile.getPath(), sub, i);
                this.addToTree("tree : "+ SHA1StringInput(thisTree.toString()) + " : " + thisFile.getName());
                currentInfo.append("tree : "+ SHA1StringInput(thisTree.toString()) + " : " + thisFile.getName());
            }
        }
        if(i+1 < list.length)
        {
            addDirectoryReccursive(list, list[i+1], currentInfo, i+1);
        }
        return currentInfo;
    }

    private Boolean insideTreeIsAllBlobs(File thisFile)
    {
        String[] list = thisFile.list();
        for(int i = 0;  i<list.length; i++)
        {
            File nowFile = new File(list[i]);
            if(nowFile.isDirectory())
            {
                return false;
            }
        }
        return true;
    }

    private String justBlobs (File subDirectory) throws IOException
    {
        String[] list = subDirectory.list();
        Tree childTree = new Tree();
        StringBuilder valueToShaToGetTreeSha = new StringBuilder();
        for(int i = 0; i<list.length; i++)
        {
            String shaOfThisBlob = SHA1FilePath(list[i]);
            valueToShaToGetTreeSha.append("blob : " + shaOfThisBlob + " : "  + list[1] + ".txt" ); //ASK IF THIS IS RIGHT
            childTree.addToTree("blob : " + shaOfThisBlob + " : "  + list[1] + ".txt" );
        }
        childTree.save();
        return ("tree : " + SHA1StringInput(valueToShaToGetTreeSha.toString()) + " : " + subDirectory.getPath());
    }

    public void save () throws IOException
    {
        File myFile = new File("./temp");
        myFile.createNewFile();
        FileWriter writer = new FileWriter ("./temp");
        PrintWriter out = new PrintWriter (writer);
        int numDone = 0;
        for (String key : myMap.keySet ())
        {
            if (myMap.get (key).getType ().equals ("blob"))
            {
                if (numDone == 0)
                {
                    out.print ("blob : " + key + " : " + myMap.get (key).getFileName ());
                }
                else
                {
                    out.print ("\nblob : " + key + " : " + myMap.get (key).getFileName ());
                }
            }
            else
            {
                if (numDone == 0)
                {
                    out.print ("tree : " + key);
                }
                else
                {
                    out.print ("\ntree : " + key);
                }
            }
            numDone++;
        }
        writer.close();
        out.close ();
        //renaming file
        sha1 = SHA1FilePath("./temp");
        File file2 = new File ("./objects/" + sha1);
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
    
    public void removeBlob (String fileName)
    {
        for (String key : myMap.keySet ())
        {
            if (myMap.get(key).getFileName ().equals(fileName))
            {
                myMap.remove(key);
            }
        }
    }

    public void removeTree (String SHA1)
    {
        if (myMap.get(SHA1).getType ().equals ("tree"))
        {
            myMap.remove(SHA1);
        }
    }

    public String SHA1FilePath(String input) throws IOException
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
            sha1 = byteToHexTree(crypt.digest());
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

    public String SHA1StringInput(String input) throws IOException
    {
        String dataAsString = input;

        String sha1 = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(dataAsString.getBytes("UTF-8"));
            sha1 = byteToHexTree(crypt.digest());
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
    public String byteToHexTree(final byte[] hash)
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

    //returns the sha1 of a saved tree
    public String getSha1(){
        return sha1;
    }
}