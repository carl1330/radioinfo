package view;
import model.HttpBadRequestException;
import com.formdev.flatlaf.FlatDarkLaf;
import controller.RadioInfoController;
import model.*;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * The main view class for the radio information application.
 * Implements the RadioViewModelObserver interface to receive data change notifications from the model.
 */
public class RadioInfoView implements RadioViewModelObserver {
    public RadioInfoController radioInfoController;
    private RadioInfoTableModel radioInfoTableModel;
    private Timer scheduleTimer;

    /**
     * Constructs the view and sets up the main UI components.
     *
     * @param radioInfoController The controller responsible for managing interactions between the view and the model.
     */
    public RadioInfoView(RadioInfoController radioInfoController) {
        this.radioInfoController = radioInfoController;
        SwingUtilities.invokeLater(() -> {
            FlatDarkLaf.setup();
            try {
                radioInfoController.fetchChannels();
                radioInfoController.addModelObserver(this);
                this.createAndShowMainFrame();
                radioInfoController.selectChannel(132);
            } catch (HttpBadRequestException | IOException | URISyntaxException | InterruptedException e) {
                JOptionPane.showMessageDialog(null, "Something went wrong while fetching channels", "Error", JOptionPane.ERROR_MESSAGE);
            }
            scheduleTimer = new Timer(1000 * 60 * 60, e -> new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    radioInfoController.updateCachedSchedules();
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        get();
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "An error occurred during schedule update", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }.execute());

            scheduleTimer.start();
        });
    }

    /**
     * Creates and displays the main application window.
     */
    private void createAndShowMainFrame() {
        JFrame mainFrame = new JFrame();
        JMenuBar menuBar = createJMenuBar();
        JScrollPane scrollPane = createScheduleTable();

        JPanel mainPanel = new JPanel(new BorderLayout());
        JComboBox<Object> channelJComboBox = createChannelDropdown();
        mainPanel.add(channelJComboBox, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainFrame.add(mainPanel);
        mainFrame.setSize(1000, 655);
        mainFrame.setMinimumSize(new Dimension(600, 400));
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setJMenuBar(menuBar);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    /**
     * Creates a scrollable table to display the schedule information.
     *
     * @return A JScrollPane containing the table.
     */
    private JScrollPane createScheduleTable() {
        radioInfoTableModel = new RadioInfoTableModel();
        JTable table = new JTable(radioInfoTableModel);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        new SwingWorker<Program, Integer>() {
                            @Override
                            protected void done() {
                                try {
                                    Font titleFont = new Font(Font.SANS_SERIF, Font.BOLD, 18);
                                    Font contentFont = new Font(Font.SANS_SERIF, Font.PLAIN, 12);

                                    Program program = get();
                                    JFrame programWindow = new JFrame();
                                    JPanel programPanel = new JPanel();
                                    programPanel.setLayout(new BorderLayout());

                                    JLabel title = new JLabel(program.title());
                                    title.setFont(titleFont);
                                    title.setAlignmentX(Component.CENTER_ALIGNMENT); // Center title horizontally

                                    JTextPane description = new JTextPane();

                                    description.setText(program.description());
                                    description.setFont(contentFont);
                                    StyledDocument doc = description.getStyledDocument();
                                    SimpleAttributeSet center = new SimpleAttributeSet();
                                    StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
                                    description.setOpaque(false);
                                    description.setFocusable(false);
                                    doc.setParagraphAttributes(0, doc.getLength(), center, false);

                                    JLabel icon = new JLabel(UIManager.getIcon("OptionPane.questionIcon"));
                                    if(!Objects.equals(program.icon(), "none")) {
                                        try {
                                            URL imageUrl = new URI(program.icon()).toURL();
                                            Image image = new ImageIcon(imageUrl).getImage().getScaledInstance(450,450, Image.SCALE_SMOOTH);
                                            icon = new JLabel(new ImageIcon(image));
                                        } catch (MalformedURLException | URISyntaxException ignored){}
                                    }
                                    icon.setAlignmentX(Component.CENTER_ALIGNMENT); // Center icon horizontally

                                    JPanel titleDesc = new JPanel();
                                    titleDesc.setLayout(new BoxLayout(titleDesc, BoxLayout.Y_AXIS));
                                    titleDesc.add(title);
                                    titleDesc.add(description);

                                    programPanel.add(titleDesc, BorderLayout.NORTH);
                                    programPanel.add(icon, BorderLayout.CENTER);

                                    programWindow.add(programPanel);
                                    programWindow.setSize(600,600);
                                    programWindow.setResizable(false);
                                    programWindow.setLocationRelativeTo(null);
                                    programWindow.setVisible(true);
                                } catch (InterruptedException | ExecutionException ex) {
                                    JOptionPane.showMessageDialog(null, "An error occured while opening program dialog", "Error", JOptionPane.ERROR_MESSAGE);
                                }

                            }

                            @Override
                            protected Program doInBackground() {
                                return radioInfoTableModel.getProgramAtRow(selectedRow);
                            }
                        }.execute();
                    }
                }
            }
        });
        return new JScrollPane(table);
    }

    /**
     * Creates the menu bar for the main application window.
     *
     * @return The created JMenuBar.
     */
    private JMenuBar createJMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem reload = new JMenuItem("Reload cached schedules");
        reload.addActionListener((e) -> new Thread(() -> {
            try {
                radioInfoController.updateCachedSchedules();
            } catch (HttpBadRequestException | IOException | URISyntaxException | InterruptedException ex) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Något gick fel vid hämtning av tablåer", "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start());

        menu.add(reload);
        menuBar.add(menu);
        return menuBar;
    }

    /**
     * Creates a dropdown menu to select radio channels.
     *
     * @return The created JComboBox for channel selection.
     */
    private JComboBox<Object> createChannelDropdown() {
        DefaultComboBoxModel<Object> comboBoxModel;
        comboBoxModel = new DefaultComboBoxModel<>(radioInfoController.getChannels().values().toArray());
        JComboBox<Object> channelSelector = new JComboBox<>(comboBoxModel);
        channelSelector.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if(value instanceof Channel) {
                    setText(((Channel) value).name());
                }
                return this;
            }
        });
        channelSelector.addActionListener((e) -> new Thread(() -> {
            Channel selectedChannel = (Channel) channelSelector.getSelectedItem();
            try {
                assert selectedChannel != null;
                radioInfoController.selectChannel(selectedChannel.id());
            } catch (HttpBadRequestException | IOException | URISyntaxException | InterruptedException ex) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Något gick fel vid hämtning av tablåer", "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start());
        return channelSelector;
    }

    /**
     * Receives notifications when the radio data in the model changes and updates the UI accordingly.
     */
    @Override
    public void onRadioDataChanged() {
        ArrayList<Program> schedule = (ArrayList<Program>) radioInfoController.getSelectedChannelSchedule();
        radioInfoTableModel.setSchedule(schedule);
    }

    /**
     * A custom table model to display schedule information.
     */
    private static class RadioInfoTableModel extends AbstractTableModel {
        public static final String[] columnNames = {"Program", "Starttid", "Sluttid"};
        private ArrayList<Program> schedule = new ArrayList<>();
        @Override
        public int getRowCount() {
            return schedule.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        /**
         * Sets the schedule data and notifies the table that the data has changed.
         *
         * @param schedule The new schedule data to be displayed in the table.
         */
        public void setSchedule(ArrayList<Program> schedule) {
            this.schedule = schedule;
            SwingUtilities.invokeLater(this::fireTableDataChanged);
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return switch (columnIndex) {
                case 0 -> schedule.get(rowIndex).title();
                case 1 -> schedule.get(rowIndex).startTime();
                case 2 -> schedule.get(rowIndex).endTime();
                default -> null;
            };
        }

        public Program getProgramAtRow(int rowIndex) {
            return schedule.get(rowIndex);
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }
    }
}
