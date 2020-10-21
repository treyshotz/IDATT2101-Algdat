import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class for compression with Lempel Ziv method
 */

public class LZ772 {
    /**
     * final values for buffer looking back through array, and for use by pointer class
     */
    private static final int BUFFERSIZE = (1 << 7) - 1; //7 bits for looking back
    private static final int POINTERSIZE = (1 << 4) - 1; //4 bits for match size
    private static final int MIN_SIZE_POINTER = 3; //compressed text may only be larger than 3 characters
    private int buffersize;
    private char[] data;
    
    DataInputStream inputStream;
    DataOutputStream outputStream;
    
    /**
     * LZ77 constructor sets our non final buffersize equal to BUFFERSIZE
     */
    public LZ772() {
        this.buffersize = BUFFERSIZE;
    }
    
    /**
     * Compression method taking in the path to the file we want to compress
     * @param path
     * @throws IOException
     */
    public void compress(String path) throws IOException {
        //DataStreams for reading and writing bytes
        inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
        outputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path+"compress")));
        data = new char[inputStream.available()]; //sets the length of character array to the size of the input stream
        
        String text = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8); //we read all the bytes from the input stream into a string
        data = text.toCharArray(); //we convert it to a character array
        
        StringBuilder incompressible = new StringBuilder(); //creating a string builder as to more easily handle all characters
        for (int i = 0; i < data.length;){ //for the entire length of all characters
            Pointer pointer = findPointer(i); //we find the given pointer (if any) to the current index
            if (pointer != null){ //if a pointer is returned
                if (incompressible.length() != 0) { //if the stringbuilder is not empyt
                    outputStream.writeByte((byte) incompressible.length()); //we write the length of the stringbuilder as a byte to our output stream
                    for(int c = 0; c < incompressible.length(); c++) //we iterate through the stringbuilder
                        outputStream.writeByte((byte)incompressible.charAt(c)); //and write each character as a byte to out outputstream
                    incompressible = new StringBuilder(); //we then reset the stringbuilder
                }
                outputStream.writeByte((byte)(-pointer.getDistance())); //we then write the negative distance as byte (to help the decompression know when to look backwards)
                outputStream.writeByte((byte)pointer.getLength());
                i += pointer.getLength(); //increment the index by the length of pointer
            } else {
                incompressible.append(data[i]); //if we have an empty pointer, we append the bytes at index i
                i += 1;
            }
        }
        //if our string builder is not empty
        if (incompressible.length() != 0) {
            outputStream.writeByte((byte)incompressible.length()); //we write the length of the string builder
            for(int c = 0; c < incompressible.length(); c++)
                outputStream.writeByte((byte) incompressible.charAt(c)); //we write each character as a byte in the outputstream
        }
        //close and flush streams
        inputStream.close();
        outputStream.flush();
        outputStream.close();
        //printAfter(path);
        deCompress(path); //then decompress
    }
    
    public void printAfter(String path) throws IOException {
        //create a new input stream from the compressed path
        DataInputStream compressed = new DataInputStream(new BufferedInputStream(new FileInputStream(path+"compress")));
        byte[] bytes = new byte[compressed.available()];
        compressed.readFully(bytes);
        System.out.println("\n\n--< After compression >--");
        for (byte b:bytes) {
            System.out.print(b + " ");
        }
    }
    
    private Pointer findPointer(int currentIndex){
        Pointer pointer = new Pointer();
        
        int max = currentIndex + POINTERSIZE; //Maximum index in the search word
        if (max > data.length - 1)
            max = data.length - 1;
        
        int min = currentIndex - buffersize; //Minimum index of the sliding window
        if (min < 0)
            min = 0;
        
        char[] buffer = Arrays.copyOfRange(data, min, currentIndex); //Seach buffer
        
        int i = currentIndex + MIN_SIZE_POINTER  - 1; //The match must be at least from currentIndex to i (both excluded)
        
        outer:
        while(i <= max){
            char[] searchWord = Arrays.copyOfRange(data, currentIndex, i + 1); //The word we are searching for. Starting at length i - currentIndex
            int j = 0;
            while(searchWord.length + j <= buffer.length){ //Never compare variables outside the search buffer array
                int k = searchWord.length - 1; //Find the index (if any) where letters dont match
                while (k >= 0 && searchWord[k] == buffer[j+k]) {
                    k--;
                }
                if(k < 0){ //All characters in the search word matched the search buffer
                    pointer.setDistance(buffer.length - j);
                    pointer.setLength(searchWord.length);
                    i++;
                    //System.out.println(searchWord);
                    continue outer; //Continues loop with an additional character in search word until it fails
                }
                else {
                    int l = k-1; //Find last index of failed character from buffer in the search word if any
                    while(l >= 0 && searchWord[l] != buffer[j+k]) {
                        l--;
                    }
                    j += k - l; //Slide scope according to Boyer Moore
                }
            }
            break; //If it comes to this there was no match for the last search word
        }
        if (pointer.getLength() > 0 && pointer.getLength() > 0) //If it was a match
            return pointer;
        return null; //If it was not a match
    }
    
    /**
     * Decompresses the file based on our compressing algorithm
     *
     * The start of every file is started by having a pointer which controlles how many bytes is supposed to be uncompressed
     * After this we are redirected to a new pointer. This pointer is a negative, indicating how many steps we should jump back and what length we are using
     * This keeps on going with pointers indicating either compressed or uncrompressed bytes until the file is run through
     *
     * @param path to files
     * @throws IOException
     */
    public void deCompress(String path) throws IOException {
        inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(path+"compress")));
        outputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path+"decompress")));
        int nrOfBytes = inputStream.available();
        byte[] bytes = new byte[nrOfBytes]; //Array of bytes from the compressed file
        inputStream.readFully(bytes);
        int last = 0;
        List<Byte> bytesArr = new ArrayList<>(); //Arraylist containing the bytes that will be the result of decompression
        
        int index = 0; //Index to keep track of where in the compressed file we are
        while (index < bytes.length){
            int jump = bytes[index]; //Indicates how long the sequence will be
            if (jump >= 0){ //Checks if is uncompressed bytes
                for (int unCompPos = 0; unCompPos < jump; unCompPos++) {
                    bytesArr.add(last + unCompPos, bytes[index + unCompPos + 1]); //Adds all the uncompressed bytes to the array
                }
                index += jump + 1; //Updates the index to the next pointer
                last += jump; //Updates where the previous position was
            }
            else { //Handles compressed data
                int backJump = bytes[index+1]; //Indicates how long backwards the jump will be
                for (int compPos = 0; compPos < backJump; compPos++){
                    bytesArr.add(last + compPos, bytesArr.get(last + jump + compPos)); //Adds the compressed data based on the previously uncompressed data
                }
                index += 2; //Updates index to the next pointer
                last += backJump; //Updates where the previous position was
            }
        }
        for(int i = 0; i < bytesArr.size()-1; i++) {
            outputStream.write(bytesArr.get(i)); //Writes all the bytes to the decompressed file
        }
        outputStream.write('\n');
        outputStream.flush();
        outputStream.close();
    }
    
    /**
     * Pointer Class to point to a position in Array
     * Defining where and how much to compress
     */
    
    class Pointer {
        
        /**
         * length: the length of the text to compress
         * distance: how far back from current position
         */
        private int length;
        private int distance;
        
        /**
         * Constructor defining initial length and distance values as -1
         */
        public Pointer() {
            this(-1,-1);
        }
        
        /**
         * Constructor defining lenght and distance
         * @param matchLength
         * @param matDistance
         */
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
    
    public static void main(String[] args) throws IOException {
        String inpath = "/Users/madslun/Documents/Programmering/AlgDat/Oblig7/Java/d.txt";
        LZ772 lz772 = new LZ772();
        lz772.compress(inpath);
        System.out.println("Compression and Decompression finished successfully!");
    }
}
