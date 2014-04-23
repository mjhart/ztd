package mapbuilder;

import gameEngine.Base;
import gameEngine.Referee;
import gameEngine.towers.AbstractTower;
import gameEngine.zombie.Zombie;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import javax.imageio.ImageIO;

import cs195n.Vec2f;
import cs195n.Vec2i;
import mapbuilder.MapWay;
//import mapbuilder.PathFinder.MyComparator;

public class Map {
	private List<MapWay> _ways;
	private List<MapWay> _highways;
	private List<MapNode> _nodes;
	private double[] wMin;
	private double[] wMax;
	private Vec2i _size;
	//private PathFinder _pf;
	private MapNode _baseNode;
	private BufferedImage _baseSprite;
	private List<MapNode> _srcs;
	private Referee _ref;
	
	public Map(String address, Referee ref) {
		
		_ref = ref;
		
		wMin = new double[2];
		wMax = new double[2];
		
		//File stadd = Retriever.getFromAddress(address);
		File stadd = new File("stadd.xml");
		XmlParser x = new XmlParser(this);
		Point2D.Double cent = x.parseAddress(stadd);
		DistConverter dc = new DistConverter(cent.y, cent.x);
		wMin[0] = dc.getLeft(cent.x);
		wMin[1] = dc.getBott(cent.y);
		wMax[0] = dc.getRight(cent.x);
		wMax[1] = dc.getTop(cent.y);
		//File box = Retriever.getBox(wMin[0], wMin[1], wMax[0], wMax[1]);
		File box = new File("box.xml");
		x.parseBox(box);
		_ways = x.getWays();
		_nodes = x.getNodes();
		_highways = x.getHighs();
		
		// find closest highway node to center
		double dist = Double.MAX_VALUE;
		for(MapWay w : _highways) {
			for(MapNode n : w.getNodes()) {
				double d2 = (n.getX()-50)*(n.getX()-50) + (n.getY()-50)*(n.getY()-50);
				if(d2 < dist) {
					dist = d2;
					_baseNode = n;
				}
			}
		}
		
		try {
			_baseSprite = ImageIO.read(new File("stuff/bldgsprite.png"));
		} catch (IOException e) {
			System.out.println("ERROR: Could not get image (SpriteImp)");
		}
		
	}
	
	
	public void setSize(Vec2i size) {
		_size = size;
	}
	
	public List<MapNode> getSourceList() {
		return Collections.unmodifiableList(_srcs);
	}
	

	public MapNode getBaseNode() {
		return _baseNode;
	}
	
	public List<MapWay> getWays() {
		return Collections.unmodifiableList(_ways);
	}
	
	public List<MapWay> getHighways() {
		return Collections.unmodifiableList(_highways);
	}
	
	
	/* path finding methods */
	
	
	private List<MapNode> potentialSrcs() {
		List<MapNode> results = new LinkedList<MapNode>();
		for(MapNode n : _nodes) {
			if(n.getX() <= 100 && n.getX() + 5 > 100) {
				results.add(n);
				continue;
			}
			if(n.getY() <= 100 && n.getY() + 5 > 100) {
				results.add(n);
				continue;
			}
			if(n.getX() >= 0 && n.getX() - 5 < 0) {
				results.add(n);
				continue;
			}
			if(n.getY() >= 0 && n.getY() - 5 < 0) {
				results.add(n);
				continue;
			}
		}
		//System.out.println(results);
		return results;
	}
	
	private List<MapNode> findPaths(List<MapNode> srcList, MapNode base) {
		LinkedList<MapNode> spawns = new LinkedList<MapNode>();
		HashSet<MapNode> visited = new HashSet<MapNode>();
		HashMap<MapNode, Double> dist = new HashMap<MapNode, Double>();
		
		for(MapNode n : _nodes) {
			dist.put(n, Double.MAX_VALUE);
		}
		
		visited.add(base);
		dist.put(base, 0d);
		
		PriorityQueue<MapNode> pq = new PriorityQueue<MapNode>(10, new MyComparator(dist));
		
		pq.add(base);
		
		/*
		MapNode node;
		while(!pq.isEmpty()) {
			node = pq.poll();
			System.out.println(node);
			Vec2f nv = new Vec2f((float)node.lon,(float) node.lat);
			visited.add(node);
			
			if(srcList.contains(node)) {
				spawns.add(node);
				srcList.remove(node);
			}
			
			for(MapNode nbor : getAdjacentNodes(node)) {
				if(!visited.contains(nbor)) {
					
					Vec2f nv2 = new Vec2f((float)nbor.lon,(float) nbor.lat);
					float d = nv.dist2(nv2);
					
					if(dist.get(node) + d < dist.get(nbor)) {
						dist.put(nbor, (int) (dist.get(nbor)+d));
						pq.remove(nbor);
						pq.add(nbor);
						nbor.setNext(node);
					}
				}
			}
		}
		*/
		
		///*
		MapNode node;
		while(!pq.isEmpty()) {
			node = pq.poll();
			//System.out.println("Popped: " + node);
			//System.out.println("Popped dist: " + dist.get(node));
			Vec2f nv = node._coords;
			visited.add(node);
			
			if(srcList.contains(node)) {
				spawns.add(node);
				srcList.remove(node);
			}
			
			for(MapNode nbor : getAdjacentNodes(node)) {
				if(!visited.contains(nbor)) {
					
					Vec2f nv2 = nbor._coords;
					float d = nv.dist2(nv2);
					//System.out.println(nbor.id);
					//System.out.println("distance to: " + Double.toString((dist.get(node) + d)));
					
					if(dist.get(node) + d < dist.get(nbor)) {
						dist.put(nbor, dist.get(node)+d);
						pq.remove(nbor);
						pq.add(nbor);
						nbor.setNext(node);
					}
				}
			}
			//run = false;
		}
		//*/
		
		return spawns;
	}
	
	private List<MapNode> getAdjacentNodes(MapNode node) {
		List<MapNode> results = new LinkedList<MapNode>();
		
		for(MapWay w : node.getWays()) {
			int f;
			List<MapNode> nodes = w.getNodes();
			if((f = nodes.indexOf(node)) >= 0) {
				if(f > 0) {
					MapNode before = nodes.get(f-1);
					if(!before.equals(node.getNext())) {
						results.add(before);
					}
				}
				if(f < nodes.size()-1) {
					MapNode after = nodes.get(f+1);
					if(!after.equals(node.getNext())) {
						results.add(after);
					}
				}
			}
		}
		//System.out.println("adj: " + results);
		return results;
	}
	
	class MyComparator implements Comparator<MapNode> {
		
		private HashMap<MapNode, Double> dist;
		
		public MyComparator(HashMap<MapNode, Double> distMap) {
			dist = distMap;
		}

		@Override
		public int compare(MapNode o1, MapNode o2) {
			return dist.get(o1).compareTo(dist.get(o2));
		}
		
	}
	
	public List<MapNode> getSources() {
		System.out.println(_baseNode);
		List<MapNode> srcs = potentialSrcs();
		System.out.println(srcs);
		_srcs = findPaths(srcs, _baseNode);
		System.out.println("Sources set " + _srcs);
		return _srcs;
	}
	
	public double[] getwMax() {
		return wMax;
	}
	
	public double[] getwMin() {
		return wMin;
	}
	
	public float latToY(double lat) {
		return  (float) ((wMax[1]-lat)/(wMax[1]-wMin[1]) * 100);
	}
	
	public float lonToX(double lon) {
		return  (float) ((lon - wMin[0])/(wMax[0]-wMin[0]) * 100);
	}
	
}
