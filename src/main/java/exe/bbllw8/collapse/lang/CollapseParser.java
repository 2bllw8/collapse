package exe.bbllw8.collapse.lang;

import exe.bbllw8.either.Try;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class CollapseParser {

    private final CollapseLangVisitor<CollapseExpr> visitor;

    public CollapseParser() {
        this.visitor = new CollapseLangVisitorImpl();
    }

    public Try<CollapseExpr.Program> parse(String program) {
        return Try.from(() -> visitor.visit(new CollapseLangParser(new CommonTokenStream(
                        new CollapseLangLexer(CharStreams.fromString(program)))).program()))
                .filter(CollapseExpr.Program.class::isInstance)
                .map(CollapseExpr.Program.class::cast);
    }
}
