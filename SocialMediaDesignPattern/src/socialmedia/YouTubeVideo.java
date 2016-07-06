package socialmedia;

/**
 * Encapsulates the data of a YouTube video.
 * 
 * DO NOT MODIFY THIS CLASS!!
 * 
 * @author sburton
 */

// DO NOT MODIFY THIS CLASS!
public class YouTubeVideo {
    private String author;
    private String title;
    private String description;

    public YouTubeVideo() {
        author = "";
        title = "";
        description = "";
    }

    public YouTubeVideo(String author, String title, String description) {
        this.author = author;
        this.title = title;
        this.description = description;
    }
    
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
