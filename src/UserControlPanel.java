import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
public class UserControlPanel extends JPanel implements ActionListener {

    private final JComboBox<String> listOfSearchingAlgorithms;
    private final JSlider gridSizeSeekBar;
    private final JSlider delaySeekBar;
    private final JLabel gridSizeSeekBarLabel;
    private final JLabel delaySeekBarLabel;
    private final JCheckBox enableDiagonalMove;

    private final GridPanel gridPanel;
    AlgorithmExecutor algorithmExecutor = null;

    public UserControlPanel(int panelWidth, int panelHeight, GridPanel gridPanel){

        this.gridPanel = gridPanel;
        Dimension panelDimension = new Dimension(panelWidth, panelHeight);
        this.setPreferredSize(panelDimension);
        this.setLayout(null);
        this.requestFocus();
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                UserControlPanel userControlPanel = (UserControlPanel) e.getSource();
                userControlPanel.requestFocus();
            }
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE){
                    if (algorithmExecutor == null || !Utils.IsSolving){
                        return;
                    }

                    if (Utils.IsAlgorithmPaused){
                        Utils.IsAlgorithmPaused = false;
                        // resume the thread
                        //algorithmExecutor.resume();
                        gridPanel.revalidate();
                        gridPanel.repaint();
                        return;
                    }
                    Utils.IsAlgorithmPaused = true;
                    // suspending the thread
                    //algorithmExecutor.suspend();
                    gridPanel.revalidate();
                    gridPanel.repaint();
                }
            }
        });

        JButton searchButton = new JButton("Start Searching");
        searchButton.setBounds(25,10,150,30);
        searchButton.addActionListener(this);
        searchButton.setFocusable(false);

        JButton generateNewMapButton = new JButton("Generate Random Map");
        generateNewMapButton.setBounds(25, 50, 150, 30);
        generateNewMapButton.setFocusable(false);
        generateNewMapButton.addActionListener(e -> {
            if (Utils.IsSolving){
                return;
            }
            gridPanel.removeObstacles();
            Grid newGrid = gridPanel.getGrid();
            Random random = new Random(System.currentTimeMillis());

            for (int i = 0; i < newGrid.getTotalRows(); i++) {
                for (int j = 0; j < newGrid.getTotalColumns(); j++) {
                    Node node = newGrid.getNode(i, j);
                    if (!node.isStart() && !node.isFinish() && random.nextFloat() > 0.7) {
                        node.setObstacle(true);
                    }
                }
            }
        });

        JButton resetButton = new JButton("Clear");
        resetButton.setBounds(25, 90, 150, 30);
        resetButton.setFocusable(false);
        resetButton.addActionListener(e -> {
            Utils.IsSolving = false;

            gridPanel.resetGrid();
            gridPanel.revalidate();
            gridPanel.repaint();
        });

        JButton resetPathButton = new JButton("Clear Path");
        resetPathButton.setBounds(25, 130, 150, 30);
        resetPathButton.setFocusable(false);
        resetPathButton.addActionListener(e -> {
            if (Utils.IsSolving) {
                return;
            }
            gridPanel.resetPath();
            gridPanel.repaint();
        });

        String[] algorithmsName = {"Breadth First Search", "Depth First Search", "Best First Search", "A Star"};
        listOfSearchingAlgorithms = new JComboBox<>(algorithmsName);
        listOfSearchingAlgorithms.setSelectedIndex(0);
        listOfSearchingAlgorithms.setBounds(25, 175, 150, 30);
        listOfSearchingAlgorithms.setFocusable(false);
        listOfSearchingAlgorithms.addActionListener(e -> {
            if (Utils.IsSolving) {
                listOfSearchingAlgorithms.setSelectedIndex(Utils.chosenAlgorithm);
                return;
            }
            Utils.chosenAlgorithm = listOfSearchingAlgorithms.getSelectedIndex();
        });

        enableDiagonalMove = new JCheckBox("Allow diagonial moves");
        enableDiagonalMove.setBounds(20, 215, 200, 20);
        enableDiagonalMove.setFocusable(false);
        enableDiagonalMove.addChangeListener(e -> {
            if (Utils.IsSolving) {
                enableDiagonalMove.setSelected(Utils.enableDiagonals);
                return;
            }
            Utils.enableDiagonals = enableDiagonalMove.isSelected();
        });

        gridSizeSeekBarLabel = new JLabel("Size: 20x20");
        gridSizeSeekBarLabel.setBounds(24, 245, 150, 20);
        gridSizeSeekBarLabel.setFocusable(false);

        gridSizeSeekBar = new JSlider(20, 60, Node.size);
        gridSizeSeekBar.setMajorTickSpacing(10);
        gridSizeSeekBar.setMinorTickSpacing(10);
        gridSizeSeekBar.setBounds(18, 270, 150, 20);
        gridSizeSeekBar.setFocusable(false);
        gridSizeSeekBar.addChangeListener(e -> {
            if (Utils.IsSolving) {
                return;
            }

            int cellSize = gridSizeSeekBar.getValue() / 10 * 10;

            if (cellSize != Node.size) {
                gridPanel.updateTheSizeOfTheGrid(cellSize);
                gridSizeSeekBarLabel.setText("Size: " + gridPanel.getGrid().getTotalRows() + "x" + gridPanel.getGrid().getTotalColumns());
            }
        });

        delaySeekBarLabel = new JLabel("Delay: 30ms");
        delaySeekBarLabel.setBounds(24, 295, 150, 20);
        delaySeekBarLabel.setFocusable(false);

        delaySeekBar = new JSlider(0, 100, Utils.totalDelay);
        delaySeekBar.setMajorTickSpacing(10);
        delaySeekBar.setMinorTickSpacing(10);
        delaySeekBar.setBounds(18, 320, 150, 20);
        delaySeekBar.setFocusable(false);

        delaySeekBar.addChangeListener(e -> {
            Utils.totalDelay = delaySeekBar.getValue();
            delaySeekBarLabel.setText("Delay: " + Utils.totalDelay + "ms");
        });

        JLabel startingNodeLabel = new JLabel("Starting node");
        startingNodeLabel.setBounds(50, 350, 100, 15);
        startingNodeLabel.setFocusable(false);

        JLabel targetNodeLabel = new JLabel("Target node");
        targetNodeLabel.setBounds(50, 375, 100, 15);
        targetNodeLabel.setFocusable(false);

        JLabel currentNodeLabel = new JLabel("Current node");
        currentNodeLabel.setBounds(50, 400, 100, 15);
        currentNodeLabel.setFocusable(false);

        JLabel visitedNodeLabel = new JLabel("Visited nodes");
        visitedNodeLabel.setBounds(50, 425, 100, 15);
        visitedNodeLabel.setFocusable(false);

        JLabel frontierLabel = new JLabel("Frontier nodes");
        frontierLabel.setBounds(50, 450, 100, 15);
        frontierLabel.setFocusable(false);

        JLabel finalPathLabel = new JLabel("Path node");
        finalPathLabel.setBounds(50, 475, 100, 15);
        finalPathLabel.setFocusable(false);

        JLabel obstacleLabel = new JLabel("Obstacle node");
        obstacleLabel.setBounds(50, 500, 100, 15);
        obstacleLabel.setFocusable(false);

        this.add(searchButton);
        this.add(resetButton);
        this.add(generateNewMapButton);
        this.add(listOfSearchingAlgorithms);
        this.add(gridSizeSeekBarLabel);
        this.add(gridSizeSeekBar);
        this.add(delaySeekBarLabel);
        this.add(delaySeekBar);
        this.add(resetPathButton);
        this.add(enableDiagonalMove);
        this.add(startingNodeLabel);
        this.add(targetNodeLabel);
        this.add(currentNodeLabel);
        this.add(visitedNodeLabel);
        this.add(frontierLabel);
        this.add(finalPathLabel);
        this.add(obstacleLabel);
    }

    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        super.paintComponent(graphics2D);
        graphics2D.setColor(Color.orange);
        graphics2D.fillRect(25, 350, 15, 15);

        graphics2D.setColor(Color.red);
        graphics2D.fillRect(25, 375, 15, 15);

        graphics2D.setColor(Color.magenta);
        graphics2D.fillRect(25, 400, 15, 15);

        graphics2D.setColor(Color.cyan);
        graphics2D.fillRect(25, 425, 15, 15);

        graphics2D.setColor(Color.green);
        graphics2D.fillRect(25, 450, 15, 15);

        graphics2D.setColor(Color.yellow);
        graphics2D.fillRect(25, 475, 15, 15);

        graphics2D.setColor(Color.black);
        graphics2D.fillRect(25, 500, 15, 15);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.requestFocus();

        if (Utils.IsSolving){
            return;
        }
        gridPanel.resetPath();
        Utils.IsSolving = true;
        algorithmExecutor = new AlgorithmExecutor(gridPanel.getGrid(), gridPanel);
        algorithmExecutor.start();
    }
}
