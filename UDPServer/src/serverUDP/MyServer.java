package serverUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class MyServer {
    DatagramSocket serverSocket;
    List<Client> myClients;
    //initialize variables in constructor
    public MyServer() throws SocketException {
        serverSocket = new DatagramSocket(20000);
        myClients = new ArrayList<>();
    }
    public void run() throws IOException {
        while (true) {
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            System.out.println("Server starts listening");
            serverSocket.receive(packet);

            String line= new String(packet.getData()).trim();
            String response = "";

            if (line.startsWith("join")&&line.length()>5) {
                String[] tokens = line.split(" ");

                Client searchedClient=searchClient(tokens[1]);
                if (searchedClient==null) {
                    Client newClient = new Client();
                    newClient.setName(tokens[1]);
                    newClient.setIp(packet.getAddress());
                    newClient.setPort(packet.getPort());

                    myClients.add(newClient);

                    response = "User \"" + newClient.getName() + "\" has been successfully created(" + newClient.getIp().getHostAddress() + ", " + newClient.getPort() + ')';
                }
                else{
                    response="Sorry, Username taken!";
                }
            }
            else if (line.startsWith("search")&&line.length()>7) {
                String[] tokens = line.split(" ");
                Client searchedClient=searchClient(tokens[1]);
                if (searchedClient!=null)
                    response="Founded! ("+searchedClient.getName()+", "+searchedClient.getIp().getHostAddress()+", "+searchedClient.getPort()+')';
                else
                    response="Not founded!";
            }
            else if (line.startsWith("connect")&&line.length()>8) {
                String[] tokens = line.split(" ");
                Client searchedClient=searchClient(tokens[1]);
                if (searchedClient!=null) {
                    response = "open-up "+ searchedClient.getIp().getHostAddress()
                            + ' ' +searchedClient.getPort();
                    System.out.println(response);
                    sendPack(response, searchedClient.getIp(), searchedClient.getPort());
                    response="ready "+searchedClient.getIp().getHostAddress()+ ' ' +searchedClient.getPort();
                 }
                else
                    response="Not founded!";
            }

            System.out.println(response);
            sendPack(response, packet.getAddress(), packet.getPort());
        }
    }
    //to send packages for clients
    private void sendPack(String response, InetAddress ip, int port) throws IOException {
        DatagramPacket sendPacket = new DatagramPacket(response.getBytes(), response.getBytes().length, ip, port);
        serverSocket.send(sendPacket);
    }
    //to search a client in list
    private Client searchClient(String name){
        Client searchedClient=null;
        for (Client myClient : myClients) {
            if (myClient.getName().equalsIgnoreCase(name)) {
                searchedClient = myClient;
                break;
            }
        }
        return searchedClient;
    }
    private Client searchClient(InetAddress ip){
        Client searchedClient=null;
        for (Client myClient : myClients) {
            if (myClient.getIp().equals(ip)) {
                searchedClient = myClient;
                break;
            }
        }
        return searchedClient;
    }
    //main method to run application
    public static void main(String[] args) {
        try {
            MyServer server = new MyServer();
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
