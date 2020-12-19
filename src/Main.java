import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/* the application is build with the assumption that it would be a different number for each brick.
As well for some of the given examples they could be few correct outputs.
 */

public class Main {


    // a number that we use to mark the bricks with
    private static int counter = 1;

    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // reading the size of the matrix
        String[] dimensions = reader.readLine().split("\\s+");
        int rows = Integer.parseInt(dimensions[0]);
        int cols = Integer.parseInt(dimensions[1]);

        // checking matrix dimensions
        try {
            validateInput(rows, cols);

        }
        catch (IllegalArgumentException ex){
            System.out.println("-1 " + ex.getMessage());
            return;
        }

        /*
         creating a hashmap to validate if the bricks are
         correct(each brick is marked with 2 identical numbers - no more, no less);
         */
        HashMap<String, Integer> bricks = new HashMap<>();
        // filling up the matrix
        String[][] matrix = readMatrix(rows, cols, reader, bricks);


        // checking if the bricks are not spanning 3 rows or columns;
       if(!validateBricks(bricks)){
           System.out.println("-1 Invalid input. No solution exist.");
           return;
       }

        //creating an output matrix
        String[][] matrix2 = new String[rows][cols];

        for (int r = 0; r < matrix.length - 1; r +=2) {
            // Pattern A is the one when both ends start with a horizontal or a vertical brick;
            String patternA = checkifPatternA(matrix, r);
            if (!patternA.equals("else")) {
                // putting the end bricks
                fillUpMatrix2endPoints(matrix, matrix2, patternA, r);
                if (patternA.equals("horizontal")) {
                    try {
                        fillUpMatrixHorizontal(matrix, matrix2, r);
                    } catch (IllegalArgumentException ex) {
                        System.out.println(ex.getMessage());
                    }
                } else {
                    fillUpMatrixVertical(matrix, matrix2, r);

                }
            } else {
                //checking if it starts with a horizontal brick
                if (matrix[r][0].equals(matrix[r][1])) {
                    try {
                        fillUpMatrixPatternB(matrix, matrix2, r);
                    } catch (IllegalArgumentException ex) {
                        System.out.println(ex.getMessage());
                        return;
                    }
                }
                else {
                    String[][] reversedMatrix = new String[rows][cols];
                    try {
                        fillUpMatrixPatternB(matrix, reversedMatrix, r);
                    } catch (IllegalArgumentException ex) {
                        System.out.println(ex.getMessage());
                        return;
                    }
                    /* it`s the same pattern - (one vertical, one horizontal,
                     but comes in the opposite direction -
                    starting with vertical and horizontal at the end. This is why we are using a reversed order. */

                    List<String> reversedRowFirst = Arrays.asList(reversedMatrix[r].clone());
                    List<String> reversedRowSecond = Arrays.asList(reversedMatrix[r + 1].clone());


                    Collections.reverse(reversedRowFirst);
                    Collections.reverse(reversedRowSecond);

                    for (int i = 0; i < cols; i++) {
                        matrix2[r][i] = reversedRowFirst.get(i);
                    }
                    for (int i = 0; i < cols; i++) {
                        matrix2[r + 1][i] = reversedRowSecond.get(i);
                    }

                }
            }
        }



        printMatrix2(matrix2);


    }

    private static void fillUpMatrixPatternB(String[][] matrix, String[][] matrix2, int r) {
        matrix2[r][0] = String.valueOf(counter);
        matrix2[r + 1][0] = String.valueOf(counter);
        counter++;
        matrix2[r][matrix2[r].length - 1] = String.valueOf(counter);
        matrix2[r][matrix2[r].length - 2] = String.valueOf(counter);
        counter++;
        matrix2[r + 1][matrix2[r].length - 1] = String.valueOf(counter);
        matrix2[r + 1][matrix2[r].length - 2] = String.valueOf(counter);
        counter++;
        matrix2[r][1] = String.valueOf(counter);
        matrix2[r][2] = String.valueOf(counter);
        counter++;
        matrix2[r + 1][1] = String.valueOf(counter);
        matrix2[r + 1][2] = String.valueOf(counter);
        counter++;

        for (int i = r; i < r + 1; i++) {
            for (int col = 3; col < matrix[i].length - 2; col += 2) {
                //checking if the brick will overlap and if the next space
                // is available as the end of the matrix has been already filled up
                if (!matrix[i][col].equals(matrix[i][col + 1]) && matrix2[i][col + 1] == null) {
                    matrix2[i][col] = String.valueOf(counter);
                    matrix2[i][col + 1] = String.valueOf(counter);
                    counter++;
                    matrix2[i + 1][col] = String.valueOf(counter);
                    matrix2[i + 1][col + 1] = String.valueOf(counter);
                    counter++;
                } else {
                    matrix2[i][col] = String.valueOf(counter);
                    matrix2[i + 1][col] = String.valueOf(counter);
                    if (matrix[i][col].equals(matrix[i + 1][col])) {
                        throw new IllegalArgumentException("-1. There is no possible solution.");
                    }
                    col--;
                    counter++;
                }
            }

        }

    }

    private static void fillUpMatrixVertical(String[][] matrix, String[][] matrix2, int r) {
        for (int i = r; i < r + 2; i++) {
            for (int j = 2; j < matrix[i].length - 2; j += 2) {
                if (!checkForBrick(matrix, i, j)) {
                    matrix2[i][j] = String.valueOf(counter);
                    matrix2[i][j + 1] = String.valueOf(counter);
                    counter++;
                } else throw new IllegalArgumentException("-1. No solution exist with the given data");
            }

        }
    }

    private static void fillUpMatrixHorizontal(String[][] matrix, String[][] matrix2, int r) {
        for (int i = r; i < r + 2; i++) {
            for (int j = 1; j < matrix[i].length - 2; j+=2) {
                if(!checkForBrick(matrix,i,j)){
                matrix2[i][j] = String.valueOf(counter);
                matrix2[i][j+1] = String.valueOf(counter);
                counter++;
                }
                else throw new IllegalArgumentException("-1. No solution exist with the given data");
            }

        }
    }

    private static boolean checkForBrick(String[][] matrix, int r, int j) {
       return matrix[r][j].equals(matrix[r][j + 1]);
    }

    private static void fillUpMatrix2endPoints(String[][] matrix, String[][] matrix2, String patternA, int r) {
        if(patternA.equals("horizontal")){
            matrix2[r][0] = String.valueOf(counter);
            matrix2[r+1][0] = String.valueOf(counter);
            counter++;
            matrix2[r][matrix[r].length - 1] = String.valueOf(counter);
            matrix2[r+1][matrix[r].length - 1] = String.valueOf(counter);
            counter++;
        }
        else{
            matrix2[r][0] = String.valueOf(counter);
            matrix2[r][1] = String.valueOf(counter);
            counter++;
            matrix2[r][matrix[r].length - 1] = String.valueOf(counter);
            matrix2[r][matrix[r].length - 2 ] = String.valueOf(counter);
            counter++;
            matrix2[r+1][0] = String.valueOf(counter);
            matrix2[r+1][1] = String.valueOf(counter);
            counter++;
            matrix2[r+1][matrix[r].length - 1] = String.valueOf(counter);
            matrix2[r+1][matrix[r].length - 2 ] = String.valueOf(counter);
            counter++;
        }

    }

    //checking if both ends start with the same pattern - vertical or horizontal
    private static String checkifPatternA(String[][] matrix, int r) {

        if(matrix[r][0].equals(matrix[r + 1][0]) && matrix[r][matrix[r].length - 1].equals(matrix[r + 1][matrix[r].length -1])){
            return "vertical";
        }
        if(matrix[r][0].equals(matrix[r][1]) && matrix[r][matrix[r].length -1].equals(matrix[r][matrix[r].length -2])){
            return "horizontal";
        }
        return "else";
    }

    private static boolean validateBricks(HashMap<String, Integer> bricks) {
        long number = bricks.entrySet().stream().filter(entry -> entry.getValue() != 2).count();
        if (number > 0) {
            return false;
        }
        return true;
    }

    private static void printMatrix2(String[][] matrix2) {

        for (int i = 0; i < matrix2.length; i++) {
            for (int j = 0; j < matrix2[i].length; j++) {
                System.out.print(matrix2[i][j] + " ");
                if (j < matrix2[i].length - 1) {
                    if (!matrix2[i][j + 1].equals(matrix2[i][j])) {
                        System.out.print("* ");
                    }
                }
            }
            if (i < matrix2.length - 1) {
                System.out.println();
                System.out.println(printStars(i, matrix2));
            }
        }
    }

    private static String printStars(int i, String[][] matrix2) {
        StringBuilder sb = new StringBuilder();
        for (int j = i; j < i+1 ; j++) {
            int number = 0;
            boolean isLastColumn = false;
            for (int k = 0; k < matrix2[j].length - 1; k += 2) {
                if (matrix2[j][k].equals(matrix2[j][k + 1])) {
                    if (k == matrix2[j].length - 2) {
                        isLastColumn = true;
                    }
                    // checking if the number is form from one or two symbols
                    number = Integer.parseInt(matrix2[j][k]);
                    // adding symbols for both numbers and the spaces between them
                    int symbols;
                    if (number > 9) {
                        symbols = 5;
                    } else {
                        symbols = 3;
                    }
                    for (int l = 0; l < symbols; l++) {
                        sb.append("*");
                    }
                    if(!isLastColumn) {
                        sb.append(" * ");
                    }
                }
                /*if the brick is vertical we don`t append a "*", however we need to change the pattern from skipping
                a brick to checking the next one*/
                else {
                        k--;
                        if (number > 9) {
                            sb.append("   * ");
                        } else {
                            sb.append("  * ");
                        }
                    }
                }
        }
            return sb.toString();

    }


    //
    private static String[][] readMatrix(int rows, int cols, BufferedReader reader, HashMap<String, Integer> bricks) throws IOException {
        String[][] matrix = new String[rows][cols];

            for (int i = 0; i < rows; i++) {
                String[] elements = reader.readLine().split("\\s+");
                for (int j = 0; j < cols; j++) {
                    matrix[i][j] = elements[j];
                    bricks.putIfAbsent(elements[j], 0);
                    bricks.put(elements[j], bricks.get(elements[j]) + 1);
                }
            }
            return matrix;
    }

        // validating input
        private static void validateInput (int n, int m){
            if (n <= 0 || m <= 0 || n > 100 || m > 100 || n % 2 != 0 || m % 2 != 0) {
                throw new IllegalArgumentException("Invalid input. No solution exist.");
            }
        }

    }
