import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class Node implements Comparable<Node> {

    // x coordinate of the node
    private final int nodeCoordinateX;
    // y coordinate of the node
    private final int nodeCoordinateY;
    // cost of the path from the start node to the current node.
    private int G;
    // estimated cost of the path from the current node to the goal node.
    private double H;
    // represents the total cost of the path from the start node to the goal node
    private double F;
    // the color of the node is decided by its type
    private Status nodeStatus = Status.DEFAULTNODE;
    private Node parentNode;

    private boolean isObstacle = false;
    private boolean isStart = false;
    private boolean isFinish = false;
    private boolean alreadyVisited;
    // size of the node 30 pixels so it can appear as square in the grid
    public static int size = 30;

    public boolean isStart() {
        return isStart;
    }

    public void setNodeStatus(Status nodeStatus) {
        if (isStart || isFinish) {
            return;
        }
        this.nodeStatus = nodeStatus;
    }

    public void setAlreadyVisited(boolean alreadyVisited) {
        this.alreadyVisited = alreadyVisited;
    }

    public Status getNodeStatus() {
        return nodeStatus;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public static int getSize() {
        return size;
    }

    public void setStart(boolean isStart) {
        if (isStart) {
            this.nodeStatus = Status.STARTINGNODE;
        } else {
            this.nodeStatus = Status.DEFAULTNODE;
        }
        this.isStart = isStart;
    }

    public void setFinish(boolean isFinish) {
        if (isFinish) {
            this.nodeStatus = Status.FINISHINGNODE;
        } else {
            this.nodeStatus = Status.DEFAULTNODE;
        }
        this.isFinish = isFinish;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public Node(int nodeXCoordinate, int nodeYCoordinate) {
        this.nodeCoordinateX = nodeXCoordinate;
        this.nodeCoordinateY = nodeYCoordinate;
    }

    public LinkedList<Node> getNeighborNodes(Grid grid){
        LinkedList<Node> neighborNodes = new LinkedList<>();
        if (this.nodeCoordinateX + 1 < grid.getTotalRows() && nodeCoordinateX >= 0) {
            Node node = grid.getNode(this.nodeCoordinateX + 1, this.nodeCoordinateY);
            if (!node.alreadyVisited && !node.isObstacle) {
                createNeighbor(node, grid);
                neighborNodes.add(node);
            }
        }

        if (this.nodeCoordinateY + 1 < grid.getTotalColumns() && this.nodeCoordinateY >= 0) {
            Node node = grid.getNode(this.nodeCoordinateX, this.nodeCoordinateY + 1);
            if (!node.alreadyVisited && !node.isObstacle) {
                createNeighbor(node, grid);
                neighborNodes.add(node);
            }
        }

        if (this.nodeCoordinateY - 1 >= 0 && this.nodeCoordinateY < grid.getTotalColumns()) {
            Node node = grid.getNode(this.nodeCoordinateX, this.nodeCoordinateY - 1);
            if (!node.alreadyVisited && !node.isObstacle) {
                createNeighbor(node, grid);
                neighborNodes.add(node);
            }
        }

        if (this.nodeCoordinateX - 1 >= 0 && this.nodeCoordinateX < grid.getTotalRows()) {
            Node node = grid.getNode(this.nodeCoordinateX - 1, this.nodeCoordinateY);
            if (!node.alreadyVisited && !node.isObstacle) {
                createNeighbor(node, grid);
                neighborNodes.add(node);
            }
        }

        if (Utils.enableDiagonals) {
            if (this.nodeCoordinateX - 1 >= 0 && this.nodeCoordinateY - 1 >= 0) {
                Node node = grid.getNode(this.nodeCoordinateX - 1, this.nodeCoordinateY - 1);
                if (!node.alreadyVisited && !node.isObstacle) {
                    createNeighbor(node, grid);
                    neighborNodes.add(node);
                }
            }
            if (this.nodeCoordinateX - 1 >= 0 && this.nodeCoordinateY + 1 < grid.getTotalColumns()) {
                Node node = grid.getNode(this.nodeCoordinateX - 1, this.nodeCoordinateY + 1);
                if (!node.alreadyVisited && !node.isObstacle) {
                    createNeighbor(node, grid);
                    neighborNodes.add(node);
                }
            }
            if (this.nodeCoordinateX + 1 < grid.getTotalRows() && this.nodeCoordinateY + 1 < grid.getTotalColumns()) {
                Node node = grid.getNode(this.nodeCoordinateX + 1, this.nodeCoordinateY + 1);
                if (!node.alreadyVisited && !node.isObstacle) {
                    createNeighbor(node, grid);
                    neighborNodes.add(node);
                }
            }

            if (this.nodeCoordinateX + 1 < grid.getTotalRows() && this.nodeCoordinateY - 1 >= 0) {
                Node node = grid.getNode(this.nodeCoordinateX + 1, this.nodeCoordinateY - 1);
                if (!node.alreadyVisited && !node.isObstacle) {
                    createNeighbor(node, grid);
                    neighborNodes.add(node);
                }
            }
        }
        return neighborNodes;
    }

    // creating neighbor nodes and setting its properties
    // sets the parent node of the neighboring node to the current node,
    // and sets the cost G of the path from the starting node to the neighboring node by
    // adding the cost of the path from the starting node to the current node to the cost
    // of moving from the current node to the neighboring node.
    // also we estimate the cost H of the path from the neighboring node to the goal node by calling the heuristic method.
    //  then we calculate the total estimated cost F of the path from the starting node to
    //  the goal node by adding the cost of the path from the starting node to the neighboring
    //  node and the estimated cost of the path from the neighboring node to the goal node.
    public void createNeighbor(Node node, Grid grid) {
        node.setParentNode(this);
        node.setG(this.G + 1);
        node.setH(node.heuristic(grid));
        node.setF();
    }

    public int getNodeCoordinateX() {
        return nodeCoordinateX;
    }

    public int getNodeCoordinateY() {
        return nodeCoordinateY;
    }

    // calculate an estimate of the cost of the cheapest path from the current node to the goal node using 2
    // heuristic algorithms 1) manhattan distance    2) Euclidean distance
    private double heuristic(Grid grid) {
        double distance;
        if (Utils.enableDiagonals) {
            // here we used the Euclidean distance to calculate distance between the current node and the goal node
            // the algorithm is based on calculating the square root of the sum of the
            // squares of the horizontal and vertical distances.
            distance = Math.hypot(Math.abs(this.nodeCoordinateX - grid.getTargetNode().getNodeCoordinateX()),
                    Math.abs(this.nodeCoordinateY - grid.getTargetNode().getNodeCoordinateY()));
        } else {
            // here we used the manhattan distance algorithm
            // manhattan distance is calculated by adding the absolute differences of the x and y
            // coordinates of the current node and the goal node.
            distance = Math.abs(this.nodeCoordinateX - grid.getTargetNode().getNodeCoordinateX()) + Math.abs(this.nodeCoordinateY- grid.getTargetNode().getNodeCoordinateY());
        }
        return distance;
    }

    // this method draws the color on the cell based on its type
    public void drawCellColor(Graphics2D graphics2D, JPanel panel) {
        graphics2D.setColor(Color.black);
        if (isObstacle) {
            graphics2D.fillRect(nodeCoordinateX * size, nodeCoordinateY * size, size, size);
        }

        switch (nodeStatus) {
            case STARTINGNODE:
                graphics2D.setColor(Color.orange);
                break;
            case VISITEDNODE:
                graphics2D.setColor(Color.cyan);
                break;
            case CURRENTNODE:
                graphics2D.setColor(Color.magenta);
                break;
            case FINISHINGNODE:
                graphics2D.setColor(Color.red);
                break;
            case FRONTIERNODE:
                graphics2D.setColor(Color.GREEN);
                break;
            case PATHNODE:
                graphics2D.setColor(Color.yellow);
                break;

            default:
                return;
        }
        graphics2D.setStroke(new BasicStroke(1.5f));
        graphics2D.fillRect(nodeCoordinateX * size, nodeCoordinateY * size, size, size);
        panel.revalidate();
        panel.repaint();
    }

    // this method is used to compare 2 nodes i used to check if the current node is the goal node
    //based on the X and Y coordinates of the 2 nodes
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Node other = (Node) obj;
        return nodeCoordinateX == other.nodeCoordinateX && nodeCoordinateY == other.nodeCoordinateY;
    }

    public double getH() {
        return H;
    }

    public double getF() {
        return F;
    }

    @Override
    public int compareTo(Node n2) {
        switch (Utils.chosenAlgorithm) {
            case 2 -> {
                if (this.equals(n2)) {
                    return 0;
                }
                if (this.getH() > n2.getH()) {
                    return 1;
                } else if (this.getH() < n2.getH()) {
                    return -1;
                } else {
                    return 1;
                }
            }
            case 3 -> {
                if (this.equals(n2)) {
                    return 0;
                }
                if (this.getF() > n2.getF()) {
                    return 1;
                } else if (this.getF() < n2.getF()) {
                    return -1;
                } else {
                    return 1;
                }
            }
            default -> {
            }
        }
        return 0;
    }
    public void setG(int g) {
        G = g;
    }

    public void setH(double h) {
        H = h;
    }

    public void setF() {
        this.F = this.G + this.H;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    public void setObstacle(boolean IsObstacle) {
        if (isObstacle) {
            this.setNodeStatus(Status.OBSTACLENODE);
        } else {
            this.setNodeStatus(Status.DEFAULTNODE);
        }
        this.isObstacle = IsObstacle;
    }
}
