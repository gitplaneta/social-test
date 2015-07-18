package eu.busz.codurance.persistence;

import com.google.common.collect.ImmutableList;
import com.google.inject.Singleton;
import eu.busz.codurance.model.Clock;
import eu.busz.codurance.model.Message;
import eu.busz.codurance.model.command.follow.Following;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class InMemoryMessageRepository implements MessageRepository {

    private final Map<String, List<Message>> messageRepository = new HashMap<>();
    private final Map<String, Set<Following>> followingsRepository = new HashMap<>();
    private final Clock clock;

    @Override
    public void saveMessage(String userName, String textMessage) {
        List<Message> messages = messageRepository.getOrDefault(userName, new ArrayList<>());

        messages.add(Message.builder()
                .userName(userName)
                .text(textMessage)
                .date(clock.getCurrentDateTime())
                .build());
        messageRepository.put(userName, messages);
    }

    @Override
    public List<Message> getUserMessages(String userName) {
        if (messageRepository.get(userName) == null) {
            return emptyList();
        }
        return ImmutableList.copyOf(messageRepository.get(userName));
    }

    @Override
    public List<Message> getUserWallMessages(String userName) {
        List<Message> wallMessages = new ArrayList<>(getUserMessages(userName));
        Set<Following> userFollowings = followingsRepository.get(userName);

        if (userFollowings != null) {
            wallMessages.addAll(userFollowings.stream()
                    .map(following -> getUserMessages(following.getTargetUserNamed()))
                    .flatMap(Collection::stream)
                    .collect(toList()));
        }
        return wallMessages;
    }

    @Override
    public void saveFollowing(Following following) {
        Set<Following> userFollows = followingsRepository.getOrDefault(following.getSourceUserName(), new HashSet<>());
        userFollows.add(following);
        followingsRepository.put(following.getSourceUserName(), userFollows);
    }
}
