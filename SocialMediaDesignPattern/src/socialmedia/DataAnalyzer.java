package socialmedia;


import java.util.List;

/**
 * Provides methods for analysis of social media data
 *
 * DO NOT MODIFY THIS CLASS!!
 * 
 * @author sburton
 */

// DO NOT MODIFY THIS CLASS!!
public class DataAnalyzer {
    /**
     * Analyzes a list of social media entries
     * @param entries 
     */
    public void analyzeEntries(List<SocialMediaEntry> entries) {
        for (SocialMediaEntry entry : entries) {
            // we would do actual anlysis here
            // but for now, just print a message about it
            System.out.println("Analyzing '" + entry.getPostText() + "' by " + entry.getUser());
        }
        
        // Print a blank line at the end
        System.out.println();
    }
}
