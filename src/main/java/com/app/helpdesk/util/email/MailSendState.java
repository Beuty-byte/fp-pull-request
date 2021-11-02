package com.app.helpdesk.util.email;

import com.app.helpdesk.model.enums.State;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

import static com.app.helpdesk.model.enums.State.*;

@Getter
@AllArgsConstructor
public
enum MailSendState {
    NEW_NEW(Map.of(NEW, NEW)),
    DRAFT_NEW(Map.of(DRAFT, NEW)),
    DECLINED_NEW(Map.of(DECLINED, NEW)),
    NEW_APPROVED(Map.of(NEW, APPROVED)),
    NEW_DECLINED(Map.of(NEW, DECLINED)),
    NEW_CANCELLED(Map.of(NEW, CANCELLED)),
    APPROVED_CANCELLED(Map.of(APPROVED, CANCELLED)),
    IN_PROGRESS_DONE(Map.of(IN_PROGRESS, DONE)),
    DONE_DONE(Map.of(DONE, DONE));

    private final Map<State, State> previousStateToCurrentStateMap;

    public boolean isPreviousStateToCurrentStateMapEqualTo(Map<State, State> other) {
        return previousStateToCurrentStateMap.equals(other);
    }
}
