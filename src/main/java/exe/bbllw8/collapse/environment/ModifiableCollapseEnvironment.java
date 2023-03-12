package exe.bbllw8.collapse.environment;

import com.hp.creals.CR;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public final class ModifiableCollapseEnvironment implements CollapseEnvironment {
    private static final Map<String, CR> CONSTANTS = Map.of(
            "PI", CR.PI
    );

    private final Map<String, CR> values = new HashMap<>();

    public ModifiableCollapseEnvironment() {
        values.putAll(CONSTANTS);
    }

    @Override
    public Optional<CR> get(String identifier) {
        return values.containsKey(identifier)
                ? Optional.of(values.get(identifier))
                : Optional.empty();
    }

    public void set(String identifier, CR value) {
        values.put(identifier, value);
    }

    public boolean remove(String identifier) {
        if (values.containsKey(identifier)) {
            values.remove(identifier);
            return true;
        } else {
            return false;
        }
    }

    public void reset() {
        values.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof ModifiableCollapseEnvironment that) {
            return Objects.equals(values, that.values);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }

    @Override
    public String toString() {
        return values.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("\n"));
    }
}
