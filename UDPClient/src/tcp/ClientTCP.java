package tcp;

import java.io.*;
import java.net.Socket;

public class ClientTCP {
    Socket socket;
    BufferedReader reader;
    BufferedWriter writer;

    public ClientTCP(String host, int port) throws IOException {
        socket = new Socket(host, port);
        System.out.println("Client Socket is initialized");
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.run();
    }

    public void run() throws IOException {
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                //send to server
                System.out.print("You: ");
                String line = consoleReader.readLine();
                writer.write(line+"\n");
                writer.flush();

                if (line.startsWith("exit")) {
                    break;
                }

                //read from server
                line = reader.readLine();
                System.out.println("=> " + line);

                if (line.startsWith("exit")) {
                    break;
                }

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        try {
            consoleReader.close();
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
    }

}
