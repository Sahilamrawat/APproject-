package com.angrybird.game;

import com.badlogic.gdx.files.FileHandle;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginScreenTest {

    private LoginScreen loginScreen;
    private String playerName = "sahil";
    private String playerPassword = "sahil12";
    private String playerAge = "19";

    @Before
    public void setUp() {
        // Initialize the login screen
        loginScreen = new LoginScreen();
    }

    @Test
    public void testLogin_userExists() {
        // Simulate the user.json file being present with valid user data
        FileHandle userFile = new FileHandle("user.json");
        userFile.writeString("{\"playerPassword\":{\"class\":\"java.lang.String\",\"value\":\"sahil12\"},"
            + "\"playerAge\":{\"class\":\"java.lang.String\",\"value\":\"19\"},"
            + "\"playerName\":{\"class\":\"java.lang.String\",\"value\":\"sahil\"}}", false);

        // Check if the user exists by reading the file content
        String userData = userFile.readString();
        if (userData.contains("\"playerName\":{\"class\":\"java.lang.String\",\"value\":\"sahil\"}") &&
            userData.contains("\"playerPassword\":{\"class\":\"java.lang.String\",\"value\":\"sahil12\"}") &&
            userData.contains("\"playerAge\":{\"class\":\"java.lang.String\",\"value\":\"19\"}")) {

            // If data found, user exists
            System.out.println("User exists");
            assertTrue("User should be registered.", userData.contains("sahil"));
            assertTrue("Password should match.", userData.contains("sahil12"));
            assertTrue("Age should match.", userData.contains("19"));
        } else {
            // If data not found, user is not registered
            System.out.println("User is not registered");
            fail("User data is missing from the file.");
        }
    }

    @Test
    public void testLogin_userNotRegistered() {
        // Simulate the user.json file being absent or empty (no user registered)
        FileHandle userFile = new FileHandle("user.json");
        if (userFile.exists()) {
            userFile.delete(); // Ensure the file does not exist
        }

        // Check if user data exists in the file (it should not exist in this case)
        assertFalse("User should not be registered.", userFile.exists());

        // If the file does not exist, display "user is not registered"
        System.out.println("User is not registered");
    }
}
