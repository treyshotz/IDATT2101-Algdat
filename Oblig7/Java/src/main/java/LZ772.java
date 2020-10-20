import java.io.*;
import java.util.Arrays;

public class LZ772 {
    private static int BUFFERSIZE = (1 << 12) - 1; //12 bits for looking back
    private static int POINTERSIZE = ((1 << 4) - 1); //4 bits for match size
    private int buffersize;
    private char[] data;

    DataInputStream inputStream;
    DataOutputStream outputStream;

    public LZ772() {
        this.buffersize = Math.min(100,BUFFERSIZE);
        //buffersize = BUFFERSIZE;
    }

    public void compress(String path) throws IOException {
        inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
        outputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path+"compress")));
        if (inputStream == null) System.out.println("fack input");
        if (outputStream == null) System.out.println("fack output");
        data = new char[inputStream.available()];
        String text = new String(inputStream.readAllBytes(), "UTF-8");
        data = text.toCharArray();
        for (char c:data ){
            System.out.print(c);
        }
        for (int i = 0; i < data.length;){
            Pointer pointer = findPointer(i);
            if (pointer != null){
                outputStream.write(1);
                System.out.println("FUNK A");
                outputStream.write(pointer.getDistance());
                outputStream.write(pointer.getLength());
                //System.out.println(1 + "" + pointer.getDistance() +"" +pointer.getLength());
                i = i + pointer.getLength();
            } else {
                outputStream.write(0);
                System.out.println("FUNK B");
                outputStream.write(data[i]);
                i = i+1;
            }
        }
        inputStream.close();
        outputStream.flush();
        outputStream.close();
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
            int flag = inputStream.read();
        } while (true);
    }

    public static void main(String[] args) throws IOException {
        String inpath = "/Users/madslun/Documents/Programmering/AlgDat/Oblig7/Java/d.txt";
        String outpath = "src\\testin.compress";
        LZ772 lz772 = new LZ772();
        lz772.compress(inpath);
        //lz77.decompress(outpath, restored);
    }
}

class Pointer2 {
    private int length;
    private int distance;


    public Pointer2() {
        this(-1,-1);
    }

    public Pointer2(int matchLength, int matDistance) {
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
