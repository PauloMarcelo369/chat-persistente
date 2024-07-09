package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;
import controller.*;
import entitys.MessageEntity;
import entitys.UserEntity;
import util.JsonTratament;
import util.Protocols;

public class ClientsManager {
    private Socket socket;
    private PrintWriter writter;
    private BufferedReader reader;
    private MessageController messageController;
    private UserController userController;
    public static List<ClientsManager> onlineClients = new ArrayList<>(); 
    public String loginName;

    public ClientsManager(Socket socket) {
        try{
            messageController = new MessageController();
            userController = new UserController();
            this.socket = socket;
            writter = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch(IOException e){
           System.out.println("Não foi possivel criar um gerenciador de clientes para este cliente: " + e.getMessage());
        }
    }

    public Boolean userIsOnline(String name){
        for (ClientsManager client : onlineClients){
            if (client.loginName.equals(name)) return true;
        }
        return false;
    }
    
    public void clientMsgLoop(){
        String msg;
        System.out.println("Novo cliente");
        while ((msg = getMessage()) != null){
            String[] command = msg.split("::");
            switch (command[0]) {

                case Protocols.CLIENT_ACCESS:
                    if (!userIsOnline(command[1])){
                        userController.insertUser(command[1]);  
                        this.loginName = command[1];
                        onlineClients.add(this);
                        SendMsg(Protocols.SUCESS_LOGIN + "::O cliente foi logado com sucesso!");

                    } else SendMsg(Protocols.ERROR_ACESS + "::O cliente já está logado no servidor!");
                    break;

                case Protocols.GET_OLD_MESSAGES:
                    String messageList = messageController.getMessages(command[1]);
                    SendMsg(messageList != null 
                    ? (Protocols.RECEIVE_MESSAGE_LIST + "::" + messageList) 
                    : Protocols.NULL_RESPONSE + "::");
                    break;

                case Protocols.GET_LIST:
                    String clientList = userController.getUsers();
                    SendMsg(clientList != null 
                    ? Protocols.LIST + "::" + clientList 
                    : Protocols.NULL_RESPONSE + "::");
                    break;

                case Protocols.SEND_MESSAGE:
                    messageController.insertMsg(command[2]);

                    onlineClients.forEach(client -> {
                        String receiver = JsonTratament.fromJson(client.loginName, UserEntity.class).getUserName();
                        if (command[1].equals(receiver)) {
                            client.SendMsg(Protocols.RECEIVE_MESSAGE + 
                            "::" + command[2]);
                        }
                    });
                    break;

                case Protocols.QUIT:
                    onlineClients.remove(this);
                    close();
                    return;
                    
                default:
                    break;
            }
        }
    }

    public String getMessage(){
        try {
            return reader.readLine();
        } catch (IOException e) {
            System.out.println("Mensagem não pode ser enviada!" + e.getMessage());
            return null;
        }
    }

    public Boolean SendMsg(String msg){
        writter.println(msg);
        return !writter.checkError();
    }

    private void close() {
        try {
            writter.close();
            reader.close();
            socket.close();
            
        } catch (IOException e) {
            System.out.println("Erro ao fechar o sockete: " + e.getMessage());
        }
    }    
}
