package main.Controller;

import main.Model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class AddToFile {

    static void save(Student student) {
        try (FileWriter writer = new FileWriter(DisplayFile.FILE_NAME, true)) {
            writer.write(student.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showInputWindow(DefaultTableModel model) {
        JDialog dialog = new JDialog((Frame) null, "Add Student Record", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(380, 340);
        dialog.setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idField = new JTextField(15);
        JTextField nameField = new JTextField(15);
        JComboBox<String> yearBox = new JComboBox<>(new String[]{
                "1st Year", "2nd Year", "3rd Year", "4th Year"
        });
        JComboBox<String> programBox = new JComboBox<>(new String[]{
                "BSIT", "BSCS", "BSIS", "BSECE", "BSCE"
        });
        JTextField gwaField = new JTextField(15);

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
                int id = Integer.parseInt(idField.getText().trim());
                String name = nameField.getText().trim();
                String year = (String) yearBox.getSelectedItem();
                String program = (String) programBox.getSelectedItem();
                double gwa = Double.parseDouble(gwaField.getText().trim());
                
                // Input validation
                if (name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty.");
                if (gwa < 1.00 || gwa > 4.00)
                    throw new IllegalArgumentException("GWA must be between 1.00 and 4.00.");
                
                // Loops through the content and checks for existing ID
                List<Student> existing = DisplayFile.loadStudents();
                for (Student s : existing) {
                    if (s.getID() == id)
                        throw new IllegalArgumentException("Student ID already exists.");
                }
                
                // Assigns the information into the model
                Student student = new Student();
                student.setID(id);
                student.setName(name);
                student.setYear(year);
                student.setProgram(program);
                student.setGrade(gwa);
                
                // Writes to file
                save(student);
                DisplayFile.refreshTable(model);
                dialog.dispose();
                JOptionPane.showMessageDialog(null, "Student added successfully.");
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