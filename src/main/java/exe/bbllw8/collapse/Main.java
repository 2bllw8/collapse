package exe.bbllw8.collapse;

import exe.bbllw8.collapse.ui.cli.CommandLineInterface;
import picocli.CommandLine;

public final class Main {

    public static void main(String[] args) {
        final int exitCode = new CommandLine(new CommandLineInterface()).execute(args);
        System.exit(exitCode);
    }
}
