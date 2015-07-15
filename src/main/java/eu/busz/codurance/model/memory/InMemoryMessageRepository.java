package eu.busz.codurance.model.memory;

import com.google.inject.Singleton;
import eu.busz.codurance.model.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class InMemoryMessageRepository {

    private final Map<String, List<Message>> repository = new HashMap<>();

    public void saveMessage(String userName, String textMessage) {
        List<Message> messages = new ArrayList<>();
        if (repository.containsKey(userName)) {
            messages = repository.get(userName);
        }

        messages.add(Message.builder()
                .text(textMessage)
                .userName(userName)
                .build());
        repository.put(userName, messages);
    }
}
