package com.gmail.a1ekskosyak;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IOUtils {

    private static final String PATH_TO_USER_FILE = "C:\\Users\\A1eks\\IdeaProjects\\MessengerSDA\\Files\\Users\\";

    Scanner scanner;

    public IOUtils() {
        scanner = new Scanner(System.in);
    }

    public boolean fileExist(Path fileName) {
        return Files.exists(fileName);
    }

    public boolean fileExist(String filename) {
        Path filePath = Paths.get(filename + ".txt");
        return fileExist(filePath);
    }

    public void writeMessage(String message) {
        System.out.println(message);
    }

    public String readNextLine() {
        return scanner.nextLine();
    }

    public User readUser(String email) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(
                    new File(PATH_TO_USER_FILE + email + ".txt")));
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            bufferedReader.close();
            User user = new User(lines.get(1), lines.get(0), lines.get(2));
            return user;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveUser(User user) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(
                new FileWriter(PATH_TO_USER_FILE + user.getEmail() + ".txt"));
        bufferedWriter.write(user.toString());
        bufferedWriter.close();
    }

    public boolean deleteUser(String email){
        File pathToFile = new File(PATH_TO_USER_FILE + email + ".txt");
        return pathToFile.delete();
    }
}
