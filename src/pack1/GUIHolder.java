package pack1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.Map;

/**
 * Created by IvanOP on 28.04.2017.
 */
public class GUIHolder extends JPanel implements ActionListener {
    private JButton startButton;
    private JButton stopButton;
    private JButton readTextFieldsOfPeriod;
    private Habitat habitat;
    private JToggleButton showTimeSwitcher;
    private JToggleButton showInfo;
    private JDialog dialog;
    private JFrame frameOwner;
    private JTextField textFieldOfMaleBeePeriod;
    private JTextField textFieldOfBeeWorkerPeriod;
    private JComboBox comboBox;
    private DefaultListModel listModel;
    private JList jList;
    private JButton getSelectedProbabilityFromComboBox, getGetSelectedProbabilityFromJList;
    private JMenuBar menuBar;
    private JMenu menu, submenu;
    private JMenuItem menuItem;
    private JToolBar toolBar;
    private JTextArea textAreaOfConsole;
    private PipedWriter pipedOutputStreamFromConsole;
    private PipedReader pipedInputStreamFromHabitat;
    private String messageToHabitat = "";
    private int population;

    private Thread pipedInputStreamThread = new Thread(() -> {
        while (true) {
            try {
                Thread.sleep(100);
                population = pipedInputStreamFromHabitat.read();
                if (population != 0) {
                    System.out.println(population);
                    textAreaOfConsole.append(String.valueOf(population) + "\n");
                }
                population = 0;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    });
    private Thread pipedOutputStreamThread = new Thread(() -> {
        while (true) {

            try {
                Thread.sleep(100);
                pipedOutputStreamFromConsole.write(messageToHabitat);
                messageToHabitat = "";
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    });


    public GUIHolder(Habitat habitat, JFrame frameHolder) {
        setBackground(Color.WHITE);
        this.habitat = habitat;
        this.frameOwner = frameHolder;
        createAndShowGUI();
    }

    public Dimension getPreferredSize() {
        return new Dimension(200, 800);
    }

    private void createAndShowGUI() {
        this.setLayout(new GridBagLayout());

        startButton = new JButton("Start simulation");
        startButton.addActionListener(actionEvent -> {
            habitat.startSimulation();
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        });
        add(startButton, new GridBagConstraints(0, 0, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        stopButton = new JButton("Stop simulation");
        stopButton.setEnabled(false);
        stopButton.addActionListener(actionEvent -> {
            frameOwner.requestFocusInWindow();
            createModalWindowOfStats();
            dialog.setVisible(true);
            habitat.stopSimulation();
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        });
        add(stopButton, new GridBagConstraints(0, 1, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));


        showTimeSwitcher = new JToggleButton("Show time");
        showTimeSwitcher.addActionListener(actionEvent -> {
            if (showTimeSwitcher.isSelected()) {
                showTimeSwitcher.setText("Hide time");
                habitat.showSimulationTime();
            } else {
                showTimeSwitcher.setText("Show time");
                habitat.showSimulationTime();
            }
        });
        add(showTimeSwitcher, new GridBagConstraints(0, 2, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        showInfo = new JToggleButton("Show info");
        showInfo.addActionListener(actionEvent -> {
            if (!dialog.isVisible()) {
                createModalWindowOfStats();
                dialog.setVisible(true);
            } else {
                dialog.dispose();
            }
        });
        add(showInfo, new GridBagConstraints(0, 3, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        JLabel jLabelOfMaleBeeBornPeriod = new JLabel("Born period of male bee:");
        add(jLabelOfMaleBeeBornPeriod, new GridBagConstraints(0, 4, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        textFieldOfMaleBeePeriod = new JTextField();
        textFieldOfMaleBeePeriod.setText("Current T: " + String.valueOf(habitat.maleBeeUpdatePeriod / 1000));
        add(textFieldOfMaleBeePeriod, new GridBagConstraints(0, 5, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        JLabel jLabelOfBeeWorkerBornPeriod = new JLabel("Born period of bee worker:");
        add(jLabelOfBeeWorkerBornPeriod, new GridBagConstraints(0, 6, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        textFieldOfBeeWorkerPeriod = new JTextField();
        textFieldOfBeeWorkerPeriod.setText("Current T: " + String.valueOf(habitat.beeWorkerUpdatePeriod / 1000));
        add(textFieldOfBeeWorkerPeriod, new GridBagConstraints(1, 7, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        readTextFieldsOfPeriod = new JButton("Set periods");
        readTextFieldsOfPeriod.addActionListener(actionEvent -> {
            String value = textFieldOfMaleBeePeriod.getText();
            value = value.replaceAll("[^0-9.]", "");
            habitat.maleBeeUpdatePeriod = Float.valueOf(value) * 1000;
            value = textFieldOfBeeWorkerPeriod.getText();
            value = value.replaceAll("[^0-9.]", "");
            habitat.beeWorkerUpdatePeriod = Float.valueOf(value) * 1000;
            habitat.maleBeeUpdateTimer.setDelay(((int) habitat.maleBeeUpdatePeriod));
            habitat.beeWorkerUpdateTimer.setDelay(((int) habitat.beeWorkerUpdatePeriod));
        });
        add(readTextFieldsOfPeriod, new GridBagConstraints(0, 8, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        JLabel jLabelOfMaleBeeLifeTime = new JLabel("Life time of male bee:");
        add(jLabelOfMaleBeeLifeTime, new GridBagConstraints(0, 9, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        JTextField lifeTimeOfMaleBee = new JTextField();
        lifeTimeOfMaleBee.setText("Life time of male bee = " + habitat.lifeTimeOfMaleBee);
        add(lifeTimeOfMaleBee, new GridBagConstraints(0, 10, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        JLabel jLabelOfBeeWorkerLifeTime = new JLabel("Life time of bee worker:");
        add(jLabelOfBeeWorkerLifeTime, new GridBagConstraints(0, 11, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        JTextField lifeTimeOfBeeWorker = new JTextField();
        lifeTimeOfBeeWorker.setText("Life time of bee worker = " + habitat.lifeTimeOfBeeWorker);
        add(lifeTimeOfBeeWorker, new GridBagConstraints(0, 12, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        JButton setLifeTimes = new JButton("Set life times");
        setLifeTimes.addActionListener(actionEvent -> {
            String value = lifeTimeOfMaleBee.getText();
            value = value.replaceAll("[^0-9]", "");
            habitat.lifeTimeOfMaleBee = Integer.valueOf(value);
            value = lifeTimeOfBeeWorker.getText();
            value = value.replaceAll("[^0-9]", "");
            habitat.lifeTimeOfBeeWorker = Integer.valueOf(value);
        });
        add(setLifeTimes, new GridBagConstraints(0, 13, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        createModalWindowOfStats();
        JLabel jLabelOfProbability = new JLabel("Born probability combo box:");
        add(jLabelOfProbability, new GridBagConstraints(0, 14, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        String[] probabilityValues = {"0%", "10%", "20%", "30%", "40%", "50%", "60%", "70%", "80%", "90%", "100%"};
        comboBox = new JComboBox(probabilityValues);
        comboBox.setSelectedIndex(3);
        add(comboBox, new GridBagConstraints(0, 15, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        JLabel jLabelOfProbabilityJLIst = new JLabel("Born probability list:");
        add(jLabelOfProbabilityJLIst, new GridBagConstraints(0, 16, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        listModel = new DefaultListModel();
        for (String probabilityValue : probabilityValues) {
            listModel.addElement(probabilityValue);
        }
        jList = new JList(listModel);
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList.setSelectedIndex(3);
        jList.setVisibleRowCount(3);
        JScrollPane jScrollPane = new JScrollPane(jList);
        add(jScrollPane, new GridBagConstraints(0, 17, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        getSelectedProbabilityFromComboBox = new JButton("Select from combo box");
        getSelectedProbabilityFromComboBox.addActionListener(actionEvent -> {
            String value = (String) comboBox.getSelectedItem();
            value = value.replaceAll("[^0-9]", "");
            habitat.probability = Double.parseDouble(value) / 100;
        });
        add(getSelectedProbabilityFromComboBox, new GridBagConstraints(0, 18, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        getGetSelectedProbabilityFromJList = new JButton("Select from list");
        getGetSelectedProbabilityFromJList.addActionListener(actionEvent -> {
            String value = (String) jList.getSelectedValue();
            value = value.replaceAll("[^0-9]", "");
            habitat.probability = Double.parseDouble(value);
        });
        add(getGetSelectedProbabilityFromJList, new GridBagConstraints(0, 19, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        JButton showCurrentBees = new JButton("Show current alive bees");
        showCurrentBees.addActionListener(actionEvent -> {
            createModalWindowOfAliveBees();
        });
        add(showCurrentBees, new GridBagConstraints(0, 20, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        frameOwner.setJMenuBar(getMenuBar());
        frameOwner.add(getToolBar(), BorderLayout.PAGE_START);

        JButton pauseThreads = new JButton("Pause threads");
        pauseThreads.addActionListener(actionEvent -> {
            CollectionsForObjects.getInstance().getAbstractBeeArrayList().forEach(abstractBee -> {
                abstractBee.baseAI.pauseThread();
            });
        });
        add(pauseThreads, new GridBagConstraints(0, 22, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        JButton resumeThreads = new JButton("Resume threads");
        resumeThreads.addActionListener(actionEvent -> {
            CollectionsForObjects.getInstance().getAbstractBeeArrayList().forEach(abstractBee -> {
                abstractBee.baseAI.resumeThread();
            });
        });
        add(resumeThreads, new GridBagConstraints(0, 23, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        //String[] priorities = {"1","2","3","4","5","6","7","8","9","10"};
        JLabel jLabelOfMaleBeeThreadPriorityComboBox = new JLabel("Male bee thread priority");
        add(jLabelOfMaleBeeThreadPriorityComboBox, new GridBagConstraints(0, 24, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        Integer[] ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        JComboBox<Integer> jComboBoxOfMaleBee = new JComboBox<>(ints);
        jComboBoxOfMaleBee.setSelectedIndex(4);
        add(jComboBoxOfMaleBee, new GridBagConstraints(0, 25, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        JLabel jLabelOfBeeWorkerPriorityComboBox = new JLabel("Bee worker thread priority");
        add(jLabelOfBeeWorkerPriorityComboBox, new GridBagConstraints(0, 26, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        JComboBox<Integer> jComboBoxOfBeeWorker = new JComboBox<>(ints);
        jComboBoxOfBeeWorker.setSelectedIndex(4);
        add(jComboBoxOfBeeWorker, new GridBagConstraints(0, 27, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        JButton readPriorities = new JButton("Set thread priorities");
        readPriorities.addActionListener(actionEvent -> {
            CollectionsForObjects.getInstance().getAbstractBeeArrayList().forEach(abstractBee -> {
                abstractBee.baseAI.movementThread.setPriority((int) jComboBoxOfBeeWorker.getSelectedItem());
            });
        });
        add(readPriorities, new GridBagConstraints(0, 28, 2, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));


    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        frameOwner.requestFocusInWindow();
    }

    private void createModalWindowOfAliveBees() {
        JDialog jDialog = new JDialog(frameOwner);
        JTextArea textArea1 = new JTextArea();
        textArea1.setSize(300, 300);
        for (Map.Entry<Long, Long> entry : CollectionsForObjects.getInstance().getLongLongTreeMap().entrySet()) {
            textArea1.append("Born time = " + entry.getValue() + " : " + "hash code = " + entry.getKey() + "\n");
        }
        jDialog.add(textArea1);
        jDialog.pack();
        jDialog.setVisible(true);
        jDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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


    private JMenuBar getMenuBar() {

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the first menu.
        menu = new JMenu("A Menu");
        menuBar.add(menu);

        //a group of JMenuItems
        //start simulation
        menuItem = new JMenuItem("Start simulation");
        menuItem.addActionListener(actionEvent -> {
            habitat.startSimulation();
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        });
        menu.add(menuItem);

        //stop simulation
        menuItem = new JMenuItem("Stop simulation");
        menuItem.addActionListener(actionEvent -> {
            createModalWindowOfStats();
            dialog.setVisible(true);
            habitat.stopSimulation();
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("Console");
        menuItem.addActionListener(actionEvent -> {
            try {
                habitat.initializePipedStreams();
                pipedOutputStreamFromConsole = new PipedWriter();
                pipedOutputStreamFromConsole.connect(habitat.pipedInputStreamFromConsole);
                pipedInputStreamFromHabitat = new PipedReader(habitat.pipedOutputStreamToConsole);
            } catch (IOException e) {
                e.printStackTrace();
            }
            getConsole();
            pipedInputStreamThread.start();
            pipedOutputStreamThread.start();
        });
        menu.add(menuItem);

        //a submenu
        menu.addSeparator();
        submenu = new JMenu("A submenu");


        menuItem = new JMenuItem("Show time");
        menuItem.addActionListener(actionEvent -> {
            if (showTimeSwitcher.isSelected()) {
                showTimeSwitcher.setText("Hide time");
                habitat.showSimulationTime();
            } else {
                showTimeSwitcher.setText("Show time");
                habitat.showSimulationTime();
            }
        });
        submenu.add(menuItem);

        menuItem = new JMenuItem("Show info");
        menuItem.addActionListener(actionEvent -> {
            if (!dialog.isVisible()) {
                createModalWindowOfStats();
                dialog.setVisible(true);
            } else {
                dialog.dispose();
            }
        });
        submenu.add(menuItem);
        menu.add(submenu);
        return menuBar;
    }

    private JToolBar getToolBar() {
        toolBar = new JToolBar();
        toolBar.setRollover(true);
        toolBar.setPreferredSize(new Dimension(400, 30));

        JButton button = new JButton("Start simulation");
        button.addActionListener(actionEvent -> {
            habitat.startSimulation();
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        });
        toolBar.add(button);

        button = new JButton("Stop simulation");
        button.addActionListener(actionEvent -> {
            createModalWindowOfStats();
            dialog.setVisible(true);
            habitat.stopSimulation();
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        });
        toolBar.add(button);

        button = new JButton("Show time");
        button.addActionListener(actionEvent -> {

            if (showTimeSwitcher.isSelected()) {
                showTimeSwitcher.setText("Hide time");
                habitat.showSimulationTime();
            } else {
                showTimeSwitcher.setText("Show time");
                habitat.showSimulationTime();
            }
        });
        toolBar.add(button);

        button = new JButton("Show info");
        button.addActionListener(actionEvent -> {
            if (!dialog.isVisible()) {
                createModalWindowOfStats();
                dialog.setVisible(true);
            } else {
                dialog.dispose();
            }
        });
        toolBar.add(button);

        return toolBar;
    }

    private void getConsole() {
        JFrame jFrame = new JFrame("Console window");
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridBagLayout());
        textAreaOfConsole = new JTextArea(20, 50);
        JTextField jTextField = new JTextField(20);

        jTextField.addActionListener(actionEvent -> {
            messageToHabitat = jTextField.getText();
            textAreaOfConsole.append(jTextField.getText() + "\n");
            jTextField.setText("");
        });
        JScrollPane jScrollPane = new JScrollPane(textAreaOfConsole);
        textAreaOfConsole.setLineWrap(true);
        jPanel.add(jTextField, new GridBagConstraints(0, 0, 1, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        jPanel.add(jScrollPane, new GridBagConstraints(0, 1, 1, 1, 0.5, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        jFrame.add(jPanel);
        jFrame.pack();
        jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jFrame.setVisible(true);
        jFrame.setFocusable(true);
    }


}
