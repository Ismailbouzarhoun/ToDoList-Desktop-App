package todolist;

import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import javax.swing.JFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.swing.Timer;



/**
 *
 * @author Ismail
 */

public final class Home extends javax.swing.JFrame {
    private final Timer timer;
    private final Set<String> notifiedTasks = new HashSet<>();
    public Home() {
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fetchDataFromDatabase();
        timer = new Timer(6000, e ->searchFetchtoTable());
        timer.start();
        table.getColumnModel().getColumn(5).setCellRenderer(new StatusCellRenderer());
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        HomeLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        addButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        updateButton = new javax.swing.JButton();
        searchField = new javax.swing.JTextField();
        serachButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFocusCycleRoot(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        HomeLabel.setBackground(new java.awt.Color(255, 255, 255));
        HomeLabel.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        HomeLabel.setForeground(new java.awt.Color(255, 255, 255));
        HomeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        HomeLabel.setText("To Do List");
        getContentPane().add(HomeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, 610, 41));
        HomeLabel.getAccessibleContext().setAccessibleDescription("");

        jScrollPane1.setBackground(new java.awt.Color(0, 0, 0));

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Number", "Title", "details", "Date", "date Remainig", "status"
            }
        ));
        table.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane1.setViewportView(table);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 170, 950, 290));

        addButton.setBackground(new java.awt.Color(102, 153, 255));
        addButton.setForeground(new java.awt.Color(255, 255, 255));
        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        getContentPane().add(addButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 160, 50));

        deleteButton.setBackground(new java.awt.Color(102, 153, 255));
        deleteButton.setForeground(new java.awt.Color(255, 255, 255));
        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        getContentPane().add(deleteButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 60, 160, 50));

        updateButton.setBackground(new java.awt.Color(102, 153, 255));
        updateButton.setForeground(new java.awt.Color(255, 255, 255));
        updateButton.setText("Update");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });
        getContentPane().add(updateButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 160, 50));
        getContentPane().add(searchField, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 770, 30));

        serachButton.setText("Search");
        serachButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serachButtonActionPerformed(evt);
            }
        });
        getContentPane().add(serachButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 130, 160, 30));

        setSize(new java.awt.Dimension(976, 502));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        AddTask add = new AddTask(this);
        add.setVisible(true);
    }//GEN-LAST:event_addButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        int[] selectedRows = table.getSelectedRows();
        if(selectedRows.length == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select rows to delete.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);

        }else if (selectedRows.length > 0) {
            int option = javax.swing.JOptionPane.showConfirmDialog(this, "Are you sure you want to delete selected row(s)?", "Confirmation", javax.swing.JOptionPane.YES_NO_OPTION);

            if (option == javax.swing.JOptionPane.YES_OPTION) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();

                try {
                    Connection connection = ConnexionDB.getConnection();

                    for (int i = selectedRows.length - 1; i >= 0; i--) {
                        int modelRow = table.convertRowIndexToModel(selectedRows[i]);
                        String title = (String) model.getValueAt(modelRow, 1);

                        String deleteQuery = "DELETE FROM tache WHERE title = ?";
                        PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                        deleteStatement.setString(1, title);
                        deleteStatement.executeUpdate();
                        deleteStatement.close();

                        model.removeRow(modelRow);
                    }

                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "there is an error please try agian later.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
        int[] selectedRows = table.getSelectedRows();
        if(selectedRows.length == 0){
            javax.swing.JOptionPane.showMessageDialog(this, "Please select rows to update.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }else if(selectedRows.length == 1){
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            String title = (String) model.getValueAt(selectedRows[0], 1);
            String details = (String) model.getValueAt(selectedRows[0], 2);
            java.util.Date date = (java.util.Date) model.getValueAt(selectedRows[0], 3);
            String status = (String) model.getValueAt(selectedRows[0], 4);
            
            int id = getIdByTitle(title);
            
            UpdateTask update = new UpdateTask(this,title,details,date,status,id);
            update.setVisible(true);
        }else if(selectedRows.length > 1){
            javax.swing.JOptionPane.showMessageDialog(this, "Please select one row to update.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_updateButtonActionPerformed

    private void serachButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serachButtonActionPerformed
        searchFetchtoTable();
    }//GEN-LAST:event_serachButtonActionPerformed
    public void searchFetchtoTable(){
        String searchText = searchField.getText();

        if (!searchText.isEmpty()) {
            try {
                Connection connection = ConnexionDB.getConnection();
                String query = "SELECT ROW_NUMBER() OVER (ORDER BY FIELD(status, 'not do', 'in process', 'finished'), date ASC) AS number, title, details, date, status FROM tache WHERE title LIKE ? OR details LIKE ? OR date LIKE ? OR status LIKE ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                String likeParam = "%" + searchText + "%";
                preparedStatement.setString(1, likeParam);
                preparedStatement.setString(2, likeParam);
                preparedStatement.setString(3, likeParam);
                preparedStatement.setString(4, likeParam);

                ResultSet resultSet = preparedStatement.executeQuery();

                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setRowCount(0);

                Date currentDate = new Date();

                while (resultSet.next()) {
                    Date taskDate = resultSet.getDate("date");
                    long timeRemaining = taskDate.getTime() - currentDate.getTime();
                    String timeRemainingStr = formatTimeRemaining(timeRemaining);

                    if (timeRemaining > 0 && timeRemaining <= 24 * 60 * 60 * 1000) {
                        model.addRow(new Object[]{
                                resultSet.getInt("number"),
                                resultSet.getString("title"),
                                resultSet.getString("details"),
                                taskDate,
                                "<html><font color='yellow'>" + timeRemainingStr + "</font></html>",
                                resultSet.getString("status")
                        });

                        String taskTitle = resultSet.getString("title");
                        if (!notifiedTasks.contains(taskTitle)) {
                            displayNotification("Task Reminder", "Task \"" + taskTitle + "\" has less than 1 day remaining.");
                            notifiedTasks.add(taskTitle);
                        }
                    } else if (timeRemaining > 0) {
                        model.addRow(new Object[]{
                                resultSet.getInt("number"),
                                resultSet.getString("title"),
                                resultSet.getString("details"),
                                taskDate,
                                timeRemainingStr,
                                resultSet.getString("status")
                        });
                    } else {
                        model.addRow(new Object[]{
                                resultSet.getInt("number"),
                                resultSet.getString("title"),
                                resultSet.getString("details"),
                                taskDate,
                                "<html><font color='red'>" + "Deadline Passed" + "</font></html>",
                                resultSet.getString("status")
                        });
                    }
                }

                table.setModel(model);
                resultSet.close();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            fetchDataFromDatabase();
        }
    }
    public void fetchDataFromDatabase() {
        try {
            Connection connection = ConnexionDB.getConnection();
            String query = "SELECT ROW_NUMBER() OVER (ORDER BY FIELD(status, 'not do', 'in process', 'finished'), date ASC) AS number, title, details, date, status FROM tache ORDER BY FIELD(status, 'not do', 'in process', 'finished'), date ASC";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);

            Date currentDate = new Date();

            while (resultSet.next()) {
                Date taskDate = resultSet.getDate("date");
                long timeRemaining = taskDate.getTime() - currentDate.getTime();
                String timeRemainingStr = formatTimeRemaining(timeRemaining);

                if (timeRemaining > 0 && timeRemaining <= 24 * 60 * 60 * 1000) {
                    model.addRow(new Object[]{
                            resultSet.getInt("number"),
                            resultSet.getString("title"),
                            resultSet.getString("details"),
                            taskDate,
                            "<html><font color='yellow'>" + timeRemainingStr + "</font></html>",
                            resultSet.getString("status")
                    });

                    String taskTitle = resultSet.getString("title");
                    if (!notifiedTasks.contains(taskTitle)) {
                        displayNotification("Task Reminder", "Task \"" + taskTitle + "\" has less than 1 day remaining.");
                        notifiedTasks.add(taskTitle);
                    }
                } else if (timeRemaining > 0) {
                    model.addRow(new Object[]{
                            resultSet.getInt("number"),
                            resultSet.getString("title"),
                            resultSet.getString("details"),
                            taskDate,
                            timeRemainingStr,
                            resultSet.getString("status")
                    });
                } else {
                    model.addRow(new Object[]{
                            resultSet.getInt("number"),
                            resultSet.getString("title"),
                            resultSet.getString("details"),
                            taskDate,
                            "<html><font color='red'>" + "Deadline Passed" + "</font></html>",
                            resultSet.getString("status")
                    });
                }
            }

            table.setModel(model);
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String formatTimeRemaining(long timeRemaining) {
        if (timeRemaining <= 0) {
            return "";
        }

        long seconds = timeRemaining / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        return String.format("%02d:%02d:%02d:%02d", days, hours % 24, minutes % 60, seconds % 60);
    }
    
    
    public int getIdByTitle(String title) {
        int id = -1;
        try {
            Connection connection = ConnexionDB.getConnection();
            String query = "SELECT id FROM tache WHERE title=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
        }

        return id;
    }
    
    public void displayNotification(String title, String message) {
        if (SystemTray.isSupported()) {
            try {
                SystemTray tray = SystemTray.getSystemTray();
                Image image = Toolkit.getDefaultToolkit().createImage("/notification.png");
                TrayIcon trayIcon = new TrayIcon(image, "To-Do List Notification");
                trayIcon.setImageAutoSize(true);
                trayIcon.setToolTip("To-Do List Notifications");
                tray.add(trayIcon);
                trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("System tray not supported!");
        }
    }
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            FlatDarkLaf.setup();
            new Home().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel HomeLabel;
    private javax.swing.JButton addButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField searchField;
    private javax.swing.JButton serachButton;
    private javax.swing.JTable table;
    private javax.swing.JButton updateButton;
    // End of variables declaration//GEN-END:variables
}
