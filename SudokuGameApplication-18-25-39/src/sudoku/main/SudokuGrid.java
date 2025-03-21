package sudoku.main;

import java.util.Random;
class SudokuGrid {
    private int[][] grid;
    //constructor
    public SudokuGrid() {
        grid = new int[9][9];  //initialise sudoku grid
    }
    //method to print the sudoku grid
    public static void printGrid(int[][] grid) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                System.out.print(grid[row][col] + " ");
            }
            System.out.println();
        }
    }
    //method to check if a number placement validity (acc. to sudoku rules)
    private boolean isValidPlacement(int row, int col, int num) {
        //check row
        for (int i = 0; i < 9; i++) {
            if (grid[row][i] == num) {
                return false;
            }
        }

        //check column
        for (int i = 0; i < 9; i++) {
            if (grid[i][col] == num) {
                return false;
            }
        }

        //check the 3x3 subgrid
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (grid[i][j] == num) {
                    return false;
                }
            }
        }

        return true;
    }
    //method to fill the grid (recursive approach)
    private boolean fillGrid() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (grid[row][col] == 0) {
                    Random rand = new Random();
                    int[] nums = rand.ints(1, 10).distinct().limit(9).toArray();

                    for (int num : nums) {
                        if (isValidPlacement(row, col, num)) {
                            grid[row][col] = num;
                            if (fillGrid()) {
                                return true;
                            }
                            grid[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }
    public static int[][] generateSudokuGrid() {
        SudokuGrid sd= new SudokuGrid();
        sd.fillGrid();
        return sd.grid;
    }

}