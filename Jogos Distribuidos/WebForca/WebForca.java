/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webforca;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
public class WebForca {
    
    public static boolean isClient = true;
    // public final static String IP_SERVER = "10.102.104.66"; 
    public final static String IP_SERVER = "192.168.1.6"; 
    public final static int PORT = 666;
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    // ================== NETWORK ==================
    public static void main(String[] args) throws IOException {
        if (args.length != 0 && "sv".equalsIgnoreCase(args[0])) {
            isClient = false;
        }
        
        if (isClient) {
            Socket _client = new Socket(IP_SERVER, PORT);
            System.out.println("Attempting to connect to: " + IP_SERVER);
            
        }
        else {
            ServerSocket _server = new ServerSocket(PORT);
            Socket _client = _server.accept();
            
            System.out.println("Cliente conectado: " + _client.getInetAddress());
            Forca f = new Forca ();
            while (true) {
                System.out.println(_client.getInetAddress() + " sends: " + _client.getInputStream());
            }
        }
    }
    
    // ================== GAME LOGIC ==================
    private static class Forca {
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
        
        public Forca() {
            this.shuffle();
        }
        
        
        public void guessChar (char c) throws IOException {
            char cLower = Character.toLowerCase(c);
            int result = this._word.toLowerCase().indexOf(cLower);
            if (result == -1) {
                this.lives--;
                this.attempts.add(cLower);
                
                if (this.lives <= 0) {
                    this.pauseGame("Game Over!");
                }
            }
            else {
                this.hint.setCharAt(result, c);
                System.out.println(this.hint);
                
                if(this.hint.toString().equals(this._word)) {
                    this.pauseGame("Victory!");
                }
            }
        }
        
        private void shuffle () {
            this.lives = 6;
            this.attempts.clear();
            Random r = new Random ();
            this._word = _dictionary[r.nextInt(_dictionary.length)];
            this.hint.setLength(0);
            for (int i = 0; i < _word.length(); i++) {
                this.hint.append("_ ");
            }
            
            System.out.println(this._word);
            System.out.println(this.hint);
        }
        
        private void pauseGame (String message) throws IOException {
            System.out.println(message);
            System.in.read();
            this.shuffle();
        }
        
    }
    
}
//System.out.println();