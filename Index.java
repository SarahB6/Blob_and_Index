
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class Index 
{
    File f;
    boolean firstAdded;
    public Index()
    {
        firstAdded = true;
    }
    public void setFirstAdded(Boolean which)
    {
        firstAdded = which;
    }
    //checks if the file is already in the Index list
    public Boolean alrInIndex(String fileName) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader("index"));
        Blob currentBlob = new Blob(fileName);
        String SHA1_of_file = currentBlob.SHA1NameBlob(fileName);
        StringBuilder currentString = new StringBuilder();
        while(br.ready())
        {
            currentString.append(br.readLine());
            if(currentString.indexOf(fileName + " : " + SHA1_of_file) > -1)
            {
                br.close ();
                return true;
            }
            currentString.setLength(0);
        }
        br.close();
        return false;
    }

    public void addDirectory(String thisDirectory) throws IOException //CAN I TAKE IN A STRING INSTEAD???
    {
        BufferedWriter bw = new BufferedWriter(new FileWriter("index", true));
        File f = new File(thisDirectory);
        Tree thisDirectoryTree = new Tree();
        String shaOfThisDirectoryTree = thisDirectoryTree.addDirectory(thisDirectory);
        if(firstAdded)
        {
            bw.write("tree : " + shaOfThisDirectoryTree + " : " + f.getName()); //FILE NAME IDK HOW TO GET IT THO
        }
        else
        {
            bw.write("\ntree : " + shaOfThisDirectoryTree + " : " + f.getName());
        }
        bw.close();
        firstAdded = false;
    }

/* 
        //adds the file to the index if it doesn't already exist
    public void addFile(String fileName) throws IOException
    {
        BufferedWriter bw = new BufferedWriter(new FileWriter("index", true));
        Blob currentBlob = new Blob(fileName);
        String SHA1_of_file = currentBlob.SHA1NameBlob(fileName);
        if(!alrInIndex(fileName)) //SHOULD IT BE CHECKING IF THE HASH IS THE SAME????
        {
            System.out.println(fileName + "does not alr exist");
            bw.write(fileName + " : " + SHA1_of_file + "\n");
        }
        bw.close();
    }*/

    //adds the file to the index if it doesn't already exist
    public void addFile(String fileName) throws IOException
    {
        BufferedWriter bw = new BufferedWriter(new FileWriter("./index", true));
        Blob currentBlob = new Blob(fileName);
        String SHA1_of_file = currentBlob.SHA1NameBlob(fileName);

        if(!alrInIndex(fileName)) //SHOULD IT BE CHECKING IF THE HASH IS THE SAME????
        {
            System.out.println(fileName + "does not alr exist");       
            if(firstAdded)
            {
                bw.write("blob : " + SHA1_of_file + " : " + fileName); //FILE NAME IDK HOW TO GET IT THO
            }
            else
            {
                bw.write("\nblob : " + SHA1_of_file + " : " + fileName);
            }
        }
        
        bw.close();
        firstAdded = false;
    }

    //removes file from the index list and objects folder
    public void removeFile(String fileName) throws IOException
    {
        if(alrInIndex(fileName))
        {
            BufferedReader br = new BufferedReader(new FileReader("index"));
            StringBuilder newIndex = new StringBuilder();
            Blob currentBlob = new Blob(fileName);
            String SHA1_of_file = currentBlob.SHA1NameBlob(fileName);
            StringBuilder currentString = new StringBuilder();
            while(br.ready())
            {
                currentString.append(br.readLine());
                if(currentString.indexOf(fileName + " : " + SHA1_of_file) == -1)
                {
                    newIndex.append(currentString + "\n");
                }
                currentString.setLength(0);
                
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter("index", false));
            
            bw.write(newIndex.toString());
            
            bw.close();
            br.close();
        }
    }


    public void initialize() throws IOException
    {
        new File("./objects").mkdirs();
        f = new File("./index");
        f.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter("index", false));
        bw.close();
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
}