import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class BinaryTreeVisualizer extends JFrame {
    private TreePanel treePanel;
    private JButton addButton, deleteButton, bstButton, avlButton, findButton, clearPathButton;
    private JTextField inputField;
    private JLabel modeLabel;
    private boolean isAVL = false;
    private BinaryTree bstTree = new BinarySearchTree();
    private BinaryTree avlTree = new AVLTree();
    private java.util.List<Integer> highlightedPath = new ArrayList<>();

    public BinaryTreeVisualizer() {
        setTitle("🌳 Binary Tree Visualizer");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set custom look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create gradient background panel
        treePanel = new TreePanel();
        add(treePanel, BorderLayout.CENTER);

        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.SOUTH);

        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        setupButtonListeners();

        setVisible(true);
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(45, 45, 80));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        inputField = new JTextField(8);
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputField.setBackground(new Color(60, 60, 90));
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.WHITE);
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 120), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        addButton = createStyledButton("Add Node", new Color(50, 205, 50));
        deleteButton = createStyledButton("Delete Node", new Color(220, 20, 60));
        findButton = createStyledButton("Find Path", new Color(255, 165, 0));
        clearPathButton = createStyledButton("Clear Path", new Color(100, 149, 237));
        bstButton = createStyledButton("BST Mode", new Color(0, 191, 255));
        avlButton = createStyledButton("AVL Mode", new Color(138, 43, 226));

        JLabel nodeLabel = new JLabel("Node Value:");
        nodeLabel.setForeground(Color.WHITE);
        nodeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        controlPanel.add(nodeLabel);
        controlPanel.add(inputField);
        controlPanel.add(addButton);
        controlPanel.add(deleteButton);
        controlPanel.add(findButton);
        controlPanel.add(clearPathButton);
        controlPanel.add(bstButton);
        controlPanel.add(avlButton);

        return controlPanel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw background
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

                // Draw border
                g2d.setColor(new Color(20, 20, 40));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

                // Draw text
                super.paintComponent(g);
            }
        };

        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(30, 30, 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        modeLabel = new JLabel("Current Mode: BST");
        modeLabel.setForeground(Color.WHITE);
        modeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel titleLabel = new JLabel("Binary Tree Visualizer");
        titleLabel.setForeground(new Color(218, 165, 32));
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(modeLabel, BorderLayout.EAST);

        return topPanel;
    }

    private void setupButtonListeners() {
        addButton.addActionListener(e -> {
            try {
                int val = Integer.parseInt(inputField.getText());
                getCurrentTree().insert(val);
                treePanel.repaint();
                inputField.setText("");
            } catch (NumberFormatException ex) {
                showError("Please enter a valid integer.");
            }
        });

        deleteButton.addActionListener(e -> {
            try {
                int val = Integer.parseInt(inputField.getText());
                getCurrentTree().delete(val);
                highlightedPath.clear();
                treePanel.repaint();
                inputField.setText("");
            } catch (NumberFormatException ex) {
                showError("Please enter a valid integer.");
            }
        });

        findButton.addActionListener(e -> {
            try {
                int val = Integer.parseInt(inputField.getText());
                highlightedPath.clear();
                highlightedPath = getCurrentTree().findPath(val);
                treePanel.repaint();
                inputField.setText("");
            } catch (NumberFormatException ex) {
                showError("Please enter a valid integer.");
            }
        });

        clearPathButton.addActionListener(e -> {
            highlightedPath.clear();
            treePanel.repaint();
        });

        bstButton.addActionListener(e -> {
            isAVL = false;
            highlightedPath.clear();
            treePanel.repaint();
            modeLabel.setText("Current Mode: BST");
            JOptionPane.showMessageDialog(this, "BST Mode Enabled 🌳", "Mode Switched", JOptionPane.INFORMATION_MESSAGE);
        });

        avlButton.addActionListener(e -> {
            isAVL = true;
            highlightedPath.clear();
            treePanel.repaint();
            modeLabel.setText("Current Mode: AVL");
            JOptionPane.showMessageDialog(this, "AVL Tree Mode Enabled 🌲", "Mode Switched", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    private BinaryTree getCurrentTree() {
        return isAVL ? avlTree : bstTree;
    }

    class TreePanel extends JPanel {
        private final int nodeDiameter = 50;
        private final Color bgGradientStart = new Color(20, 20, 40);
        private final Color bgGradientEnd = new Color(40, 40, 80);

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Create gradient background
            GradientPaint gradient = new GradientPaint(0, 0, bgGradientStart, 0, getHeight(), bgGradientEnd);
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // Draw tree
            TreeNode root = getCurrentTree().getRoot();
            drawConnections(g2d, root, getWidth() / 2, 80, getWidth() / 4);
            drawNodes(g2d, root, getWidth() / 2, 80, getWidth() / 4);
        }

        private void drawConnections(Graphics2D g, TreeNode node, int x, int y, int xOffset) {
            if (node == null) return;

            // Draw lines to children first
            g.setStroke(new BasicStroke(2.5f));

            if (node.left != null) {
                boolean isOnPath = isOnPathBetween(node.val, node.left.val);
                g.setColor(isOnPath ? new Color(255, 215, 0) : new Color(120, 120, 180));
                g.drawLine(x, y, x - xOffset, y + 80);
                drawConnections(g, node.left, x - xOffset, y + 80, xOffset / 2);
            }

            if (node.right != null) {
                boolean isOnPath = isOnPathBetween(node.val, node.right.val);
                g.setColor(isOnPath ? new Color(255, 215, 0) : new Color(120, 120, 180));
                g.drawLine(x, y, x + xOffset, y + 80);
                drawConnections(g, node.right, x + xOffset, y + 80, xOffset / 2);
            }
        }

        private boolean isOnPathBetween(int parent, int child) {
            if (highlightedPath.isEmpty()) return false;
            int parentIndex = highlightedPath.indexOf(parent);
            int childIndex = highlightedPath.indexOf(child);
            return parentIndex >= 0 && childIndex >= 0 &&
                    Math.abs(parentIndex - childIndex) == 1;
        }

        private void drawNodes(Graphics2D g, TreeNode node, int x, int y, int xOffset) {
            if (node == null) return;

            boolean isHighlighted = highlightedPath.contains(node.val);

            // Draw node glow effect for highlighted nodes
            if (isHighlighted) {
                g.setColor(new Color(255, 215, 0, 100));
                g.fillOval(x - nodeDiameter/2 - 6, y - nodeDiameter/2 - 6,
                        nodeDiameter + 12, nodeDiameter + 12);
            }

            // Draw node shadow
            g.setColor(new Color(0, 0, 0, 50));
            g.fillOval(x - nodeDiameter/2 + 3, y - nodeDiameter/2 + 3,
                    nodeDiameter, nodeDiameter);

            // Create gradient for node
            Color nodeColor = isHighlighted ?
                    new Color(255, 215, 0) : new Color(120, 180, 255);
            Color nodeGradientEnd = isHighlighted ?
                    new Color(255, 165, 0) : new Color(65, 105, 225);

            GradientPaint nodeGradient = new GradientPaint(
                    x - nodeDiameter/2, y - nodeDiameter/2, nodeColor,
                    x + nodeDiameter/2, y + nodeDiameter/2, nodeGradientEnd);

            g.setPaint(nodeGradient);
            g.fillOval(x - nodeDiameter/2, y - nodeDiameter/2, nodeDiameter, nodeDiameter);

            // Draw node border
            g.setColor(isHighlighted ? new Color(255, 140, 0) : new Color(70, 130, 180));
            g.setStroke(new BasicStroke(2.0f));
            g.drawOval(x - nodeDiameter/2, y - nodeDiameter/2, nodeDiameter, nodeDiameter);

            // Draw node value
            String nodeText = Integer.toString(node.val);
            g.setFont(new Font("Segoe UI", Font.BOLD, 16));
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(nodeText);
            int textHeight = fm.getHeight();
            g.setColor(Color.WHITE);
            g.drawString(nodeText, x - textWidth/2, y + textHeight/4);

            // Draw children
            if (node.left != null) {
                drawNodes(g, node.left, x - xOffset, y + 80, xOffset / 2);
            }
            if (node.right != null) {
                drawNodes(g, node.right, x + xOffset, y + 80, xOffset / 2);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BinaryTreeVisualizer::new);
    }
}

class TreeNode {
    int val;
    TreeNode left, right;
    TreeNode(int val) {
        this.val = val;
    }
}

interface BinaryTree {
    void insert(int val);
    void delete(int val);
    TreeNode getRoot();
    java.util.List<Integer> findPath(int val);
}

class BinarySearchTree implements BinaryTree {
    private TreeNode root;

    public void insert(int val) {
        root = insertRecursive(root, val);
    }

    private TreeNode insertRecursive(TreeNode node, int val) {
        if (node == null) return new TreeNode(val);
        if (val < node.val) node.left = insertRecursive(node.left, val);
        else if (val > node.val) node.right = insertRecursive(node.right, val);
        return node;
    }

    public void delete(int val) {
        root = deleteRecursive(root, val);
    }

    private TreeNode deleteRecursive(TreeNode node, int val) {
        if (node == null) return null;
        if (val < node.val) node.left = deleteRecursive(node.left, val);
        else if (val > node.val) node.right = deleteRecursive(node.right, val);
        else {
            if (node.left == null) return node.right;
            else if (node.right == null) return node.left;
            TreeNode minNode = findMin(node.right);
            node.val = minNode.val;
            node.right = deleteRecursive(node.right, minNode.val);
        }
        return node;
    }

    private TreeNode findMin(TreeNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    public TreeNode getRoot() {
        return root;
    }

    public java.util.List<Integer> findPath(int val) {
        java.util.List<Integer> path = new ArrayList<>();
        findPathRecursive(root, val, path);
        return path;
    }

    private boolean findPathRecursive(TreeNode node, int val, java.util.List<Integer> path) {
        if (node == null) return false;

        path.add(node.val);

        if (node.val == val) return true;

        if ((node.left != null && findPathRecursive(node.left, val, path)) ||
                (node.right != null && findPathRecursive(node.right, val, path))) {
            return true;
        }

        path.remove(path.size() - 1);
        return false;
    }
}

class AVLTree implements BinaryTree {
    private TreeNode root;

    public void insert(int val) {
        root = insertAVL(root, val);
    }

    public void delete(int val) {
        root = deleteAVL(root, val);
    }

    public TreeNode getRoot() {
        return root;
    }

    public java.util.List<Integer> findPath(int val) {
        java.util.List<Integer> path = new ArrayList<>();
        findPathRecursive(root, val, path);
        return path;
    }

    private boolean findPathRecursive(TreeNode node, int val, java.util.List<Integer> path) {
        if (node == null) return false;

        path.add(node.val);

        if (node.val == val) return true;

        if ((node.left != null && findPathRecursive(node.left, val, path)) ||
                (node.right != null && findPathRecursive(node.right, val, path))) {
            return true;
        }

        path.remove(path.size() - 1);
        return false;
    }

    private TreeNode insertAVL(TreeNode node, int val) {
        if (node == null) return new TreeNode(val);
        if (val < node.val) node.left = insertAVL(node.left, val);
        else if (val > node.val) node.right = insertAVL(node.right, val);
        return balance(node);
    }

    private TreeNode deleteAVL(TreeNode node, int val) {
        if (node == null) return null;
        if (val < node.val) node.left = deleteAVL(node.left, val);
        else if (val > node.val) node.right = deleteAVL(node.right, val);
        else {
            if (node.left == null) return node.right;
            else if (node.right == null) return node.left;
            TreeNode minNode = findMin(node.right);
            node.val = minNode.val;
            node.right = deleteAVL(node.right, minNode.val);
        }
        return balance(node);
    }

    private int height(TreeNode node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    private int getBalance(TreeNode node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private TreeNode balance(TreeNode node) {
        int balance = getBalance(node);

        if (balance > 1) {
            if (getBalance(node.left) < 0) node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balance < -1) {
            if (getBalance(node.right) > 0) node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private TreeNode rotateLeft(TreeNode node) {
        TreeNode newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;
        return newRoot;
    }

    private TreeNode rotateRight(TreeNode node) {
        TreeNode newRoot = node.left;
        node.left = newRoot.right;
        newRoot.right = node;
        return newRoot;
    }

    private TreeNode findMin(TreeNode node) {
        while (node.left != null) node = node.left;
        return node;
    }
}