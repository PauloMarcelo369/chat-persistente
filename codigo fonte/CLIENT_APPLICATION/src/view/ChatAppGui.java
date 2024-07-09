package view;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import controller.ChatApplicationController;
import listener.MessageReiceved;
import models.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChatAppGui extends JFrame {
    private JPanel loginPanel;
    private JPanel mainPanel;
    private JTextArea taEditor = new JTextArea("Digite a sua mensagem: ");
    private JTextArea taVisor = new JTextArea();
    private JList userList = new JList();
    private JLabel notificationBar = new JLabel("Você tem uma nova mensagem!");
    private final Integer WIDTH = 600;
    private final Integer HEIGHT = 350;
    private final int NOTIFICATION_DURATION = 4000;
    private ChatApplicationController chatClient;


    //inicializo a configuração inicial da GUI
    public ChatAppGui(){
        initilializeEventTratament();
        setResizable(false);
        setTitle("Chat App - By PauloMx9");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setVisible(true); // criando a tela de login
        loginPanel = login();
        add(loginPanel);
        setSize(WIDTH, HEIGHT);
    }

    //criação de um panel especializado para a tela de login
    private JPanel login() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(300, 150));
        panel.setBackground(Color.WHITE);
    
        JLabel loginLabel = new JLabel("Login");
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginLabel.setFont(new Font("Arial", Font.BOLD, 20));
    
        JTextField userNameField = new JTextField(15);
        userNameField.setFont(new Font("Arial", Font.PLAIN, 14));
    
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(Color.BLUE);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chatClient.userLogin(userNameField.getText());
            }
        });
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Usuário: "));
        inputPanel.add(userNameField);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(loginButton);
        panel.add(loginLabel, BorderLayout.NORTH);
        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }
    
    //iniciando os componente da GUI
    private JPanel initializeComponents() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
    
        // Barra de notificação
        JPanel notificationPanel = new JPanel(new BorderLayout());
        notificationPanel.setBackground(Color.LIGHT_GRAY);
        notificationPanel.setPreferredSize(new Dimension(WIDTH, 40));
        notificationBar.setForeground(Color.RED);
        notificationBar.setHorizontalAlignment(SwingConstants.CENTER);
        notificationPanel.add(notificationBar, BorderLayout.CENTER);
        notificationBar.setVisible(false);
    
        // Campo de entrada de mensagem
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(Color.WHITE);
        taEditor.setPreferredSize(new Dimension(350, 40));
        messagePanel.add(taEditor, BorderLayout.CENTER);
    
        // Lista de usuários
        JPanel userListPanel = new JPanel(new BorderLayout());
        userListPanel.setBackground(Color.WHITE);
        JScrollPane userScrollPane = new JScrollPane(userList);
        userScrollPane.setPreferredSize(new Dimension(150, HEIGHT));
        userListPanel.add(userScrollPane, BorderLayout.CENTER);
    
        // Área de exibição de mensagens
        JPanel messageDisplayPanel = new JPanel(new BorderLayout());
        messageDisplayPanel.setBackground(Color.WHITE);
        taVisor.setBackground(Color.LIGHT_GRAY);
        taVisor.setEditable(false);
        JScrollPane messageScrollPane = new JScrollPane(taVisor);
        messageScrollPane.setPreferredSize(new Dimension(400, HEIGHT));
        messageDisplayPanel.add(messageScrollPane, BorderLayout.CENTER);
    
        panel.add(notificationPanel, BorderLayout.NORTH);
        panel.add(messagePanel, BorderLayout.SOUTH);
        panel.add(userListPanel, BorderLayout.WEST);
        panel.add(messageDisplayPanel, BorderLayout.CENTER);
    
        return panel;
    }
    
    //criar os eventos da GUI
    private void initilializeEventTratament(){
        chatClient = new ChatApplicationController();
        chatClient.setGuiListener(new MessageReiceved() {

            @Override
            public void receiveMsg(Message message){
                msgGui(message);
            }

            @Override
            public void receiveUserList(ArrayList<String> list){
                buildFriendsList(list);
            }

            @Override
            public void clientAccess(Boolean resposta, String message){
                loginIsSuscefull(resposta, message);
            }

            @Override
            public void receiveOldMessages(ArrayList<Message> oldMessages){
                loadOldMessages(oldMessages);
            }
        });

        taEditor.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    taVisor.append(messageGui(chatClient.sendMsgToFriend(taEditor.getText())));
                    taVisor.append("\n");
                    taEditor.setText("");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });
        

        //ouvinte que faz o cliente deslogar do servidor
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                chatClient.quitServer();
            }
        });
        chatClient.getUsers();
    }
    
    //coloca a mensagem no visor
    private void msgGui(Message message){
        if (message.getSender().equals(chatClient.currentFriend)){
            taVisor.append(messageGui(message));
            taVisor.append("\n");
        }else showNotification();
    }

    private void loadOldMessages(ArrayList<Message> oldMessages){
        for (Message message : oldMessages){
            taVisor.append(messageGui(message));
            taVisor.append("\n");
        }
    }

    //construo a lista de amigos e adiciono evento de click para as celulas
    private void buildFriendsList(ArrayList<String> list){
        DefaultListModel<String> model = new DefaultListModel<>();
        userList = new JList<>(model);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        for (String userName : list) {
            model.addElement(userName);
        }

        userList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedFriend = (String)userList.getSelectedValue();
                    if (selectedFriend != null) {
                        chatClient.currentFriend = selectedFriend;
                        taVisor.setText("");
                        chatClient.getOldMessages();
                    }
                }
            }
        });
    }

    private String messageGui(Message message){
        return message.getSender() + ": " + message.getContent() + "\n" + message.getTimeStamp();
    }


    public void showNotification() {
        notificationBar.setVisible(true);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                hideNotification();
            }
        }, NOTIFICATION_DURATION);
    }

    public void hideNotification() {
        notificationBar.setVisible(false);
    }

    private void loginIsSuscefull(Boolean resposta, String message){
        if (resposta){
            remove(loginPanel);
            mainPanel = initializeComponents();
            add(mainPanel);
            revalidate();
            repaint();
            System.out.println("SUCESS: " + message);
        }else {
            System.out.println("ERROR: " + message);
        }
    }
        
}
