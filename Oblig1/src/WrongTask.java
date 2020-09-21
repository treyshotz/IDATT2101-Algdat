public class WrongTask {
	//Task 2.1-2 Oblig 1
	
	public static boolean palindrom(String word, int length) {
		int n = word.length()-1;
		
		if(length < n/2)
			return true;

		return (word.charAt(length) == word.charAt(n-length) && palindrom(word, n-length));
	}
	
	public static void main(String[] args) {
		System.out.println(palindrom("abba", 3)); //True
		System.out.println(palindrom("helt", "helt".length()-1)); //False
		System.out.println(palindrom("redder", "redder".length()-1)); //True
		System.out.println(palindrom("hjemme", "hjemme".length()-1));
		System.out.println(palindrom("tut", "tut".length()-1)); //True
	}
}
