import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
    String oldTree;

    public Tree ()
    {
        myMap = new HashMap <String, TreeEntry> ();
        oldTree = "";
    }

    public void addTreeForCommit(String sha) throws IOException
    {
        myMap.put(SHA1StringInput(sha), new TreeEntry("tree", ""));
    }

    public String addDirectory (String name) throws IOException, InvalidPathException
    {
        
        File ogFile = new File(name);
        if(!ogFile.exists())
        {
            throw new java.nio.file.InvalidPathException(name, "Invalid path reason");
        }
        String[] list = ogFile.list();
        Tree insideTree = new Tree();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<list.length; i++)
        {
            File thisFile = new File(name + "/" + list[i]);

            if(!thisFile.isDirectory())
            {
                String shaOfThisBlob = SHA1FilePath("./" + name + "/" + list[i]);
                Blob b = new Blob("./" + name + "/" + list[i]);
                insideTree.addToTree("blob : " + shaOfThisBlob + " : "  + list[i], ""); //should it really be this??
                sb.append("blob : " + shaOfThisBlob + " : "  + list[i] + "\n");
                
            }
            else
            {
                Tree thisDirectoryTree = new Tree ();
                insideTree.addToTree ("tree : " + thisDirectoryTree.addDirectory (name + "/" + list[i]) + " : " + list[i], "");
            }
        }
        insideTree.save();
        if(sb.length()>1)
        {
            sb.deleteCharAt(sb.length()-1);
        }
        myMap.put(SHA1StringInput(sb.toString()), new TreeEntry("tree", name));

        return insideTree.getSha1();
        
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
            else if(myMap.get(key).getFileName().length() > 0)
            {
                if (numDone == 0)
                {
                    out.print ("tree : " + key + " : " + myMap.get (key).getFileName());
                }
                else
                {
                    out.print ("\ntree : " + key + " : " + myMap.get (key).getFileName());
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
    
    public void addToTree (String typeAndContent, String oldTree) throws IOException
    {
        if (typeAndContent.substring (0,4).equals ("blob"))
        {
            myMap.put (typeAndContent.substring (7,47), new TreeEntry ("blob", typeAndContent.substring (50)));//CHANGED TO 50
        }
        else if(typeAndContent.substring (0,4).equals ("tree") && typeAndContent.length() < 50)
        {
            myMap.put (typeAndContent.substring (7,47), new TreeEntry ("tree", ""));//CHANGED TO 50 //this is for a commit tree
            oldTree = typeAndContent.substring(7,47);
        }
        else if(typeAndContent.contains("*deleted*"))
        {
            deleteObj(typeAndContent.substring(10), oldTree);//Old Tree is empty
        }
        else if(typeAndContent.contains("*edited*"))
        {
            editObj(typeAndContent.substring(9), oldTree);
        }
        else
        {
            myMap.put (typeAndContent.substring (7,47), new TreeEntry ("tree", typeAndContent.substring(50)));
        }
    }

    private void editObj(String fileName, String treeSha) throws IOException
    {
        File editedFile = new File(fileName);
        StringBuilder thisInfo = new StringBuilder();
        String sha = "";
        if(editedFile.isDirectory())
        {
            addDirectory(fileName); //does this work like does it make blobs and stuff
            
        }
        else
        {
            Blob newBlob = new Blob(fileName);
            sha = newBlob.SHA1NameBlob(fileName);
        }
        
        File thisF = new File("./objects/" + treeSha);
        if(thisF.exists())
        {
            BufferedReader br = new BufferedReader(new FileReader(thisF));
            
            String oldTreeShaOfThisTree = "";
            Boolean isInThisTree = false;
            while(br.ready())
            {
                String line = br.readLine();
                if(line.contains("tree : ") && line.length() < 50)
                {
                    oldTreeShaOfThisTree = line.substring(7,47);
                    
                }
                else
                {
                    if(line.contains(fileName))
                    {
                        isInThisTree = true;
                        addToTree("blob : " + sha + " : " + fileName, treeSha);
                    }
                    else
                    {
                        thisInfo.append(line + "\n");
                    }
                }
                
            }
            br.close();
            if(isInThisTree)
            {
                if(oldTreeShaOfThisTree.length() > 0)
                {
                thisInfo.append("tree : " + oldTreeShaOfThisTree);
                }
            }

            String[] arr = thisInfo.toString().split("\n");
            for(int i = 0; i<arr.length; i++)
            {
                if (arr[i].length() == 0)
                {

                }
                else if(arr[i].contains("blob : ") || arr[i].length() < 50)
                {
                    addToTree(arr[i], treeSha);
                }
                else
                {
                    addDirectory(arr[i]);
                }
            }
           
            if(!isInThisTree)
            {
                editObj(fileName, oldTreeShaOfThisTree); 
            }  
        }
    }

    private void deleteObj(String fileName, String treeSha) throws IOException
    {
        File f = new File("./objects/" + treeSha);
        if(treeSha.length() == 0)
        {
            throw new FileNotFoundException();
        }
        if(f.exists())
        {
            BufferedReader br = new BufferedReader(new FileReader(f));
            StringBuilder thisInfo = new StringBuilder();
            String oldTreeShaOfThisTree = "";
            Boolean isInThisTree = false;
            while(br.ready())
            {
                String line = br.readLine();
                if(line.contains("tree : ") && line.length() < 50)
                {
                    oldTreeShaOfThisTree = line.substring(7,47);
                    //thisInfo.append(line + "\n");
                }
                else
                {
                    if(line.contains(fileName))
                    {
                        isInThisTree = true;
                    }
                    else
                    {
                        thisInfo.append(line + "\n");
                    }
                }
                
            }
            br.close();
            if(isInThisTree)
            {
                thisInfo.append("tree : " + oldTreeShaOfThisTree);
            }
            String[] arr = thisInfo.toString().split("\n");
            for(int i = 0; i<arr.length; i++)
            {
                if (arr[i].length() == 0)
                {

                }
                else if(arr[i].contains("blob : ") || arr[i].length() < 50)
                {
                    addToTree(arr[i], treeSha);
                }
                else
                {
                    addDirectory(arr[i]);
                }
            }
           
            if(!isInThisTree)
            {
                deleteObj(fileName, oldTreeShaOfThisTree);   
            }


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
    public String getSha1() throws IOException{
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
            else if(myMap.get(key).getFileName().length() > 0)
            {
                if (numDone == 0)
                {
                    out.print ("tree : " + key + " : " + myMap.get (key).getFileName());
                }
                else
                {
                    out.print ("\ntree : " + key + " : " + myMap.get (key).getFileName());
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

        sha1 = SHA1FilePath("./temp");
        myFile.delete();
        return sha1;
    }
}