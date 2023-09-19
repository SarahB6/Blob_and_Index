public class Commit {
    private String nextSHA1, author, summary;

    //constructor with nextSHA1
    public Commit(String nextSHA1, String author, String summary){
        this.nextSHA1 = nextSHA1;
        this.author = author;
        this.summary = summary;
    }

    //constructor without nextSHA1
    public Commit(String author, String summary){
        this.author = author;
        this.summary = summary;
    }
}
