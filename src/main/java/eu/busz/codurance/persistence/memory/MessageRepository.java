package eu.busz.codurance.persistence.memory;

import eu.busz.codurance.model.Message;
import eu.busz.codurance.model.command.follow.Following;

import java.util.List;


public interface MessageRepository {
    void saveMessage(String userName, String textMessage);

    List<Message> getMessagesByUserName(String userName);

    List<Message> getWallMessagesByUserName(String userName);

    void saveFollowing(Following following);
}
