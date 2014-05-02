package mapbuilder;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class parses the two types of xml we use for the application: address xml and box xml.
 * As such, it has two main methods, parseBox and parseAddress. It has several helper methods
 * as well, plus getters to access the parsed info. Note that in this class it is important
 * to differentiate between xml nodes, OSM nodes, and MapNodes
 * @author mmkaplan
 *
 */
public class XmlParser {
	
	private List<MapNode> mapnodes;
	private List<MapWay> mapways;
	private List<MapWay> highways;
	private List<Building> buildings;
	private List<Building> landuse;
	private List<Building> waterways;
	private List<MapWay> footways;
	private List<MapWay> residential;
	private List<MapWay> secondary;
	private List<MapWay> tertiary;

	private HashMap<String, MapNode> mnhash;
	private boolean doneparsebox;
	private Map _m;
	
	public XmlParser(Map m) {
		mapnodes = new ArrayList<MapNode>(0);
		mapways = new ArrayList<MapWay>(0);
		mnhash = new HashMap<String, MapNode>();
		highways = new ArrayList<MapWay>(0);
		buildings = new ArrayList<>(0);
		landuse = new ArrayList<>(0);
		waterways = new ArrayList<>(0);
		footways = new ArrayList<MapWay>(0);
		residential = new ArrayList<MapWay>(0);
		secondary = new ArrayList<MapWay>(0);
		tertiary = new ArrayList<MapWay>(0);

		doneparsebox = false;
		_m = m;
	}
	
	/**
	 * This method parses the box info that OSM returns into MapNodes and MapWays
	 * @param xmlfile All info contained in a box defined by lat and lon bounds
	 */
	public void parseBox(File xmlfile) {
		try {
			//Start up the DOM
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(xmlfile);
			doc.getDocumentElement().normalize();
			
			NodeList nnodes = doc.getElementsByTagName("node"); //Make a list of all elements marked "node"
			//For each xml node
			for (int i = 0; i < nnodes.getLength(); i++) {
				Node node = nnodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element el = (Element) node;
					MapNode mn = new MapNode(el.getAttribute("id"), 
							_m.latToY(Double.parseDouble(el.getAttribute("lat"))),
							_m.lonToX(Double.parseDouble(el.getAttribute("lon")))); //Pull out and save desired attributes
					mnhash.put(mn.id, mn);
					mapnodes.add(mn);
				}
			}
			
			
			NodeList wnodes = doc.getElementsByTagName("way");  //Make a list of all elements marked "way"
			//For each xml way
			for (int i = 0; i < wnodes.getLength(); i++) {
				Node node = wnodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					
					
					Element el = (Element) node;
					MapWay mw = new MapWay(el.getAttribute("id"));
					
					boolean isHighway = false;
					if (XmlParser.isBlank("tag", el, "highway") == true) {
						isHighway = true;
						highways.add(mw);
					}
					


					List<String> mapnodeids = XmlParser.getMapNodes("nd", el); //Get the nodes on this way
					for (String id: mapnodeids) {
						MapNode n = mnhash.get(id);
						if ((n = mnhash.get(id)) == null) {
							System.out.println("ERROR: Ways and nodes do not match in input file");
						}
						else { //Add way to node
							if (isHighway) {
								n.addWay(mw);
							}
							mw.addNode(n);
						}
					}
					if(!isHighway) {
						mapways.add(mw);
					}
					
					
					if (XmlParser.isBlank("tag", el, "building") == true) {
						Building b = new Building(mw);
						b.setName(XmlParser.getVFromK("tag", el, "name"));
						buildings.add(b);
					}
					if (XmlParser.isBlank("tag", el, "waterway") == true) {
						Building b = new Building(mw);
						b.setName(XmlParser.getVFromK("tag", el, "name"));
						waterways.add(b);
					}
					if (XmlParser.isBlank("tag", el, "landuse") == true) {
						if (XmlParser.getVFromK("tag", el, "landuse").equals("grass")) {
								Building b = new Building(mw);
								b.setName(XmlParser.getVFromK("tag", el, "name"));
								landuse.add(b);
						}
					}
					
					if (XmlParser.isBlank("tag", el, "highway") == true) {
						if (XmlParser.getVFromK("tag", el, "highway").equals("footway")) {
							footways.add(mw);
						}
					}
					if (XmlParser.isBlank("tag", el, "highway") == true) {
						if (XmlParser.getVFromK("tag", el, "highway").equals("residential")) {
							residential.add(mw);
						}
					}
					if (XmlParser.isBlank("tag", el, "highway") == true) {
						if (XmlParser.getVFromK("tag", el, "highway").equals("secondary")) {
							secondary.add(mw);
						}
					}
					if (XmlParser.isBlank("tag", el, "highway") == true) {
						if (XmlParser.getVFromK("tag", el, "highway").equals("tertiary")) {
							tertiary.add(mw);
						}
					}

					
					
				}
			}

			doneparsebox = true;
		
		}
		catch (NumberFormatException nfe) {
			System.out.println("ERROR: Bad input file");
		}
		catch (Exception e) {
			System.out.println("ERROR: Problem in parseBox");
			e.printStackTrace();
		}	
	}
	
	
	/**
	 * This helper method returns the map nodes associated with a given way
	 * @param tag In this case it is "nd"
	 * @param e The way we wish to examine
	 * @return A list of nodes for the given way
	 */
	private static List<String> getMapNodes(String tag, Element e) {
		List<String> res = new ArrayList<String>(0);
		NodeList nl = e.getElementsByTagName(tag);
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element el = (Element) n;
				res.add(el.getAttribute("ref"));
			}
		}
		return res;
	}
	

	

	private static boolean isBlank(String tag, Element e, String key) {
		boolean markblank = false;
		List<String> res = new ArrayList<String>(0);
		NodeList nl = e.getElementsByTagName(tag);
		//Add all "tag" attributes
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element el = (Element) n;
				res.add(el.getAttribute("k"));
			}
		}
		//Iterate through tags searching for highway, mark if found
		for (String s: res) {
			if (s.equals(key)) {
				markblank = true;
			}
		}
		return markblank;
	}
	
	private static String getVFromK(String tag, Element e, String key) {
		String v = null;
		HashMap<String, String> res = new HashMap<>(0);
		NodeList nl = e.getElementsByTagName(tag);
		//Add all "tag" attributes
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element el = (Element) n;
				res.put(el.getAttribute("k"), el.getAttribute("v"));
			}
		}
		return res.get(key);
	}
	
	
	
	/**
	 * This mehtod takes in an xml file containing all OSM infor for a particular address
	 * and parses it to obtain it's lat and lon.
	 * @param xmlfile An xml file from OSM containing relevant info
	 * @return A mapnode containing the lat and lon of the location
	 */
	public Point2D.Double parseAddress(File xmlfile) {
		try {
			//Start up the DOM
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(xmlfile);
			doc.getDocumentElement().normalize();
			
			//Get a list of all nodes marked search results (there will only be one node in this list)
			NodeList nnodes = doc.getElementsByTagName("searchresults");
			
			Node node = nnodes.item(0);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element el = (Element) node;
				String place_id = XmlParser.getAddInfo("place", "place_id", el);
				double lat = Double.parseDouble(XmlParser.getAddInfo("place", "lat", el));
				double lon = Double.parseDouble(XmlParser.getAddInfo("place", "lon", el));
				Point2D.Double mn = new Point2D.Double(lon, lat);
				return mn;
			}
			else {
				//Dialogue box needed
				System.out.println("ERROR: Could not find address");
			}
		}
		catch (Exception e) {
			System.out.println("ERROR: Problem in parseAddress");
		}
		return null;
		
	}
	
	/**
	 * This method takes in a tag, an attribute, and an element and returns the value
	 * of the attribute. This method should only be used in parseAddress
	 * @param tag The xml tag
	 * @param att The xml attribute
	 * @param e The element of interest
	 * @return The value of the attribute
	 */
	private static String getAddInfo(String tag, String att, Element e) {
		NodeList nl = e.getElementsByTagName(tag);
		Node n = nl.item(0);
		String res = null;
		if (n.getNodeType() == Node.ELEMENT_NODE) {
			Element el = (Element) n;
			res = el.getAttribute(att);
		}
		return res;
	}
	
	
	
	//TODO Make these contingent on doneparsing
	
	public List<MapWay> getWays() {
		return mapways;
	}
	
	public List<MapWay> getHighs() {
		return highways;
	}
	
	public List<MapNode> getNodes() {
		return mapnodes;
	}
	
	public HashMap<String, MapNode> getNodesHash() {
		return mnhash;
	}
	
	public List<Building> getBuildings() {
		return buildings;
	}
	
	public List<Building> getLanduse() {
		return landuse;
	}
	
	public List<Building> getWaterways() {
		return waterways;
	}
	
	public List<MapWay> getFootways() {
		return footways;
	}
	
	public List<MapWay> getResidential() {
		return residential;
	}
	
	public List<MapWay> getSecondary() {
		return secondary;
	}
	
	public List<MapWay> getTertiary() {
		return tertiary;
	}

}
















