package exe.bbllw8.collapse.lang;

import com.hp.creals.CR;

final class CollapseLangVisitorImpl extends CollapseLangBaseVisitor<CollapseExpr> {

    @Override
    public CollapseExpr visitProgram(CollapseLangParser.ProgramContext ctx) {
        return new CollapseExpr.Program(ctx.entry().stream()
                .map(this::visit)
                .toList());
    }

    @Override
    public CollapseExpr visitEntryExpr(CollapseLangParser.EntryExprContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public CollapseExpr visitEntryAssign(CollapseLangParser.EntryAssignContext ctx) {
        return new CollapseExpr.Assignment(ctx.IDENTIFIER().getText(), visit(ctx.expr()), ctx.start.getLine());
    }

    @Override
    public CollapseExpr visitExprTerm(CollapseLangParser.ExprTermContext ctx) {
        return visit(ctx.term());
    }

    @Override
    public CollapseExpr visitExprOp(CollapseLangParser.ExprOpContext ctx) {
        final String opStr = ctx.OP_2_P0().getText();
        final CollapseOp2 op = switch (opStr) {
            case "+" -> CollapseOp2.ADD;
            case "-" -> CollapseOp2.SUB;
            default -> throw new IllegalArgumentException("Invalid operation: " + opStr);
        };
        return new CollapseExpr.Op2(op, visit(ctx.expr()), visit(ctx.term()), ctx.start.getLine());
    }

    @Override
    public CollapseExpr visitTermFactor(CollapseLangParser.TermFactorContext ctx) {
        return visit(ctx.factor());
    }

    @Override
    public CollapseExpr visitTermOp(CollapseLangParser.TermOpContext ctx) {
        final String opStr = ctx.OP_2_P1().getText();
        final CollapseOp2 op = switch (opStr) {
            case "*" -> CollapseOp2.MUL;
            case "/" -> CollapseOp2.DIV;
            case "^" -> CollapseOp2.POW;
            case "%" -> CollapseOp2.MOD;
            default -> throw new IllegalArgumentException("Invalid operation: " + opStr);
        };
        return new CollapseExpr.Op2(op, visit(ctx.term()), visit(ctx.factor()), ctx.start.getLine());
    }

    @Override
    public CollapseExpr visitFactorParen(CollapseLangParser.FactorParenContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public CollapseExpr visitValOp1(CollapseLangParser.ValOp1Context ctx) {
        final String opStr = ctx.OP_1().getText();
        final CollapseOp1 op = switch (opStr) {
            case "sin" -> CollapseOp1.SIN;
            case "cos" -> CollapseOp1.COS;
            case "tan" -> CollapseOp1.TAN;
            case "asin" -> CollapseOp1.ASIN;
            case "acos" -> CollapseOp1.ACOS;
            case "atan" -> CollapseOp1.ATAN;
            case "sqrt", "√" -> CollapseOp1.SQRT;
            case "ln" -> CollapseOp1.LN;
            case "exp" -> CollapseOp1.EXP;
            default -> throw new IllegalArgumentException("Invalid op: " + opStr);
        };
        return new CollapseExpr.Op1(op, visit(ctx.expr()), ctx.start.getLine());
    }

    @Override
    public CollapseExpr visitValNeg(CollapseLangParser.ValNegContext ctx) {
        return new CollapseExpr.Op1(CollapseOp1.NEG, visit(ctx.value()), ctx.start.getLine());
    }

    @Override
    public CollapseExpr visitValNumber(CollapseLangParser.ValNumberContext ctx) {
        final String valueStr = ctx.NUMBER().getText();
        final CR value;
        if (valueStr.startsWith("0x")) {
            value = CR.valueOf(valueStr.substring(2), 16);
        } else if (valueStr.startsWith("0b")) {
            value = CR.valueOf(valueStr.substring(2), 2);
        } else {
            value = CR.valueOf(valueStr, 10);
        }
        return new CollapseExpr.Literal(value, ctx.start.getLine());
    }

    @Override
    public CollapseExpr visitValConstant(CollapseLangParser.ValConstantContext ctx) {
        return new CollapseExpr.Constant(ctx.getText(), ctx.start.getLine());
    }

    @Override
    public CollapseExpr visitValIdentifier(CollapseLangParser.ValIdentifierContext ctx) {
        return new CollapseExpr.Identifier(ctx.getText(), ctx.start.getLine());
    }
}
