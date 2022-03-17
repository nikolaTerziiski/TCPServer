package connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {

        List<ClientHandler> clients = new ArrayList<>();

        try {

            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server started and listening for connect requests");

            Socket clientSocket;
            while (true) {

                clientSocket = serverSocket.accept();
                System.out.println("Accepted connection request from client " + clientSocket.getInetAddress());

                // We want each client to be processed in a separate thread
                // to keep the current thread free to accept() requests from new clients
                ClientHandler clientHandler = new ClientHandler(clientSocket, clients);
                clients.add(clientHandler);

                new Thread(clientHandler).start();
            }

        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the server socket", e);
        }
    }
}
