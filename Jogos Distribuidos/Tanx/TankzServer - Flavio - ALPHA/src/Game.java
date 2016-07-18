
import java.awt.EventQueue;
import java.awt.Image;
import java.net.SocketException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * @brief Classe que implementa o jogo em si.
 * 
 *        Executada pela invocação do java
 *
 */
public class Game
{

	public static final String GAME_NAME = "Protocol Test ";
	public static final String GAME_VERSION = "1.0 pre-beta";

	public static String PLAYER = "P1";

	/**
	 * Método principal da classe main
	 * 
	 * @param args
	 * @throws SocketException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws SocketException, InterruptedException {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				startGui();
			}
		});
	}

	/**
	 * 
	 * @return
	 */
	public static String getLocalPlayerId() {
		return GamePanel.getInstance().getPlayerId();
	}

	protected static void startGui()
	{
		new RepeatingReleasedEventsFixer().install();

		/*
		 * GraphicsEnvironment ge =
		 * GraphicsEnvironment.getLocalGraphicsEnvironment(); Font[] fonts =
		 * ge.getAllFonts(); for (int i = 0; i < fonts.length; i++) {
		 * 
		 * System.out.print(fonts[i].getFontName() + " : ");
		 * System.out.println(fonts[i].getFamily()); }
		 */
		String lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
		try {
			UIManager.setLookAndFeel(lookAndFeel);			
		} catch (Exception x) {

		}
		
		Image icon = GamePanel.loadImage("smd.png");
		String name = null;
		do
		{
			name = JOptionPane.showInputDialog("Digite o seu nick no jogo!");
			if (name != null) {
				name = name.trim();
				if (name.length() < 1)
					name = null;
			}
			
		} while (name == null);
		PLAYER = name;

		// cria a tela do jogo em si
		JFrame fr = new JFrame( Game.GAME_NAME );			
		GamePanel gp = GamePanel.getInstance();
				
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.setContentPane(gp);
		fr.pack();	
		
		fr.setIconImage(icon);
		
		fr.setResizable(false);
		fr.setVisible(true);
		fr.addKeyListener(LocalGameInput.getInstance());
	}
	
}
