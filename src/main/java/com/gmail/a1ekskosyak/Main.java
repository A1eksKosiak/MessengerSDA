package com.gmail.a1ekskosyak;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import static com.gmail.a1ekskosyak.FileUserService.*;

public class Main {
    public static void main(String[] args) {

        System.out.println("Welcome to our Messenger!");
        printGreeting();


    }

    static void printGreeting() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose your action:");
        System.out.println("1 - if you are new user");
        System.out.println("2 - if you already have an account");
        System.out.println("0 - to exit our application");

        try {
            int input = scanner.nextInt();
            switch (input) {
                case 1:
                    try {
                        createNewUser();
                    } catch (IOException e) {
                        e.getMessage();
                    }
                case 2:
                    loginMenu();
                    break;
                case 0:
                    return;
                default:
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Incorrect input");
            printGreeting();
        }
        scanner.close();
    }


}
