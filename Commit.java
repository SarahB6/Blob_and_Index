import java.io.IOException;

public class Commit {
    private String treeSha1, prevSha1, nextSha1, author, summary, date;

    //constructor with nextSHA1
    public Commit(String prevSha1, String nextSha1, String author, String summary) throws IOException{
        this.treeSha1 = createTree() + "\n";
        this.prevSha1 = prevSha1 + "\n";
        this.nextSha1 = nextSha1 + "\n";
        this.author = author + "\n";
        this.summary = summary + "\n";
        this.date = getDate();
    }

    //constructor without nextSHA1
    public Commit(String prevSha1, String author, String summary) throws IOException{
        this.treeSha1 = createTree() + "\n";
        this.prevSha1 = prevSha1 + "\n";
        this.nextSha1 = "\n";
        this.author = author + "\n";
        this.summary = summary + "\n";
        this.date = getDate();
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
        return "";
    }
}
