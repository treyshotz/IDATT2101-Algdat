import java.io.*;
import java.util.Arrays;

public class LZ77 {
	private static int BUFFERSIZE = (1 << 12) - 1; //12 bits for looking back
	private static int POINTERSIZE = ((1 << 4) - 1); //4 bits for match size
	private int buffersize;
	private char[] data;
	
	DataInputStream compressionInput;
	DataOutputStream compressionOutput;
	DataInputStream deCompressionInput;
	DataOutputStream deCompressionOutput;
	
	public LZ77(String path) throws FileNotFoundException {
		this.buffersize = Math.min(100,BUFFERSIZE);
		this.compressionInput = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
		this.compressionOutput = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path+"compressed")));
		this.deCompressionInput = null;
		this.deCompressionOutput = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path+"decompressed")));
	}
	
	/**
	 * Compression algorithm
	 *
	 * Takes in a file and creates a new file containing the compressed data
	 * Reads file and creates a char array of the content
	 * Find a sufficient pointer for a char
	 * If a pointer is found jump the length of char found and writes to output file
	 * Else writes the char and checks the next
	 * Finally closes the files
	 *
	 * @throws IOException
	 */
	public void compress() throws IOException {
		if (this.compressionInput == null) System.out.println("fack input");
		if (this.compressionOutput == null) System.out.println("fack output");
		data = new char[this.compressionInput.available()];
		String text = new String(this.compressionInput.readAllBytes(), "UTF-8");
		data = text.toCharArray();
		for (char c:data ){
			System.out.print(c);
		}
		for (int i = 0; i < data.length;){
			Pointer pointer = findPointer(i);
			if (pointer != null){
				this.compressionOutput.write(1);
				System.out.println("FUNK A");
				this.compressionOutput.write(pointer.getDistance());
				this.compressionOutput.write(pointer.getLength());
				//System.out.println(1 + "" + pointer.getDistance() +"" +pointer.getLength());
				i = i + pointer.getLength();
			} else {
				this.compressionOutput.write(0);
				System.out.println("FUNK B");
				this.compressionOutput.write(data[i]);
				i = i+1;
			}
		}
		this.compressionInput.close();
		this.compressionOutput.flush();
		this.compressionOutput.close();
	}
	
	private Pointer findPointer(int currentIndex){
		Pointer pointer = new Pointer();
		int stop = currentIndex + POINTERSIZE;
		if (stop > data.length -1) stop = data.length - 1;
		for (int j = currentIndex + 2; j < stop; j++){
			int start = currentIndex - buffersize;
			if (start < 0) start = 0;
			char[] chars = Arrays.copyOfRange(data, currentIndex, j);
			for (int i = start; i < currentIndex; i++){
				int repeat = chars.length / (currentIndex - i);
				int remaining = chars.length % (currentIndex - i);
				char[] tempArray = new char[(currentIndex - i) * repeat + (i + remaining - i)];
				int m = 0;
				for (; m < repeat; m++) {
					int pos = m * (currentIndex - i);
					System.arraycopy(data, i, tempArray, pos, currentIndex - i);
				}
				int pos = m * (currentIndex - i);
				System.arraycopy(data, i, tempArray, pos, remaining);
				if (Arrays.equals(tempArray, chars) && chars.length > pointer.getLength()){
					pointer.setLength(chars.length);
					pointer.setDistance(currentIndex - i);
				}
			}
		}
		if (pointer.getLength() > 0 && pointer.getDistance() > 0){
			return pointer;
		}
		return null;
	}
	
	public void deCompress(String path) throws IOException {
		//inputStream = new BufferedInputStream(new FileInputStream(path));
		//outputStream = new BufferedOutputStream(new FileOutputStream(path+"decompress"));
		do {
			int flag = compressionInput.read();
		} while (true);
	}
	
	public static void main(String[] args) throws IOException {
		String inpath = "/Users/madslun/Documents/Programmering/AlgDat/Oblig7/Java/d.txt";
		LZ77 lz77= new LZ77(inpath);
		
		
		lz77.compress();
		//lz77.decompress(outpath, restored);
	}
}

class Pointer {
	private int length;
	private int distance;
	
	
	public Pointer() {
		this(-1,-1);
	}
	
	public Pointer(int matchLength, int matDistance) {
		super();
		this.length = matchLength;
		this.distance = matDistance;
	}
	
	public int getLength() {
		return length;
	}
	
	public void setLength(int matchLength) {
		this.length = matchLength;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public void setDistance(int matDistance) {
		this.distance = matDistance;
	}
	
}
