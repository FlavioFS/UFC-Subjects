/** Model Class for the game "Hangman"
  *
  */

import java.util.ArrayList;
import java.util.Random;
import java.io.IOException;

public class Hangman {
    private static final String [] _dictionary = {
        "Compilador",
        "Prolixidade",
        "Esparadrapo",
        "Ampulheta",
        "Delicadamente",
        "Gambiarra",
        "Sabedoria",
        "Intrepidez"
    };
    
    private String _word;
    
    public StringBuilder hint;
    public int lives;
    public ArrayList<Character> attempts;
    public boolean victory;
    
    public Hangman() {
    	hint = new StringBuilder();
    	attempts = new ArrayList<Character> ();
        this.shuffle();
    }
    
    /* Returns
     * -> true:  when the guess is a valid play
     * -> false: when the guess is an invalid play
     */
    public boolean guessChar (char c) throws IOException {
        char cLower = Character.toLowerCase(c);
        
        // Character already belongs to the hint
        int result = this.hint.toString().toLowerCase().indexOf(cLower);
        if (result != -1) {
            return false; // Invalid play
        }

        result = this._word.toLowerCase().indexOf(cLower);
        if (result == -1) { // Wrong
            this.lives--;
            this.attempts.add(cLower);
        }
        else {              // Correct
            this.hint.setCharAt(result, c);
            System.out.println(this.hint);
            
            // Got the whole word?
            if(this.hint.toString().equals(this._word)) {
                this.victory = true;
            }
        }

        return true; // Valid play
    }
    
    // Restarts the game by resetting the class
    private void shuffle () {
        this.lives = 6;
        this.attempts.clear();
        this.victory = false;
        Random r = new Random ();
        this._word = _dictionary[r.nextInt(_dictionary.length)];
        this.hint.setLength(0);
        for (int i = 0; i < _word.length(); i++) {
            this.hint.append("_ ");
        }
        
        // System.out.println("\n" + this._word);
        // System.out.println(this.hint + "(" + this._word.length() + " letras)\n");
    }
}