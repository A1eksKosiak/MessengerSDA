package com.gmail.a1ekskosyak;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class FileUserService {

    private static final String PATH_TO_USER_FILE = "C:\\Users\\A1eks\\IdeaProjects\\MessengerSDA\\Files\\Users\\";

    static void createNewUser() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input your name");
        String newUserName = scanner.next();
        System.out.println("Input your email");
        String newUserEmail = scanner.next();
        if (userExists(newUserEmail)) {
            System.out.println("User with email " + newUserEmail + " already exists in the system.\n Try login");
            return;
        }
        System.out.println("Input your password");
        String newUserPassword = scanner.next();
        scanner.close();

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
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert your email");
        String email = scanner.next();
        System.out.println("Insert your password");
        String password = scanner.next();
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
        } catch (WrongEmailException e) {
            e.printStackTrace();
        }
        System.out.println("Successfully logged in");

    }

    private static boolean checkPassword(String email, String password) throws IOException, WrongEmailException {
        File pathToFile = new File(PATH_TO_USER_FILE + email + ".txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToFile));
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        if (!lines.get(0).equals(email)) {
            throw new WrongEmailException("Email doesn't match file name");
        }
        if (!lines.get(2).equals(password)) {
            return false;
        }
        return true;
    }

}
