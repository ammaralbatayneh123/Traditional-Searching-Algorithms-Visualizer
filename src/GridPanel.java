import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GridPanel extends JPanel implements MouseListener, MouseMotionListener {

    private Grid grid;
    private final int panelHeight;
    private final int panelWidth;
    Node currentNode = null;

    public GridPanel(int panelHeight, int panelWidth, Grid grid) {
        this.grid = grid;
        this.panelHeight = panelHeight;
        this.panelWidth = panelWidth;
        this.setSize(panelHeight, panelWidth);
        this.setFocusable(true);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    public void updateTheSizeOfTheGrid(int cellSize) {
        if (!Utils.IsSolving) {
            Node.size = cellSize;
            int totalRows = (int) Math.floor(panelHeight / Node.size);
            int totalColumns = (int) Math.floor(panelWidth / Node.size);

            grid = new Grid(totalRows, totalColumns);
            this.revalidate();
            this.repaint();
        }
    }

    public void resetGrid() {
        Utils.IsSolving = false;
        Utils.breakAlgorithm = true;
        Utils.IsAlgorithmPaused= false;
        grid.constructTheGrid();
        this.revalidate();
        this.repaint();
    }

    public void removeObstacles() {
        grid.removeObstacles();
        this.revalidate();
        this.repaint();
    }

    public Grid getGrid() {
        return grid;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        super.paintComponent(graphics2D);
        graphics2D.setStroke(new BasicStroke(1.5f));
        graphics2D.setColor(Color.black);
        grid.renderTheGrid(graphics2D, this);

        if(Utils.IsAlgorithmPaused) {
            graphics2D.setFont(new Font("Poppins", Font.BOLD, 60));
            graphics2D.setColor(new Color(239, 193, 9));
            graphics2D.drawString("Paused", panelWidth / 2 - 110 , panelHeight / 4);
            graphics2D.setFont(new Font("Poppins", Font.BOLD, 30));
            graphics2D.drawString("press space to continue", panelWidth / 2 - 160 , panelHeight / 3);
        }
    }

    public void resetPath() {
        Utils.IsAlgorithmPaused = false;
        grid.resetPath();
        this.revalidate();
        this.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (Utils.IsSolving) {
            return;
        }
        int mouseXCoordinate = e.getX() / Node.size;
        int mouseYCoordinate = e.getY() / Node.size;
        if (mouseXCoordinate < 0 || mouseYCoordinate >= grid.getTotalColumns() || mouseYCoordinate < 0 || mouseXCoordinate >= grid.getTotalColumns()) {
            return;
        }

        if (SwingUtilities.isLeftMouseButton(e)) {
            if (grid.getNode(mouseXCoordinate, mouseYCoordinate) != null && !grid.getNode(mouseXCoordinate, mouseYCoordinate).isFinish() && !grid.getNode(mouseXCoordinate, mouseYCoordinate).isStart()) {
                grid.getNode(mouseXCoordinate, mouseYCoordinate).setObstacle(true);
            }
        }

        currentNode = grid.getNode(mouseXCoordinate, mouseYCoordinate);
        this.revalidate();
        this.repaint();

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (Utils.IsSolving) {
            return;
        }
        int mouseXCoordinate = e.getX() / Node.size;
        int mouseYCoordinate = e.getY() / Node.size;
        if (mouseXCoordinate < 0 || mouseYCoordinate >= grid.getTotalColumns() || mouseYCoordinate < 0 || mouseXCoordinate >= grid.getTotalRows()) {
            return;
        }

        if (SwingUtilities.isLeftMouseButton(e)) {
            if (grid.getNode(mouseXCoordinate, mouseYCoordinate) != null && !grid.getNode(mouseXCoordinate, mouseYCoordinate).isFinish() && !grid.getNode(mouseXCoordinate, mouseYCoordinate).isStart()) {
                grid.getNode(mouseXCoordinate, mouseYCoordinate).setObstacle(true);
            }
        }
        if (SwingUtilities.isRightMouseButton(e)) {
            if (grid.getNode(mouseXCoordinate, mouseYCoordinate) != null && !grid.getNode(mouseXCoordinate, mouseYCoordinate).isFinish() && !grid.getNode(mouseXCoordinate, mouseYCoordinate).isStart()) {
                grid.getNode(mouseXCoordinate, mouseYCoordinate).setObstacle(false);
            }
        }

        currentNode = null;
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        this.revalidate();
        this.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (Utils.IsSolving) {
            return;
        }
        int mouseXCoordinate = e.getX() / Node.size;
        int mouseYCoordinate = e.getY() / Node.size;

        if (mouseXCoordinate < 0 || mouseYCoordinate >= grid.getTotalColumns() || mouseYCoordinate < 0 || mouseXCoordinate >= grid.getTotalRows()) {
            return;
        }

        if (SwingUtilities.isLeftMouseButton(e)) {
            if (currentNode != null && currentNode.isStart()) {
                currentNode = grid.getNode(mouseXCoordinate, mouseYCoordinate);
                currentNode.setObstacle(false);
                grid.setStart(currentNode);
            }
        }
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (currentNode != null && currentNode.isFinish()) {
                currentNode = grid.getNode(mouseXCoordinate, mouseYCoordinate);
                currentNode.setObstacle(false);
                grid.setFinish(currentNode);
            }
        }

        if (SwingUtilities.isLeftMouseButton(e)) {
            if (currentNode != null && !currentNode.isFinish() && !currentNode.isStart()) {
                currentNode = grid.getNode(mouseXCoordinate, mouseYCoordinate);
                currentNode.setObstacle(true);
            }
        }
        if (SwingUtilities.isRightMouseButton(e)) {
            if (currentNode != null && !currentNode.isFinish() && !currentNode.isStart()) {
                currentNode = grid.getNode(mouseXCoordinate, mouseYCoordinate);
                currentNode.setObstacle(false);
            }
        }
        this.revalidate();
        this.repaint();

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (Utils.IsSolving) {
            return;
        }
        int mouseXCoordinate = e.getX() / Node.size;
        int mouseYCoordinate = e.getY() / Node.size;

        if (mouseXCoordinate < 0 || mouseYCoordinate >= grid.getTotalColumns() || mouseYCoordinate < 0 || mouseXCoordinate >= grid.getTotalRows()) {
            return;
        }

        Node node = grid.getNode(mouseXCoordinate, mouseYCoordinate);

        if (Utils.IsSolving) {
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        } else if (node != null && (node.isStart() || node.isFinish())) {
            this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
        } else {
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        this.revalidate();
        this.repaint();
    }
}
