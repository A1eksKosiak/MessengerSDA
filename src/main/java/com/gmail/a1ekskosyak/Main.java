package com.gmail.a1ekskosyak;

import java.util.InputMismatchException;

import static com.gmail.a1ekskosyak.FileUserService.createNewUserMenu;
import static com.gmail.a1ekskosyak.FileUserService.loginMenu;

public class Main {
    private static IOUtils ioUtils = new IOUtils();

    public static void main(String[] args) {


        ioUtils.writeMessage("Welcome to our Messenger!");
        printGreeting();

    }

    static void printGreeting() {
        ioUtils.writeMessage("Choose your action:");
        ioUtils.writeMessage("Insert \"1\" to create a new account");
        ioUtils.writeMessage("Insert \"2\" if you already have an account");
        ioUtils.writeMessage("Insert \"0\" to exit application");

        try {
            int input = Integer.parseInt(ioUtils.readNextLine());
            switch (input) {
                case 1:
                    createNewUserMenu();
                case 2:
                    loginMenu();
                    break;
                case 0:
                    return;
                default:

                    printGreeting();
                    break;
            }
        } catch (InputMismatchException e) {
            ioUtils.writeMessage("Incorrect input");
            printGreeting();
        }
    }


}
