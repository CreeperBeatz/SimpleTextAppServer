package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Random;

public class Receiver extends Thread{
    private Socket socket;
    private String username;
    int color;

    public Receiver(Socket socket) {
        this.socket = socket;
        Sender.addClient(socket);
        Random random = new Random();
        color = random.nextInt(ThreadColor.NUMBER_OF_COLORS);
    }

    @Override
    public void run() {
        System.out.println(ThreadColor.THREAD_COLORS[color] + "RECEIVER: "  + "Connected to client on " + this.getName());

        try {
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            this.username = input.readLine(); //Always first line is client username
            Sender.addLine(username + " has connected to the chat\n");

            while(true) {
                String echoString = input.readLine();
                System.out.println(ThreadColor.THREAD_COLORS[color] + "RECEIVER: " + username + ": " + echoString);
                Sender.addLine(username + ":");
                Sender.addLine(echoString + "\n");
                if(echoString == null) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println(ThreadColor.THREAD_COLORS[color] + "RECEIVER: "  + "Receiver error!");
        } finally {
            try {
                System.out.println(ThreadColor.THREAD_COLORS[color] + "RECEIVER: "  + "Client disconnected...");
                Sender.removeClient(socket.hashCode());
                Sender.addLine(username + " Disconnected\n");
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }
}
