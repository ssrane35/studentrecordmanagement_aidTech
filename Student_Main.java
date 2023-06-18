/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package leetcode;

/**
 *
 * @author abhijeet
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Student implements Serializable {
    private String name;
    private int rollNumber;
    private String address;
    private String phoneNumber;

    public Student(String name, int rollNumber, String address, String phoneNumber) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Roll Number: " + rollNumber + ", Address: " + address + ", Phone Number: " + phoneNumber;
    }
}

class StudentRecord {
    private List<Student> students;

    public StudentRecord() {
        students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(Student student) {
        students.remove(student);
    }

    public List<Student> getStudents() {
        return students;
    }

    public Student findStudentByRollNumber(int rollNumber) {
        for (Student student : students) {
            if (student.getRollNumber() == rollNumber) {
                return student;
            }
        }
        return null;
    }

    public List<Student> findStudentsByName(String name) {
        List<Student> result = new ArrayList<>();
        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(name)) {
                result.add(student);
            }
        }
        return result;
    }

    public void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName));
            outputStream.writeObject(students);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile(String fileName) {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName));
            students = (List<Student>) inputStream.readObject();
            inputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class StudentRecordManagementSystem {
    private StudentRecord studentRecord;
    private JFrame frame;
    private JTextField nameTextField, rollNumberTextField, addressTextField, phoneNumberTextField;
    private JTextArea outputTextArea;

    public StudentRecordManagementSystem() {
        studentRecord = new StudentRecord();
        studentRecord.loadFromFile("student_record.ser"); // Load records from file if it exists

        frame = new JFrame("Student Record Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createUI();

        frame.pack();
        frame.setVisible(true);
    }

    private void createUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        JLabel nameLabel = new JLabel("Name:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(nameLabel, constraints);

        nameTextField = new JTextField(20);
        constraints.gridx = 1;
        panel.add(nameTextField, constraints);

        JLabel rollNumberLabel = new JLabel("Roll Number:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(rollNumberLabel, constraints);

        rollNumberTextField = new JTextField(10);
        constraints.gridx = 1;
        panel.add(rollNumberTextField, constraints);

        JLabel addressLabel = new JLabel("Address:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(addressLabel, constraints);

        addressTextField = new JTextField(30);
        constraints.gridx = 1;
        panel.add(addressTextField, constraints);

        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(phoneNumberLabel, constraints);

        phoneNumberTextField = new JTextField(15);
        constraints.gridx = 1;
        panel.add(phoneNumberTextField, constraints);

        JButton addButton = new JButton("Add Student");
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });
        panel.add(addButton, constraints);

        JButton removeButton = new JButton("Remove Student");
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeStudent();
            }
        });
        panel.add(removeButton, constraints);

        JButton displayButton = new JButton("Display Student");
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 2;
        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayStudent();
            }
        });
        panel.add(displayButton, constraints);

        outputTextArea = new JTextArea(10, 40);
        outputTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        constraints.gridx = 0;
        constraints.gridy = 7;
        constraints.gridwidth = 2;
        panel.add(scrollPane, constraints);

        frame.getContentPane().add(panel);
    }

    private void addStudent() {
        String name = nameTextField.getText();
        int rollNumber = Integer.parseInt(rollNumberTextField.getText());
        String address = addressTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();

        Student student = new Student(name, rollNumber, address, phoneNumber);
        studentRecord.addStudent(student);
        outputTextArea.append("Added Student: " + student + "\n");

        clearFields();
    }

    private void removeStudent() {
        int rollNumber = Integer.parseInt(rollNumberTextField.getText());
        Student student = studentRecord.findStudentByRollNumber(rollNumber);

        if (student != null) {
            studentRecord.removeStudent(student);
            outputTextArea.append("Removed Student: " + student + "\n");
        } else {
            outputTextArea.append("Student with Roll Number " + rollNumber + " not found\n");
        }

        clearFields();
    }

    private void displayStudent() {
        String name = nameTextField.getText();
        if (name.isEmpty()) {
            int rollNumber = Integer.parseInt(rollNumberTextField.getText());
            Student student = studentRecord.findStudentByRollNumber(rollNumber);
            if (student != null) {
                outputTextArea.append("Found Student: " + student + "\n");
            } else {
                outputTextArea.append("Student with Roll Number " + rollNumber + " not found\n");
            }
        } else {
            List<Student> students = studentRecord.findStudentsByName(name);
            if (!students.isEmpty()) {
                outputTextArea.append("Found Students with Name " + name + ":\n");
                for (Student student : students) {
                    outputTextArea.append(student + "\n");
                }
            } else {
                outputTextArea.append("No students found with Name " + name + "\n");
            }
        }

        clearFields();
    }

    private void clearFields() {
        nameTextField.setText("");
        rollNumberTextField.setText("");
        addressTextField.setText("");
        phoneNumberTextField.setText("");
    }

    public void saveRecordsToFile() {
        studentRecord.saveToFile("student_record.ser");
    }
}

public class Student_Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new StudentRecordManagementSystem();
            }
        });
    }
}
