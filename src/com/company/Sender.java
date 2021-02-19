package com.company;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sender extends Thread{
    private static List<Socket> socketList = new ArrayList<>();
    public static List<String> printList = new ArrayList<>();

    public static void addClient(Socket socket) {
        synchronized (socketList) {
            socketList.add(socket);
        }
    }

    public static void addLine(String line) {
        synchronized (printList) {
            printList.add(line);
        }
    }

    public static void removeClient(int socketHash) {

        int i = 0;
        synchronized (socketList) {
            for (Socket current : socketList) {
                if (current.hashCode() == socketHash) {
                    socketList.remove(i);
                    System.out.println(ThreadColor.ANSI_WHITE + "SENDER: client removed");
                    return;
                }
                i++;
            }
        }
        System.out.println(ThreadColor.ANSI_WHITE + "SENDER: CLIENT NOT FOUND!");
    }

    @Override
    public void run() {
        try {
            while (true) {
                synchronized (printList) {
                    if (printList.size() != 0) {
                        synchronized (socketList) {
                            for (Socket current : socketList) {
                                PrintWriter output = new PrintWriter(current.getOutputStream() , true);
                                for (String currentLine : printList) {
                                    output.println(currentLine);
                                }
                            }
                        }
                        printList.clear();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(ThreadColor.ANSI_WHITE + "SENDER: Error while sending messages");
        }
    }
}
