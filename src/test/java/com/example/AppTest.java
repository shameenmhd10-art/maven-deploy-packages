package com.example;

public class App {

    public String getMessage() {
        return "Hello Jenkins CI/CD!";
    }

    public static void main(String[] args) {
        App app = new App();
        System.out.println(app.getMessage());
    }
}
