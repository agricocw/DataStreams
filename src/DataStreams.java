import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.*;
import java.util.stream.*;

public class DataStreams extends JFrame {
    private JTextArea originalTextArea, filteredTextArea;
    private JTextField searchField;
    private JButton loadButton, searchButton, quitButton;
    private Path filePath;

    public DataStreams() {
        setTitle("Data Streams GUI");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        originalTextArea = new JTextArea();
        originalTextArea.setEditable(false);
        JScrollPane originalScrollPane = new JScrollPane(originalTextArea);

        filteredTextArea = new JTextArea();
        filteredTextArea.setEditable(false);
        JScrollPane filteredScrollPane = new JScrollPane(filteredTextArea);

        JPanel topPanel = new JPanel();
        searchField = new JTextField(20);
        loadButton = new JButton("Load File");
        searchButton = new JButton("Search");
        quitButton = new JButton("Quit");
        topPanel.add(new JLabel("Search String:"));
        topPanel.add(searchField);
        topPanel.add(loadButton);
        topPanel.add(searchButton);
        topPanel.add(quitButton);

        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(DataStreams.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    filePath = fileChooser.getSelectedFile().toPath();
                    try {
                        String fileContent = new String(Files.readAllBytes(filePath));
                        originalTextArea.setText(fileContent);
                        filteredTextArea.setText("");
                        searchField.setEnabled(true);
                        searchButton.setEnabled(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(DataStreams.this, "Error loading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchField.getText();
                try {
                    Stream<String> lines = Files.lines(filePath);
                    String filteredContent = lines
                            .filter(line -> line.contains(searchTerm))
                            .collect(Collectors.joining("\n"));
                    lines.close();
                    filteredTextArea.setText(filteredContent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(DataStreams.this, "Error searching file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        add(topPanel, BorderLayout.NORTH);
        add(originalScrollPane, BorderLayout.WEST);
        add(filteredScrollPane, BorderLayout.EAST);

        searchField.setEnabled(false);
        searchButton.setEnabled(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DataStreams gui = new DataStreams();
            gui.setVisible(true);
        });
    }
}
