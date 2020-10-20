import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class LZ77 {
	private static int BUFFERSIZE = (1 << 12) - 1; //12 bits for looking back
	private static int POINTERSIZE = ((1 << 4) - 1); //4 bits for match size
	private int buffersize;
	private byte[] data;
	
	DataInputStream compressionInput;
	DataOutputStream compressionOutput;
	DataInputStream deCompressionInput;
	DataOutputStream deCompressionOutput;
	
	public LZ77(String path) throws FileNotFoundException {
		this.buffersize = Math.min(100,BUFFERSIZE);
		this.compressionInput = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
		this.compressionOutput = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path+"compressed")));
		this.deCompressionInput = null;
		this.deCompressionOutput = null;
		//this.deCompressionInput = new DataInputStream(new BufferedInputStream(new FileInputStream(path+"compressed")));
		//this.deCompressionOutput = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path+"decompressed")));
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
		if (this.compressionInput == null) throw new NullPointerException("Error with input file");
		if (this.compressionOutput == null) throw new NullPointerException("Error with output file");
		data = new byte[this.compressionInput.available()];
		
		data = this.compressionInput.readAllBytes();
		for (byte b : data ){
			System.out.print(b);
		}
		
		for (int i = 0; i < data.length;){
			Pointer pointer = findPointer(i);
			if (pointer != null){
				this.compressionOutput.write(1);
				this.compressionOutput.write((byte)pointer.getDistance() >> 4);
				this.compressionOutput.write((byte) pointer.getDistance() & 0x0F << 4 | pointer.getLength());
				i = i + pointer.getLength();
			} else {
				this.compressionOutput.write(0);
				this.compressionOutput.write(data[i]);
				i = i+1;
			}
		}
		this.compressionInput.close();
		this.compressionOutput.flush();
		this.compressionOutput.close();
	}
	
	private Pointer findPointer(int currentIndex){
		if (currentIndex == 0)
			return null;
		
		Pointer pointer = new Pointer();
		int stop = currentIndex + POINTERSIZE;
		if (stop > data.length -1) stop = data.length - 1;
		for (int j = stop; j > currentIndex + 3; j--){
			int min = currentIndex - buffersize;
			if (min < 0)
				min = 0;
			byte[] bytes = Arrays.copyOfRange(data, currentIndex, j);
			
			for (int i = min; i < currentIndex; i++){
				int repeat = bytes.length / (currentIndex - i);
				int remaining = bytes.length % (currentIndex - i);
				byte[] tempArray = new byte[(currentIndex - i) * repeat + (i + remaining - i)];
				int m = 0;
				for (; m < repeat; m++) {
					int pos = m * (currentIndex - i);
					System.arraycopy(data, i, tempArray, pos, currentIndex - i);
				}
				int pos = m * (currentIndex - i);
				System.arraycopy(data, i, tempArray, pos, remaining);
				if (Arrays.equals(tempArray, bytes) && bytes.length > pointer.getLength()){
					pointer.setLength(bytes.length);
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
		this.deCompressionInput = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
		//outputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path+"decompress")));
		ByteBuffer byteBuffer = ByteBuffer.allocate(1);
		RandomAccessFile outputFileStream;
		FileChannel outChannel;
		do {
			outputFileStream = new RandomAccessFile(path+"dec", "rw");
			outChannel = outputFileStream.getChannel();
			int condition = this.deCompressionInput.read();
			if (condition == 0) {
				byteBuffer.clear();
				byteBuffer.put(this.deCompressionInput.readByte());
				byteBuffer.flip();
				outChannel.write(byteBuffer, outChannel.size());
				outChannel.position(outChannel.size());
			} else {
				byte b1 = this.deCompressionInput.readByte();
				byte b2 = this.deCompressionInput.readByte();
				int dis = (b1 << 4) | (b2 >> 4);
				int len = (b2 & 0x0f);
				for (int i = 0; i < len; i++) {
					byteBuffer.clear();
					outChannel.read(byteBuffer, outChannel.position() - dis);
					byteBuffer.flip();
					outChannel.write(byteBuffer, outChannel.size());
					outChannel.position(outChannel.size());
				}
			}
			outputFileStream.close();
			outChannel.close();
			this.deCompressionInput.close();
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
