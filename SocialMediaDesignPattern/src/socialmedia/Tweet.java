package socialmedia;

/**
 * Encapsulates the data of a Twitter 'Tweet'.
 * @author sburton
 */
public class Tweet implements SocialMediaEntry {
    private String user;
    private String postText;
    private int retweets;


    public Tweet() {
        user = "";
        postText = "";
        retweets = 0;
    }
    
    public Tweet(String user, String postText, int retweets) {
        this.user = user;
        this.postText = postText;
        this.retweets = retweets;
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

    public int getRetweets() {
        return retweets;
    }

    public void setRetweets(int retweets) {
        this.retweets = retweets;
    }
    
    
}
