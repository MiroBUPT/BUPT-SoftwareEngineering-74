package boundary;

/*public class AIAdvicePanel extends Panel {
    public AIAdvicePanel(Color border, Color fill) {
        super(border, fill);
    }

    @Override
    public void init() {
        System.out.println("AI建议面板初始化");
    }

    @Override
    public void updateData() {
        System.out.println("AI建议面板数据更新到最新数据");
    }
}*/

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import control.AIManager;

/**
 * Panel for generating and displaying AI-powered financial advice.
 * Provides different types of financial analysis and recommendations.
 */
public class AIAdvicePanel extends JPanel {
    /** Button for generating general financial advice */
    private JButton generateButtonGeneral;
    /** Button for generating budget analysis */
    private JButton generateButtonBudget;
    /** Button for generating consumption analysis */
    private JButton generateButtonConsumption;
    /** Button for generating savings analysis */
    private JButton generateButtonSavings;
    /** Button for generating long-term analysis */
    private JButton generateButtonLongTerm;
    /** Button for generating holiday advice */
    private JButton generateButtonHoliday;
    /** Label for displaying generation status */
    private JLabel statusLabel;
    /** Text area for displaying generated advice */
    private JTextArea adviceArea;

    /**
     * Constructs a new AIAdvicePanel with specified colors.
     * @param borderColor The color for the panel's border
     * @param fillColor The background color for the panel
     */
    public AIAdvicePanel(Color borderColor, Color fillColor) {
        System.out.println("Initializing AIAdvicePanel...");
        this.setBorder(BorderFactory.createLineBorder(borderColor));
        this.setBackground(fillColor);
        setLayout(new GridLayout(2, 3, 10, 10)); // 2 rows, 3 columns with gaps

        // Create six blocks
        System.out.println("Creating blocks...");
        createBlock("General Advice", "Generate general financial advice", "general");
        createBlock("Budget Analysis", "Analyze budget execution", "budget");
        createBlock("Consumption Analysis", "Analyze spending habits", "consumption");
        createBlock("Savings Analysis", "Analyze savings potential", "savings");
        createBlock("Long-term Analysis", "Analyze financial trends", "longterm");
        createBlock("Holiday Advice", "Get holiday spending advice", "holiday");
        System.out.println("AIAdvicePanel initialization completed.");
    }

    /**
     * Creates a block for a specific type of advice.
     * @param title The title of the advice block
     * @param description The description of the advice type
     * @param type The type identifier for the advice
     */
    private void createBlock(String title, String description, String type) {
        System.out.println("Creating block for: " + title);
        JPanel block = new JPanel();
        block.setLayout(new BorderLayout(5, 5));
        block.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        block.add(titleLabel, BorderLayout.NORTH);

        // Description
        JLabel descLabel = new JLabel(description);
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        block.add(descLabel, BorderLayout.CENTER);

        // Button
        JButton button = new JButton("Generate");
        button.addActionListener(e -> {
            System.out.println("Button clicked for: " + title);
            generateAdvice(type);
        });
        block.add(button, BorderLayout.SOUTH);

        // Store button reference based on type
        switch (type) {
            case "general":
                generateButtonGeneral = button;
                break;
            case "budget":
                generateButtonBudget = button;
                break;
            case "consumption":
                generateButtonConsumption = button;
                break;
            case "savings":
                generateButtonSavings = button;
                break;
            case "longterm":
                generateButtonLongTerm = button;
                break;
            case "holiday":
                generateButtonHoliday = button;
                break;
        }

        add(block);
        System.out.println("Block created and added for: " + title);
    }

    /**
     * Generates advice for the specified type and displays it in a dialog.
     * @param type The type of advice to generate
     */
    private void generateAdvice(String type) {
        System.out.println("Generate advice button clicked for type: " + type);
        setButtonsEnabled(false);
        
        // Create a dialog to show the advice
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "AI Advice", true);
        dialog.setLayout(new BorderLayout(10, 10));
        
        // Add padding
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Status label
        JLabel statusLabel = new JLabel("Generating advice...");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(statusLabel, BorderLayout.NORTH);
        
        // Advice text area
        JTextArea adviceArea = new JTextArea();
        adviceArea.setEditable(false);
        adviceArea.setLineWrap(true);
        adviceArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(adviceArea);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        dialog.add(contentPanel);
        
        // Show dialog
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        
        System.out.println("Executing worker...");
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                System.out.println("SwingWorker doInBackground started...");
                System.out.println("Calling AIManager.getInstance().generateAdvice() with type: " + type);
                try {
                    String advice = AIManager.getInstance().generateAdvice(type);
                    System.out.println("AIManager call completed.");
                    System.out.println("AIManager returned advice, length: " + (advice != null ? advice.length() : 0));
                    return advice;
                } catch (Exception e) {
                    System.err.println("Error in background task during AIManager call: " + e.getMessage());
                    e.printStackTrace();
                    throw e; // Re-throw to be caught by done()
                }
            }

            @Override
            protected void done() {
                System.out.println("SwingWorker done() method started...");
                try {
                    System.out.println("Attempting to get result from background task...");
                    String advice = get(); // This will throw an exception if doInBackground threw one
                    System.out.println("Successfully got result from background task.");
                    
                    if (advice != null && !advice.isEmpty()) {
                        adviceArea.setText(advice);
                        statusLabel.setText("Advice generated successfully!");
                        System.out.println("Advice displayed successfully");
                    } else {
                        adviceArea.setText("Failed to generate advice: Empty response.");
                        statusLabel.setText("Generation failed: Empty response");
                        System.out.println("No advice content received or empty string");
                    }
                } catch (java.util.concurrent.CancellationException e) {
                     System.err.println("SwingWorker task was cancelled.");
                     statusLabel.setText("Generation cancelled.");
                     adviceArea.setText("Advice generation was cancelled.");
                } catch (java.util.concurrent.ExecutionException e) {
                    System.err.println("Error during SwingWorker execution:");
                    e.printStackTrace();
                    Throwable cause = e.getCause();
                    if (cause != null) {
                        System.err.println("Caused by: " + cause.getMessage());
                        cause.printStackTrace();
                         adviceArea.setText("Error generating advice: " + cause.getMessage());
                         statusLabel.setText("Generation failed: " + cause.getMessage());
                    } else {
                        adviceArea.setText("Error generating advice: " + e.getMessage());
                        statusLabel.setText("Generation failed: " + e.getMessage());
                    }
                } catch (InterruptedException e) {
                     System.err.println("SwingWorker task was interrupted.");
                     statusLabel.setText("Generation interrupted.");
                     adviceArea.setText("Advice generation was interrupted.");
                } catch (Exception ex) {
                    System.err.println("Unexpected error in done() method: " + ex.getMessage());
                    ex.printStackTrace();
                    adviceArea.setText("Unexpected error: " + ex.getMessage());
                    statusLabel.setText("Generation failed");
                } finally {
                    setButtonsEnabled(true);
                    // dialog.dispose(); // Keep dialog open to show message
                    System.out.println("Buttons re-enabled and dialog remains open");
                }
            }
        };

        System.out.println("Executing worker...");
        worker.execute();
        System.out.println("Worker execution started");
        
        // Show dialog AFTER the worker has started
        dialog.setVisible(true);
    }

    /**
     * Enables or disables all advice generation buttons.
     * @param enabled Whether the buttons should be enabled
     */
    private void setButtonsEnabled(boolean enabled) {
        generateButtonGeneral.setEnabled(enabled);
        generateButtonBudget.setEnabled(enabled);
        generateButtonConsumption.setEnabled(enabled);
        generateButtonSavings.setEnabled(enabled);
        generateButtonLongTerm.setEnabled(enabled);
        generateButtonHoliday.setEnabled(enabled);
    }

    /**
     * Main method for testing the AIAdvicePanel.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("AI Advice Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new AIAdvicePanel(java.awt.Color.BLACK, Color.WHITE));
        frame.pack();
        frame.setSize(1200, 800); // Increased size for the grid layout
        frame.setVisible(true);
    }
}