package tcp;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerTCP {
    ServerSocket serverSocket;
    ExecutorService pool;
    Socket connectionSocket;
    BufferedReader reader;
    BufferedWriter writer;


    public ServerTCP(String host, int port) throws IOException {
        serverSocket = new ServerSocket(port,50, InetAddress.getByName(host));
        System.out.println("Server Socket is initialized");
        this.connectionSocket = serverSocket.accept();
        reader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));
        this.run();
    }

    public void run() {

        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Wait for other side....");
        while (true) {
            try {
                //read from client
                String line = reader.readLine();
                System.out.println("=>" + line);

                if (line.startsWith("exit")) {
                    break;
                }

                //send to client
                System.out.print("You: ");
                line = consoleReader.readLine();
                writer.write(line+"\n");
                writer.flush();

                if (line.startsWith("exit")) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        try {
            reader.close();
            writer.close();
            connectionSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
