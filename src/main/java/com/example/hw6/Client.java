package com.example.hw6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Client {
    private static boolean stopChat = false;
    private static Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        try {
            socket = new Socket("localhost", 1234);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Подключение к серверу установлено.");

            Timer timer = new Timer();
            timer.schedule(new CheckBooleanTask(), 0, 1000); // Проверка каждую секунду

            clientTern();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void clientTern() throws IOException {
        while (true) {
            System.out.print("Клиент: ");
            String message = readMessageFromConsole();
            out.println(message);
            if (message.equalsIgnoreCase("!end")) {
                break;
            }
            if (message.equalsIgnoreCase("#STOP")) {
                stopChat = true;
            }
        }
        serverTern();
    }

    private static void serverTern() throws IOException {
        while (true) {
            String response = in.readLine();
            System.out.println("Сервер: " + response);
            if (response.equalsIgnoreCase("!end")) {
                break;
            }
            if (response.equalsIgnoreCase("#STOP")) {
                stopChat = true;
            }
        }
        clientTern();
    }

    private static String readMessageFromConsole() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }
    static class CheckBooleanTask extends TimerTask {
        public void run() {
            if (stopChat) {
                try {
                    socket.close();
                    System.out.println("Подключение к серверу закрыто.");
                    System.exit(-1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}