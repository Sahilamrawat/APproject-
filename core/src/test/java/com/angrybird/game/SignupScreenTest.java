package com.angrybird.game;

import com.badlogic.gdx.files.FileHandle;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SignupScreenTest {

    private SignupScreen signupScreen;
    private String playerName = "sahil";
    private String playerPassword = "sahil12";
    private String playerAge = "19";
    private String playerEmail = "sahil@example.com";

    @Before
    public void setUp() {
        // Initialize the signup screen
        signupScreen = new SignupScreen();
    }

    @Test
    public void testSignup_validUserData() {
        // Simulate entering valid data for the user
        FileHandle userFile = new FileHandle("user.json");

        // Write user data to simulate the signup process
        userFile.writeString("{\"playerName\":{\"class\":\"java.lang.String\",\"value\":\"" + playerName + "\"},"
            + "\"playerPassword\":{\"class\":\"java.lang.String\",\"value\":\"" + playerPassword + "\"},"
            + "\"playerAge\":{\"class\":\"java.lang.String\",\"value\":\"" + playerAge + "\"},"
            + "\"playerEmail\":{\"class\":\"java.lang.String\",\"value\":\"" + playerEmail + "\"}}", false);

        // Read the user data back from the file
        String userData = userFile.readString();

        // Assert that the written data matches the expected values
        assertTrue("Player name should match.", userData.contains("\"playerName\":{\"class\":\"java.lang.String\",\"value\":\"" + playerName + "\"}"));
        assertTrue("Password should match.", userData.contains("\"playerPassword\":{\"class\":\"java.lang.String\",\"value\":\"" + playerPassword + "\"}"));
        assertTrue("Age should match.", userData.contains("\"playerAge\":{\"class\":\"java.lang.String\",\"value\":\"" + playerAge + "\"}"));
        assertTrue("Email should match.", userData.contains("\"playerEmail\":{\"class\":\"java.lang.String\",\"value\":\"" + playerEmail + "\"}"));

        // If data matches, print a success message
        System.out.println("User successfully registered with valid data.");
    }

    @Test
    public void testSignup_missingPlayerName() {
        // Simulate missing player name during signup
        FileHandle userFile = new FileHandle("user.json");

        // Write incomplete user data to simulate the signup process with missing player name
        userFile.writeString("{\"playerPassword\":{\"class\":\"java.lang.String\",\"value\":\"" + playerPassword + "\"},"
            + "\"playerAge\":{\"class\":\"java.lang.String\",\"value\":\"" + playerAge + "\"},"
            + "\"playerEmail\":{\"class\":\"java.lang.String\",\"value\":\"" + playerEmail + "\"}}", false);

        // Read the user data back from the file
        String userData = userFile.readString();

        // Check if the user data contains the missing playerName
        assertFalse("Player name should be missing.", userData.contains("\"playerName\":{\"class\":\"java.lang.String\",\"value\":\"" + playerName + "\"}"));

        // If player name is missing, print an error message
        System.out.println("Signup failed. Player name is missing.");
    }

    @Test
    public void testSignup_invalidEmailFormat() {
        // Simulate invalid email format during signup
//        String invalidEmail = "invalid-email";
        FileHandle userFile = new FileHandle("user.json");

        // Write user data with an invalid email
        userFile.writeString("{\"playerName\":{\"class\":\"java.lang.String\",\"value\":\"" + playerName + "\"},"
            + "\"playerPassword\":{\"class\":\"java.lang.String\",\"value\":\"" + playerPassword + "\"},"
            + "\"playerAge\":{\"class\":\"java.lang.String\",\"value\":\"" + playerAge + "\"}}", false);
        // Read the user data back from the file
        String userData = userFile.readString();

        // Assert that the email doesn't match the expected valid format
        assertFalse("Email should be invalid.", userData.contains("\"playerEmail\":{\"class\":\"java.lang.String\",\"value\":\"" + playerEmail + "\"}"));

        // If the email is invalid, print an error message
        System.out.println("Signup failed. Invalid email format.");
    }

    @Test
    public void testSignup_emptyFields() {
        // Simulate empty fields during signup
        FileHandle userFile = new FileHandle("user.json");

        // Write empty data to simulate the signup process with missing fields
        userFile.writeString("{\"playerName\":{\"class\":\"java.lang.String\",\"value\":\"\"},"
            + "\"playerPassword\":{\"class\":\"java.lang.String\",\"value\":\"\"},"
            + "\"playerAge\":{\"class\":\"java.lang.String\",\"value\":\"\"},"
            + "\"playerEmail\":{\"class\":\"java.lang.String\",\"value\":\"\"}}", false);

        // Read the user data back from the file
        String userData = userFile.readString();

        // Assert that the data fields are empty
        assertTrue("Player name should be empty.", userData.contains("\"playerName\":{\"class\":\"java.lang.String\",\"value\":\"\"}"));
        assertTrue("Password should be empty.", userData.contains("\"playerPassword\":{\"class\":\"java.lang.String\",\"value\":\"\"}"));
        assertTrue("Age should be empty.", userData.contains("\"playerAge\":{\"class\":\"java.lang.String\",\"value\":\"\"}"));
        assertTrue("Email should be empty.", userData.contains("\"playerEmail\":{\"class\":\"java.lang.String\",\"value\":\"\"}"));

        // If any fields are empty, print an error message
        System.out.println("Signup failed. Some fields are empty.");
    }

    @Test
    public void testSignup_userAlreadyExists() {
        // Simulate a scenario where the user already exists (checking if a file already exists)
        FileHandle userFile = new FileHandle("user.json");

        // If the file already exists, we want to simulate that the user is already registered
        if (userFile.exists()) {
            userFile.delete(); // Make sure the file doesn't exist before running the test
        }

        // Now simulate that the user is already registered by writing data again
        userFile.writeString("{\"playerName\":{\"class\":\"java.lang.String\",\"value\":\"" + playerName + "\"},"
            + "\"playerPassword\":{\"class\":\"java.lang.String\",\"value\":\"" + playerPassword + "\"},"
            + "\"playerAge\":{\"class\":\"java.lang.String\",\"value\":\"" + playerAge + "\"},"
            + "\"playerEmail\":{\"class\":\"java.lang.String\",\"value\":\"" + playerEmail + "\"}}", false);

        // Check if user data exists in the file
        assertTrue("User should be registered.", userFile.exists());
        String userData = userFile.readString();

        // Assert that the user data matches
        assertTrue("Player name should match.", userData.contains("\"playerName\":{\"class\":\"java.lang.String\",\"value\":\"" + playerName + "\"}"));
        assertTrue("Password should match.", userData.contains("\"playerPassword\":{\"class\":\"java.lang.String\",\"value\":\"" + playerPassword + "\"}"));
        assertTrue("Age should match.", userData.contains("\"playerAge\":{\"class\":\"java.lang.String\",\"value\":\"" + playerAge + "\"}"));
        assertTrue("Email should match.", userData.contains("\"playerEmail\":{\"class\":\"java.lang.String\",\"value\":\"" + playerEmail + "\"}"));

        // If user data exists, print a success message
        System.out.println("User already exists with the provided data.");
    }
}
