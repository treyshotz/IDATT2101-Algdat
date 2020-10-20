import java.io.*;

public class Compression {
	DataInputStream inputFile;
	DataOutputStream outputFile;
	
	public Compression(DataInputStream inputFile, DataOutputStream outputFile) {
		this.inputFile = inputFile;
		this.outputFile = outputFile;
	}
	
	byte[] readDataToBytes() throws IOException {
		int len = this.inputFile.available();
		byte[] data = new byte[len];
		int pos = 0;
		//int len = this.inputFile.readInt();
		this.inputFile.readFully(data, pos, len);
		return data;
	}
	
	void compress() throws IOException {
		try {
			System.out.println(this.inputFile.readChar());
		} catch (EOFException e) {
		}
	}
	
	public static void main(String[] args) throws IOException {
		
		DataInputStream inputFile = new DataInputStream(new BufferedInputStream(new FileInputStream("d.txt")));
		DataOutputStream outputFile = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("test.txt")));
		
		Compression c = new Compression(inputFile, outputFile);
		byte[] b = c.readDataToBytes();
		char C = 'c';
		outputFile.write(C);
		System.out.println(C);
		
		c.compress();
		
		/*
		//Output array with bytes at decimal
		System.out.println(Arrays.toString(b));
		
		//Output every byte as hex
		for(byte B : b ) {
			System.out.println(Integer.toHexString(B));
		}*/
		
		
	}
}
