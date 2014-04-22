package ztdpac;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageRet {
	
	public File getImage(double left, double bottom, double right, double top) {
			
			String address = "http://open.mapquestapi.com/staticmap/v4/getmap?key=Fmjtd%7Cluur2quyn9%2C8s%3Do5-9aas04&bestfit="; //The mapquest server address
			address = address + top + ",";
			address = address + left + ",";
			address = address + bottom + ",";
			address = address + right + ",";
			address = address + "&size=400,400&type=map&imagetype=png";
			
			try {
				URL mq = new URL(address);
				BufferedReader in = new BufferedReader(new InputStreamReader(mq.openStream()));
				File map = new File("map.png"); //Make a new file called box.xml, or overwrite the existing file
				BufferedImage image = ImageIO.read(mq);
				ImageIO.write(image,  "png", map);
				
				//Close resources
				in.close();			
				
				System.out.println("Got File");
				
				return map;
			}
			catch (MalformedURLException e) {
				System.out.println("ERROR: Bad URL, could not access OpenStreetMap");
				e.printStackTrace();
			}
			catch (IOException e) {
				System.out.println("ERROR: Bad connection, could not access OpenStreetMap");
				e.printStackTrace();
			}
			//TODO Check for null whenever we call this
			return null;
		
		
	}

}
