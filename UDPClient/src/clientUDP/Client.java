package clientUDP;

import tcp.ClientTCP;
import tcp.ServerTCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class Client {

    DatagramSocket clientSocket;

    public Client(int portNumber) throws SocketException {
        clientSocket = new DatagramSocket(portNumber);
    }

    public void run() throws IOException {

        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        String line = "";
        String response = "";
        do {
            System.out.print("Enter your username(one word): ");
            line = "join " + consoleReader.readLine();
            response = sendAndReceive(line);
            System.out.println(response);
        } while (response.startsWith("Sorry"));

        boolean flag = true;
        do {
            System.out.print("'1' for search other users,\n'2' for available to others,\n'0' for exit: ");
            String input;
            input = consoleReader.readLine().trim();
            switch (input) {
                case "1":
                    System.out.print("Search other users(username would be enough): ");
                    String name;
                    name = consoleReader.readLine();
                    line = "search " + name;
                    response = sendAndReceive(line);
                    System.out.println(response);
                    if (response.length() > 15) {
                        System.out.print("Do you want to chat with " + name + "? (y/n) ");
                        input = consoleReader.readLine().trim();
                        if (input.equalsIgnoreCase("y")) {
                            line = "connect " + name;
                            response = sendAndReceive(line);
                            if (response.startsWith("ready")) {
                                String[] tokens = response.split(" ");
                                new ClientTCP(tokens[1], Integer.parseInt(tokens[2]));
                            }
                        }
                    }
                    break;
                case "2":
                    System.out.println("Waiting for others to find us!");
                    byte[] buffer = new byte[1024];
                    DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
                    clientSocket.receive(receivedPacket);
                    response = new String(receivedPacket.getData()).trim();
                    if (response.startsWith("open-up")) {
                        String[] tokens = response.split(" ");
                        System.out.println("Starting conversation...");
                        new ServerTCP(tokens[1], Integer.parseInt(tokens[2]));
                    }
                    break;
                case "0":
                    flag = false;
                    break;
            }
        } while (flag);
        clientSocket.close();
    }
    private String sendAndReceive(String line) throws IOException {
        DatagramPacket packet = new DatagramPacket(line.getBytes(), line.getBytes().length,
                InetAddress.getByName("localhost"), 20000);
        clientSocket.send(packet);

        byte[] buffer = new byte[1024];
        DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
        clientSocket.receive(receivedPacket);

        return new String(receivedPacket.getData()).trim();
    }

    public static void main(String[] args) {
        Client client;
        try {
            client = new Client(12000);
            client.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
