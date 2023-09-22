import java.io.IOException;

public class Commit {
    private String treeSha1,nextSha1, author, summary, date;

    //constructor with nextSHA1
    public Commit(String nextSha1, String author, String summary) throws IOException{
        this.treeSha1 = createTree();
        this.nextSha1 = nextSha1;
        this.author = author;
        this.summary = summary;
        this.date = getDate();
    }

    //constructor without nextSHA1
    public Commit(String author, String summary) throws IOException{
        this.treeSha1 = createTree();
        this.author = author;
        this.summary = summary;
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
}
