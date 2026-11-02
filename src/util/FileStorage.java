package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import model.Account;

// Unit V: object serialization and deserialization.
// Saves all accounts (with their transactions) into accounts.dat and loads them back.
// Unit III: uses try / catch / finally to handle file errors.
public class FileStorage {

    // Unit I: static final constant - the name of the data file
    private static final String DATA_FILE = "accounts.dat";

    // Serialization: writes the whole array of accounts into the file as one object
    public static boolean saveAccounts(Account[] accounts) {
        ObjectOutputStream output = null;
        try {
            output = new ObjectOutputStream(new FileOutputStream(DATA_FILE));
            output.writeObject(accounts);
            return true;
        } catch (IOException e) {
            System.out.println("Could not save accounts: " + e.getMessage());
            return false;
        } finally {
            // finally always runs, so the file is closed even if an error happened
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    System.out.println("Could not close the file: " + e.getMessage());
                }
            }
        }
    }

    // Deserialization: reads the array of accounts back from the file.
    // Returns an empty array if the file does not exist yet (first run).
    public static Account[] loadAccounts() {
        // Unit V: Java NIO - Path and Files are used to check if the file exists
        Path path = Paths.get(DATA_FILE);
        if (!Files.exists(path)) {
            return new Account[0];
        }

        ObjectInputStream input = null;
        try {
            input = new ObjectInputStream(new FileInputStream(DATA_FILE));
            // readObject returns an Object, so we cast it back to an Account array
            return (Account[]) input.readObject();
        } catch (IOException e) {
            System.out.println("Could not load accounts: " + e.getMessage());
            return new Account[0];
        } catch (ClassNotFoundException e) {
            System.out.println("Saved data is in an unknown format: " + e.getMessage());
            return new Account[0];
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println("Could not close the file: " + e.getMessage());
                }
            }
        }
    }
}
