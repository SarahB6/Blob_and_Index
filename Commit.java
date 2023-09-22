import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Commit {
    private String treeSha1, prevSha1, nextSha1, author, summary, date;

    //constructor with nextSHA1
    public Commit(String prevSha1, String nextSha1, String author, String summary) throws IOException{
        this.treeSha1 = createTree() + "\n";
        this.prevSha1 = prevSha1 + "\n";
        this.nextSha1 = nextSha1 + "\n";
        this.author = author + "\n";
        this.date = getDate() + "\n";
        this.summary = summary;
        
    }

    //constructor without nextSHA1
    public Commit(String prevSha1, String author, String summary) throws IOException{
        this.treeSha1 = createTree() + "\n";
        this.prevSha1 = prevSha1 + "\n";
        this.nextSha1 = "\n";
        this.author = author + "\n";
        this.date = getDate() + "\n";
        this.summary = summary;
    }

    //writes all of the instance variable info to a file in the objects folder
    public void writeToFile() throws FileNotFoundException{
        String fileContents = treeSha1 + prevSha1 + nextSha1 + author + date + summary;
        System.out.println(fileContents);
        File commitFile = new File("./objects/" + getSha1());
        PrintWriter pw = new PrintWriter(commitFile);
        pw.print(fileContents);
        pw.close();
    }
    //returns the current date in YYYY-MM-DD
    public String getDate(){
        return java.time.LocalDate.now().toString();
    }

    //creates and saves an empty tree to the objects folder, returns the sha of the tree
    public String createTree() throws IOException{
        Tree tree = new Tree();
        tree.save();
        return tree.getSha1();
    }

    public String getSha1(){
        String fileContents = treeSha1 + prevSha1 + nextSha1 + author + date + summary;
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
