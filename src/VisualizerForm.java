import javax.swing.*;
import java.awt.*;

public class VisualizerForm extends JFrame {
    public static final int FORM_WIDTH = 800;
    public static final int FORM_HEIGHT = 600;
    public static int totalRows = (int) Math.floor(FORM_HEIGHT / Node.size);
    public static int totalColumns = (int) Math.floor(FORM_HEIGHT / Node.size);

    public VisualizerForm(){
        Grid newGrid = new Grid(totalRows, totalColumns);
        JPanel formContainer = new JPanel();
        GridPanel gridPanel = new GridPanel(FORM_WIDTH - 200, FORM_HEIGHT, newGrid);
        UserControlPanel userControlPanel = new UserControlPanel(200, FORM_HEIGHT, gridPanel);
        formContainer.setLayout(new BorderLayout());
        formContainer.add(BorderLayout.WEST, userControlPanel);
        formContainer.add(BorderLayout.CENTER, gridPanel);
        this.setContentPane(formContainer);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Traditional Search Algorithm Visualizer");
        this.getContentPane().setPreferredSize(new Dimension(FORM_WIDTH, FORM_HEIGHT));
        this.pack();
        this.setVisible(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
    }
}
