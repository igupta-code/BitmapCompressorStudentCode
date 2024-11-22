/******************************************************************************
 *  Compilation:  javac BitmapCompressor.java
 *  Execution:    java BitmapCompressor - < input.bin   (compress)
 *  Execution:    java BitmapCompressor + < input.bin   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   q32x48.bin
 *                q64x96.bin
 *                mystery.bin
 *
 *  Compress or expand binary input from standard input.
 *
 *  % java DumpBinary 0 < mystery.bin
 *  8000 bits
 *
 *  % java BitmapCompressor - < mystery.bin | java DumpBinary 0
 *  1240 bits
 ******************************************************************************/

/**
 *  The {@code BitmapCompressor} class provides static methods for compressing
 *  and expanding a binary bitmap input.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  @author Zach Blick
 *  @author Isha Gupta
 */
public class BitmapCompressor {

    /**
     * Reads a sequence of bits from standard input, compresses them,
     * and writes the results to standard output.
     */
    public static void compress() {
        boolean isOne = BinaryStdIn.readBoolean();

        boolean curIsOne;
        int length = 0;
        while(!BinaryStdIn.isEmpty()){
            curIsOne = BinaryStdIn.readBoolean();
            // If the current is the same as rest of the string add to string length
            if(curIsOne == isOne){
                length++;
            }
            else{
                // If it's different, print out the string
                if(isOne) {
                    // First number represents if it's one or zero, the other 7 represent how many
                    BinaryStdOut.write(1,1);
                    BinaryStdOut.write(length, 7);
                }
                else{
                    BinaryStdOut.write(0,1);
                    BinaryStdOut.write(length, 7);
                }
                // And start a new count w/opposite value of isOne
                isOne = !isOne;
                length = 1;
            }
        }
        BinaryStdOut.close();
    }

    /**
     * Reads a sequence of bits from standard input, decodes it,
     * and writes the results to standard output.
     */
    public static void expand() {
        boolean isOne = BinaryStdIn.readBoolean();
        int length = BinaryStdIn.readInt(7);

        while(!BinaryStdIn.isEmpty()) {
            for (int i = 0; i < length; i++) {
                // Writes it out as a boolean (one bit)
                BinaryStdOut.write(isOne);
            }
            // Reads in the next 8 bits
            isOne = BinaryStdIn.readBoolean();
            length = BinaryStdIn.readInt(7);
        }
        BinaryStdOut.close();
    }

    /**
     * When executed at the command-line, run {@code compress()} if the command-line
     * argument is "-" and {@code expand()} if it is "+".
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}