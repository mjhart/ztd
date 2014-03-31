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
	
	
	
	public File getBox(double left, double bottom, double right, double top) {
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
				System.out.println(line);
				pw.write(line);
				in.readLine();
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
	
	
	public File getFromAddress(String stadd) {
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
//
//
//package mapbuilder;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.text.DecimalFormat;
//
//public class Retriever {
//	
//	
//	
//	public void getBox(double left, double bottom, double right, double top) {
//		String address = "http://api.openstreetmap.org/api/0.6/map?bbox=";
//		address = address + left + ",";
//		address = address + bottom + ",";
//		address = address + right + ",";
//		address = address + top;
//		
//		try {
//			URL osm = new URL(address);
//			BufferedReader in = new BufferedReader(new InputStreamReader(osm.openStream()));
////			PrintWriter writer = new PrintWriter("boxData.xml", "UTF-8");
////			int i = 0;
////			while (i < 50) {
////				System.out.println(in.readLine());
////				i++;
////			}
//		}
//		catch (MalformedURLException e) {
//			System.out.println("ERROR: Bad URL, could not access OpenStreetMap");
//			e.printStackTrace();
//		}
//		catch (IOException e) {
//			System.out.println("ERROR: Bad connection, could not access OpenStreetMap");
//			e.printStackTrace();
//		}
//		
//		
//		
//	}
//	
//	
//	public void getFromAddress(String stadd) {
//		String address = "http://nominatim.openstreetmap.org/search?q=";
//		String[] staddarr = stadd.split("\\s+");
//		int len = staddarr.length;
//		for (int i = 0; i < (len-1); i++) {
//			address = address + staddarr[i] + "+";
//		}
//		address = address + staddarr[len-1] + "&format=xml";
//		
//		try {
//			URL nomatim = new URL(address);
//			BufferedReader in = new BufferedReader(new InputStreamReader(nomatim.openStream()));
//			String line = in.readLine();
//			while (line != null) {
//				System.out.println(line);
//				line = in.readLine();
//			}
//			
//		}
//		catch (MalformedURLException e) {
//			System.out.println("ERROR: Bad URL, could not access Nomatim");
//			e.printStackTrace();
//		}
//		catch (IOException e) {
//			System.out.println("ERROR: Bad connection, could not access Nomatim");
//			e.printStackTrace();
//		}
//		
//		
//	}
//	
//	
//	
//	
//	
//
//}













/**
try {
	URL osm = new URL(address);
	BufferedReader in = new BufferedReader(new InputStreamReader(osm.openStream()));
	FileOutputStream out = new FileOutputStream("box.xml");
	PrintWriter pw = new PrintWriter("");
	
	
	String line = in.readLine();
	
	String filename = "getBox.xml"; //This might have to be more like home/course/....etc.
	File box = new File(filename);
	
	while (line != null) {
		
	}
*/