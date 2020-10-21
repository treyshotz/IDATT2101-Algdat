import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
		this.buffersize = Math.min(100, BUFFERSIZE);
		this.compressionInput = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
		this.compressionOutput = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path + "compressed")));
		this.deCompressionInput = new DataInputStream(new BufferedInputStream(new FileInputStream(path + "compressed")));
		this.deCompressionOutput = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path + "decompressed")));
		//this.deCompressionInput = new DataInputStream(new BufferedInputStream(new FileInputStream(path+"compressed")));
		//this.deCompressionOutput = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path+"decompressed")));
	}
	
	/**
	 * Compression algorithm
	 * <p>
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
		System.out.print("Before compression: ");
		for (byte b : data) {
			//System.out.print(b + " ");
		}
		
		for (int i = 0; i < data.length; ) {
			Pointer pointer = findPointer(i);
			byte b = (byte) data[i];
			//System.out.println("\nChar: " + b);
			if (pointer != null) {
				this.compressionOutput.write(1);
				//this.compressionOutput.write((byte)pointer.getDistance() >> 4);
				this.compressionOutput.write(pointer.getDistance());
				byte a = (byte) (pointer.getDistance() >>> 4);
				int u = 4;
				byte k = (byte) u;
				//System.out.println("\nPlassering: " + i);
				//System.out.println("4 som hex " + Integer.toHexString(k));
				//System.out.println("Distanse: " + pointer.getDistance());
				//System.out.println("Bitshifter med 4: " + a);
				//this.compressionOutput.write((byte) pointer.getDistance() & 0x0F << 4 | pointer.getLength());
				this.compressionOutput.write(pointer.getLength());
				//System.out.println("Lengden: " + pointer.getLength());
				//System.out.print("Bitshifter og masse rart: ");
				//System.out.println(pointer.getDistance() & 0x0F << 4 | pointer.getLength());
				i = i + pointer.getLength();
			} else {
				//this.compressionOutput.write(0);
				this.compressionOutput.write(data[i]);
				i = i + 1;
			}
		}
		this.compressionInput.close();
		this.compressionOutput.flush();
		this.compressionOutput.close();
	}
	
	
	public void depressing() throws IOException {
		data = this.deCompressionInput.readAllBytes();
		ArrayList<Byte> output = new ArrayList<>();
		int backwardsJump;
		int length;
		int prev = 0;
		int count = 0;
		System.out.print("\nDecompressing:      ");
		for (int i = 0; i < data.length; i++) {
			if (data[i] == 1) {
				count ++;
				if (prev == 0) {
					backwardsJump = data[i + 1];
				}
				else backwardsJump = data[i+1] - prev + (count*2)-1;
				length = data[i + 2];
				int pos = i-backwardsJump;
				for (int j = 0; j < length; j++) {
					//System.out.print(output.get(pos) +" ");
					//this.deCompressionOutput.write(data[pos]);
					this.deCompressionOutput.write(output.get(pos));
					output.add(output.get(pos));
					pos++;
				}
				prev = length;
				i += 2;
			}
			else {
				//this.deCompressionOutput.write(data[i]);
				output.add(data[i]);
				//System.out.print(data[i] + " ");
				this.deCompressionOutput.write(data[i]);
			}
		}
		this.deCompressionOutput.flush();
		this.deCompressionOutput.close();
		//System.out.println("\n"+ output.toString());
	}
	
	
	private Pointer findPointer(int currentIndex) {
		if (currentIndex == 0)
			return null;
		
		Pointer pointer = new Pointer();
		int stop = currentIndex + POINTERSIZE;
		if (stop > data.length - 1) stop = data.length - 1;
		for (int j = stop; j > currentIndex + 5; j--) {
			int min = currentIndex - buffersize;
			if (min < 0)
				min = 0;
			byte[] bytes = Arrays.copyOfRange(data, currentIndex, j);
			
			for (int i = min; i < currentIndex; i++) {
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
				if (Arrays.equals(tempArray, bytes) && bytes.length > pointer.getLength()) {
					pointer.setLength(bytes.length);
					pointer.setDistance(currentIndex - i);
				}
			}
		}
		if (pointer.getLength() > 0 && pointer.getDistance() > 0) {
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
			outputFileStream = new RandomAccessFile(path + "dec", "rw");
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
		String inpath = "/Users/madslun/Documents/Programmering/AlgDat/Oblig7/Java/diverse.txt";
		LZ77 lz77 = new LZ77(inpath);
		
		lz77.compress();
		lz77.depressing();
		//lz77.decompress(outpath, restored);
	}
}


class Pointer {
	private int length;
	private int distance;
	
	public Pointer() {
		this(-1, -1);
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

