package main.Controller;
import main.Model.Student;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DisplayFile {

    public static final String FILE_NAME = "records.txt";
    
    // Returns a list after reading through the file
    public static List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        File f = new File(FILE_NAME);
        if (!f.exists()) return students;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                Student s = Student.fromLine(line);
                if (s != null) students.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }

    // Refreshes the table
    public static void refreshTable(DefaultTableModel model) {
        populateTable(model, loadStudents());
    }

    // Method to generate the content of the table with a List
    public static void populateTable(DefaultTableModel model, List<Student> students) {
        model.setRowCount(0);
        for (Student s : students) {
            model.addRow(new Object[]{
                    s.getID(),
                    s.getName(),
                    s.getYear(),
                    s.getProgram(),
                    String.format("%.2f", s.getGrade()),
                    s.getGradeRange()
            });
        }
    }
}