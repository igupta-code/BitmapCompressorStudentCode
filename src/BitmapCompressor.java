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
    public static void compress(){
        // Assume the first one is false
        boolean isOne = false;
        // runSize = Length
        int runSize = 0;
        // Current string of numbers is 0 or 1
        boolean curIsOne = false;

        while(!BinaryStdIn.isEmpty()){
            // Read this in here instead of 3 places
            isOne = BinaryStdIn.readBoolean();

            // Length must be within what 8-bits can store
            if(runSize < MAX_8BIT){
                // If it's the same number, add to length of run and read next
                if(curIsOne == isOne){
                    runSize++;
                }
                else{
                    // If the number switches, write out the string length and continue
                    BinaryStdOut.write(runSize,8);

                    // Start a new count, switch the current number we're on, read in next number
                    runSize = 1;
                    curIsOne = isOne;
                }
            }
            else{
                // If run too big, split it into two: print out run and then 8 zeros to switch
                BinaryStdOut.write(MAX_8BIT, 8);
                // Represents 0 of the other number to get two consecutive runs of the same #
                BinaryStdOut.write(0, 8);
                // 0 or 1???
                runSize = 1;
            }
        }
        // Might not need this
        BinaryStdOut.write(runSize,8);

        BinaryStdOut.close();
    }

    /**
     * Reads a sequence of bits from standard input, decodes it,
     * and writes the results to standard output.
     */
    public static void expand(){
        int length = BinaryStdIn.readInt(8);
        int counter = 0;

        // Since we are writing and reading in 8 bits at a time there should be no excess
        while(!BinaryStdIn.isEmpty()){
            // Write out the zero or one for how many times the 8bit code indicates
            for (int i = 0; i < length; i++) {
                // Writes it out as a boolean (one bit)
                BinaryStdOut.write(counter%2, 1);
            }
            // Reads in the next 8 bits and increases counter to switch from 0 to 1 or vice versa
            counter++;
            length = BinaryStdIn.readInt(8);
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