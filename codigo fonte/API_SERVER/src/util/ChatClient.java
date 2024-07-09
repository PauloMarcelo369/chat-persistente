package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 9999;

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public ChatClient() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Inicia uma nova Thread para lidar com a entrada do servidor
            new Thread(this::receiveMessages).start();

            // Envio de mensagens de teste
            // Acesso do cliente
            sendMessage("CLIENT_ACCESS::{\"username\":\"user123\"}");

            // Obter mensagens antigas
            // sendMessage("GET_OLD_MESSAGES::{\"sender\":\"user1\",\"receiver\":\"user2\"}");

            // // Enviar mensagem
            // sendMessage("SEND_MESSAGE::{\"sender\":\"user1\",\"receiver\":\"user2\",\"content\":\"Olá, mundo!\"}");

            // // Obter lista de usuários
            // sendMessage("GET_LIST");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveMessages() {
        String message;
        try {
            while ((message = reader.readLine()) != null) {
                // Processa as mensagens recebidas do servidor
                System.out.println("Mensagem do servidor: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    public static void main(String[] args) {
        new ChatClient();
    }
}
