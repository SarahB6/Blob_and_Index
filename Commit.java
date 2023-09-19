public class Commit {
    private String nextSHA1, author, summary, date;

    //constructor with nextSHA1
    public Commit(String nextSHA1, String author, String summary){
        this.nextSHA1 = nextSHA1;
        this.author = author;
        this.summary = summary;
        this.date = getDate();
    }

    //constructor without nextSHA1
    public Commit(String author, String summary){
        this.author = author;
        this.summary = summary;
        this.date = getDate();
    }

    //returns the current date in YYYY-MM-DD
    public String getDate(){
        return java.time.LocalDate.now().toString();
    }
}
