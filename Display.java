import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
 public class Display extends JFrame {
        public Display(int[][] matrix) {
            setTitle("QR Code");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(800, 800);  // Increased window size
            setFocusable(true);
            setLocationRelativeTo(null);
            requestFocusInWindow();
    
            QRPanel panel = new QRPanel(matrix);
            panel.setBackground(Color.WHITE);
            add(panel);
    
            setVisible(true);
        }
    }
    
    class QRPanel extends JPanel {
        final private int[][] matrix;
        private JButton saveButton;

        public QRPanel(int[][] matrix) {
            this.matrix = matrix;
            setLayout(null);

            saveButton = new JButton("Save QR Code");
            saveButton.setBackground(Color.BLACK);
            saveButton.setForeground(Color.WHITE);
            saveButton.setFont(new Font("Arial", Font.BOLD, 14));
            saveButton.setFocusPainted(false);
            
            // Move button higher in the margin
            int buttonWidth = 150;
            int buttonHeight = 40;
            int topMargin = 10; // Reduced from 20 to 10
            saveButton.setBounds((800 - buttonWidth) / 2, topMargin, buttonWidth, buttonHeight);
            
            saveButton.addActionListener(e -> saveAsImage());
            add(saveButton);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawQRCode(g);
        }

        private void drawQRCode(Graphics g) {
            int rows = matrix.length;
            int cols = matrix[0].length;

            final int MARGIN = 3;
            final int TOP_SPACING = 70; // Increased spacing from top to avoid button

            // Calculate cell size with adjusted height
            int availableWidth = getWidth() - (2 * MARGIN);
            int availableHeight = getHeight() - (2 * MARGIN) - TOP_SPACING;
            int cellSize = Math.min(availableWidth / cols, availableHeight / rows);

            // Center the QR code horizontally and position it below the button
            int startX = (getWidth() - (cols * cellSize)) / 2;
            int startY = TOP_SPACING + ((getHeight() - TOP_SPACING - (rows * cellSize)) / 2);

            // Draw background
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());

            // Draw QR code
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    g.setColor(matrix[i][j] == 1 ? Color.BLACK : Color.WHITE);
                    g.fillRect(startX + (j * cellSize), startY + (i * cellSize), cellSize, cellSize);
                }
            }
        }

        public void saveAsImage() {
            BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();
            paint(g2d);
            g2d.dispose();

            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            FileDialog fd = new FileDialog((Frame) SwingUtilities.getWindowAncestor(this), "Save QR Code", FileDialog.SAVE);
            System.setProperty("awt.file.dialog.use.native", "true");
            fd.setFile("*.png");
            fd.setFilenameFilter((dir, name) -> name.toLowerCase().endsWith(".png"));
            fd.setVisible(true);

            String filename = fd.getFile();
            String directory = fd.getDirectory();
            
            if (filename != null && directory != null) {
                try {
                    String fullPath = directory + filename;
                    if (!fullPath.toLowerCase().endsWith(".png")) {
                        fullPath += ".png";
                    }
                    ImageIO.write(image, "png", new File(fullPath));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, 
                        "Error saving the file: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        }
    }