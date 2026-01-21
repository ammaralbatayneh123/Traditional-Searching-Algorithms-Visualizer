import javax.swing.*;
import java.awt.*;

public class Grid {
    private final int totalRows;
    private final int totalColumns;

    private final Node[][] grid;
    private Node startingNode;
    private Node targetNode;

    public Grid(int totalRows, int totalColumns) {
        this.totalRows = totalRows;
        this.totalColumns = totalColumns;
        grid = new Node[totalRows][totalColumns];
        constructTheGrid();
    }

    public void constructTheGrid() {
        for (int i = 0; i < totalRows; i++) {
            for (int j = 0; j < totalColumns; j++) {
                grid[i][j] = new Node(i, j);
            }
        }
        startingNode = grid[0][0];
        startingNode.setStart(true);
        targetNode = grid[totalRows - 1][totalColumns - 1];
        targetNode.setFinish(true);
    }

    public void removeObstacles() {
        for (int i = 0; i < totalRows; i++) {
            for (int j = 0; j < totalColumns; j++) {
                grid[i][j].setObstacle(false);
            }
        }
    }

    public void resetPath() {
        for (int i = 0; i < totalRows; i++) {
            for (int j = 0; j < totalColumns; j++) {
                Node node = grid[i][j];
                node.setParentNode(null);
                node.setAlreadyVisited(false);
                node.setNodeStatus(Status.DEFAULTNODE);
                node.setG(0);
                node.setH(0);
                node.setF();
            }
        }
    }

    public void renderTheGrid(Graphics2D graphics2D, JPanel gridPanel) {
        for (int i = 0; i < totalRows; i++) {
            for (int j = 0; j < totalColumns; j++) {
                grid[i][j].drawCellColor(graphics2D, gridPanel);
                graphics2D.setColor(Color.black);
                graphics2D.setStroke(new BasicStroke(1.5f));
                graphics2D.drawRect(i * Node.getSize(), j * Node.getSize(), Node.getSize(), Node.getSize());
            }
        }
        startingNode.drawCellColor(graphics2D, gridPanel);
        targetNode.drawCellColor(graphics2D, gridPanel);
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    public int getTotalRows() {
        return totalRows;
    }

    public int getTotalColumns() {
        return totalColumns;
    }

    public Node getStartingNode() {
        return startingNode;
    }

    public Node getTargetNode() {
        return targetNode;
    }

    public void setStart(Node startingNode) {
        if (startingNode.equals(targetNode)) {
            return;
        }
        this.startingNode.setStart(false);
        this.startingNode = startingNode;
        this.startingNode.setStart(true);
    }

    public void setFinish(Node finishingNode) {
        if (startingNode.equals(finishingNode)) {
            return;
        }
        this.targetNode.setFinish(false);
        this.targetNode = finishingNode;
        this.targetNode.setFinish(true);
    }

    public Node getNode(int nodeCoordinateX, int nodeCoordinateY) {
        return grid[nodeCoordinateX][nodeCoordinateY];
    }
}
