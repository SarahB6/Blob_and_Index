import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Commit {
    private String treeSha1, prevSha1Commit, nextSha1Commit, author, summary, date;


    //constructor with nextSHA1
    public Commit(String prevSha1Commit, String nextSha1Commit, String author, String summary) throws IOException{
        
        this.prevSha1Commit = prevSha1Commit + "\n";
        this.nextSha1Commit = nextSha1Commit + "\n";
        this.author = author + "\n";
        this.date = getDate() + "\n";
        this.summary = summary;
        this.treeSha1 = createTree() + "\n";
        writeToFile();

        
    }

    //constructor without nextSHA1
    public Commit(String prevSha1Commit, String author, String summary) throws IOException{
        
        this.prevSha1Commit = prevSha1Commit + "\n";
        this.nextSha1Commit = "\n";
        this.author = author + "\n";
        this.date = getDate() + "\n";
        this.summary = summary;
        if(prevSha1Commit.length() < 1)
        {
            this.treeSha1 = createTreeWithoutPrev() + "\n";
        }
        else
        {
            this.treeSha1 = createTree() + "\n";
            addNextShaToOldTree(getFirstLine(prevSha1Commit));
        }
        writeToFile();
    }

    public void addNextShaToOldTree(String oldtreeSha) throws IOException
    {
        String s =  prevSha1Commit.substring(0, prevSha1Commit.length()-1);
        File oldCommitFile = new File("./objects/" + s);
        BufferedReader br1 = new BufferedReader(new FileReader(oldCommitFile));

        StringBuilder newInfo = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(oldCommitFile));
        for(int i = 0;  i<2; i++)
        {
            newInfo.append(br.readLine() + "\n");
        }
        newInfo.append(getSha1() + "\n");
        br.readLine();
        while(br.ready())
        {
            newInfo.append(br.readLine() + "\n");
        }
        newInfo.setLength(newInfo.length()-1);
        BufferedWriter bw = new BufferedWriter(new FileWriter(oldCommitFile, false));
        bw.write(newInfo.toString());
        bw.close();
        br.close();
    }

    public Commit(String author, String summary) throws IOException
    {
        this.treeSha1 = createTreeWithoutPrev() + "\n";
        this.prevSha1Commit = ""+ "\n";
        this.nextSha1Commit = "\n";
        this.author = author + "\n";
        this.date = getDate() + "\n";
        this.summary = summary;
        writeToFile();
    }

    public String getFirstLine(String c) throws IOException
    {   
        BufferedReader br = new BufferedReader(new FileReader("./objects/" + c));
        String s = br.readLine();
        br.close();
        return s;
    }

    public String createTree() throws IOException{
        String prevShaWithoutNextLine = prevSha1Commit.substring(0, prevSha1Commit.length()-1);
        String oldTreeSha = getFirstLine(prevShaWithoutNextLine);
        //writes the staged changes into the tree
        Tree tree = new Tree();
        File f1 = new File("./index");
        Boolean neededToDeleteOrEdit = false;
        BufferedReader br = new BufferedReader(new FileReader("./index"));
        while(br.ready())
        {
            String line = br.readLine();
            if(line.contains("blob : ") || line.length()<50)
            {
                tree.addToTree(line, oldTreeSha);
                if(line.contains("deleted") || line.contains("edited"))
                {
                    neededToDeleteOrEdit = true;
                }
            }
            else if(line.contains("tree : "))
            {
                tree.addDirectory(line.substring(50));
            }
            
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter("./index", false));
        //clears the index
        bw.write("");
        bw.close();
        //adds previous tree
        if(!neededToDeleteOrEdit)
        {
            tree.addToTree("tree : " + oldTreeSha, "");
        }
        tree.save();
        br.close();
        return tree.getSha1();
    }



    //writes all of the instance variable info to a file in the objects folder
    public void writeToFile() throws FileNotFoundException{
        String fileContents = treeSha1 + prevSha1Commit + nextSha1Commit + author + date + summary;
        System.out.println(fileContents); //POINT OF THIS??
        File commitFile = new File("./objects/" + getSha1());
        PrintWriter pw = new PrintWriter(commitFile);
        pw.print(fileContents);
        pw.close();
    }
    //returns the current date in YYYY-MM-DD
    public String getDate(){
        return java.time.LocalDate.now().toString();
    }

    public String createTreeWithoutPrev() throws IOException
    {
        //String oldTreeSha = getFirstLine(prevSha1);
        //writes the staged changes into the tree
        Tree tree = new Tree();
        BufferedReader br = new BufferedReader(new FileReader("./index"));
        while(br.ready())
        {
            String line = br.readLine();
            if(line.contains("blob : "))
            {
                tree.addToTree(line, "");
            }
            else
            {
                tree.addDirectory(line.substring(50));
            }
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter("./index", false));
        //clears the index
        bw.write("");
        bw.close();
        //adds previous tree
        tree.save();
        br.close();
        return tree.getSha1();
    }

   // public void checkout(String SHA1OfCommit)
    //{
        //create tree with everything from that commit tree
        
   // }


  

    public String getSha1(){
        String fileContents = treeSha1 + prevSha1Commit + nextSha1Commit+ author + date + summary;
        System.out.println(fileContents);
        return getStringHash(fileContents);
    }

    //returns the sha1 of an inputed string
    public static String getStringHash(String input)
    {
        try {
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}