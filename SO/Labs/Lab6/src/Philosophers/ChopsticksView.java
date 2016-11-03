import org.omg.CORBA._IDLTypeStub;

public class ChopsticksView {
	/* =====================================================================================
	 *   CONSTANTS
	 * ===================================================================================== */
	public static final int THINKING = 0;
	public static final int HUNGRY = 1;
	public static final int EATING = 2;
	public static final int LEFT = 3;
	public static final int RIGHT = 4;
	
	private static final String[] SYMBOLS = {"...", " ! ", " \\/", " \\ ", "  /"};
	
	private static boolean _isJumper;
	
	private static final String MEGA_JUMP = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
			+ "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
			+ "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
			+ "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
			+ "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";
	
	
	/* =====================================================================================
	 *   ATTRIBUTES
	 * ===================================================================================== */
	private String[] _names;
	private int[] _values;
	private int _hashiCount;
	private String _headerStr;
	private String _valuesStr;
	
	IChopsticks _table;
	
	
	/* =====================================================================================
	 *   CONSTRUCTOR
	 * ===================================================================================== */
	public ChopsticksView (final int size, String[] names, final boolean jumper) {
		_hashiCount = size;
		_isJumper = jumper;
		_names = new String [_hashiCount];
		_values = new int [_hashiCount];
				
		for (int i=0; i < _hashiCount; i++){
			_names[i] = names[i];
			_values[i] = THINKING;
		}
		
		calcHeader();

		if(!_isJumper) {
			System.out.println(_headerStr);
		}
	}
	
	
	/* =====================================================================================
	 *   METHODS
	 * ===================================================================================== */
	public void updateValue (final int position, final int value) {
		_values[position] = value;
	}
	
	public void display () {
		updateValues();
		
		if (_isJumper) {
			System.out.print(MEGA_JUMP + _headerStr + "\n" + _valuesStr);
		}
		else {
			System.out.print("\r" + _valuesStr);
		}
	}
	
	private void updateValues () {
		 _valuesStr = String.format("| %3s ", SYMBOLS[_values[0]]);
		 for (int i=1; i<_hashiCount; i++) {
		 	_valuesStr += String.format("| %3s ", SYMBOLS[_values[i]]);
		 }
		 _valuesStr += "|";
	}
		 
	 private void calcHeader () {
		 _headerStr = String.format(
	 		"Thinking:     %3s\n" +
	 		"Hungry:       %3s\n" +
	 		"Eating:       %3s\n" +
	 		"Left Hashi:   %3s\n" +
	 		"Right Hashi:  %3s\n\n" +
	 		"| %3s ", SYMBOLS[THINKING], SYMBOLS[HUNGRY], SYMBOLS[EATING], SYMBOLS[LEFT], SYMBOLS[RIGHT], _names[0]);
	 
		 for (int i=1; i<_hashiCount; i++) {
		 	_headerStr += String.format("| %3s ", _names[i]);
		 }
		 
		 _headerStr += String.format("|\n+-----", _names[0]);
		 for (int i=1; i<_hashiCount; i++) {
		 	_headerStr += String.format("+-----", _names[i]);
		 }
		 _headerStr += String.format("+", _names[_hashiCount-1]);
	}
}
