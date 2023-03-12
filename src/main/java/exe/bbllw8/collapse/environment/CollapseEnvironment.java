package exe.bbllw8.collapse.environment;

import com.hp.creals.CR;

import java.util.Optional;

public interface CollapseEnvironment {

    Optional<CR> get(String identifier);
}
