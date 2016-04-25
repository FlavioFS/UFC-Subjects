class StrB {
	public static void main(String[] args) {
		StringBuilder SB = new StringBuilder ();
		SB.append("abcdefghijklmnopqrs");
		SB.setLength(SB.length()-1);
		SB.replace(2, 2, "#");

		System.out.println(SB.toString());
	}
}