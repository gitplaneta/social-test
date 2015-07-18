package eu.busz.codurance.persistence;

import eu.busz.codurance.model.Message;
import eu.busz.codurance.model.command.follow.Following;

import java.util.List;

public interface MessageRepository {
    void saveMessage(String userName, String textMessage);

    List<Message> getUserMessages(String userName);

    List<Message> getUserWallMessages(String userName);

    void saveFollowing(Following following);
}
