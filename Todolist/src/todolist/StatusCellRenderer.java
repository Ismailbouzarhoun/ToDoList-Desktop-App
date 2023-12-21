package todolist;


import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            String status = (String) value;

            // Color the status based on its value
            switch (status) {
                case "not do":
                    c.setForeground(Color.RED);
                    break;
                case "in process":
                    c.setForeground(Color.YELLOW);
                    break;
                case "finished":
                    c.setForeground(Color.GREEN);
                    break;
                default:
                    // Handle other cases if needed
                    c.setForeground(table.getForeground());
            }

            return c;
        }
    }