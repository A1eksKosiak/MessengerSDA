package com.gmail.a1ekskosyak;

import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

abstract class FileUserService {

    private static final String PATH_TO_USER_FILE = "C:\\Users\\A1eks\\IdeaProjects\\MessengerSDA\\Files\\Users\\";
    private static final Scanner SCAN_INPUT_FROM_USER = new Scanner(System.in);

    static void createNewUser() throws IOException {
        System.out.println("Input your name");
        String newUserName = SCAN_INPUT_FROM_USER.next();
        System.out.println("Input your email");
        String newUserEmail = SCAN_INPUT_FROM_USER.next();
        if (userExists(newUserEmail)) {
            System.out.println("User with email " + newUserEmail + " already exists in the system.\n Try login");
            return;
        }
        System.out.println("Input your password");
        String newUserPassword = SCAN_INPUT_FROM_USER.next();

        User newUser = new User(newUserName, newUserEmail, newUserPassword);

        // save new user to file
        BufferedWriter bufferedWriter = new BufferedWriter(
                new FileWriter(PATH_TO_USER_FILE + newUserEmail + ".txt"));
        bufferedWriter.write(newUser.toString());
        bufferedWriter.close();
        System.out.println("User successfully created.\n Please login.");

    }

    static boolean userExists(String email) {
        File pathToFile = new File(PATH_TO_USER_FILE + email + ".txt");
        return pathToFile.exists();
    }

    static void loginMenu() {
        System.out.println("Insert your email");
        String email = SCAN_INPUT_FROM_USER.next();
        System.out.println("Insert your password");
        String password = SCAN_INPUT_FROM_USER.next();
        if (!userExists(email)) {
            System.out.println("Wrong password or user " + email + " does not exist.");
            return;
        }
        try {
            if (!checkPassword(email, password)) {
                System.out.println("Wrong password or user " + email + " does not exist.");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Successfully logged in!");
        userOptionsMenu(email);
    }

    private static void userOptionsMenu(String email) {
        System.out.println("Choose your next actions:");
        System.out.println("1 - if you are new user.");
        System.out.println("9 - delete your user user.");
        System.out.println("0 - to exit our application.");
        try {
            int input = SCAN_INPUT_FROM_USER.nextInt();
            switch (input) {
                case 1:
                    break;
                case 9:
                    System.out.println("To delete account, please insert confirm with your password.");
                    String password = SCAN_INPUT_FROM_USER.next();
                    try {
                        if (checkPassword(email, password)) {
                            if (deleteUser(email)) {
                                System.out.println("Your account " + email + " was deleted.");
                            } else {
                                System.out.println("We couldn't delete your account.");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Incorrect input");
        }
    }

    private static boolean deleteUser(String email) {
        File pathToFile = new File(PATH_TO_USER_FILE + email + ".txt");
        return pathToFile.delete();
    }

    private static boolean checkPassword(String email, String password) throws IOException {
        File pathToFile = new File(PATH_TO_USER_FILE + email + ".txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToFile));
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        bufferedReader.close();
        return lines.get(2).equals(password);
    }

}
