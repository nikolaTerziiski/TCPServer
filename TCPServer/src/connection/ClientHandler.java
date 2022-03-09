package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClientHandler implements Runnable{

    private Socket client;

    private static final String DATE_COMMAND = "date";
    private static final String HELP_COMMAND = "help";
    private static final String BYE_COMMAND = "bye";
    private static final String CLIENTS_COMMAND = "clients";
    private static final String SET_NAME = "setname";
    private static final String GET_NAME = "getname";
    private static final String GET_CLIENTS = "getclients";

    private List<ClientHandler> clientHandlerList;

    private String name;

    public ClientHandler(Socket clientSocket, List<ClientHandler> clientHandlerList) {
        this.client = clientSocket;
        this.clientHandlerList = clientHandlerList;
        this.name = "Unknown" + this.clientHandlerList.size();
    }

    @Override
    public void run() {
        try {

            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) { // read the message from the client
                List<String> commands = Arrays.stream(inputLine.split(" ")).toList();
                switch (commands.get(0)) {
                    case DATE_COMMAND -> out.println("Current date is: " + LocalDateTime.now());
                    case HELP_COMMAND -> out.println("This is a small tcp server that understands few commands like: \"help\", \"date\", \"bye\", \"clients\", \"getname\", \"setname\"," +
                        "\"getclients\"");
                    case BYE_COMMAND -> out.println("Goodbye! Hope you get graded well :)");
                    case CLIENTS_COMMAND -> out.println("Total clients: " + this.clientHandlerList.size());
                    case SET_NAME -> {
                        this.setName(commands.get(1));
                        out.println("You set your name to be: " + this.name);
                    }
                    case GET_NAME -> out.println("Your name is: " + this.name);
                    case GET_CLIENTS -> out.println("All client names: " + this.clientHandlerList.stream().map(e -> e.getName()).collect(
                        Collectors.joining(", ")));
                    default -> out.println("ERROR! INVALID COMMAND! Type \"help\" to learn the commands!");
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    private void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
}
