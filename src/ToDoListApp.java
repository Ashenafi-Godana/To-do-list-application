import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ToDoListApp extends JFrame {
    private ToDoList toDoList;
    private DefaultListModel<Task> taskListModel;
    private JList<Task> taskList;
    private JTextField titleField;
    private JTextField descriptionField;

    public ToDoListApp() {
        toDoList = new ToDoList();
        setupGUI();
        showWelcomeMessage();
    }

    private void setupGUI() {
        setTitle("To-Do List Application");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        taskList.setFont(new Font("Serif", Font.PLAIN, 18));
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setCellRenderer(new TaskRenderer());
        taskList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int index = taskList.locationToIndex(e.getPoint());
                if (index != -1) {
                    Task task = taskListModel.getElementAt(index);
                    task.markCompleted();
                    taskList.repaint();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(taskList);

        titleField = new JTextField(20);
        titleField.setFont(new Font("Serif", Font.PLAIN, 18));
        descriptionField = new JTextField(20);
        descriptionField.setFont(new Font("Serif", Font.PLAIN, 18));

        JButton addButton = new JButton("Add Task");
        addButton.setFont(new Font("Serif", Font.PLAIN, 18));
        addButton.addActionListener(e -> {
            String title = titleField.getText();
            String description = descriptionField.getText();
            if (!title.isEmpty() && !description.isEmpty()) {
                Task newTask = new Task(title, description);
                toDoList.addToDo(newTask);
                taskListModel.addElement(newTask);
                titleField.setText("");
                descriptionField.setText("");
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 1));
        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionField);
        inputPanel.add(addButton);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
    }

    private void showWelcomeMessage() {
        String message = "Welcome to the To-Do List Application!\n\n"
                + "How to use this app:\n"
                + "1. Enter the title and description of a new task in the provided fields.\n"
                + "2. Click 'Add Task' to add the task to your to-do list.\n"
                + "3. Click on a task in the list to mark it as completed.\n"
                + "4. Completed tasks will be displayed in gray.\n\n"
                + "Enjoy staying organized!";
        JOptionPane.showMessageDialog(this, message, "Welcome", JOptionPane.INFORMATION_MESSAGE);
    }

    private static class TaskRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (c instanceof JLabel && value instanceof Task) {
                Task task = (Task) value;
                JLabel label = (JLabel) c;
                label.setText("• " + task.toString());
                if (task.isCompleted()) {
                    label.setForeground(Color.GRAY);
                } else {
                    label.setForeground(Color.BLACK);
                }
            }
            return c;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ToDoListApp().setVisible(true));
    }
}
