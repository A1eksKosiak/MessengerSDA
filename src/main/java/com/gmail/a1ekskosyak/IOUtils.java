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
    private static final String PATH_TO_USER_SIZES_FILE = "C:\\Users\\A1eks\\IdeaProjects\\MessengerSDA\\Files\\Users\\!UserFilesSizes.txt";

    Scanner scanner;

    public IOUtils() {
        scanner = new Scanner(System.in);
    }

    public boolean fileExist(Path fileName) {
        return Files.exists(fileName);
    }

    public boolean fileExist(String filename) {
        Path filePath = Paths.get(PATH_TO_USER_FILE + filename + ".txt");
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
        addUserToNewMessagesChecker(user.getEmail());
    }

    public boolean deleteUser(String email) {
        return new File(PATH_TO_USER_FILE + email + ".txt").delete();
    }

    public void sendMessageToAnotherUser(String fromUser, String toUser) throws IOException {
        if (!fileExist(toUser)) {
            writeMessage("This user does not exist!");
            return;
        }
        BufferedWriter bufferedWriter = new BufferedWriter(
                new FileWriter(PATH_TO_USER_FILE + toUser + ".txt", true));
        writeMessage("Input your message");
        String message = readNextLine();
        bufferedWriter.append("\n" + fromUser + ": " + message);
        bufferedWriter.close();
    }

    public void checkNewMessages(String email) throws IOException {
        if (newMessages(email)) {
            List<String> lines = Files.readAllLines(Paths.get(PATH_TO_USER_SIZES_FILE));
            List<String> userLines = Files.readAllLines(Paths.get(PATH_TO_USER_FILE + email + ".txt"));
            int messagesCount = 0;
            for (String line : lines) {
                String[] split = line.split(",");
                if (split[0].equals(email)) {
                    messagesCount = userLines.size() - Integer.parseInt(split[1]);
                    break;
                }
            }
            writeMessage("You have " + messagesCount + " new messages:");
            for (int i = messagesCount; i > 0; i--) {
                writeMessage(userLines.get(userLines.size() - messagesCount));
                messagesCount--;
            }
        }
        updateNewMessagesCount(email);
    }

    public boolean newMessages(String email) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(PATH_TO_USER_SIZES_FILE));
        List<String> userLines = Files.readAllLines(Paths.get(PATH_TO_USER_FILE + email + ".txt"));
        for (String line : lines) {
            String[] split = line.split(",");
            if (split[0].equals(email)) {
                if (Integer.parseInt(split[1]) != userLines.size()) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public void addUserToNewMessagesChecker(String email) throws IOException {
        List<String> userLines = Files.readAllLines(Paths.get(PATH_TO_USER_FILE + email + ".txt"));
        BufferedWriter bufferedWriter = new BufferedWriter(
                new FileWriter(PATH_TO_USER_SIZES_FILE, true));
        bufferedWriter.append("\n" + email + "," + userLines.size());
        bufferedWriter.close();
    }

    private void updateNewMessagesCount(String email) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(PATH_TO_USER_SIZES_FILE));
        List<String> userLines = Files.readAllLines(Paths.get(PATH_TO_USER_FILE + email + ".txt"));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(PATH_TO_USER_SIZES_FILE));
        for (String line : lines) {
            String[] split = line.split(",");
            if (split[0].equals(email)) {
                split[1] = String.valueOf(userLines.size());
                line = "";
                for (String element : split) {
                    line += element + ",";
                }
            }
            if (!(line.equals("") || line.equals(null))) {
                bufferedWriter.write(line + "\n");
            }
        }
        bufferedWriter.close();
    }
}
