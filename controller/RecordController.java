package main.Controller;

import main.Model.Student;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RecordController {

    public static void filterAndDisplay(DefaultTableModel model, String searchIdStr, String filterProgram, 
                                        String minGwaStr, String maxGwaStr, String rangeFilter, String sortBy) {
        
        List<Student> students = DisplayFile.loadStudents();
        double minGwa = -1, maxGwa = -1;
        try { if (!minGwaStr.isBlank()) minGwa = Double.parseDouble(minGwaStr.trim()); } catch (Exception ignored) {}
        try { if (!maxGwaStr.isBlank()) maxGwa = Double.parseDouble(maxGwaStr.trim()); } catch (Exception ignored) {}

        // Apply Filters
        List<Student> filteredList = new ArrayList<>();
        for (Student s : students) {
            if (filterProgram != null && !filterProgram.equals("All Programs") && !s.getProgram().equals(filterProgram)) continue;
            
            if (minGwa != -1 && s.getGrade() < minGwa) continue;
            if (maxGwa != -1 && s.getGrade() > maxGwa) continue;

            if (rangeFilter != null && !rangeFilter.isEmpty() && !rangeFilter.equals("Grade Range")) {
                double gwa = s.getGrade();
                boolean inRange = false;
                switch (rangeFilter) {
                    case "1.00 - 1.50": inRange = (gwa >= 1.00 && gwa <= 1.50); break;
                    case "1.50 - 2.00": inRange = (gwa > 1.50 && gwa <= 2.00); break;
                    case "2.00 - 2.50": inRange = (gwa > 2.00 && gwa <= 2.50); break;
                    case "2.50 - 3.00": inRange = (gwa > 2.50 && gwa <= 3.00); break;
                    case "3.00 - 3.50": inRange = (gwa > 3.00 && gwa <= 3.50); break;
                    case "3.50 - 4.00": inRange = (gwa > 3.50 && gwa <= 4.00); break;
                }
                if (!inRange) continue;
            }
            filteredList.add(s);
        }

        // Apply Binary Search (by ID)
        if (searchIdStr != null && !searchIdStr.trim().isEmpty() && !searchIdStr.equals("Enter ID...")) {
            try {
                int targetId = Integer.parseInt(searchIdStr.trim());
                filteredList.sort((s1, s2) -> Integer.compare(s1.getID(), s2.getID()));
                Student found = binarySearch(filteredList, targetId);
                
                List<Student> resultList = new ArrayList<>();
                if (found != null) resultList.add(found);
                
                DisplayFile.populateTable(model, resultList);
                return; 
            } catch (NumberFormatException e) {}
        }

        // Apply Sorting
        if (sortBy != null) {
            switch (sortBy) {
                case "GWA": 
                    filteredList.sort(Comparator.comparingDouble(Student::getGrade)); 
                    break;
                case "Name": 
                    filteredList.sort((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName())); 
                    break;
            }
        }

        // Display the final filtered & sorted list
        DisplayFile.populateTable(model, filteredList);
    }

    private static Student binarySearch(List<Student> sortedStudents, int targetId) {
        int left = 0, right = sortedStudents.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int midId = sortedStudents.get(mid).getID();
            if (midId == targetId) return sortedStudents.get(mid);
            if (midId < targetId) left = mid + 1;
            else right = mid - 1;
        }
        return null;
    }
    
    public static void resetTable(DefaultTableModel model) {
        DisplayFile.refreshTable(model);
    }
}