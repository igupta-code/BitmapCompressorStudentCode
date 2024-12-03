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
    public static final int MAX_8BIT = 255;

    /**
     * Reads a sequence of bits from standard input, compresses them,
     * and writes the results to standard output.
     */
    public static void compress() {
        // Assume the first bit is a 0 --- curIsOne = value of the current string of bits
        boolean curIsOne = false;
        boolean isOne = false;
        int runSize = 0;

        while (!BinaryStdIn.isEmpty()) {
            isOne = BinaryStdIn.readBoolean();

            // If next bit is the same as the rest of the sequence, nothing changes(keep adding to runSize)
            if (curIsOne == isOne) {
                // Checks case where run is longer than 8-bit code
                if (runSize == MAX_8BIT) {
                    BinaryStdOut.write(runSize, 8);
                    // Writes the empty 8 bits to switch back to original code
                    runSize = 0;
                    BinaryStdOut.write(runSize, 8);
                }
            }
            else{
                // If the number switches, write out the string length and continue
                BinaryStdOut.write(runSize, 8);

                // Start a new count and switch the current number we're on
                runSize = 0;
                curIsOne = isOne;
            }
            runSize++;
        }
        // Prints last run
        BinaryStdOut.write(runSize, 8);
        BinaryStdOut.close();
    }

    /**
     * Reads a sequence of bits from standard input, decodes it,
     * and writes the results to standard output.
     */
    public static void expand(){
        int length;
        int counter = 0;

        while(!BinaryStdIn.isEmpty()){
            // Reads in 8 bits to find length of run
            length = BinaryStdIn.readInt(8);

            // Repeatedly prints out 0 or 1 length times
            for (int i = 0; i < length; i++) {
                // Counter % 2 switches from 0 to 1
                BinaryStdOut.write(counter%2, 1);
            }
            counter++;
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