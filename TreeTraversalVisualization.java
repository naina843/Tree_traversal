package visualizer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

// Tree node structure
class TreeNode {
    Integer data;
    TreeNode left, right;

    public TreeNode(Integer data) {
        this.data = data;
        left = right = null;
    }
}

// Binary Tree class
class BinaryTree {
    TreeNode root;

    public BinaryTree() {
        root = null;
    }

    // Method to insert nodes in level order
    public void insertLevelOrder(Integer[] nodes) {
        if (nodes.length == 0) {
            return;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        root = new TreeNode(nodes[0]);
        queue.add(root);

        int i = 1;
        while (!queue.isEmpty() && i < nodes.length) {
            TreeNode currentNode = queue.poll();

            // Insert left child
            if (i < nodes.length && nodes[i] != null) {
                currentNode.left = new TreeNode(nodes[i]);
                queue.add(currentNode.left);
            }
            i++;

            // Insert right child
            if (i < nodes.length && nodes[i] != null) {
                currentNode.right = new TreeNode(nodes[i]);
                queue.add(currentNode.right);
            }
            i++;
        }
    }
}

// Binary Search Tree class
class BinarySearchTree {
    TreeNode root;

    public BinarySearchTree() {
        root = null;
    }

    // Method to insert nodes in BST order and balance the tree (AVL balance)
    public void insert(Integer data) {
        root = insertRecursive(root, data);
    }

    private TreeNode insertRecursive(TreeNode root, Integer data) {
        if (root == null) {
            return new TreeNode(data);
        }

        // Insert data into the appropriate subtree
        if (data < root.data) {
            root.left = insertRecursive(root.left, data);
        } else if (data > root.data) {
            root.right = insertRecursive(root.right, data);
        } else {
            // Duplicate data insertion is not allowed in BST; handle as needed
            return root;
        }

        // Update the height of this ancestor node
        root = balance(root);

        return root;
    }

    // Method to calculate the height of a node
    private int height(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    // Method to get the balance factor of a node
    private int balanceFactor(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return height(node.left) - height(node.right);
    }

    // Method to perform right rotation
    private TreeNode rotateRight(TreeNode y) {
        TreeNode x = y.left;
        TreeNode T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        return x;
    }

    // Method to perform left rotation
    private TreeNode rotateLeft(TreeNode x) {
        TreeNode y = x.right;
        TreeNode T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        return y;
    }

    // Method to balance the AVL tree
    private TreeNode balance(TreeNode node) {
        // Check the balance factor of the current node
        int balanceFactor = balanceFactor(node);

        // If the node becomes unbalanced, perform rotations
        if (balanceFactor > 1 && balanceFactor(node.left) >= 0) {
            return rotateRight(node);
        }
        if (balanceFactor > 1 && balanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balanceFactor < -1 && balanceFactor(node.right) <= 0) {
            return rotateLeft(node);
        }
        if (balanceFactor < -1 && balanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        // Return the unchanged node if balanced
        return node;
    }
}


// Class for tree traversal algorithms
class TreeTraversal {
    private StringBuilder traversalPath;

    public TreeTraversal() {
        traversalPath = new StringBuilder();
    }

    // Method to perform inorder traversal
    public String inorder(TreeNode root) {
        traversalPath.setLength(0); // Clear previous results
        inorderRecursive(root);
        return traversalPath.toString().trim();
    }

    private void inorderRecursive(TreeNode root) {
        if (root != null) {
            inorderRecursive(root.left);
            traversalPath.append(root.data).append(" ");
            inorderRecursive(root.right);
        }
    }

    // Method to perform preorder traversal
    public String preorder(TreeNode root) {
        traversalPath.setLength(0); // Clear previous results
        preorderRecursive(root);
        return traversalPath.toString().trim();
    }

    private void preorderRecursive(TreeNode root) {
        if (root != null) {
            traversalPath.append(root.data).append(" ");
            preorderRecursive(root.left);
            preorderRecursive(root.right);
        }
    }

    // Method to perform postorder traversal
    public String postorder(TreeNode root) {
        traversalPath.setLength(0); // Clear previous results
        postorderRecursive(root);
        return traversalPath.toString().trim();
    }

    private void postorderRecursive(TreeNode root) {
        if (root != null) {
            postorderRecursive(root.left);
            postorderRecursive(root.right);
            traversalPath.append(root.data).append(" ");
        }
    }

    // Method to perform level-order traversal (Breadth-First Search)
    public String levelOrder(TreeNode root) {
        traversalPath.setLength(0); // Clear previous results

        if (root == null) {
            return "";
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            TreeNode current = queue.poll();
            traversalPath.append(current.data).append(" ");

            if (current.left != null) {
                queue.add(current.left);
            }
            if (current.right != null) {
                queue.add(current.right);
            }
        }

        return traversalPath.toString().trim();
    }
}

// JavaFX application class for visualization
public class TreeTraversalVisualization extends Application {

    private CheckBox bstModeCheckbox;
    private BinaryTree binaryTree;
    private BinarySearchTree binarySearchTree;
    private TreeTraversal traversal;
    private GraphicsContext gc;
    private Timeline animation;
    private TextArea resultTextArea; // TextArea object to display traversal result

    @Override
    public void start(Stage primaryStage) {
        binaryTree = new BinaryTree();
        binarySearchTree = new BinarySearchTree();
        traversal = new TreeTraversal();

        primaryStage.setTitle("Binary Tree Traversal Visualization");

        // Set up the main layout
        BorderPane mainLayout = new BorderPane();

        // Canvas for drawing the tree
        Canvas canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();

        // Control Panel
        VBox controlsBox = new VBox(15);
        controlsBox.setPadding(new Insets(20));
        controlsBox.setStyle("-fx-background-color: #2c3e50;");
        controlsBox.setPrefWidth(300);

        Label inputLabel = new Label("Enter nodes (comma-separated):");
        inputLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #ecf0f1;");
        TextField nodeInputField = new TextField();
        nodeInputField.setStyle("-fx-font-size: 14px; -fx-background-color: #ecf0f1;");
        ComboBox<String> traversalComboBox = new ComboBox<>();
        traversalComboBox.getItems().addAll("Inorder", "Preorder", "Postorder", "Level-order");
        traversalComboBox.setValue("Inorder");
        traversalComboBox.setStyle("-fx-font-size: 14px;");
        bstModeCheckbox = new CheckBox("Binary Search Tree Mode");
        bstModeCheckbox.setStyle("-fx-font-size: 14px; -fx-text-fill: #ecf0f1;");
        Button visualizeButton = new Button("Visualize Traversal");
        visualizeButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px;");

        // New buttons for additional features
        Button topViewButton = new Button("Top View");
        topViewButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px;");
        Button bottomViewButton = new Button("Bottom View");
        bottomViewButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px;");
        Button rightViewButton = new Button("Right View");
        rightViewButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px;");
        Button leftViewButton = new Button("Left View");
        leftViewButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px;");
        Button boundaryTraversalButton = new Button("Boundary Traversal");
        boundaryTraversalButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px;");

        // TextArea for displaying traversal result
        resultTextArea = new TextArea();
        resultTextArea.setEditable(false);
        resultTextArea.setStyle("-fx-font-size: 14px; -fx-text-fill: #ecf0f1; -fx-control-inner-background: #34495e;");
        resultTextArea.setWrapText(true);
        resultTextArea.setPrefHeight(100);

        controlsBox.getChildren().addAll(
                inputLabel, nodeInputField, traversalComboBox, bstModeCheckbox,
                visualizeButton, topViewButton, bottomViewButton, rightViewButton,
                leftViewButton, boundaryTraversalButton, resultTextArea);

        mainLayout.setLeft(controlsBox);
        mainLayout.setCenter(canvas);

        Scene scene = new Scene(mainLayout, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Button action for Visualize Traversal
        visualizeButton.setOnAction(event -> {
            try {
                String input = nodeInputField.getText().trim();
                if (!input.isEmpty()) {
                    String[] nodesStr = input.split(",");
                    Integer[] nodes = new Integer[nodesStr.length];
                    for (int i = 0; i < nodesStr.length; i++) {
                        nodes[i] = nodesStr[i].trim().isEmpty() ? null : Integer.parseInt(nodesStr[i].trim());
                    }

                    if (bstModeCheckbox.isSelected()) {
                        // Insert into Binary Search Tree
                        binarySearchTree = new BinarySearchTree();
                        for (Integer node : nodes) {
                            if (node != null) {
                                binarySearchTree.insert(node);
                            }
                        }
                        visualizeTraversal(traversalComboBox.getValue(), binarySearchTree.root);
                    } else {
                        // Insert into Binary Tree
                        binaryTree.insertLevelOrder(nodes);
                        visualizeTraversal(traversalComboBox.getValue(), binaryTree.root);
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter integers separated by commas.");
            }
        });

        // Button actions for additional features
        topViewButton.setOnAction(event -> {
            visualizeTopView(bstModeCheckbox.isSelected() ? binarySearchTree.root : binaryTree.root);
        });

        bottomViewButton.setOnAction(event -> {
            visualizeBottomView(bstModeCheckbox.isSelected() ? binarySearchTree.root : binaryTree.root);
        });

        rightViewButton.setOnAction(event -> {
            visualizeRightView(bstModeCheckbox.isSelected() ? binarySearchTree.root : binaryTree.root);
        });

        leftViewButton.setOnAction(event -> {
            visualizeLeftView(bstModeCheckbox.isSelected() ? binarySearchTree.root : binaryTree.root);
        });

        boundaryTraversalButton.setOnAction(event -> {
            visualizeBoundaryTraversal(bstModeCheckbox.isSelected() ? binarySearchTree.root : binaryTree.root);
        });
    }

    private void visualizeTraversal(String traversalType, TreeNode root) {
        gc.clearRect(0, 0, 800, 600);

        String traversalResult = "";

        switch (traversalType) {
            case "Inorder":
                traversalResult = traversal.inorder(root);
                resultTextArea.setText("Inorder Traversal Result:\n" + traversalResult);
                break;
            case "Preorder":
                traversalResult = traversal.preorder(root);
                resultTextArea.setText("Preorder Traversal Result:\n" + traversalResult);
                break;
            case "Postorder":
                traversalResult = traversal.postorder(root);
                resultTextArea.setText("Postorder Traversal Result:\n" + traversalResult);
                break;
            case "Level-order":
                traversalResult = traversal.levelOrder(root);
                resultTextArea.setText("Level-order Traversal Result:\n" + traversalResult);
                break;
        }

        animateTraversal(traversalResult);
    }
 // Method to visualize Top View
    private void visualizeTopView(TreeNode root) {
        gc.clearRect(0, 0, 800, 600);

        if (root == null) {
            resultTextArea.setText("Top View: Tree is empty");
            return;
        }

        Map<Integer, Integer> topViewMap = new TreeMap<>();
        Queue<TreeNode> queue = new LinkedList<>();
        Queue<Integer> levelQueue = new LinkedList<>();

        queue.add(root);
        levelQueue.add(0);

        while (!queue.isEmpty()) {
            TreeNode current = queue.poll();
            int level = levelQueue.poll();

            if (!topViewMap.containsKey(level)) {
                topViewMap.put(level, current.data);
            }

            if (current.left != null) {
                queue.add(current.left);
                levelQueue.add(level - 1);
            }

            if (current.right != null) {
                queue.add(current.right);
                levelQueue.add(level + 1);
            }
        }

        resultTextArea.setText("Top View:");
        double x = 400; // starting x position
        double y = 50; // starting y position

        for (Map.Entry<Integer, Integer> entry : topViewMap.entrySet()) {
            drawTreePosition(x, y, entry.getValue().toString());
            x += 50; // spacing between nodes
        }
    }

    // Helper method to draw tree nodes
    private void drawTreePosition(double x, double y, String nodeValue) {
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.fillOval(x, y, 30, 30);
        gc.strokeOval(x, y, 30, 30);
        gc.setFill(Color.BLACK);
        gc.fillText(nodeValue, x + 10, y + 18);
    }

 // Method to visualize Bottom View
    private void visualizeBottomView(TreeNode root) {
        gc.clearRect(0, 0, 800, 600);

        if (root == null) {
            resultTextArea.setText("Bottom View: Tree is empty");
            return;
        }

        Map<Integer, Integer> bottomViewMap = new TreeMap<>();
        Queue<TreeNode> queue = new LinkedList<>();
        Queue<Integer> levelQueue = new LinkedList<>();

        queue.add(root);
        levelQueue.add(0);

        while (!queue.isEmpty()) {
            TreeNode current = queue.poll();
            int level = levelQueue.poll();

            bottomViewMap.put(level, current.data);

            if (current.left != null) {
                queue.add(current.left);
                levelQueue.add(level - 1);
            }

            if (current.right != null) {
                queue.add(current.right);
                levelQueue.add(level + 1);
            }
        }

        resultTextArea.setText("Bottom View:");
        double x = 400; // starting x position
        double y = 500; // starting y position

        for (Map.Entry<Integer, Integer> entry : bottomViewMap.entrySet()) {
            drawTreePosition(x, y, entry.getValue().toString());
            x += 50; // spacing between nodes
        }
    }


    private void animateTraversal(String traversalResult) {
        if (animation != null) {
           
            animation.stop();
        }

        animation = new Timeline();
        animation.setCycleCount(traversalResult.split(" ").length);
        String[] nodes = traversalResult.split(" ");
        for (int i = 0; i < nodes.length; i++) {
            final int index = i;
            KeyFrame frame = new KeyFrame(Duration.seconds(i + 1), event -> {
                gc.clearRect(0, 0, 800, 600);
                if (bstModeCheckbox.isSelected()) {
                    drawTree(binarySearchTree.root, 400, 50, 200, nodes[index]);
                } else {
                    drawTree(binaryTree.root, 400, 50, 200, nodes[index]);
                }
            });
            animation.getKeyFrames().add(frame);
        }
        animation.play();
    }
 // Method to visualize Right View
    private void visualizeRightView(TreeNode root) {
        gc.clearRect(0, 0, 800, 600);

        if (root == null) {
            resultTextArea.setText("Right View: Tree is empty");
            return;
        }

        Map<Integer, Integer> rightViewMap = new TreeMap<>();
        visualizeRightViewRecursive(root, rightViewMap, 0);

        resultTextArea.setText("Right View:");
        double x = 750; // starting x position
        double y = 300; // starting y position

        for (Map.Entry<Integer, Integer> entry : rightViewMap.entrySet()) {
            drawTreePosition(x, y, entry.getValue().toString());
            y += 50; // spacing between nodes
        }
    }

    // Helper method for recursive traversal to get Right View
    private void visualizeRightViewRecursive(TreeNode node, Map<Integer, Integer> rightViewMap, int level) {
        if (node == null) {
            return;
        }

        rightViewMap.put(level, node.data);

        visualizeRightViewRecursive(node.left, rightViewMap, level + 1);
        visualizeRightViewRecursive(node.right, rightViewMap, level + 1);
    }

 // Method to visualize Left View
    private void visualizeLeftView(TreeNode root) {
        gc.clearRect(0, 0, 800, 600);

        if (root == null) {
            resultTextArea.setText("Left View: Tree is empty");
            return;
        }

        Map<Integer, Integer> leftViewMap = new TreeMap<>();
        visualizeLeftViewRecursive(root, leftViewMap, 0);

        resultTextArea.setText("Left View:");
        double x = 50; // starting x position
        double y = 300; // starting y position

        for (Map.Entry<Integer, Integer> entry : leftViewMap.entrySet()) {
            drawTreePosition(x, y, entry.getValue().toString());
            y += 50; // spacing between nodes
        }
    }
 // Method to visualize Boundary Traversal
    private void visualizeBoundaryTraversal(TreeNode root) {
        gc.clearRect(0, 0, 800, 600);

        if (root == null) {
            resultTextArea.setText("Boundary Traversal: Tree is empty");
            return;
        }

        resultTextArea.setText("Boundary Traversal:");
        List<Integer> boundaryNodes = new ArrayList<>();

        // Add root
        boundaryNodes.add(root.data);

        // Add left boundary
        addLeftBoundary(root.left, boundaryNodes);

        // Add leaf nodes
        addLeafNodes(root, boundaryNodes);

        // Add right boundary
        addRightBoundary(root.right, boundaryNodes);

        double x = 400; // starting x position
        double y = 50; // starting y position

        for (int i = 0; i < boundaryNodes.size(); i++) {
            drawTreePosition(x, y, boundaryNodes.get(i).toString());
            x += 50; // spacing between nodes
        }
    }

    // Helper method to add left boundary nodes
    private void addLeftBoundary(TreeNode node, List<Integer> boundaryNodes) {
        if (node == null) {
            return;
        }

        if (node.left != null || node.right != null) {
            boundaryNodes.add(node.data);
        }

        addLeftBoundary(node.left, boundaryNodes);
    }

    // Helper method to add leaf nodes
    private void addLeafNodes(TreeNode node, List<Integer> boundaryNodes) {
        if (node == null) {
            return;
        }

        if (node.left == null && node.right == null) {
            boundaryNodes.add(node.data);
        }

        addLeafNodes(node.left, boundaryNodes);
        addLeafNodes(node.right, boundaryNodes);
    }

    // Helper method to add right boundary nodes
    private void addRightBoundary(TreeNode node, List<Integer> boundaryNodes) {
        if (node == null) {
            return;
        }

        if (node.left != null || node.right != null) {
            boundaryNodes.add(node.data);
        }

        addRightBoundary(node.right, boundaryNodes);
    }

    // Helper method for recursive traversal to get Left View
    private void visualizeLeftViewRecursive(TreeNode node, Map<Integer, Integer> leftViewMap, int level) {
        if (node == null) {
            return;
        }

        if (!leftViewMap.containsKey(level)) {
            leftViewMap.put(level, node.data);
        }

        visualizeLeftViewRecursive(node.left, leftViewMap, level + 1);
        visualizeLeftViewRecursive(node.right, leftViewMap, level + 1);
    }

    private void drawTree(TreeNode node, double x, double y, double xOffset, String currentTraversalNode) {
        if (node != null) {
            gc.setFill(Color.WHITE);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(2);
            gc.fillOval(x, y, 30, 30);
            gc.strokeOval(x, y, 30, 30);
            gc.setFill(Color.BLACK);
            gc.fillText(node.data == null ? "null" : node.data.toString(), x + 10, y + 18);

            // Highlight current traversal node
            if (!currentTraversalNode.isEmpty() && node.data != null && currentTraversalNode.equals(node.data.toString())) {
                gc.setFill(Color.LIGHTGREEN);
                gc.fillOval(x, y, 30, 30);
                gc.setFill(Color.BLACK);
                gc.fillText(currentTraversalNode, x + 10, y + 18);
            }

            if (node.left != null) {
                double xLeft = x - xOffset;
                double yLeft = y + 60;
                gc.strokeLine(x + 15, y + 30, xLeft + 15, yLeft);
                drawTree(node.left, xLeft, yLeft, xOffset / 2, currentTraversalNode);
            }

            if (node.right != null) {
                double xRight = x + xOffset;
                double yRight = y + 60;
                gc.strokeLine(x + 15, y + 30, xRight + 15, yRight);
                drawTree(node.right, xRight, yRight, xOffset / 2, currentTraversalNode);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
