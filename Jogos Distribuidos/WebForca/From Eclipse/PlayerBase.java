import java.net.Socket;
import java.util.ArrayList;

public class PlayerBase {
	private ArrayList<String>  _IPs;		// IP addresses connected
	private ArrayList<String>  _nicknames;		// Nicknames of players
	private ArrayList<Boolean> _dead;			// Guessed wrong the whole word
	private ArrayList<Integer> _score;			// Correct letter count
	private ArrayList<String>  _playerfound;	// Letters found by each player
	private ArrayList<String>  _killedBy;		// Died guessing by this word
	
	
	private ArrayList<Boolean> _ready;			// Who is ready to start the game?
	private ArrayList<Socket> _sockets;			// Saving Sockets for room broadcast
	
	public PlayerBase () {
		_IPs = new ArrayList<String>();
		_nicknames = new ArrayList<String>();
		_dead = new ArrayList<Boolean>();
		_score = new ArrayList<Integer>();
		_playerfound = new ArrayList<String>();
		_killedBy = new ArrayList<String>();
		_ready = new ArrayList<Boolean>();
		_sockets = new ArrayList<Socket> ();
	}
	
	public void join (String IP, Socket sock) {
		if (!_IPs.contains(IP)) {
			_IPs.add(IP);
			_nicknames.add("< Anonymous >");
			_dead.add(false);
			_score.add(0);
			_playerfound.add(" ");
			_killedBy.add(" ");
			_ready.add(false);
			_sockets.add(sock);
		}
	}
	
	public void quit (String IP) {
		int position = getIPIndex(IP);
		if (position != -1) {
			_IPs.remove(position);
			_nicknames.remove(position);
			_dead.remove(position);
			_score.remove(position);
			_playerfound.remove(position);
			_killedBy.remove(position);
			_ready.remove(position);
		}
	}
	
	public void clear () {
		_IPs.clear();
		_nicknames.clear();
		_dead.clear();
		_score.clear();
		_playerfound.clear();
		_killedBy.clear();
		_ready.clear();
	}
	
	public void reset () {
		for (int i = 0; i < _IPs.size(); i++) {
			_dead.set(i, false);
			_score.set(i, 0);
			_playerfound.set(i, " ");
			_killedBy.set(i, " ");
			_ready.set(i, false);
		}
	}
	
	public boolean isEmpty () {
		return _IPs.isEmpty();
	}
	
	public int getIPIndex (String IP) {
		return _IPs.indexOf(IP);
	}
	
	public ArrayList<String> getIPIndexList () {
		return _IPs;
	}
	
	// Nick ================================================
	public String getNickname (int index) {
		return _nicknames.get(index);
	}
	public String getNickname (String IP) {
		return getNickname(getIPIndex(IP));
	}
	public ArrayList<String> getNickList () {
		return _nicknames;
	}
	public ArrayList<String> getNickList (ArrayList<String> roomIPs) {
		ArrayList<String> rv = new ArrayList<String> ();
		
		for (String it : roomIPs)
			rv.add(getNickname(it));
		
		return rv;
	}
	
	public void setNickname (int i, String nick) {
		_nicknames.set(i, nick);
	}
	public void setNickname (String IP, String nick) {
		setNickname(getIPIndex(IP), nick);
	}
	
	// Dead ================================================
	public boolean getDead (String IP) {
		return _dead.get(getIPIndex(IP)).booleanValue();
	}
	public ArrayList<Boolean> getDeadList () {
		return _dead;
	}
	public ArrayList<Boolean> getDeadList (ArrayList<String> roomIPs) {
		ArrayList<Boolean> rv = new ArrayList<Boolean> ();
		
		for (String it : roomIPs)
			rv.add(getDead(it));
		
		return rv;
	}
	
	
	public void setDead (int i, boolean isDead) {
		_dead.set(i, new Boolean(isDead));
	}
	public void setDead (String IP, boolean isDead) {
		setDead(getIPIndex(IP), isDead);
	}
	
	public boolean someoneAlive () {
		return _dead.contains(false);
	}
	
	// Score ===============================================
	public int getScore (int i) {
		return _score.get(i).intValue();
	}
	public int getScore (String IP) {
		return getScore(getIPIndex(IP));
	}
	public ArrayList<Integer> getScoreList () {
		return _score;
	}
	public ArrayList<Integer> getScoreList (ArrayList<String> roomIPs) {
		ArrayList<Integer> rv = new ArrayList<Integer> ();
		
		for (String it : roomIPs)
			rv.add(getScore(it));
		
		return rv;
	}
	
	public void setScore (int i, int score) {
		_score.set(i, new Integer(score));
	}
	public void setScore (String IP, int score) {
		setScore(getIPIndex(IP), score);
	}
	
	public void increaseScore (int i) {
		setScore(i, getScore(i) + 1);
	}
	public void increaseScore (String IP) {
		int index = getIPIndex(IP);
		increaseScore(index);
	}
	public void increaseScore (int i, int amount) {
		setScore(i, getScore(i) + amount);
	}
	
	public ArrayList<String> winning () {
		int maxIndex = 0;
		int max = getScore(maxIndex);
		ArrayList<String> winners = new ArrayList<String> ();
		
		// Finds the maximum value
		for (int i = 1; i < _score.size(); i++) {
			int newGuy = getScore(i);
			if (newGuy > max) {
				max = newGuy;
				maxIndex = i;
			}
		}
		
		// Saves the players who got the max score
		for (int i = 0; i < _score.size(); i++) {
			if (getScore(i) == max) {
				winners.add(_nicknames.get(i));
			}
		}
		
		return winners;
	}
	
	// Player Found ========================================
	public String getPlayerFound (String IP) {
		return _playerfound.get(getIPIndex(IP));
	}
	public ArrayList<String> getPlayerFoundList () {
		return _playerfound;
	}
	public ArrayList<String> getFoundList (ArrayList<String> roomIPs) {
		ArrayList<String> rv = new ArrayList<String> ();
		
		for (String it : roomIPs)
			rv.add(getPlayerFound(it));
		
		return rv;
	}
	
	public void setPlayerFound (int i, String found) {
		_playerfound.set(i, found);
	}
	public void setPlayerFound (String IP, String found) {
		setPlayerFound(getIPIndex(IP), found);
	}
	
	public void resetPlayerFound (int len) {
		StringBuilder result = new StringBuilder ();
		
		// Empty hint: _ _ _ _ _ _
		for (int i = 0; i < len; i++) {
			result.append("_ "); 
		}
		
		result.setLength(result.length()-1);
		String emptyHint = result.toString();
		
		// Apply to everyone
		for (int i = 0; i < _playerfound.size(); i++) {
			setPlayerFound(i, emptyHint);
		}
	}
	
	// Killed By ===========================================
	public String getKilledBy (String IP) {
		return _killedBy.get(getIPIndex(IP));
	}
	public ArrayList<String> getKilledByList () {
		return _killedBy;
	}
	public ArrayList<String> getKilledByList (ArrayList<String> roomIPs) {
		ArrayList<String> rv = new ArrayList<String> ();
		
		for (String it : roomIPs)
			rv.add(getKilledBy(it));
		
		return rv;
	}
	
	public void setKilledBy (int i, String word) {
		_killedBy.set(i, word);
	}
	public void setKilledBy (String IP, String word) {
		setKilledBy(getIPIndex(IP), word);
	}
	
	// Ready ================================================
	public boolean getReady (String IP) {
		return _ready.get(getIPIndex(IP)).booleanValue();
	}
	public ArrayList<Boolean> getReadyList () {
		return _ready;
	}
	public ArrayList<Boolean> getReadyList (ArrayList<String> roomIPs) {
		ArrayList<Boolean> rv = new ArrayList<Boolean> ();
		
		for (String it : roomIPs)
			rv.add(getReady(it));
		
		return rv;
	}
	
	public void setReady (int i, boolean isReady) {
		_ready.set(i, new Boolean(isReady));
	}
	public void setReady (String IP, boolean isReady) {
		setReady(getIPIndex(IP), isReady);
	}
	
	public boolean everyoneReady () {
		return !_ready.contains(false);
	}
	
	// Socket ===============================================
	public Socket getSocket (String IP) {
		return _sockets.get(getIPIndex(IP));
	}
	public ArrayList<Socket> getSocketList (ArrayList<String> roomIPs) {
		ArrayList<Socket> rv = new ArrayList<Socket> ();
		
		for (String it : roomIPs)
			rv.add(getSocket(it));
		
		return rv;
	}
	
	public void setSocket (int i, Socket sock) {
		_sockets.set(i, sock);
	}
	public void setSocket (String IP, Socket sock) {
		setSocket(getIPIndex(IP), sock);
	}
}
