package context;

import java.util.HashMap;
import java.util.Map;

/**
 * Scenario-scoped key–value store for passing data between steps.
 */
public class ScenarioContext {


    private final Map<String, Object> contextMap = new HashMap<>();

    /** Save a value under a key for later use in the same scenario. */
    public void set(String key, Object value) {
        contextMap.put(key, value);
    }

    /** Get a value by key, cast to the requested type. */
    public <T> T get(String key, Class<T> type) {
        return type.cast(contextMap.get(key));
    }


}
