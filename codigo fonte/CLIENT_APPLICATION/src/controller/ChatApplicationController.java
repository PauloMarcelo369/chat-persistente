package controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import listener.MessageReiceved;
import models.Client;
import models.Message;
import util.JsonTratament;
import util.Protocols;

public class ChatApplicationController {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    public String clientName;
    public String currentFriend;
    private MessageReiceved messageReicevedListener;

    public ChatApplicationController(){
        try {
            socket = new Socket("127.0.0.1", 9999);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            new Thread(() -> getMessages()).start();

        } catch (Exception e) {
            System.out.println("Erro ao tentar conectar ao servidor: " + e.getMessage());
        }
    }

    public void getOldMessages(){
        sendMsg(Protocols.GET_OLD_MESSAGES + "::" + JsonTratament.toJson(new Message(clientName, currentFriend, "", null)));
    }

    public Message sendMsgToFriend(String msg){
        Message message = new Message(clientName, currentFriend, msg, java.time.LocalDateTime.now());
        sendMsg(Protocols.SEND_MESSAGE + "::" + currentFriend + "::" 
        + JsonTratament.toJson(message));
        return message;
    }

    public void userLogin(String userName){
        clientName = userName;
        sendMsg(Protocols.CLIENT_ACCESS + "::" + JsonTratament.toJson(new Client(userName)));
    }

    public void quitServer(){
        sendMsg(Protocols.QUIT + "::");
    }

    public void getUsers(){
        sendMsg(Protocols.GET_LIST + "::");
    }

    private void getMessages(){
        String msg;
        while ((msg = receiveMsg()) != null){
            String[] command = msg.split("::");
            switch (command[0]) {
                case Protocols.SUCESS_LOGIN:
                    messageReicevedListener.clientAccess(true, command[1]);
                    break;
                case Protocols.ERROR_ACESS:
                    messageReicevedListener.clientAccess(false, command[1]);
                    break;
                case Protocols.LIST:
                    messageReicevedListener.receiveUserList(JsonTratament.jsonArrayToList(command[1]));
                    break;
                case Protocols.RECEIVE_MESSAGE:
                    messageReicevedListener.receiveMsg(JsonTratament.fromJson(command[1], Message.class));
                    break;
                case Protocols.RECEIVE_MESSAGE_LIST:
                    messageReicevedListener.receiveOldMessages(JsonTratament.jsonListToArrayList(command[2]));
                default:
                    break;
            }
        }
    }

    private String receiveMsg(){
        try {
            return reader.readLine();
        } catch (Exception e) {
            System.out.println("Houve algum erro ao receber a mensagem");
            return null;
        }
    }

    private Boolean sendMsg(String msg){
        writer.println(msg);
        return writer.checkError();
    }
    
    private void close(){
        try{
            socket.close();
            writer.close();
            reader.close();
        }
        catch(Exception e){
            System.out.println("Não foi possivel fechar a conexão: " + e.getMessage());
        }

    }

    public void setGuiListener(MessageReiceved listener){
        messageReicevedListener = listener;
    }
}