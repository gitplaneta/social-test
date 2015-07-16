package eu.busz.codurance.persistence.memory;

import eu.busz.codurance.model.Message;

import java.util.List;


public interface MessageRepository {
    void saveMessage(String userName, String textMessage);

    List<Message> getMessagesByUserName(String userName);
}
