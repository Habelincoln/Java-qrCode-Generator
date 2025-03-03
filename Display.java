import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Desktop;

public class Display extends JFrame {
    private final QRPanel panel;
    private final int[][] matrix;

    public Display(int[][] matrix) {
        this.matrix = matrix;
        setTitle("QR Code");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        this.panel = new QRPanel(matrix);
        panel.setBackground(Color.WHITE);
        mainPanel.add(panel, BorderLayout.CENTER);
        
        JPanel topContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topContainer.setBackground(Color.WHITE);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setPreferredSize(new Dimension(400, 60)); 
        
        JButton saveButton = new JButton("Save QR Code");
        saveButton.setFont(new Font("Arial", Font.BOLD, 18)); 
        saveButton.setBackground(Color.BLACK);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false); 
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); 
        
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSaveDialog();
            }
        });
        
        buttonPanel.add(saveButton);
        topContainer.add(buttonPanel);
        mainPanel.add(topContainer, BorderLayout.NORTH); 
        
        add(mainPanel);
        setVisible(true);
    }
    
    private void showSaveDialog() {
        
        FileDialog fileDialog = new FileDialog(this, "Save QR Code", FileDialog.SAVE);
        fileDialog.setFilenameFilter((dir, name) -> name.toLowerCase().endsWith(".png"));
        fileDialog.setFile("*.png");
        fileDialog.setVisible(true);
        
        if (fileDialog.getFile() != null) {
            String filePath = fileDialog.getDirectory() + fileDialog.getFile();
            if (!filePath.toLowerCase().endsWith(".png")) {
                filePath += ".png";
            }
            saveCleanImage(filePath);
        }
    }
    
    private void saveCleanImage(String filePath) {
        QRPanel cleanPanel = new QRPanel(matrix);
        cleanPanel.setBackground(Color.WHITE);
        cleanPanel.setSize(panel.getWidth(), panel.getHeight());
        
        BufferedImage image = new BufferedImage(
            cleanPanel.getWidth(),
            cleanPanel.getHeight(),
            BufferedImage.TYPE_INT_RGB
        );
        
        cleanPanel.paint(image.getGraphics());
        
        try {
            File file = new File(filePath);
            javax.imageio.ImageIO.write(image, "png", file);
            
            Desktop.getDesktop().open(file.getParentFile());
            
            dispose();
            System.exit(0);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving image: " + e.getMessage());
        }
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

        final int MARGIN = 3; 

        int availableWidth = getWidth() - (2 * MARGIN);
        int availableHeight = getHeight() - (2 * MARGIN);
        int cellSize = Math.min(availableWidth / cols, availableHeight / rows);

        int startX = (getWidth() - (cols * cellSize)) / 2;
        int startY = (getHeight() - (rows * cellSize)) / 2;

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                g.setColor(matrix[i][j] == 1 ? Color.BLACK : Color.WHITE);
                g.fillRect(startX + (j * cellSize), startY + (i * cellSize), cellSize, cellSize);
            }
        }
    }
}
