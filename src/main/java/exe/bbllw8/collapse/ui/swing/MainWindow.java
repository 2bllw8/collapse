package exe.bbllw8.collapse.ui.swing;

import com.hp.creals.CR;
import exe.bbllw8.collapse.CollapseExecutor;
import exe.bbllw8.collapse.environment.ModifiableCollapseEnvironment;
import exe.bbllw8.collapse.lang.CollapseExpr;
import exe.bbllw8.collapse.lang.CollapseParser;
import exe.bbllw8.either.Try;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

public class MainWindow extends JFrame {
    private static final String MAIN_TITLE = "Collapse";

    private final CollapseExecutor core;
    private final CollapseParser parser;

    public MainWindow() {
        super();
        core = new CollapseExecutor(new ModifiableCollapseEnvironment());
        parser = new CollapseParser();
        initUi();
    }

    public void init() {
        pack();
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initUi() {
        setMinimumSize(new Dimension(200, 300));
        final JPanel mainPanel = new JPanel(new BorderLayout());

        final RSyntaxTextArea viewArea = new RSyntaxTextArea(20, 20);
        viewArea.setEditable(false);
        viewArea.setHighlightCurrentLine(false);

        final RSyntaxTextArea textArea = new RSyntaxTextArea(20, 80);
        textArea.getDocument().addDocumentListener(new TextDocumentListener(core, parser, viewArea::setText));

        final RTextScrollPane viewAreaScrollPane = new RTextScrollPane(viewArea);
        mainPanel.add(viewAreaScrollPane, BorderLayout.EAST);

        final RTextScrollPane textAreaScrollPane = new RTextScrollPane(textArea);
        mainPanel.add(textAreaScrollPane, BorderLayout.WEST);
        setContentPane(mainPanel);
        setTitle(MAIN_TITLE);
    }

    private static class TextDocumentListener implements DocumentListener {
        private final CollapseExecutor core;
        private final CollapseParser parser;
        private final Consumer<String> setOutputs;
        private String documentText = "";

        TextDocumentListener(CollapseExecutor core,
                             CollapseParser parser,
                             Consumer<String> setOutputs) {
            this.core = core;
            this.parser = parser;
            this.setOutputs = setOutputs;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            try {
                final String updatedDocumentText = documentText.substring(0, e.getOffset())
                        + e.getDocument().getText(e.getOffset(), e.getLength())
                        + documentText.substring(e.getOffset());
                updateProgram(updatedDocumentText);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            final String updatedDocumentText = documentText.substring(0, e.getOffset())
                    + documentText.substring(e.getOffset() + e.getLength());
            updateProgram(updatedDocumentText);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }

        private void updateProgram(String newText) {
            parser.parse(newText).forEach(program -> {
                final List<CollapseExpr> expressions = program.expressions();
                final List<Try<CR>> results = core.evaluate(expressions);

                final StringBuilder sb = new StringBuilder();
                int lastLine = 1;
                for (int i = 0; i < results.size(); i++) {
                    final CollapseExpr expr = expressions.get(i);
                    if (expr == null) {
                        continue;
                    }
                    final int line = expr.lineNumber();
                    sb.append("\n".repeat(line - lastLine))
                            .append(results.get(i).fold(Throwable::getMessage, CR::toString));
                    lastLine = line;
                }
                setOutputs.accept(sb.toString());
            });
            documentText = newText;
        }
    }
}
