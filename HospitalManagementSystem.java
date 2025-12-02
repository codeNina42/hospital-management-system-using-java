import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

// MAIN CLASS
public class HospitalManagementSystem extends JFrame {

    // In-memory "database"
    private ArrayList<Patient> patients = new ArrayList<>();
    private ArrayList<Doctor> doctors = new ArrayList<>();
    private ArrayList<Appointment> appointments = new ArrayList<>();

    // Patient tab components
    private JTable patientTable;
    private DefaultTableModel patientTableModel;
    private JTextField txtPatientName, txtPatientAge, txtPatientPhone;
    private JTextArea txtPatientAddress;
    private JComboBox<String> comboPatientGender;

    // Doctor tab components
    private JTable doctorTable;
    private DefaultTableModel doctorTableModel;
    private JTextField txtDoctorName, txtDoctorSpec, txtDoctorPhone;

    // Appointment tab components
    private JTable appointmentTable;
    private DefaultTableModel appointmentTableModel;
    private JComboBox<Patient> comboApptPatient;
    private JComboBox<Doctor> comboApptDoctor;
    private JTextField txtApptDate, txtApptTime;
    private JTextArea txtApptReason;

    public HospitalManagementSystem() {
        setTitle("Hospital Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null);

        // Initialize shared combo boxes for appointments
        comboApptPatient = new JComboBox<>();
        comboApptDoctor = new JComboBox<>();

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Patients", createPatientPanel());
        tabs.addTab("Doctors", createDoctorPanel());
        tabs.addTab("Appointments", createAppointmentPanel());

        add(tabs);
    }

    // ===================== PATIENT PANEL =====================
    private JPanel createPatientPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Patient"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtPatientName = new JTextField(20);
        txtPatientAge = new JTextField(5);
        txtPatientPhone = new JTextField(15);
        txtPatientAddress = new JTextArea(3, 20);
        txtPatientAddress.setLineWrap(true);
        txtPatientAddress.setWrapStyleWord(true);
        comboPatientGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtPatientName, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtPatientAge, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        formPanel.add(comboPatientGender, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtPatientPhone, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(txtPatientAddress), gbc);

        row++;
        JButton btnAdd = new JButton("Add Patient");
        JButton btnClear = new JButton("Clear");
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.add(btnAdd);
        btnPanel.add(btnClear);

        gbc.gridx = 1; gbc.gridy = row;
        formPanel.add(btnPanel, gbc);

        btnAdd.addActionListener(e -> addPatient());
        btnClear.addActionListener(e -> clearPatientForm());

        // Table
        patientTableModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Age", "Gender", "Phone", "Address"},
                0
        );
        patientTable = new JTable(patientTableModel);
        JScrollPane tableScroll = new JScrollPane(patientTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Patient List"));

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(tableScroll, BorderLayout.CENTER);

        return panel;
    }

    private void addPatient() {
        String name = txtPatientName.getText().trim();
        String ageStr = txtPatientAge.getText().trim();
        String gender = (String) comboPatientGender.getSelectedItem();
        String phone = txtPatientPhone.getText().trim();
        String address = txtPatientAddress.getText().trim();

        if (name.isEmpty() || ageStr.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Name, Age, and Phone are required.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Age must be a valid number.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Patient p = new Patient(name, age, gender, phone, address);
        patients.add(p);

        patientTableModel.addRow(new Object[]{
                p.getId(), p.getName(), p.getAge(), p.getGender(), p.getPhone(), p.getAddress()
        });

        // Also update appointment patient combo
        comboApptPatient.addItem(p);

        clearPatientForm();
    }

    private void clearPatientForm() {
        txtPatientName.setText("");
        txtPatientAge.setText("");
        txtPatientPhone.setText("");
        txtPatientAddress.setText("");
        comboPatientGender.setSelectedIndex(0);
    }

    // ===================== DOCTOR PANEL =====================
    private JPanel createDoctorPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Doctor"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtDoctorName = new JTextField(20);
        txtDoctorSpec = new JTextField(20);
        txtDoctorPhone = new JTextField(15);

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtDoctorName, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Specialization:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtDoctorSpec, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtDoctorPhone, gbc);

        row++;
        JButton btnAdd = new JButton("Add Doctor");
        JButton btnClear = new JButton("Clear");
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.add(btnAdd);
        btnPanel.add(btnClear);

        gbc.gridx = 1; gbc.gridy = row;
        formPanel.add(btnPanel, gbc);

        btnAdd.addActionListener(e -> addDoctor());
        btnClear.addActionListener(e -> clearDoctorForm());

        // Table
        doctorTableModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Specialization", "Phone"},
                0
        );
        doctorTable = new JTable(doctorTableModel);
        JScrollPane tableScroll = new JScrollPane(doctorTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Doctor List"));

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(tableScroll, BorderLayout.CENTER);

        return panel;
    }

    private void addDoctor() {
        String name = txtDoctorName.getText().trim();
        String spec = txtDoctorSpec.getText().trim();
        String phone = txtDoctorPhone.getText().trim();

        if (name.isEmpty() || spec.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Name, Specialization, and Phone are required.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Doctor d = new Doctor(name, spec, phone);
        doctors.add(d);

        doctorTableModel.addRow(new Object[]{
                d.getId(), d.getName(), d.getSpecialization(), d.getPhone()
        });

        // Also update appointment doctor combo
        comboApptDoctor.addItem(d);

        clearDoctorForm();
    }

    private void clearDoctorForm() {
        txtDoctorName.setText("");
        txtDoctorSpec.setText("");
        txtDoctorPhone.setText("");
    }

    // ===================== APPOINTMENT PANEL =====================
    private JPanel createAppointmentPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Create Appointment"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtApptDate = new JTextField(10); // e.g. 2025-12-02
        txtApptTime = new JTextField(10); // e.g. 10:30 AM
        txtApptReason = new JTextArea(3, 20);
        txtApptReason.setLineWrap(true);
        txtApptReason.setWrapStyleWord(true);

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Patient:"), gbc);
        gbc.gridx = 1;
        formPanel.add(comboApptPatient, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Doctor:"), gbc);
        gbc.gridx = 1;
        formPanel.add(comboApptDoctor, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Date (e.g. 2025-12-02):"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtApptDate, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Time (e.g. 10:30 AM):"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtApptTime, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Reason:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(txtApptReason), gbc);

        row++;
        JButton btnAdd = new JButton("Create Appointment");
        JButton btnClear = new JButton("Clear");
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.add(btnAdd);
        btnPanel.add(btnClear);

        gbc.gridx = 1; gbc.gridy = row;
        formPanel.add(btnPanel, gbc);

        btnAdd.addActionListener(e -> addAppointment());
        btnClear.addActionListener(e -> clearAppointmentForm());

        // Table
        appointmentTableModel = new DefaultTableModel(
                new String[]{"ID", "Patient", "Doctor", "Date", "Time", "Reason"},
                0
        );
        appointmentTable = new JTable(appointmentTableModel);
        JScrollPane tableScroll = new JScrollPane(appointmentTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Appointment List"));

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(tableScroll, BorderLayout.CENTER);

        return panel;
    }

    private void addAppointment() {
        Patient patient = (Patient) comboApptPatient.getSelectedItem();
        Doctor doctor = (Doctor) comboApptDoctor.getSelectedItem();
        String date = txtApptDate.getText().trim();
        String time = txtApptTime.getText().trim();
        String reason = txtApptReason.getText().trim();

        if (patient == null || doctor == null) {
            JOptionPane.showMessageDialog(this,
                    "At least one patient and one doctor must be added before creating appointments.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (date.isEmpty() || time.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Date and Time are required.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Appointment appt = new Appointment(patient, doctor, date, time, reason);
        appointments.add(appt);

        appointmentTableModel.addRow(new Object[]{
                appt.getId(),
                patient.getName(),
                doctor.getName(),
                appt.getDate(),
                appt.getTime(),
                appt.getReason()
        });

        clearAppointmentForm();
    }

    private void clearAppointmentForm() {
        txtApptDate.setText("");
        txtApptTime.setText("");
        txtApptReason.setText("");
    }

    // ===================== MAIN =====================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HospitalManagementSystem app = new HospitalManagementSystem();
            app.setVisible(true);
        });
    }
}

// ===================== MODEL CLASSES =====================

class Patient {
    private static int counter = 1;
    private int id;
    private String name;
    private int age;
    private String gender;
    private String phone;
    private String address;

    public Patient(String name, int age, String gender, String phone, String address) {
        this.id = counter++;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }

    @Override
    public String toString() {
        return id + " - " + name;
    }
}

class Doctor {
    private static int counter = 1;
    private int id;
    private String name;
    private String specialization;
    private String phone;

    public Doctor(String name, String specialization, String phone) {
        this.id = counter++;
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public String getPhone() { return phone; }

    @Override
    public String toString() {
        return id + " - " + name + " (" + specialization + ")";
    }
}

class Appointment {
    private static int counter = 1;
    private int id;
    private Patient patient;
    private Doctor doctor;
    private String date;
    private String time;
    private String reason;

    public Appointment(Patient patient, Doctor doctor, String date, String time, String reason) {
        this.id = counter++;
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.time = time;
        this.reason = reason;
    }

    public int getId() { return id; }
    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getReason() { return reason; }
}
