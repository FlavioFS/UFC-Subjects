/** Model Class for the game "GameLogic"
  *
  */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.io.IOException;

public class GameLogic {
    private static final String [] _dictionary = {
        "Compilador",  "Prolixidade",  "Esparadrapo",     "Ampulheta", "Delicadamente",    "Gambiarra",
        "Sabedoria",    "Intrepidez",     "Capcioso", "Conectividade",     "Protocolo", "Dristibutivo",
        "Embarcado", "Portabilidade", "Digitalizado",  "Criptografia",  "Multijogador",  "Cooperativo"
    };
    
    public String _word;
    
    public StringBuilder hint;
    public int lives;
    public ArrayList<Character> attempts;
    public boolean victory;
    public boolean lose;
    
    public GameLogic() {
    	hint = new StringBuilder();
    	attempts = new ArrayList<Character> ();
        this.shuffle();
    }
    
    // Returns the sorted attempts in the form of a String
    public String getAttempts () {
    	StringBuilder result = new StringBuilder ();
    	
    	Collections.sort(attempts);
    	
    	result.append(attempts.get(0).toString());
    	for (int i = 1; i < attempts.size();  i++) {
    		result.append(", " + attempts.get(i).toString());
    	}
    	
    	return result.toString();
    }
    
    public boolean validateGuess (char c) {
    	char cLower = Character.toLowerCase(c);
        
        // Character already belongs to the hint
        int result = this.hint.toString().toLowerCase().indexOf(cLower);
        if (result != -1) return false; 			// Already in the hint
        if (!Character.isLetter(c)) return false;	// Not a char
        
        return true;
    }
    
    /* Returns
     * -> true:  when the guess is a valid play
     * -> false: when the guess is an invalid play
     */
    public boolean guessChar (char c) throws IOException {
        if (lose || victory) return false;
    	
    	char cLower = Character.toLowerCase(c);
    	String wLower = _word.toLowerCase();
    	
        int cIndex = wLower.indexOf(cLower);	// Where is 'c'? 
        if (cIndex == -1) { 	// No 'c' found ==> Wrong!
            this.lives--;
            this.attempts.add(cLower);
            
            lose = (lives <= 0) ? true : false; // Any chances left?
            
            return false;
        }
        else {		// Correct
        	for (int i = 0; i < _word.length(); i++) {
        		if ( wLower.charAt(i) == cLower ) {
        			hint.setCharAt(2*i, _word.charAt(i));
        		}
        	}
            
            // Got the whole word?
            if(this.hint.indexOf("_") == -1) {
                this.victory = true;
            }
            
            return true; // Valid play
        }

        
    }
    
    // Guess the whole word
    public boolean guessWord (String gword) {
    	if (lose || victory) return false;
    	
    	if ( gword.toLowerCase().equals(_word.toLowerCase()) ) {
    		this.victory = true;
    		
    		for (int i = 0; i < _word.length(); i++) {
    			hint.setCharAt(2*i, _word.charAt(i));
    		}
    		
    		return true;
    	}
    	
    	return false;
    }
    
    // Restarts the game by resetting the class
    public void shuffle () {
        this.lives = 6;
        this.attempts.clear();
        this.victory = false;
        this.lose = false;
        Random r = new Random ();
//        this._word = _dictionary[r.nextInt(_dictionary.length)];
        this._word = _dictionary[2];
        this.hint.setLength(0);
        for (int i = 0; i < _word.length(); i++) {
            this.hint.append("_ ");
        }        
    }
    
    
    public String plus (String hint, char ch) {
    	if (hint.equals(" ")) hint = "";
    	
    	StringBuilder result = new StringBuilder (hint);
    	
    	for (int i = 0; i < _word.length(); i++) {
    		if (Character.toLowerCase(_word.charAt(i)) == Character.toLowerCase(ch)) {
    			result.setCharAt(2*i, _word.charAt(i));
    		}
    	}
    	
    	if (result.toString().equals("")) return " ";
    	return result.toString();
    }
    
    public int wordSize () {
    	return _word.length();
    }
    
    public int missingLetters () {
    	int unknown = 0;
    	for (int i = 0; i < hint.length(); i++) {
    		if (hint.charAt(i) == '_') {
    			unknown++;
    		}
    	}
    	return unknown;
    }
    
}