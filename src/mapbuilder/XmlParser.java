package mapbuilder;

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

public class XmlParser {
	
	private List<MapNode> mapnodes;
	private List<MapWay> mapways;
	private HashMap<String, MapNode> mnhash;
	
	public XmlParser() {
		mapnodes = new ArrayList<MapNode>(0);
		mapways = new ArrayList<MapWay>(0);
		mnhash = new HashMap<String, MapNode>();
	}
		
	public void parseBox(String filepath) {
		
		try {
		
			File xmlfile = new File(filepath);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(xmlfile);
			doc.getDocumentElement().normalize();
			
			NodeList nnodes = doc.getElementsByTagName("node");
			for (int i = 0; i < nnodes.getLength(); i++) {
				Node node = nnodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element el = (Element) node;
					MapNode mn = new MapNode(el.getAttribute("id"), Double.parseDouble(el.getAttribute("lat")),
							Double.parseDouble(el.getAttribute("lon")));
					mnhash.put(mn.id, mn);
					mapnodes.add(mn);
				}
			}

			
			NodeList wnodes = doc.getElementsByTagName("way");
			for (int i = 0; i < wnodes.getLength(); i++) {
				Node node = wnodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element el = (Element) node;
					MapWay mw = new MapWay(el.getAttribute("id"));
					
					
					List<String> mapnodeids = getMapNodes("nd", el);
					for (String id: mapnodeids) {
						if (mnhash.get(id) == null) {
							System.out.println("ERROR: Ways and nodes do not match in input file");
						}
						else {
							mw.addNode(mnhash.get(id));
						}
					}
					
				}
			}
		
		}
		catch (NumberFormatException nfe) {
			System.out.println("ERROR: Bad input file");
		}
		catch (Exception e) {
			System.out.println("ERROR: Problem in parseBox");
			e.printStackTrace();
		}
		
	
	
	}
	
	
	
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
	
	
	

	public MapNode parseAddress(File xmlfile) {
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(xmlfile);
			doc.getDocumentElement().normalize();
			NodeList nnodes = doc.getElementsByTagName("searchresults");
			
			Node node = nnodes.item(0);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element el = (Element) node;
				String place_id = getAddInfo("place", "place_id", el);
				double lat = Double.parseDouble(getAddInfo("place", "lat", el));
				double lon = Double.parseDouble(getAddInfo("place", "lon", el));
				MapNode mn = new MapNode(place_id, lat, lon);
				System.out.println(mn.id);
				System.out.println(mn.lat);
				System.out.println(mn.lon);
				return mn;
			}
			else {
				//Dialogue box needed
				System.out.println("ERROR: Could not find address");
			}
		}
		catch (Exception e) {
			System.out.println("ERROR: Problem in parseAddress");
			e.printStackTrace();
		}
		return null;
		
	}
	
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

}
