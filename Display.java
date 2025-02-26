import java.awt.*;
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
    
        public QRPanel(int[][] matrix) {
            this.matrix = matrix;
        }
    
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawQRCode(g);
        }
    
        private void drawQRCode(Graphics g) {
            int rows = matrix.length;
            int cols = matrix[0].length;
            
            final int MARGIN = 3; // Fixed 3-pixel margin
            
            // Calculate cell size to fill the panel while maintaining the margin
            int availableWidth = getWidth() - (2 * MARGIN);
            int availableHeight = getHeight() - (2 * MARGIN);
            int cellSize = Math.min(availableWidth / cols, availableHeight / rows);
            
            // Center the QR code
            int startX = (getWidth() - (cols * cellSize)) / 2;
            int startY = (getHeight() - (rows * cellSize)) / 2;

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
    }
