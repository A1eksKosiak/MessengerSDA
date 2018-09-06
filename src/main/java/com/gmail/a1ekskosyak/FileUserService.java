package com.gmail.a1ekskosyak;

import java.io.IOException;
import java.util.InputMismatchException;

class FileUserService {

    private static IOUtils ioUtils = new IOUtils();


    public FileUserService(IOUtils ioUtils) {
        this.ioUtils = ioUtils;
    }

    static void createNewUser() {
        ioUtils.writeMessage("Input your email");
        String newUserEmail = ioUtils.readNextLine();
        if (ioUtils.fileExist(newUserEmail)) {
            ioUtils.writeMessage("User with email " + newUserEmail + " already exists in the system.");
            ioUtils.writeMessage("Try login");
            return;
        }
        ioUtils.writeMessage("Input your name");
        String newUserName = ioUtils.readNextLine();
        ioUtils.writeMessage("Input your password");
        String newUserPassword = ioUtils.readNextLine();

        User newUser = new User(newUserName, newUserEmail, newUserPassword);

        // save new user to file
        try {
            ioUtils.saveUser(newUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ioUtils.writeMessage("User successfully created.\n Please login.");
    }

    static void loginMenu() {
        ioUtils.writeMessage("Insert your email");
        String email = ioUtils.readNextLine();
        ioUtils.writeMessage("Insert your password");
        String password = ioUtils.readNextLine();
        if (!checkPassword(email, password)) {
            ioUtils.writeMessage("Wrong password or user " + email + " does not exist.");
            return;
        }
        ioUtils.writeMessage("Successfully logged in!");
        userOptionsMenu(email);
    }

    private static void userOptionsMenu(String email) {
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(1000);
                    ioUtils.checkNewMessages(email);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // start Thread to see any new emails
        thread.setDaemon(true);
        thread.start();
        ioUtils.writeMessage("Choose your next actions:");
        ioUtils.writeMessage("1 - send private message to other user.");
        ioUtils.writeMessage("2 - open group chat");
        ioUtils.writeMessage("9 - delete your user.");
        ioUtils.writeMessage("0 - to exit our application.");
        try {
            int input = Integer.parseInt(ioUtils.readNextLine());
            switch (input) {
                case 1:
                    ioUtils.writeMessage("Whom to send?");
                    String whomToSend = ioUtils.readNextLine();
                    if (ioUtils.fileExist(whomToSend)) {
                        ioUtils.writeMessage("Write your message:");
                        String message = ioUtils.readNextLine();
                        sendPrivateMessage(email, whomToSend, message);
                    } else {
                        ioUtils.writeMessage("This user does not exist!");
                    }
                    userOptionsMenu(email);
                    break;
                case 2:
                    ioUtils.writeMessage("Select group chat from below list:");
                    ioUtils.writeMessage("Type \"main\" to login to Main group");
                    String selectedGroup = ioUtils.readNextLine();
                    writeInGroupChat(selectedGroup, email);
                    break;
                case 9:
                    ioUtils.writeMessage("To delete account, please insert confirm with your password.");
                    String password = ioUtils.readNextLine();
                    if (checkPassword(email, password)) {
                        if (ioUtils.deleteUser(email)) {
                            ioUtils.writeMessage("Your account " + email + " was deleted.");
                        } else {
                            ioUtils.writeMessage("We couldn't delete your account.");
                        }
                    }
                    break;
                case 0:
                    return;
                default:
                    userOptionsMenu(email);
                    break;
            }
        } catch (InputMismatchException e) {
            ioUtils.writeMessage("Incorrect input");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendPrivateMessage(String whoSendsMessage, String whomToSend, String message) {
        if (ioUtils.fileExist(whomToSend)) {
            ioUtils.sendMessageToAnotherUser(whoSendsMessage, whomToSend, message);
            ioUtils.writeMessage("Message sent!");
        } else {
            ioUtils.writeMessage("This user does not exist!");
        }
    }

    public static boolean checkPassword(String email, String password) {
        if (!ioUtils.fileExist(email)) {
            return false;
        }
        User user = ioUtils.readUser(email);
        return user.getPassword().equals(password);
    }

    private static void writeInGroupChat(String chatName, String userEmail) {
        if (chatName.toLowerCase().equals("main") || chatName.toLowerCase().equals("main group")) {
            chatName = "Main";
        }
        if (!ioUtils.fileExist(chatName, ".txt")) {
            ioUtils.writeMessage("Sorry, this chat currently doesn't exist");
            return;
        }
        groupChatMenu(chatName, userEmail);
    }

    private static void groupChatMenu(String chatName, String userEmail) {
        Thread thread = new Thread(() -> {
            while (true) {
                checkingMessagesInGroupChatThread(userEmail, chatName);
            }
        });
        thread.setDaemon(true);
        thread.start();
        if (!ioUtils.userExistsInSettingsFile(chatName, userEmail)) {
            try {
                ioUtils.createUserInSettingsFile(chatName, userEmail);
            } catch (UpdatingGroupChatException e) {
                e.printStackTrace();
                ioUtils.writeMessage("Something went wrong, please call IT");
                return;
            }
        }
        ioUtils.writeMessage("Welcome to " + chatName + " chat!");
        ioUtils.writeMessage("Here you can communicate with other people");
        ioUtils.writeMessage("For help insert \"!help\"");
        ioUtils.writeMessage("To logout insert \"exit\"");
        String message;
        while (!(message = ioUtils.readNextLine()).equals("exit")) {
            if (message.equals("!help")) {
                printPossibleCommandsForGroupChat();
            }
            String[] splitMessage = message.split(" ");
            if (splitMessage[0].equals("private:")) {
                if (ioUtils.fileExist(splitMessage[1])) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String element : splitMessage) {
                        stringBuilder.append(element + " ");
                    }
                    sendPrivateMessage(userEmail, splitMessage[1], stringBuilder.toString());
                } else {
                    ioUtils.writeMessage("This user does not exist!");
                }
            } else {
                ioUtils.addMessageToGroupChat(chatName, userEmail, message);
            }
        }
    }

    private static void printPossibleCommandsForGroupChat() {
        ioUtils.writeMessage("For help insert \"!help\"");
        ioUtils.writeMessage("To send a private message - type \"private: email\" and the message");
        ioUtils.writeMessage("To logout insert \"exit\"");
    }

    public static void checkingMessagesInGroupChatThread(String userEmail, String chatName) {
        while (true) {
            if (ioUtils.checkForNewMessagesInGroupChat(userEmail, chatName)) {
                ioUtils.readLastMessagesFromGroupChat(userEmail, chatName);
                ioUtils.updateCountOfReadMessagesInGroupChat(userEmail, chatName);
            }
        }
    }
}
