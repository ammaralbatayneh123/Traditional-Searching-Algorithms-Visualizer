import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;
public class AlgorithmExecutor extends Thread {
    private final Grid grid;
    private final JPanel formPanel;
    private boolean IsSolutionFound = false;

    public AlgorithmExecutor(Grid grid, JPanel formPanel) {
        this.grid = grid;
        this.formPanel = formPanel;
    }

    @Override
    public void run() {
        if (Utils.IsSolving) {
            Utils.breakAlgorithm = false;
            IsSolutionFound = false;
            switch (Utils.chosenAlgorithm) {
                case 0 -> BreadthFirstSearch(grid.getStartingNode());
                case 1 -> DepthFirstSearch(grid.getStartingNode());
                case 2 -> BestFirstSearch(grid.getStartingNode());
                case 3 -> AStar(grid.getStartingNode());
            }
        }
        Utils.IsSolving = false;
        if (Utils.breakAlgorithm) {
            grid.constructTheGrid();
        }
        formPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        formPanel.revalidate();
        formPanel.repaint();
    }

    private void AStar(Node startingNode) {
        TreeSet<Node> frontier = new TreeSet<>();
        frontier.add(startingNode);
        startingNode.setAlreadyVisited(true);

        while (Utils.IsSolving && !IsSolutionFound && !frontier.isEmpty()) {
            Node currentNode = frontier.pollFirst();

            if (currentNode != null) {
                currentNode.setNodeStatus(Status.CURRENTNODE);
            }

            formPanel.revalidate();
            formPanel.repaint();
            delayExecutionInMilliSeconds(Utils.totalDelay);

            if (currentNode != null) {
                if (currentNode.equals(grid.getTargetNode())) {
                    extractPathNodes(currentNode);
                    Utils.IsSolving = false;
                    IsSolutionFound = true;
                    return;
                } else {
                    currentNode.setNodeStatus(Status.VISITEDNODE);
                    for (Node childNode : currentNode.getNeighborNodes(grid)) {
                        frontier.add(childNode);
                        childNode.setAlreadyVisited(true);
                        childNode.setNodeStatus(Status.FRONTIERNODE);
                    }
                }
            }
        }
    }

    private void extractPathNodes(Node currentNode) {
        if (!Utils.IsSolving) {
            return;
        }

        Node parentNode = currentNode.getParentNode();

        while (!grid.getStartingNode().equals(parentNode)) {
            parentNode.setNodeStatus(Status.PATHNODE);
            formPanel.revalidate();
            formPanel.repaint();
            delayExecutionInMilliSeconds(10);
            parentNode = parentNode.getParentNode();
        }
        formPanel.revalidate();
        formPanel.repaint();
    }

    private void BestFirstSearch(Node startingNode) {
        TreeSet<Node> frontier = new TreeSet<>();
        frontier.add(startingNode);
        startingNode.setAlreadyVisited(true);

        while (Utils.IsSolving && !IsSolutionFound && !frontier.isEmpty()) {
            Node currentNode = frontier.pollFirst();

            if (currentNode != null){
                currentNode.setNodeStatus(Status.CURRENTNODE);
            }

            formPanel.revalidate();
            formPanel.repaint();
            delayExecutionInMilliSeconds(Utils.totalDelay);

            if (currentNode != null){
                if (currentNode.equals(grid.getTargetNode())) {
                    extractPathNodes(currentNode);
                    Utils.IsSolving = false;
                    IsSolutionFound = true;
                    return;
                } else {
                    currentNode.setNodeStatus(Status.VISITEDNODE);
                    for (Node childNode : currentNode.getNeighborNodes(grid)) {
                        frontier.add(childNode);
                        childNode.setAlreadyVisited(true);
                        childNode.setNodeStatus(Status.FRONTIERNODE);
                    }
                }
            }
        }
    }

    private void DepthFirstSearch(Node startingNode) {
        DepthFirstSearchUntil(startingNode);
    }

    private void DepthFirstSearchUntil(Node startingNode) {
        if (!Utils.IsSolving || IsSolutionFound) {
            return;
        }
        startingNode.setNodeStatus(Status.CURRENTNODE);
        startingNode.setAlreadyVisited(true);

        formPanel.revalidate();
        formPanel.repaint();
        delayExecutionInMilliSeconds(Utils.totalDelay);

        if (startingNode.equals(grid.getTargetNode())) {
            extractPathNodes(startingNode);
            Utils.IsSolving = false;
            IsSolutionFound = true;
        } else {
            startingNode.setNodeStatus(Status.VISITEDNODE);
            LinkedList<Node> childrenNodes = startingNode.getNeighborNodes(grid);
            for (Node childNode : childrenNodes) {
                if (!IsSolutionFound) {
                    for (Node temporaryNode : childrenNodes) {
                        if (temporaryNode.equals(childNode)) {
                            continue;
                        }
                        if (!temporaryNode.getNodeStatus().equals(Status.VISITEDNODE)) {
                            temporaryNode.setNodeStatus(Status.FRONTIERNODE);
                        }
                    }
                }
                DepthFirstSearchUntil(childNode);
            }
        }
    }

    private void BreadthFirstSearch(Node startingNode) {
        Queue<Node> frontier = new LinkedList<>();
        Node currentNode;
        frontier.add(startingNode);

        while (Utils.IsSolving && !frontier.isEmpty() && !IsSolutionFound) {
            currentNode = frontier.poll();
            currentNode.setNodeStatus(Status.CURRENTNODE);

            formPanel.revalidate();
            formPanel.repaint();
            delayExecutionInMilliSeconds(Utils.totalDelay);

            if (currentNode.equals(grid.getTargetNode())) {
                extractPathNodes(currentNode);
                Utils.IsSolving = false;
                IsSolutionFound = true;
            } else {
                currentNode.setNodeStatus(Status.VISITEDNODE);
                for (Node neighbor : currentNode.getNeighborNodes(grid)) {
                    frontier.add(neighbor);
                    neighbor.setNodeStatus(Status.FRONTIERNODE);
                    neighbor.setAlreadyVisited(true);
                }
            }
        }
    }

    public void delayExecutionInMilliSeconds(int delay) {
        try {
            Thread.sleep(delay);
            formPanel.repaint();
        } catch (InterruptedException ignored) {
        }
    }
}
