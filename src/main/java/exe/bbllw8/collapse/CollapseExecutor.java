package exe.bbllw8.collapse;

import com.hp.creals.CR;
import com.hp.creals.UnaryCRFunction;
import exe.bbllw8.collapse.environment.ModifiableCollapseEnvironment;
import exe.bbllw8.collapse.lang.CollapseExpr;
import exe.bbllw8.either.Failure;
import exe.bbllw8.either.Try;

import java.util.List;
import java.util.stream.Collectors;

public final class CollapseExecutor {

    private final CoreVisitor visitor;

    public CollapseExecutor(ModifiableCollapseEnvironment environment) {
        this.visitor = new CoreVisitor(environment);
    }

    public List<Try<CR>> evaluate(List<CollapseExpr> program) {
        visitor.environment.reset();
        return program.stream()
                .map(this::evaluate)
                .collect(Collectors.toList());
    }

    public Try<CR> evaluate(CollapseExpr expr) {
        return Try.flatten(Try.from(() -> expr.accept(visitor)));
    }

    private static final class CoreVisitor extends CollapseExpr.Visitor<Try<CR>> {
        private final ModifiableCollapseEnvironment environment;

        public CoreVisitor(ModifiableCollapseEnvironment environment) {
            this.environment = environment;
        }

        @Override
        public Try<CR> visit(CollapseExpr.Op1 op1) {
            return op1.arg().accept(this)
                    .map(arg -> (switch (op1.operator()) {
                        case NEG -> UnaryCRFunction.negateFunction;
                        case SIN -> UnaryCRFunction.sinFunction;
                        case COS -> UnaryCRFunction.cosFunction;
                        case TAN -> UnaryCRFunction.tanFunction;
                        case ASIN -> UnaryCRFunction.asinFunction;
                        case ACOS -> UnaryCRFunction.acosFunction;
                        case ATAN -> UnaryCRFunction.atanFunction;
                        case SQRT -> UnaryCRFunction.sqrtFunction;
                        case LN -> UnaryCRFunction.lnFunction;
                        case EXP -> UnaryCRFunction.expFunction;
                    }).execute(arg));
        }

        @Override
        public Try<CR> visit(CollapseExpr.Op2 op2) {
            return op2.a().accept(this)
                    .flatMap(a -> op2.b().accept(this)
                            .map(b -> switch (op2.operator()) {
                                case ADD -> a.add(b);
                                case SUB -> a.subtract(b);
                                case MUL -> a.multiply(b);
                                case DIV -> a.divide(b);
                                case POW, MOD -> throw new UnsupportedOperationException("TODO");
                            }));
        }

        @Override
        public Try<CR> visit(CollapseExpr.Literal literal) {
            return Try.from(literal::value);
        }

        @Override
        public Try<CR> visit(CollapseExpr.Identifier identifier) {
            return Try.from(() -> environment.get(identifier.name()).orElseThrow());
        }

        @Override
        public Try<CR> visit(CollapseExpr.Constant constant) {
            return Try.from(() -> environment.get(constant.name()).orElseThrow());
        }

        @Override
        public Try<CR> visit(CollapseExpr.Assignment assignment) {
            return assignment.expr().accept(this)
                    .map(value -> {
                        environment.set(assignment.name(), value);
                        return value;
                    });
        }

        @Override
        public Try<CR> visit(CollapseExpr.Program program) {
            return new Failure<>(new UnsupportedOperationException("Do not invoke directly on Program instances"));
        }
    }
}
