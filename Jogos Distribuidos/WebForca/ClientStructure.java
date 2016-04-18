import java.net.*;
import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Set;


public class ClientStructure {
	public Socket  socket;
	public String  IP;
	public String  nickname;
	public boolean playing;
	public boolean ready;	
	public boolean dead;
	public int     score;
	public String  playerFound;

	public ClientStructure (Socket socket) {
		this.socket = socket;
		IP = this.socket.getRemoteSocketAddress().toString();
		nickname = IP;
		playing = false;
		ready = false;
		dead = false;
		score = 0;
		playerFound = "";
	}
}