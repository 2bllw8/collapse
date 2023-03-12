package exe.bbllw8.collapse.ui.swing;

import javax.swing.*;

public final class CollapseGui {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new MainWindow()::init);
    }
}
