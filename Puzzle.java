package 

import com.parse.ParseObject;
import com.parse.ParseClassName;

/**
 * Puzzle represents a riddle that the user solves in order to 
 * gain points. Puzzle is a read-only object since we do not 
 * want the user to accidentally change the attributes of a 
 * Puzzle object and persist those changes into the database. 
 */
@ParseClassName("Puzzle")
public class Puzzle extends ParseObject {
    
    public String getRiddle() {
        return getString("riddle");
    }

    public String getAnswer() {
        return getString("answer");
    }

    public int getPoints() {
        return getInt("points");
    }

    public int getLevel() {
        return getInt("level");
    }
    
}

