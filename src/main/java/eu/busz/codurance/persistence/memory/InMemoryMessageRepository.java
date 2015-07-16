package eu.busz.codurance.persistence.memory;

import com.google.inject.Singleton;
import eu.busz.codurance.model.Clock;
import eu.busz.codurance.model.Message;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

@Singleton
@RequiredArgsConstructor
public class InMemoryMessageRepository implements MessageRepository {

    private final Map<String, List<Message>> repository = new HashMap<>();
    private final Clock clock;

    @Override
    public void saveMessage(String userName, String textMessage) {
        List<Message> messages = new ArrayList<>();
        if (repository.containsKey(userName)) {
            messages = repository.get(userName);
        }

        messages.add(Message.builder()
                .text(textMessage)
                .userName(userName)
                .date(clock.getCurrentDateTime())
                .build());
        repository.put(userName, messages);
    }

    @Override
    public List<Message> getMessagesByUserName(String userName) {
        if (repository.get(userName) == null) {
            return emptyList();
        }
        return unmodifiableList(repository.get(userName));
    }
}
