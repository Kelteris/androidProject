package socialmedia;


/**
 * Encapsulates the data of a Facebook status update.
 * @author sburton
 */
public class FacebookPost implements SocialMediaEntry {
    private String user;
    private String postText;
    private int likes;

    public FacebookPost() {
        user = "";
        postText = "";
        likes = 0;
    }
    
    public FacebookPost(String user, String postText, int likes) {
        this.user = user;
        this.postText = postText;
        this.likes = likes;
    }
         
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
    
    
}