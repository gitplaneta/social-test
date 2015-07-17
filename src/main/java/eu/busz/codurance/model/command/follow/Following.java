package eu.busz.codurance.model.command.follow;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class Following {

    private final String sourceUserName;
    private final String targetUserNamed;
}
