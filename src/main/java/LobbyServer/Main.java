package LobbyServer;

import org.glassfish.tyrus.server.Server;

import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Set<Class<?>> endpointClasses = new HashSet<>();
        endpointClasses.add(WebsocketServer.class);

        Server server = new Server("localhost", 8080, "/",null, endpointClasses);

        try {
            server.start();
            System.out.println("WebSocket server started on ws://localhost:8080/lobby");
            System.out.println("Press any key to stop the server...");
            System.in.read(); // Enterキーなど押されるまで待機
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}