package seedu.storage;

import seedu.classes.Ui;
import seedu.classes.WiagiLogger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;

/**
 * Handles the retrieval and storage of user password data
 */
public class LoginStorage {
    private static final String PASSWORD_FILE_PATH = "./password.txt";

    /**
     * Retrieves the data of the user password from its data file into the program
     */
    static void load() {
        WiagiLogger.logger.log(Level.INFO, "Starting to load password file...");
        try {
            File file = new File(PASSWORD_FILE_PATH);
            boolean isFileCreated = file.exists();
            if (isFileCreated) {
                Scanner scanner = new Scanner(file);
                String passwordHash = scanner.next();
                Storage.password = Integer.parseInt(passwordHash);
            } else {
                createNewUser();
            }
        } catch (IOException e) {
            WiagiLogger.logger.log(Level.WARNING, "Unable to open password file", e);
            Ui.printWithTab(e.getMessage());
        } catch (NoSuchElementException e) {
            WiagiLogger.logger.log(Level.WARNING, "Password file was empty", e);
            File passwordFile = new File(PASSWORD_FILE_PATH);
            passwordFile.delete();
            createNewUser();
        }
        WiagiLogger.logger.log(Level.INFO, "Finish loading password file.");
    }

    private static void createNewUser() {
        WiagiLogger.logger.log(Level.INFO, "Creating new user...");
        try {
            FileWriter fw = new FileWriter(PASSWORD_FILE_PATH);
            int passwordHash = getNewUserPassword();
            fw.write(Integer.toString(passwordHash));
            fw.close();
            Storage.password = passwordHash;
        } catch (IOException e) {
            WiagiLogger.logger.log(Level.WARNING, "Unable to open password file", e);
            Ui.printWithTab(e.getMessage());
        }
        WiagiLogger.logger.log(Level.INFO, "Finish creating new user");
    }

    private static int getNewUserPassword() {
        Ui.printSeparator();
        Ui.printWithTab("Hi! You seem to be new, are you ready?!");
        Ui.printWithTab("Please enter your new account password:");
        String password = Ui.readCommand();
        return password.hashCode();
    }
}
