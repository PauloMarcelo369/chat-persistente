package listener;
import java.util.ArrayList;
import models.Message;

public interface MessageReiceved {
    public void receiveMsg(Message message);
    public void receiveUserList(ArrayList<String> userList);
    public void clientAccess(Boolean resosta, String mensagem);
    public void receiveOldMessages(ArrayList<Message> oldMessages);
}
