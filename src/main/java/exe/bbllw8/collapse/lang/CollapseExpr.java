package exe.bbllw8.collapse.lang;

import com.hp.creals.CR;
import java.util.List;

public sealed interface CollapseExpr {

    <T> T accept(Visitor<T> visit);

    int lineNumber();

    record Op1(CollapseOp1 operator, CollapseExpr arg, int lineNumber) implements CollapseExpr {

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visit(this);
        }
    }
    
    record Op2(CollapseOp2 operator, CollapseExpr a, CollapseExpr b, int lineNumber) implements CollapseExpr {

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visit(this);
        }
    }

    record Literal(CR value, int lineNumber) implements CollapseExpr {

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visit(this);
        }
    }

    record Identifier(String name, int lineNumber) implements CollapseExpr {

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visit(this);
        }
    }

    record Constant(String name, int lineNumber) implements CollapseExpr {

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visit(this);
        }
    }

    record Assignment(String name, CollapseExpr expr, int lineNumber) implements CollapseExpr {

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visit(this);
        }
    }

    record Program(List<CollapseExpr> expressions) implements CollapseExpr {
        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visit(this);
        }

        @Override
        public int lineNumber() {
            return -1;
        }
    }

    abstract class Visitor<T> {
        public abstract T visit(Op1 op1);

        public abstract T visit(Op2 op2);

        public abstract T visit(Literal literal);

        public abstract T visit(Identifier identifier);

        public abstract T visit(Constant constant);

        public abstract T visit(Assignment assignment);

        public abstract T visit(Program program);
    }
}
