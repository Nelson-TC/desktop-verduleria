package org.example.utils.renderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class CenterRenderer extends DefaultTableCellRenderer {
    public CenterRenderer() {
        setHorizontalAlignment(SwingConstants.CENTER);
    }
}