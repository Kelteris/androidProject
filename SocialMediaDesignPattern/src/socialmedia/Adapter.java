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
    
    public Adapter(YouTubeVideo tmp) {
        temp = tmp;
    }
    
    @Override
    public String getUser() {
        return temp.getAuthor();
    }

    @Override
    public String getPostText() {
        return temp.getTitle() + " " + temp.getDescription();
    }

}
