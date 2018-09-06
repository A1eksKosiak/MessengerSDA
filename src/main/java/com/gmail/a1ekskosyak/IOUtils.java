package com.gmail.a1ekskosyak;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class IOUtils {
    private static final String PATH_TO_USER_FILE = "Files\\Users\\";
    private static final String PATH_TO_GROUP_CHAT = "Files\\GroupChats\\";
    private static final String PATH_TO_USER_SIZES_FILE = "Files\\Users\\!UserFilesSizes.txt";

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

    public boolean fileExist(String filename, String fileFormat) {
        Path filePath = Paths.get(PATH_TO_GROUP_CHAT + filename + fileFormat);
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
            List<String> lines = Files.readAllLines(Paths.get(PATH_TO_USER_FILE + email + ".txt"));
            if (lines.size() < 3) {
                throw new UserFileIsCorruptedException("File is corrupted, incorrect number of lines.");
            }
            User user = new User(lines.get(1), lines.get(0), lines.get(2));
            return user;
        } catch (UserFileIsCorruptedException e) {
            e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveUser(User user) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(
                new FileWriter(PATH_TO_USER_FILE + user.getEmail() + ".txt"));
        bufferedWriter.write(user.toString());
        bufferedWriter.close();
        addUserToNewMessagesChecker(user.getEmail());
    }

    public boolean deleteUser(String email) throws IOException {
//        List<String> lines = Files.readAllLines(Paths.get(PATH_TO_USER_SIZES_FILE));
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(PATH_TO_USER_SIZES_FILE));
//        for (String line : lines) {
//            String[] splitLine = line.split(",");
//            if (splitLine[0].equals(email)) {
//                lines.remove(line);
//            }
//            bufferedWriter.write(line + "\n");
//        }
//        bufferedWriter.close();
        return new File(PATH_TO_USER_FILE + email + ".txt").delete();
    }

    public void sendMessageToAnotherUser(String fromUser, String toUser, String message) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(
                    new FileWriter(PATH_TO_USER_FILE + toUser + ".txt", true));
            bufferedWriter.append("\n" + fromUser + ": " + message);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkNewMessages(String email) throws IOException {
        if (newMessages(email)) {
            List<String> lines = Files.readAllLines(Paths.get(PATH_TO_USER_SIZES_FILE));
            List<String> userLines = Files.readAllLines(Paths.get(PATH_TO_USER_FILE + email + ".txt"));
            int messagesCount = 0;
            for (String line : lines) {
                String[] splitLine = line.split(",");
                if (splitLine[0].equals(email)) {
                    messagesCount = userLines.size() - Integer.parseInt(splitLine[1]);
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
                if (Integer.parseInt(split[1]) != userLines.size() && userLines.size() > 4) {
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
            String[] splitLine = line.split(",");
            if (splitLine[0].equals(email)) {
                splitLine[1] = String.valueOf(userLines.size());
                line = "";
                for (String element : splitLine) {
                    line += element + ",";
                }
            }
            if (!line.equals("")) {
                bufferedWriter.write(line + "\n");
            }
        }
        bufferedWriter.close();
    }

    public synchronized void addMessageToGroupChat(String chatName, String userEmail, String message) {
        try {
            FileWriter fileWriter = new FileWriter(PATH_TO_GROUP_CHAT + chatName + ".txt", true);
            fileWriter.append(LocalDateTime.now(Clock.systemUTC()) + ": " + userEmail + " : " + message + "\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readAllMessagesFromGroupChat(String chatName) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(PATH_TO_GROUP_CHAT + chatName + ".txt"));
            for (int i = 0; i < lines.size(); i++) {
                writeMessage(lines.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readLastMessagesFromGroupChat(String userEmail, String chatName) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(PATH_TO_GROUP_CHAT + chatName + "Settings.txt"));
            List<String> chatLines = Files.readAllLines(Paths.get(PATH_TO_GROUP_CHAT + chatName + ".txt"));
            for (String line : lines) {
                String[] splitElement = line.split(",");
                if (splitElement[0].equals(userEmail)) {
                    if (Integer.parseInt(splitElement[1]) != chatLines.size()) {
                        for (int i = 0; i < chatLines.size() - Integer.parseInt(splitElement[1]); i++) {
                            writeMessage(chatLines.get(chatLines.size() - i - 1));
                        }
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkForNewMessagesInGroupChat(String userEmail, String chatName) {
        try {
            Thread.sleep(100);
            List<String> lines = Files.readAllLines(Paths.get(PATH_TO_GROUP_CHAT + chatName + "Settings.txt"));
            List<String> chatLines = Files.readAllLines(Paths.get(PATH_TO_GROUP_CHAT + chatName + ".txt"));
            for (String line : lines) {
                String[] splitElement = line.split(",");
                if (splitElement[0].equals(userEmail)) {
                    if (Integer.parseInt(splitElement[1]) != chatLines.size()) {
                        return true;
                    }
                    return false;
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createUserInSettingsFile(String chatName, String userEmail) throws UpdatingGroupChatException {
        try {
            FileWriter fileWriter = new FileWriter(PATH_TO_GROUP_CHAT + chatName + "Settings.txt", true);
            List<String> lines = Files.readAllLines(Paths.get(PATH_TO_GROUP_CHAT + chatName + ".txt"));
            fileWriter.write(userEmail + "," + lines.size() + "\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void updateCountOfReadMessagesInGroupChat(String userEmail, String chatName) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(PATH_TO_GROUP_CHAT + chatName + "Settings.txt"));
            List<String> chatLines = Files.readAllLines(Paths.get(PATH_TO_GROUP_CHAT + chatName + ".txt"));
            FileWriter fileWriter = new FileWriter(PATH_TO_GROUP_CHAT + chatName + "Settings.txt");
            for (String line : lines) {
                String[] splitElement = line.split(",");
                if (splitElement[0].equals(userEmail)) {
                    splitElement[1] = String.valueOf(chatLines.size());
                    line = splitElement[0] + "," + splitElement[1];
                }
                fileWriter.write(line + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean userExistsInSettingsFile(String chatName, String userEmail) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(PATH_TO_GROUP_CHAT + chatName + "Settings.txt"));
            for (String line : lines) {
                String[] splitElement = line.split(",");
                if (splitElement[0].equals(userEmail)) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
