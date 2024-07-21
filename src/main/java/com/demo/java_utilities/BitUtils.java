package com.demo.java_utilities;

public class BitUtils {

    /**
     * Checks if a number is a power of two.
     *
     * @param x the number to check
     * @return true if x is a power of two, false otherwise
     */
    public static boolean isPowerOfTwo(int x) {
        return x > 0 && (x & (x - 1)) == 0;
    }

    /**
     * Counts the number of set bits (1s) in an integer.
     *
     * @param x the integer to count the bits for
     * @return the number of set bits in x
     */
    public static int countSetBits(int x) {
        int count = 0;
        while (x != 0) {
            x &= (x - 1);
            count++;
        }
        return count;
    }

    /**
     * Checks if the ith bit of a number is set (1).
     *
     * @param x the number to check
     * @param i the index of the bit to check (0-based)
     * @return true if the ith bit is set, false otherwise
     */
    public static boolean isBitSet(int x, int i) {
        return (x & (1 << i)) != 0;
    }

    /**
     * Sets the ith bit of a number to 1.
     *
     * @param x the number to modify
     * @param i the index of the bit to set (0-based)
     * @return the modified number with the ith bit set
     */
    public static int setBit(int x, int i) {
        return x | (1 << i);
    }

    /**
     * Clears the ith bit of a number (sets it to 0).
     *
     * @param x the number to modify
     * @param i the index of the bit to clear (0-based)
     * @return the modified number with the ith bit cleared
     */
    public static int clearBit(int x, int i) {
        return x & ~(1 << i);
    }

    /**
     * Toggles the ith bit of a number (0 to 1 or 1 to 0).
     *
     * @param x the number to modify
     * @param i the index of the bit to toggle (0-based)
     * @return the modified number with the ith bit toggled
     */
    public static int toggleBit(int x, int i) {
        return x ^ (1 << i);
    }

    /**
     * Returns the number with the lowest set bit (rightmost 1 bit) erased.
     *
     * @param x the number to modify
     * @return the number with the lowest set bit erased
     */
    public static int eraseLowestSetBit(int x) {
        return x & (x - 1);
    }

    /**
     * Gets the value of the lowest set bit (rightmost 1 bit).
     *
     * @param x the number to check
     * @return the value of the lowest set bit, or 0 if x is 0
     */
    public static int getLowestSetBit(int x) {
        return x & -x;
    }

    /**
     * Checks if a number has exactly one bit set (is a power of two).
     *
     * @param x the number to check
     * @return true if x is a power of two, false otherwise
     */
    public static boolean hasExactlyOneBitSet(int x) {
        return x > 0 && (x & (x - 1)) == 0;
    }

    /**
     * Checks if a number has an odd number of set bits.
     *
     * @param x the number to check
     * @return true if x has an odd number of set bits, false otherwise
     */
    public static boolean hasOddNumberOfSetBits(int x) {
        return (countSetBits(x) % 2) != 0;
    }

    /**
     * Computes the parity of a number (1 if number of set bits is odd, 0 otherwise).
     *
     * @param x the number to compute parity for
     * @return the parity of x (1 or 0)
     */
    public static int parity(int x) {
        x ^= x >> 16;
        x ^= x >> 8;
        x ^= x >> 4;
        x ^= x >> 2;
        x ^= x >> 1;
        return x & 1;
    }

    /**
     * Swaps two integers using XOR without using additional space.
     *
     * @param a the first integer
     * @param b the second integer
     */
    public static void swapWithoutTemp(int a, int b) {
        a ^= b;
        b ^= a;
        a ^= b;
    }

    /**
     * Reverses the bits of an integer.
     *
     * @param x the integer to reverse bits for
     * @return the integer with reversed bits
     */
    public static int reverseBits(int x) {
        int result = 0;
        int bitLength = 32; // Assuming integer is 32-bit
        for (int i = 0; i < bitLength; i++) {
            result <<= 1;
            result |= (x & 1);
            x >>>= 1;
        }
        return result;
    }

    /**
     * Finds the maximum integer value that can be formed by setting exactly n bits.
     *
     * @param n the number of bits to set
     * @return the maximum integer value with exactly n bits set
     */
    public static int maximumValueWithNBitsSet(int n) {
        return (1 << n) - 1;
    }

    /**
     * Finds the minimum integer value that can be formed by setting exactly n bits.
     *
     * @param n the number of bits to set
     * @return the minimum integer value with exactly n bits set
     */
    public static int minimumValueWithNBitsSet(int n) {
        return (1 << (n - 1));
    }

    /**
     * Computes the integer logarithm base 2 (floor) of a positive integer.
     *
     * @param x the integer to compute the logarithm for
     * @return the floor of the logarithm base 2 of x
     */
    public static int logBase2(int x) {
        return 31 - Integer.numberOfLeadingZeros(x);
    }

    /**
     * Generates all subsets of a set represented by a bitmask.
     *
     * @param bitmask the bitmask representing the set
     * @return a list of all subsets as bitmasks
     */
    public static List<Integer> generateSubsets(int bitmask) {
        List<Integer> subsets = new ArrayList<>();
        for (
            int subset = bitmask;
            subset != 0;
            subset = (subset - 1) & bitmask
        ) {
            subsets.add(subset);
        }
        subsets.add(0); // Include the empty subset
        return subsets;
    }

    /**
     * Example usage of the BitUtils class.
     */
    public static void main(String[] args) {
        int x = 18; // Example number for testing
        System.out.println(
            "Binary representation of " + x + ": " + Integer.toBinaryString(x)
        );

        // Example usage of methods
        System.out.println("Is " + x + " a power of two? " + isPowerOfTwo(x));
        System.out.println(
            "Number of set bits in " + x + ": " + countSetBits(x)
        );
        System.out.println(
            "Is the 2nd bit of " + x + " set? " + isBitSet(x, 2)
        );
        System.out.println("Setting the 3rd bit of " + x + ": " + setBit(x, 3));
        System.out.println(
            "Clearing the 4th bit of " + x + ": " + clearBit(x, 4)
        );
        System.out.println(
            "Toggling the 5th bit of " + x + ": " + toggleBit(x, 5)
        );
        System.out.println(
            "Erasing lowest set bit of " + x + ": " + eraseLowestSetBit(x)
        );
        System.out.println(
            "Lowest set bit of " + x + ": " + getLowestSetBit(x)
        );
        System.out.println(
            "Is " +
            x +
            " a power of two (alternate method)? " +
            hasExactlyOneBitSet(x)
        );
        System.out.println(
            "Does " +
            x +
            " have an odd number of set bits? " +
            hasOddNumberOfSetBits(x)
        );
        System.out.println("Parity of " + x + ": " + parity(x));
        System.out.println("Reverse bits of " + x + ": " + reverseBits(x));
        System.out.println(
            "Maximum value with 4 bits set: " + maximumValueWithNBitsSet(4)
        );
        System.out.println(
            "Minimum value with 4 bits set: " + minimumValueWithNBitsSet(4)
        );
        System.out.println("Log base 2 of " + x + ": " + logBase2(x));
        System.out.println(
            "Subsets of bitmask " + x + ": " + generateSubsets(x)
        );
    }
}
