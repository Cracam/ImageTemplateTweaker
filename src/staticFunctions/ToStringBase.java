/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package staticFunctions;

/**
 *
 * @author Camille LECOURT
 */
public class ToStringBase {

    /**
     * Converts a 2D integer array to a string representation.
     *
     * @param array The 2D integer array to convert.
     * @return A string representation of the 2D integer array.
     */
    public static String toString(int[][] array) {
        if (array == null) {
            return "null";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < array.length; i++) {
            sb.append("[");
            for (int j = 0; j < array[i].length; j++) {
                sb.append(array[i][j]);
                if (j < array[i].length - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]");
            if (i < array.length - 1) {
                sb.append(", ");
            }
        }

        sb.append("]");
        return sb.toString();
    }

   
}
