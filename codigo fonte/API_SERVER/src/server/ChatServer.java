package server;
import java.io.IOException;
import java.net.ServerSocket;

public class ChatServer {

    private ServerSocket serverSocket;
    public static final Integer PORT = 9999;

    public ChatServer(){
        initializeServer();
    }

    private void initializeServer(){
        try {
            System.out.println("Inicializando servidor!");
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciado!");
            clientConnectionLoop();
        } catch (IOException e) {
            System.out.println("Error ao tentar inicializar o servidor!" + e.getMessage());
        }
    }

    private void clientConnectionLoop() throws IOException{
        while(true){
            ClientsManager client = new ClientsManager(serverSocket.accept());
            System.out.println("Um novo cliente acabou de entrar!!!");
            new Thread(() -> client.clientMsgLoop()).start();;
        }
    }

}
