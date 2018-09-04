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
                Thread.sleep(1000);
                ioUtils.checkNewMessages(email);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        // start Thread to see any new emails
        ioUtils.writeMessage("Choose your next actions:");
        ioUtils.writeMessage("1 - send a message to other user.");
        ioUtils.writeMessage("9 - delete your user user.");
        ioUtils.writeMessage("0 - to exit our application.");
        try {
            int input = Integer.parseInt(ioUtils.readNextLine());
            switch (input) {
                case 1:
                    ioUtils.writeMessage("Whom to send?");
                    ioUtils.sendMessageToAnotherUser(email, ioUtils.readNextLine());
                    userOptionsMenu(email);
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

    public static boolean checkPassword(String email, String password) {
        if (!ioUtils.fileExist(email)) {
            return false;
        }
        User user = ioUtils.readUser(email);
        return user.getPassword().equals(password);
    }

}
