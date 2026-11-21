package main.View;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

import main.Controller.AddToFile;
import main.Controller.DeleteFile;
import main.Controller.EditFile;
import main.Controller.RankingController;
import main.Controller.RecordController;

public class view extends JFrame{
	
	private final JFrame mainFrame = new JFrame("Student Record Management System");
	
	public view() {
        mainFrame.setTitle("Student Records System");
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1050, 700);
        mainFrame.setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Student Records", recordsPanel());
        tabbedPane.addTab("Rankings", rankingsPanel());
        tabbedPane.addTab("Scholarship Eligibility", eligibilityPanel());
        
        mainFrame.add(tabbedPane);
        mainFrame.setVisible(true);
    }

	private JPanel recordsPanel() {
		JPanel main = new JPanel(new BorderLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10,5,5,10);
		
		JPanel functions = new JPanel(new GridBagLayout());
		functions.setPreferredSize(new Dimension(200,0));
		
		JLabel title = new JLabel("Settings and Information");
		functions.add(title, gbc);
		
		JLabel searchText = new JLabel("Search by ID:");
		gbc.gridy = 1;
		functions.add(searchText, gbc);
		
		JTextField searchField = new JTextField(50);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		searchField.setForeground(Color.GRAY);
		searchField.setText("Enter ID...");
		functions.add(searchField, gbc);
		
		JLabel filterText = new JLabel("Filter by:");
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 1.0;
		functions.add(filterText, gbc);
		
		JComboBox<String> programBox = new JComboBox<>(new String[]{
            "All Programs", "BSIT", "BSCS", "BSIS", "BSECE", "BSCE"
        });
		programBox.setToolTipText("Program Filter");
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		functions.add(programBox, gbc);
		
		JPanel minMaxPanel = new JPanel(new GridLayout(1,2,5,0));
		JTextField minimum = new JTextField(5);
		JTextField maximum = new JTextField(5);
		minimum.setToolTipText("Min GWA");
		maximum.setToolTipText("Max GWA");
		minMaxPanel.add(minimum);
		minMaxPanel.add(maximum);
		gbc.gridy = 5;
		functions.add(minMaxPanel, gbc);
		
		JComboBox<String> rangeBox = new JComboBox<>(new String[] {
			"Grade Range", "1.00 - 1.50", "1.50 - 2.00", "2.00 - 2.50", "2.50 - 3.00", "3.00 - 3.50", "3.50 - 4.00"
		});
		rangeBox.setToolTipText("Grade Range Filter");
		gbc.gridy = 6;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		functions.add(rangeBox, gbc);
		
		JLabel sortText = new JLabel("Sort by:");
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.weightx = 1.0;
		functions.add(sortText, gbc);
		
		// UPDATED: Removed Program checkbox and changed grid to 2 rows
		JPanel sortBox = new JPanel(new GridLayout(2, 1, 10, 0));
		JCheckBox gwa = new JCheckBox("GWA");
		JCheckBox name = new JCheckBox("Name");
		
		ButtonGroup sortGroup = new ButtonGroup();
		sortGroup.add(gwa);
		sortGroup.add(name);
		
		sortBox.add(gwa);
		sortBox.add(name);
		gbc.gridy = 8;
		functions.add(sortBox, gbc);
		
		JButton addRecordBtn = new JButton("Add Record");
		gbc.gridy = 9;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		functions.add(addRecordBtn, gbc);
		
		JButton editRecordBtn = new JButton("Edit Record");
		gbc.gridy = 10;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		functions.add(editRecordBtn, gbc);
		
		JButton deleteRecordBtn = new JButton("Delete Record");
		gbc.gridy = 11;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		functions.add(deleteRecordBtn, gbc);
		
		String[] columns = {"Student ID", "Name", "Year Level", "Program", "GWA", "Grade Range"};
		DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(model);
        JTableHeader header = table.getTableHeader();
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
		JScrollPane scrollPane = new JScrollPane(table);
		
        // Obtains the state of ALL filters and sorts
        Runnable refreshData = () -> {
            String query = searchField.getText().equals("Enter ID...") ? "" : searchField.getText();
            String selectedProgram = (String) programBox.getSelectedItem();
            String minStr = minimum.getText();
            String maxStr = maximum.getText();
            String range = (String) rangeBox.getSelectedItem();
            
            String sortBy = null;
            if (gwa.isSelected()) sortBy = "GWA";
            else if (name.isSelected()) sortBy = "Name";

            RecordController.filterAndDisplay(model, query, selectedProgram, minStr, maxStr, range, sortBy);
        };

		// CRUD Actions 
		addRecordBtn.addActionListener(e -> {
            AddToFile.showInputWindow(model);
            refreshData.run();
        });
        editRecordBtn.addActionListener(e -> {
            EditFile.showEditWindow(model, table.getSelectedRow());
            refreshData.run();
        });
        deleteRecordBtn.addActionListener(e -> {
            DeleteFile.deleteRow(model, table.getSelectedRow());
            refreshData.run();
        });

        // Search, Filter & Sort Actions
        searchField.addActionListener(e -> refreshData.run());
        programBox.addActionListener(e -> refreshData.run());
        gwa.addActionListener(e -> refreshData.run());
        name.addActionListener(e -> refreshData.run());
        
        // Min/Max and Range triggers
        minimum.addActionListener(e -> refreshData.run());
        maximum.addActionListener(e -> refreshData.run());
        rangeBox.addActionListener(e -> refreshData.run());

        // Handle Placeholder behavior for the Search Field
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchField.getText().equals("Enter ID...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(Color.GRAY);
                    searchField.setText("Enter ID...");
                    refreshData.run();
                }
            }
        });

        // Initial load
        RecordController.filterAndDisplay(model, "", "All Programs", "", "", "Grade Range", null);
		
		main.add(scrollPane, BorderLayout.CENTER);
		main.add(functions, BorderLayout.WEST);
		return main;
	}
	
    private JPanel rankingsPanel() {
        JPanel main = new JPanel(new BorderLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 5, 10);

        JPanel functions = new JPanel(new GridBagLayout());
        functions.setPreferredSize(new Dimension(200, 0));

        JLabel title = new JLabel("Settings and Information");
        functions.add(title, gbc);

        JLabel filterText = new JLabel("Filter by:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        functions.add(filterText, gbc);

        JComboBox<String> programBox = new JComboBox<>(new String[]{"All Programs", "BSIT", "BSCS", "BSIS", "BSECE", "BSCE"});
        programBox.setToolTipText("Program");
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        functions.add(programBox, gbc);

        JPanel minMaxPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        JTextField minimum = new JTextField(5);
        JTextField maximum = new JTextField(5);
        minMaxPanel.add(minimum);
        minMaxPanel.add(maximum);
        gbc.gridy = 3;
        functions.add(minMaxPanel, gbc);

        JLabel orderText = new JLabel("Order by:");
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        functions.add(orderText, gbc);

        JPanel orderBox = new JPanel(new GridLayout(2, 1, 10, 0));
        JCheckBox gwaHigh = new JCheckBox("Highest GWA First", true);
        JCheckBox gwaLow = new JCheckBox("Lowest GWA First");
       
        ButtonGroup orderGroup = new ButtonGroup();
        orderGroup.add(gwaHigh);
        orderGroup.add(gwaLow);
        
        orderBox.add(gwaHigh);
        orderBox.add(gwaLow);
        gbc.gridy = 5;
        functions.add(orderBox, gbc);

        JButton applyBtn = new JButton("Apply Filter");
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        functions.add(applyBtn, gbc);

        JButton resetBtn = new JButton("Reset");
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        functions.add(resetBtn, gbc);

        JButton exportBtn = new JButton("Export List");
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        functions.add(exportBtn, gbc);

        String[] columns = {"Rank", "Student ID", "Name", "Program", "GWA"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(model);
        JTableHeader header = table.getTableHeader();
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        JScrollPane scrollPane = new JScrollPane(table);

        RankingController.populateRankings(model, true, "All Programs", "", "");

        applyBtn.addActionListener(e -> {
            boolean highestFirst = gwaHigh.isSelected();
            String program = (String) programBox.getSelectedItem();
            String min = minimum.getText();
            String max = maximum.getText();
            
            RankingController.populateRankings(model, highestFirst, program, min, max);
        });

        resetBtn.addActionListener(e -> {
            programBox.setSelectedIndex(0);
            minimum.setText("");
            maximum.setText("");
            gwaHigh.setSelected(true); // Highest First
            RankingController.populateRankings(model, true, "All Programs", "", "");
        });

        main.add(scrollPane, BorderLayout.CENTER);
        main.add(functions, BorderLayout.WEST);
        return main;
    }
	
	
	private JPanel eligibilityPanel() {
		JPanel main = new JPanel(new BorderLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10,5,5,10);
		
		JPanel functions = new JPanel(new GridBagLayout());
		functions.setPreferredSize(new Dimension(200,0));
		
		JLabel scholarText = new JLabel("Scholarship type:");
		functions.add(scholarText, gbc);
		
		JComboBox<String> scholarBox = new JComboBox<>();
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		functions.add(scholarBox, gbc);
		
		JLabel maxGWAText = new JLabel("Maximum GWA:");
		gbc.gridy = 3;
		functions.add(maxGWAText, gbc);
		
		JComboBox<String> maxGWAbox = new JComboBox<>();
		gbc.gridy = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		functions.add(maxGWAbox, gbc);
		
		JLabel programText = new JLabel("Program:");
		gbc.gridy = 5;
		functions.add(programText, gbc);
		
		JComboBox<String> programBox = new JComboBox<>();
		gbc.gridy = 6;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		functions.add(programBox, gbc);
		
		JButton checkBtn = new JButton("Check Eligibility");
		gbc.gridy = 7;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		functions.add(checkBtn, gbc);
		
		JButton printBtn = new JButton("Print/Export");
		gbc.gridy = 8;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		functions.add(printBtn, gbc);
		
		
		String[] columns = {"Student ID", "Name", "Program", "GWA", "Eligibility"};
		DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JTableHeader header = table.getTableHeader();
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
		JScrollPane scrollPane = new JScrollPane(table);
		
		main.add(scrollPane, BorderLayout.CENTER);
		main.add(functions, BorderLayout.WEST);
		return main;
	}
}
