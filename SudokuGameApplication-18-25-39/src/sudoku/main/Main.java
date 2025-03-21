package sudoku.main;

import sudoku.login.Login;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

class Grid {
    int grid_size = 9;
    int[][] gridframe;
    // Constructor to initialize gridframe with a random Sudoku grid
    public Grid() {
        SudokuGrid sudokuGrid = new SudokuGrid();
        this.gridframe = sudokuGrid.generateSudokuGrid(); // Generate and assign the grid
    }
}

abstract class GridCopy extends Grid {
    int[][] duplicate_grid = new int[9][9];

    GridCopy() {
        copy();
    }
    void copy() {
        for (int i = 0; i < grid_size; i++) {
            for (int j = 0; j < grid_size; j++) {
                duplicate_grid[i][j] = gridframe[i][j];
            }
        }
    }
    boolean hasUniqueSolution() {
        int[][] boardCopy = Arrays.stream(duplicate_grid).map(int[]::clone).toArray(int[][]::new);
        return SudokuSolver.solveBoard(boardCopy);  // Verify solvability with SudokuSolver
    }
    // Deletes a specified number of elements from each 3x3 subgrid
    void deleteRandomElements(int numElementsToDelete) {
        Random rand = new Random();
        boolean validPuzzle = false;

        while (!validPuzzle) {
            // Reset duplicate_grid to the original gridframe state
            copy();

            // Delete elements as per difficulty
            for (int row = 0; row < 9; row += 3) {
                for (int col = 0; col < 9; col += 3) {
                    int deleted = 0;

                    while (deleted < numElementsToDelete) {
                        int randomRow = row + rand.nextInt(3);
                        int randomCol = col + rand.nextInt(3);

                        if (duplicate_grid[randomRow][randomCol] != 0) {
                            duplicate_grid[randomRow][randomCol] = 0;
                            deleted++;
                        }
                    }
                }
            }
            // Check if the generated puzzle has a unique solution
            validPuzzle = hasUniqueSolution();
        }
    }

    void display() {
        for (int i = 0; i < grid_size; i++) {
            if (i % 3 == 0 && i != 0) {
                System.out.println("---------------------"); // Print a horizontal separator for 3x3 grids
            }
            for (int j = 0; j < grid_size; j++) {
                if (j % 3 == 0 && j != 0) {
                    System.out.print("| "); // Print a vertical separator for 3x3 grids
                }
                System.out.print((duplicate_grid[i][j] == 0 ? "." : duplicate_grid[i][j]) + " "); //For empty cell printing
            }
            System.out.println();
        }
    }

    boolean isSafe(int row, int col, int num) {
        // Check the row
        for (int x = 0; x < grid_size; x++) {
            if (duplicate_grid[row][x] == num) return false;
        }

        // Check the column
        for (int x = 0; x < grid_size; x++) {
            if (duplicate_grid[x][col] == num) return false;
        }

        // Check 3x3 subgrid
        int startRow = row - row % 3, startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (duplicate_grid[i + startRow][j + startCol] == num) return false;
            }
        }

        return true;
    }
    //class to check if the board is fully filled
    boolean isBoardComplete() {
        for (int i = 0; i < grid_size; i++) {
            for (int j = 0; j < grid_size; j++) {
                if (duplicate_grid[i][j] == 0) { // If there's any empty cell
                    return false;
                }
            }
        }
        return true;
    }

    // Abstract method for deleting elements, to be implemented by subclasses
    abstract void deleteElements();

}

class LevelEasy extends GridCopy {
    void deleteElements() {
        deleteRandomElements(2); // Delete 2 elements in each 3x3 subgrid
    }
}

class LevelMedium extends GridCopy {
    void deleteElements() {
        deleteRandomElements(4); // Delete 4 elements in each 3x3 subgrid
    }
}

class LevelHard extends GridCopy {
    void deleteElements() {
        deleteRandomElements(6); // Delete 6 elements in each 3x3 subgrid
    }
}
public class Main {

    public static void main(String[] args) {
        Login login = new Login();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Sudoku Game!");
        System.out.println("1. Login");
        System.out.println("2. Sign Up");

        int loginChoiceAttempts = 0;
        while (true) {
            if (loginChoiceAttempts >= 2) {
                System.out.println("Too many invalid choices. Exiting...");
                return;
            }

            int loginChoice = scanner.nextInt();
            scanner.nextLine();  // Consume newline character

            if (loginChoice == 1) {  // Login process
                int passwordAttempts = 0;
                while (true) {
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();

                    if (login.login(username, password)) {
                        System.out.println("Login successful!");
                        // proceed to SudokuSolver
                        break;
                    } else {
                        passwordAttempts++;
                        System.out.println("Incorrect username or password!");
                        if (passwordAttempts >= 2) {
                            System.out.println("Too many incorrect attempts. Exiting...");
                            return;
                        }
                    }
                }
                break;

            } else if (loginChoice == 2) {  // Sign up process
                int usernameAttempts = 0;
                while (true) {
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();

                    if (login.isUsernameTaken(username)) {
                        usernameAttempts++;
                        System.out.println("Username already taken! Please choose a different username.");
                        if (usernameAttempts >= 2) {
                            System.out.println("Too many attempts. Exiting...");
                            return;
                        }
                    } else {
                        int passwordAttempts = 0;
                        while (true) {
                            System.out.print("Create password (8 characters including numbers): ");
                            String password = scanner.nextLine();

                            if (password.length() >= 8 && password.matches(".*\\d.*")) {
                                login.signup(username, password);
                                System.out.println("Signup successful!");
                                // proceed to SudokuSolver
                                break;
                            } else {
                                passwordAttempts++;
                                System.out.println("Password must be at least 8 characters long and include numbers.");
                                if (passwordAttempts >= 2) {
                                    System.out.println("Too many invalid attempts. Exiting...");
                                    return;
                                }
                            }
                        }
                        break;
                    }
                }
                break;

            } else {
                loginChoiceAttempts++;
                System.out.println("Invalid choice! Please try again.");
            }
        }

        boolean keepPlaying = true;

        while (keepPlaying) {
            int choice = getDifficultyChoice(scanner);

            if (choice == -1) {
                System.out.println("Exiting the game. Thanks for playing!");
                break;
            }

            // Start a new game with the chosen difficulty level
            startGame(choice, scanner);

            // After the game ends, ask if the user wants to restart or exit
            keepPlaying = askToRestart(scanner);
        }

        scanner.close();
    }

    // Method to get the difficulty level from the user
    private static int getDifficultyChoice(Scanner scanner) {
        System.out.println("Please select difficulty level:");
        System.out.println("1 - Easy");
        System.out.println("2 - Medium");
        System.out.println("3 - Hard");
        System.out.println("Enter '-1' to quit.");

        try {
            int choice = scanner.nextInt();
            if (choice >= 1 && choice <= 3) {
                return choice;
            } else if (choice == -1) {
                return -1; // exit code
            } else {
                System.out.println("Invalid choice. Please enter 1, 2, 3, or -1.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a number between 1 and 3, or -1 to quit.");
            scanner.next(); // clear invalid input
        }
        return getDifficultyChoice(scanner);
    }

    // Method to start a new Sudoku game
    private static void startGame(int difficulty, Scanner scanner) {
        // Record start time
        long startTime = System.currentTimeMillis();
        GridCopy gridCopy;

        switch (difficulty) {
            case 1:
                gridCopy = new LevelEasy();
                break;
            case 2:
                gridCopy = new LevelMedium();
                break;
            case 3:
                gridCopy = new LevelHard();
                break;
            default:
                System.out.println("Unexpected difficulty level. Exiting game.");
                return;
        }

        gridCopy.deleteElements();
        System.out.println("Sudoku Puzzle (fill in the blanks):");
        gridCopy.display();

        while (true) {
            System.out.println("\nEnter your move in the format 'row col num' (or enter '-1' to quit this game): ");
            int row = scanner.nextInt();
            if (row == -1) break;

            int col = scanner.nextInt();
            int num = scanner.nextInt();

            if (row < 0 || row >= 9 || col < 0 || col >= 9 || num < 1 || num > 9) {
                System.out.println("Invalid input! Row and column should be between 0-8, and number between 1-9.");
                continue;
            }


            if (gridCopy.duplicate_grid[row][col] != 0) {
                System.out.println("Position already filled. Choose another position.");
                continue;
            }

            if (gridCopy.isSafe(row, col, num)) {
                gridCopy.duplicate_grid[row][col] = num;
                System.out.println("Number placed successfully!");
            } else {
                System.out.println("Invalid move. This number cannot be placed here.");
            }
            gridCopy.display();
        }
        // Record end time
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        // Convert milliseconds to minutes and seconds
        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60;

        if (gridCopy.isBoardComplete() && SudokuSolver.solveBoard(gridCopy.duplicate_grid)) {
            System.out.println("Congratulations! The solution is correct.");
            System.out.println("Time taken: " + minutes + " minutes " + seconds + " seconds.");
        } else if (!gridCopy.isBoardComplete()) {
            System.out.println("The game is incomplete. Exiting the game.");
            System.out.println("Time taken: " + minutes + " minutes " + seconds + " seconds.");
        } else {
            System.out.println("The solution is incorrect or incomplete.");
        }
    }

    // Method to ask the user if they want to play again
    private static boolean askToRestart(Scanner scanner) {
        System.out.println("Would you like to start a new game? (yes or no)");
        String response = scanner.next();
        return response.equalsIgnoreCase("yes");
    }
}
