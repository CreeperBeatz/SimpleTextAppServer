package com.company;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {

    public static void main(String[] args) {

        new Sender().start();

	    try(ServerSocket serverSocket = new ServerSocket(PublicConstants.PORT_NUMBER_RECEIVER)) {
	        while(true) {
                System.out.println("ACCEPTER: Listening for new client...");
                new Receiver(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.out.println("ACCEPTER: Server error! while listening for clients");
        }
    }
}
