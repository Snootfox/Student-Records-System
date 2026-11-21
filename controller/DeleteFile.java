package main.Controller;

import main.Model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class DeleteFile {

    public static void deleteRow(DefaultTableModel model, int tableRow) {
    	// No row selected
        if (tableRow < 0) {
            JOptionPane.showMessageDialog(null, "Please select a row to delete.");
            return;
        }
        
        // Obtains the id of the selected row
        int id = (int) model.getValueAt(tableRow, 0);
        String name = (String) model.getValueAt(tableRow, 1);
        
        // Confirmation message for deletion
        int confirm = JOptionPane.showConfirmDialog(null,
                "Delete student record ID " + id + " (" + name + ")?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        
        // Loads information from text file to the List
        List<Student> students = DisplayFile.loadStudents();
        boolean removed = students.removeIf(s -> s.getID() == id);

        if (!removed) {
            JOptionPane.showMessageDialog(null, "Record not found in file.");
            return;
        }
        
        // Rewrites and Displays the new List
        EditFile.rewrite(students); 
        DisplayFile.refreshTable(model);
        JOptionPane.showMessageDialog(null, "Student deleted successfully.");
    }
}