import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUIView {
	
	// Title Window
	private static JFrame  _frameTitle;
	
	// Panels
	private static JPanel  _panelWindow;
	private static JPanel  _panelNick;

	// Labels
	private static JLabel  _labelEmpty;
	private static JLabel  _labelNick;
	
	// Buttons
	private static JButton _btnNewGame;
	private static JButton _btnNick;
	private static JButton _btnQuit;
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		initTitle();
	}

	public static void initTitle () {
		_frameTitle  = new JFrame  ("Meron Forca");
		
		_panelWindow = new JPanel (); 
//		_panelNick   = new JPanel ();
		
		_labelEmpty  = new JLabel  ("");
		_labelNick   = new JLabel  ("Your nick here");
		
		_btnNewGame  = new JButton ("New Game Room");
		_btnNick     = new JButton ("Set Nickname");
		_btnQuit     = new JButton ("Quit");

		// Layouts
		_panelWindow.setLayout(new GridLayout(3, 2, 5, 5));
//		_panelNick.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		// Add
		_panelWindow.add(_btnNick);
		_panelWindow.add(_labelNick);

		_panelWindow.add(_btnNewGame);	_panelWindow.add(new JLabel (""));
//		_panelWindow.add(_panelNick);
		_panelWindow.add(_btnNick);		_panelWindow.add(_labelNick);	
		_panelWindow.add(_btnQuit);		_panelWindow.add(new JLabel (""));
		
		_frameTitle.add(_panelWindow);
		
//		_panelNick.setLayout(new FlowLayout (FlowLayout.LEFT));
//		_panelNick.setLayout(new BoxLayout(_panelNick, BoxLayout.Y_AXIS));
		
		// Configs
		_frameTitle.setSize(500, 500);
		_frameTitle.setVisible(true);
	}
}
