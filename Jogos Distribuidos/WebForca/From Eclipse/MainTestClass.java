import java.io.IOException;
import java.util.Scanner;

public class MainTestClass {
	private static GameLogic gl;
	private static Scanner input = new Scanner (System.in);
	private static String inputVar = "w00t";

	public static void main(String[] args) throws IOException {
    	debug();
	}
	
	static void debug () throws IOException {
		gl = new GameLogic ();
		
		String word = gl._word;
		display(word);
		display(word.toLowerCase());
		
//    	gl.guessChar('a'); display(gl.hint.toString());
//    	gl.guessChar('E'); display(gl.hint.toString());
//    	gl.guessChar('p'); display(gl.hint.toString());
    	
//    	String hint0 = gl.hint.toString();
//    	String hint1 = gl.plus(hint0, 'a');
//    	String hint2 = gl.plus(hint1, 'e');
//    	String hint3 = gl.plus(hint2, 'p');
    	display(gl.hint.toString());
    	gl.guessWord("esparadrapo");
    	display(gl.hint.toString());
    	
//    	System.out.println(hint1);
//    	System.out.println(hint2);
//    	System.out.println(hint3);
    	
    	new Thread () {
    		public void run () {
    			while (true) {
    				System.out.println(inputVar);
    				try {
						sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    		}
    	}.start();
    	
    	new Thread () {
    		public void run () {
    			while (true) {
    				if (input.hasNextLine()) {
    					inputVar = input.next();
    					System.out.print("Git gud");
    				}
    			}
    		}
    	}.start();
	}
	
	static void display (String msg) {
		System.out.println(msg);
	}

}
