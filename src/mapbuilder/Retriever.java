package mapbuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * The Retriever connects with the OSM server and obtains the information necessary to make
 * the map screen for ZTD. It has two methods: getBox retrieves the xml information contained
 * within the specified box defined as lat and lon coordinates, and getFromAddress retrieves
 * xml for a particular address from an OSM service called nomatim
 * @author mmkaplan
 *
 */
public class Retriever {
	
	/**
	 * getBox takes in a box defined by lat and lon lines and returns all the xml
	 * info for that box from OSM
	 * @param left The left lon bound
	 * @param bottom The bottom lat bound
	 * @param right The right lon bound
	 * @param top The top lat bound
	 * @return A File containing all of the xml info from this box
	 */
	public static File getBox(double left, double bottom, double right, double top) {
		String address = "http://api.openstreetmap.org/api/0.6/map?bbox="; //The OSM server address
		address = address + left + ",";
		address = address + bottom + ",";
		address = address + right + ",";
		address = address + top;
		
		try {
			URL osm = new URL(address);
			BufferedReader in = new BufferedReader(new InputStreamReader(osm.openStream()));
			File box = new File("box.xml"); //Make a new file called box.xml, or overwrite the existing file
			PrintWriter pw = new PrintWriter(box);
			
			//Read and write line by line
			String line = in.readLine();
			while (line != null) {
				pw.write(line);
				line = in.readLine();
			}
			
			//Close resources
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
		//TODO Check for null whenever we call this
		return null;
	}
	
	/**
	 * getFromAddress takes in a string, sends it to OSM, and returns a File
	 * containing the xml for that location
	 * @param stadd A street address (or place name, i.e. "Eiffel Tower")
	 * @return A File containing the xml for this location
	 */
	public static File getFromAddress(String stadd) {
		String address = "http://nominatim.openstreetmap.org/search?q="; //The OSM server address
		
		//Cleanup the input and append to the server address
		String[] staddarr = stadd.split("\\s+");
		int len = staddarr.length;
		for (int i = 0; i < (len-1); i++) {
			address = address + staddarr[i] + "+";
		}
		address = address + staddarr[len-1] + "&format=xml";
		
		try {
			URL nomatim = new URL(address);
			BufferedReader in = new BufferedReader(new InputStreamReader(nomatim.openStream()));
			File staddfile = new File("stadd.xml"); //Make a new file called stadd.xml, or overwrite the existing file
			PrintWriter pw = new PrintWriter(staddfile);
			
			//Read and write line by line
			String line = in.readLine();
			while (line != null) {
				pw.write(line);
				line = in.readLine();
			}
			
			//Close resources
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
		//TODO Check if null when we call this
		return null;
	}
	
	
	
	
	

}
