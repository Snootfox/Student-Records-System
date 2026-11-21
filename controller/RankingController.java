package main.Controller;

import main.Model.Student;
import javax.swing.table.DefaultTableModel;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class RankingController {
    public static void populateRankings(DefaultTableModel model, boolean highestFirst, 
                                        String filterProgram, String minGwaStr, String maxGwaStr) {
    	
        // Load info from text file to a List
        List<Student> students = DisplayFile.loadStudents();
        
        // Sorting comparator
        Comparator<Student> comparator;
        if (highestFirst) {
            comparator = Comparator.comparingDouble(Student::getGrade); // 1.00 - 4.00
        } else {
            comparator = (s1, s2) -> Double.compare(s2.getGrade(), s1.getGrade()); // 4.00 - 1.00
        }

        // Initialize Priority Queue
        PriorityQueue<Student> pq = new PriorityQueue<>(comparator);

       // Filter info and push to Queue
        double minGwa = -1, maxGwa = -1;
        try { if (!minGwaStr.isBlank()) minGwa = Double.parseDouble(minGwaStr.trim()); } catch (Exception ignored) {}
        try { if (!maxGwaStr.isBlank()) maxGwa = Double.parseDouble(maxGwaStr.trim()); } catch (Exception ignored) {}

        for (Student s : students) {
            // Apply Program Filter
            if (filterProgram != null && !filterProgram.isEmpty() && !filterProgram.equals("All Programs")) {
                if (!s.getProgram().equals(filterProgram)) continue;
            }
            // Apply GWA Range Filter
            if (minGwa != -1 && s.getGrade() < minGwa) continue;
            if (maxGwa != -1 && s.getGrade() > maxGwa) continue;

            pq.add(s);
        }

        // Clear table and queue
        model.setRowCount(0);
        int rank = 1;
        while (!pq.isEmpty()) {
            Student s = pq.poll(); // Retrieves and removes the head of the queue
            model.addRow(new Object[]{
                    rank++,
                    s.getID(),
                    s.getName(),
                    s.getProgram(),
                    String.format("%.2f", s.getGrade())
            });
        }
    }
}