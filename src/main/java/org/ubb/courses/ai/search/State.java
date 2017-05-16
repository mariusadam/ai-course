package org.ubb.courses.ai.search;

import java.util.List;

/**
 * @author Marius Adam
 */
public interface State {
    Boolean isValid();

    Boolean isSolution(State initialState);

    List<State> getSuccessors();

    State getParent();

    default Integer priority() {
        return 0;
    }
}
