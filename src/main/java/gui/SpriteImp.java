package gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;



@SuppressWarnings("serial")
public class SpriteImp extends JPanel {
	
	private BufferedImage image;

	public SpriteImp(String filepath) {
		
		try {
			image = ImageIO.read(new File(filepath));
		} catch (IOException e) {
			System.out.println("ERROR: Could not get image (SpriteImp)");
			e.printStackTrace();
		}
		
		this.repaint();
		
	}
	
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Will need to be scaled
        g.drawImage(image, 0, 0, null);
    }
	
}
