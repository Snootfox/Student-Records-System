package main.Controller;

import main.Model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class EditFile {

    static void rewrite(List<Student> students) {
        try (FileWriter writer = new FileWriter(DisplayFile.FILE_NAME, false)) {
            for (Student s : students) writer.write(s.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showEditWindow(DefaultTableModel model, int tableRow) {
    	// No row selected
        if (tableRow < 0) {
            JOptionPane.showMessageDialog(null, "Please select a row to edit.");
            return;
        }
        
        int oldID = (int) model.getValueAt(tableRow, 0);
        List<Student> students = DisplayFile.loadStudents();
        Student target = students.stream()
                .filter(s -> s.getID() == oldID)
                .findFirst()
                .orElse(null);
        if (target == null) {
            JOptionPane.showMessageDialog(null, "Record not found in file.");
            return;
        }

        JDialog dialog = new JDialog((Frame) null, "Edit Student Record", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(380, 340);
        dialog.setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idField = new JTextField(String.valueOf(target.getID()), 15);
        JTextField nameField = new JTextField(target.getName(), 15);
        JComboBox<String> yearBox = new JComboBox<>(new String[]{
                "1st Year", "2nd Year", "3rd Year", "4th Year"
        });
        yearBox.setSelectedItem(target.getYear());
        JComboBox<String> programBox = new JComboBox<>(new String[]{
                "BSIT", "BSCS", "BSIS", "BSECE", "BSCE"
        });
        programBox.setSelectedItem(target.getProgram());
        JTextField gwaField = new JTextField(String.valueOf(target.getGrade()), 15);

        int y = 0;
        gbc.gridx = 0; 
        gbc.gridy = y; 
        dialog.add(new JLabel("Student ID:"), gbc);
        
        gbc.gridx = 1; 
        dialog.add(idField, gbc);

        gbc.gridx = 0; 
        gbc.gridy = ++y; 
        dialog.add(new JLabel("Student Name:"), gbc);
        
        gbc.gridx = 1; 
        dialog.add(nameField, gbc);

        gbc.gridx = 0; 
        gbc.gridy = ++y; 
        dialog.add(new JLabel("Year Level:"), gbc);
        
        gbc.gridx = 1; 
        dialog.add(yearBox, gbc);

        gbc.gridx = 0; 
        gbc.gridy = ++y; 
        dialog.add(new JLabel("Program:"), gbc);
        
        gbc.gridx = 1;
        dialog.add(programBox, gbc);

        gbc.gridx = 0; 
        gbc.gridy = ++y; 
        dialog.add(new JLabel("GWA:"), gbc);
        
        gbc.gridx = 1; 
        dialog.add(gwaField, gbc);

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        JPanel btnPanel = new JPanel();
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        gbc.gridx = 0; gbc.gridy = ++y; gbc.gridwidth = 2;
        dialog.add(btnPanel, gbc);

        cancelBtn.addActionListener(e -> dialog.dispose());
        saveBtn.addActionListener(e -> {
            try {
            	// Obtains information from the fields
                int newID = Integer.parseInt(idField.getText().trim());
                String name = nameField.getText().trim();
                String year = (String) yearBox.getSelectedItem();
                String program = (String) programBox.getSelectedItem();
                double gwa = Double.parseDouble(gwaField.getText().trim());
                
                // Input validation
                if (name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty.");
                if (gwa < 1.00 || gwa > 4.00)
                    throw new IllegalArgumentException("GWA must be between 1.00 and 4.00.");
                
                // Loops through the content and checks for similar IDs
                for (Student s : students) {
                    if (s.getID() == newID && s != target)
                        throw new IllegalArgumentException("Another student already has this ID.");
                }
                
                // Sets the new information into the designated location
                target.setID(newID);
                target.setName(name);
                target.setYear(year);
                target.setProgram(program);
                target.setGrade(gwa);
                
                rewrite(students);
                DisplayFile.refreshTable(model);
                dialog.dispose();
                JOptionPane.showMessageDialog(null, "Student updated successfully.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                        "ID must be an integer and GWA must be a number.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog,
                        ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }
}