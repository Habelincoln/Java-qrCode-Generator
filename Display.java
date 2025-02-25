 import java.awt.*;
import javax.swing.*;
 public class Display extends JFrame {
        public Display(int[][] matrix) {
            setTitle("QR Code");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(600, 600);
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
        private int[][] matrix;
    
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
            int cellSize = Math.min(getWidth() / cols, getHeight() / rows);
    
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (matrix[i][j] == 1) {
                        g.setColor(Color.BLACK);
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                }
            }
        }
    }
