package pack1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IvanOP on 28.04.2017.
 */
public class GUIHolder extends JPanel implements ActionListener {
    private JButton startButton;
    private JButton stopButton;
    private JButton readTextFields;
    private Habitat habitat;
    private JToggleButton showTimeSwitcher;
    private JToggleButton showInfo;
    private JDialog dialog;
    private JFrame frameOwner;
    private JTextField textFieldOfMaleBee;
    private JTextField textFieldOfBeeWorker;
    private JComboBox comboBox;
    private DefaultListModel listModel;
    private JList jList;
    private JButton getSelectedProbabilityFromComboBox, getGetSelectedProbabilityFromJList;
    private JMenuBar menuBar;
    private JMenu menu, submenu;
    private JMenuItem menuItem;
    private JToolBar toolBar;

    public GUIHolder(Habitat habitat, JFrame frameHolder) {
        setBackground(Color.BLACK);
        this.habitat = habitat;
        this.frameOwner = frameHolder;
        createAndShowGUI();
    }

    public Dimension getPreferredSize() {
        return new Dimension(200, 500);
    }

    private void createAndShowGUI() {
        this.setLayout(new GridBagLayout());

        startButton = new JButton("Start simulation");
        startButton.setActionCommand("start simulation");
        stopButton = new JButton("Stop simulation");
        stopButton.setEnabled(false);
        stopButton.setActionCommand("stop simulation");
        readTextFields = new JButton("Read text fields");
        readTextFields.setActionCommand("read text fields");
        showTimeSwitcher = new JToggleButton("Show time");
        showTimeSwitcher.setActionCommand("show/hide");
        showInfo = new JToggleButton("Show info");
        showInfo.setActionCommand("show or hide info");
        textFieldOfMaleBee = new JTextField();
        textFieldOfMaleBee.setText("Current T: " + String.valueOf(habitat.maleBeeUpdatePeriod / 1000));
        textFieldOfBeeWorker = new JTextField();
        textFieldOfBeeWorker.setText("Current T: " + String.valueOf(habitat.beeWorkerUpdatePeriod / 1000));
        getSelectedProbabilityFromComboBox = new JButton("Select from combo box");
        getSelectedProbabilityFromComboBox.setActionCommand("select from combo box");
        getGetSelectedProbabilityFromJList = new JButton("Select from list");
        getGetSelectedProbabilityFromJList.setActionCommand("select from list");

        createModalWindowOfStats();

        String[] probabilityValues = {"0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100"};
        comboBox = new JComboBox(probabilityValues);
        comboBox.setSelectedIndex(3);

        listModel = new DefaultListModel();
        for (int i = 0; i < probabilityValues.length; i++) {
            listModel.addElement(probabilityValues[i]);
        }
        jList = new JList(listModel);
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList.setSelectedIndex(3);
        jList.setVisibleRowCount(3);
        JScrollPane jScrollPane = new JScrollPane(jList);

        frameOwner.setJMenuBar(getMenuBar());
        frameOwner.add(getToolBar(), BorderLayout.PAGE_START);

        stopButton.addActionListener(this);
        startButton.addActionListener(this);
        showTimeSwitcher.addActionListener(this);
        showInfo.addActionListener(this);
        readTextFields.addActionListener(this);
        comboBox.addActionListener(this);
        getSelectedProbabilityFromComboBox.addActionListener(this);
        getGetSelectedProbabilityFromJList.addActionListener(this);

        add(startButton, new GridBagConstraints(0, 0, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        add(stopButton, new GridBagConstraints(0, 1, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        add(showTimeSwitcher, new GridBagConstraints(0, 2, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        add(showInfo, new GridBagConstraints(0, 3, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        add(textFieldOfMaleBee, new GridBagConstraints(0, 4, 1, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        add(textFieldOfBeeWorker, new GridBagConstraints(1, 4, 1, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        add(readTextFields, new GridBagConstraints(0, 5, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        add(comboBox, new GridBagConstraints(0, 6, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        add(jScrollPane, new GridBagConstraints(0, 7, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        add(getSelectedProbabilityFromComboBox, new GridBagConstraints(0, 8, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        add(getGetSelectedProbabilityFromJList, new GridBagConstraints(0, 9, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        frameOwner.requestFocusInWindow();
        if (actionEvent.getActionCommand().equals("start simulation")) {
            habitat.startSimulation();
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        }
        if (actionEvent.getActionCommand().equals("stop simulation")) {
            createModalWindowOfStats();
            dialog.setVisible(true);
            habitat.stopSimulation();
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        }
        if (actionEvent.getActionCommand().equals("show/hide")) {
            if (showTimeSwitcher.isSelected()) {
                showTimeSwitcher.setText("Hide time");
                habitat.showSimulationTime();
            } else {
                showTimeSwitcher.setText("Show time");
                habitat.showSimulationTime();
            }
        }
        if (actionEvent.getActionCommand().equals("show or hide info")) {
            if (!dialog.isVisible()) {
                createModalWindowOfStats();
                dialog.setVisible(true);
            } else {
                dialog.dispose();
            }
        }
        if (actionEvent.getActionCommand().equals("read text fields")) {
            try {
                habitat.maleBeeUpdatePeriod = Float.valueOf(textFieldOfMaleBee.getText());
                habitat.beeWorkerUpdatePeriod = Float.valueOf(textFieldOfBeeWorker.getText()) * 1000;
                habitat.maleBeeUpdateTimer.setDelay(((int) habitat.maleBeeUpdatePeriod));
                habitat.beeWorkerUpdateTimer.setDelay(((int) habitat.beeWorkerUpdatePeriod));
            } catch (NumberFormatException e) {
                createModalWindowOfTextFieldException();
                textFieldOfMaleBee.setText(String.valueOf(habitat.maleBeeUpdatePeriod / 1000));
                textFieldOfBeeWorker.setText(String.valueOf(habitat.beeWorkerUpdatePeriod / 1000));
            }
        }
        if (actionEvent.getActionCommand().equals("select from combo box")) {
            habitat.probability = Double.parseDouble(((String) comboBox.getSelectedItem())) / 100;
        }
        if (actionEvent.getActionCommand().equals("select from list")) {
            habitat.probability = Double.parseDouble((String) jList.getSelectedValue());
        }
    }


    private void createModalWindowOfStats() {
        dialog = new JDialog(frameOwner);
        JTextArea textArea1 = new JTextArea();
        JPanel dialogPanel = new JPanel();
        String temp1 = "Всего пчел: " + String.valueOf(habitat.hivePopulation) + "\n";
        String temp2 = "Пчел рабочих: " + String.valueOf(habitat.beeWorkerCounter) + "\n";
        String temp3 = "Пчел трутней: " + String.valueOf(habitat.maleBeeCounter) + "\n";
        String temp4 = "Вермя симуляции: " + String.valueOf(habitat.simulationTime) + "\n";
        textArea1.setSize(300, 300);
        JButton ok = new JButton("OK");
        ok.addActionListener(actionEvent1 -> {
            habitat.stopSimulation();
            dialog.dispose();
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            showInfo.setSelected(false);
        });
        JButton cancel = new JButton("CANCEL");
        cancel.addActionListener(actionEvent12 -> {
            dialog.dispose();
            showInfo.setSelected(false);

        });
        textArea1.setLineWrap(true);
        textArea1.append(temp1 + temp2 + temp3 + temp4);
        textArea1.setFocusable(false);
        dialog.add(textArea1, BorderLayout.NORTH);
        dialogPanel.add(ok, BorderLayout.SOUTH);
        dialogPanel.add(cancel, BorderLayout.SOUTH);
        dialog.add(dialogPanel);
        dialog.pack();
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void createModalWindowOfTextFieldException() {
        JDialog showError = new JDialog(frameOwner);
        JTextArea textArea1 = new JTextArea();
        String temp = "Not a number\n Type numerical value";
        textArea1.setSize(100, 100);
        textArea1.append(temp);
        textArea1.setFocusable(false);
        showError.add(textArea1);
        showError.pack();
        showError.setVisible(true);
        showError.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private JMenuBar getMenuBar() {

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the first menu.
        menu = new JMenu("A Menu");
        menuBar.add(menu);

        //a group of JMenuItems
        //start simulation
        menuItem = new JMenuItem("Start simulation");
        menuItem.setActionCommand("start simulation");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        //stop simulation
        menuItem = new JMenuItem("Stop simulation");
        menuItem.setActionCommand("stop simulation");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        //a submenu
        menu.addSeparator();
        submenu = new JMenu("A submenu");


        menuItem = new JMenuItem("Show time");
        menuItem.setActionCommand("show/hide");
        menuItem.addActionListener(this);
        submenu.add(menuItem);

        menuItem = new JMenuItem("Show info");
        menuItem.setActionCommand("show or hide info");
        menuItem.addActionListener(this);
        submenu.add(menuItem);

        menu.add(submenu);

        return menuBar;
    }

    private JToolBar getToolBar() {
        toolBar = new JToolBar();
        toolBar.setRollover(true);
        toolBar.setPreferredSize(new Dimension(400, 30));

        JButton button = new JButton("Start simulation");
        button.setActionCommand("start simulation");
        button.addActionListener(this);
        toolBar.add(button);

        button = new JButton("Stop simulation");
        button.setActionCommand("stop simulation");
        button.addActionListener(this);
        toolBar.add(button);

        button = new JButton("Show time");
        button.setActionCommand("show/hide");
        button.addActionListener(this);
        toolBar.add(button);

        button = new JButton("Show info");
        button.setActionCommand("show or hide info");
        button.addActionListener(this);
        toolBar.add(button);

        return toolBar;
    }


}
