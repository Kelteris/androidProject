/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialmedia;

/**
 *
 * @author Everyone but Asa
 */
public class Adapter implements SocialMediaEntry {
    private YouTubeVideo temp = new YouTubeVideo();
    
    public Adapter(String user, String postText, String description) {
        temp.setAuthor(user);
        temp.setTitle(postText);
        temp.setDescription(description);
    }
    
    @Override
    public String getUser() {
        return temp.getAuthor();
    }

    @Override
    public String getPostText() {
        return temp.getTitle();
    }

    public String getDescription() {
        return temp.getDescription();
    }

}
