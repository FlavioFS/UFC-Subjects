import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TitleFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txtNicknameHere;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TitleFrame frame = new TitleFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TitleFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setForeground(Color.GREEN);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewGame = new JButton("New Game");
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewGame.setFont(new Font("Purisa", Font.PLAIN, 30));
		btnNewGame.setBackground(Color.DARK_GRAY);
		btnNewGame.setForeground(Color.GREEN);
		btnNewGame.setBounds(50, 50, 800, 100);
		contentPane.add(btnNewGame);
		
		JButton btnEditNickname = new JButton("Edit Nickname");
		btnEditNickname.setForeground(Color.GREEN);
		btnEditNickname.setBackground(Color.DARK_GRAY);
		btnEditNickname.setBounds(50, 200, 200, 50);
		contentPane.add(btnEditNickname);
		
		txtNicknameHere = new JTextField();
		txtNicknameHere.setToolTipText("Change your nickname here!!");
		txtNicknameHere.setBounds(300, 200, 550, 50);
		contentPane.add(txtNicknameHere);
		txtNicknameHere.setColumns(10);
	}
}
