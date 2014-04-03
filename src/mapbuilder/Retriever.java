package mapbuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

public class Retriever {
	
	
	
	public static File getBox(double left, double bottom, double right, double top) {
		String address = "http://api.openstreetmap.org/api/0.6/map?bbox=";
		address = address + left + ",";
		address = address + bottom + ",";
		address = address + right + ",";
		address = address + top;
		
		try {
			URL osm = new URL(address);
			BufferedReader in = new BufferedReader(new InputStreamReader(osm.openStream()));
			File box = new File("box.xml");
			PrintWriter pw = new PrintWriter(box);
			
			String line = in.readLine();
			
			while (line != null) {
				pw.write(line);
				line = in.readLine();
			}
			
			in.close();
			pw.close();			
			
			return box;

		}
		catch (MalformedURLException e) {
			System.out.println("ERROR: Bad URL, could not access OpenStreetMap");
			e.printStackTrace();
		}
		catch (IOException e) {
			System.out.println("ERROR: Bad connection, could not access OpenStreetMap");
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	
	public static File getFromAddress(String stadd) {
		String address = "http://nominatim.openstreetmap.org/search?q=";
		String[] staddarr = stadd.split("\\s+");
		int len = staddarr.length;
		for (int i = 0; i < (len-1); i++) {
			address = address + staddarr[i] + "+";
		}
		address = address + staddarr[len-1] + "&format=xml";
		
		try {
			URL nomatim = new URL(address);
			BufferedReader in = new BufferedReader(new InputStreamReader(nomatim.openStream()));
			File staddfile = new File("stadd.xml");
			PrintWriter pw = new PrintWriter(staddfile);
			String line = in.readLine();
			while (line != null) {
				System.out.println(line);
				pw.write(line);
				line = in.readLine();
			}
			in.close();
			pw.close();
			return staddfile;
		}
		catch (MalformedURLException e) {
			System.out.println("ERROR: Bad URL, could not access Nomatim");
			e.printStackTrace();
		}
		catch (IOException e) {
			System.out.println("ERROR: Bad connection, could not access Nomatim");
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	

}
