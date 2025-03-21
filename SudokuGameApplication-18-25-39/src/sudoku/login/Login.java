package sudoku.login;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Login {

    private static final String FILE_PATH = "C:\\Users\\shrik\\IdeaProjects\\SudokuGameApplication-18-25-39\\src\\sudoku\\login\\logindetails";
    private Map<String, String> userDatabase = new HashMap<>();

    public Login() {
        loadUsers();
    }

    public void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    userDatabase.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(username + "," + password);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean login(String username, String password) {
        return password.equals(userDatabase.get(username));
    }

    public void signup(String username, String password) {
        userDatabase.put(username, password);
        saveUser(username, password);
    }

    public boolean isUsernameTaken(String username) {
        return userDatabase.containsKey(username);
    }
}
