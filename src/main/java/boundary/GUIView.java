package boundary;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import control.UserManager;
import entity.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Main GUI view for the Smart Personal Finance Manager application.
 * Provides the main window layout with navigation menu and content area.
 */
public class GUIView extends JFrame {
    /** Card layout manager for switching between different content panels */
    private static CardLayout cardLayout;
    /** Panel containing all content views */
    private static JPanel contentPanel;
    /** Manager for user-related operations */
    private UserManager userManager;
    /** ID of the currently logged-in user */
    private String currentUserId;
    
    /**
     * Constructs the main GUI view.
     * Initializes the window layout, navigation menu, and content panels.
     */
    public GUIView() {
        // 初始化 UserManager 和获取当前用户信息
        userManager = UserManager.getInstance();
        currentUserId = userManager.getCurrentUserId();
        User currentUser = userManager.getUserById(currentUserId);

        // 创建主窗口
        setTitle("Smart Personal Finance Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // 创建顶部标题面板
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("Smart Personal Finance Manager");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // 创建返回Home按钮
        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 切换到Home页面
                cardLayout.show(contentPanel, "Home");
            }
        });
        titlePanel.add(homeButton, BorderLayout.WEST);

        // 创建圆形用户按钮
        JButton userButton = new JButton("U");
        userButton.setPreferredSize(new Dimension(30, 30));
        userButton.setBorder(new EllipseBorder(Color.BLACK, 2));
        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 切换到Profile页面
                cardLayout.show(contentPanel, "Profile");
            }
        });
        titlePanel.add(userButton, BorderLayout.EAST);

        // 初始化目录结构
        IndexView indexView = new IndexView();
        List<Directory> directories = indexView.getDirectories();

        // 创建左侧菜单栏
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Menu");
        JTree menuTree = new JTree(root);
        menuTree.setShowsRootHandles(true);
        JScrollPane treeScrollPane = new JScrollPane(menuTree);

        // 创建右侧控制面板区域，使用CardLayout管理不同页面
        contentPanel = new JPanel(new CardLayout());
        cardLayout = (CardLayout) contentPanel.getLayout();

        for (Directory dir : directories) {
            DefaultMutableTreeNode dirNode = new DefaultMutableTreeNode(dir.getName());
            root.add(dirNode);
            for (Entry entry : dir.getEntries()) {
                DefaultMutableTreeNode entryNode = new DefaultMutableTreeNode(entry.getName());
                dirNode.add(entryNode);
                JPanel panel = entry.getTargetPanel();
                if (panel != null && entry.getName() != null) {
                    contentPanel.add(panel, entry.getName());
                    entry.init(); // 调用 init 方法
                } else {
                    System.err.println("Error: Panel or name is null for entry: " + entry);
                }
            }
        }

        // 为树添加选择监听器
        menuTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        menuTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) menuTree.getLastSelectedPathComponent();
            if (selectedNode != null && selectedNode.getUserObject() instanceof String) {
                String selectedPage = (String) selectedNode.getUserObject();
                if (!selectedPage.equals("Menu")) {
                    cardLayout.show(contentPanel, selectedPage);
                }
            }
        });

        // 将顶部标题面板、左侧菜单栏和右侧控制面板添加到主窗口
        setLayout(new BorderLayout());
        add(titlePanel, BorderLayout.NORTH);
        add(treeScrollPane, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // 显示窗口
        setVisible(true);
    }

    /**
     * Main method for launching the application.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GUIView();
        });
    }
}