package exe.bbllw8.collapse.ui.cli;

import exe.bbllw8.collapse.CollapseExecutor;
import exe.bbllw8.collapse.environment.ModifiableCollapseEnvironment;
import exe.bbllw8.collapse.lang.CollapseParser;
import picocli.CommandLine;

import java.util.Scanner;
import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "collapse",
        version = "1.0.0",
        mixinStandardHelpOptions = true
)
public class CommandLineInterface implements Callable<Integer> {

    private final CollapseExecutor core;
    private final CollapseParser parser;

    public CommandLineInterface() {
        core = new CollapseExecutor(new ModifiableCollapseEnvironment());
        parser = new CollapseParser();
    }

    @Override
    public Integer call() {
        final Scanner scanner = new Scanner(System.in);
        String input;
        while ((input = scanner.nextLine()) != null) {
            parser.parse(input)
                    .map(core::evaluate)
                    .forEach(value -> System.out.printf("> %s\n", value.toString()),
                            error -> System.err.printf("! Failed to execute: %s\n", error.getMessage()));
        }
        return 0;
    }
}
