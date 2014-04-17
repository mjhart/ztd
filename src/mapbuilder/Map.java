package mapbuilder;

import gameEngine.AbstractTower;
import gameEngine.Referee;
import gameEngine.Zombie;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
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
import mapbuilder.PathFinder.MyComparator;

public class Map {
	private List<MapWay> _ways;
	private List<MapWay> _highways;
	private List<MapNode> _nodes;
	private double[] wMin;
	private double[] wMax;
	private Vec2i _size;
	private PathFinder _pf;
	private MapNode _base;
	private BufferedImage _baseSprite;
	private List<MapNode> _srcs;
	private Referee _ref;
	
	public Map(String address, Referee ref) {
		
		_ref = ref;
		
		wMin = new double[2];
		wMax = new double[2];
		
		File stadd = Retriever.getFromAddress(address);
		//File stadd = new File("stadd.xml");
		XmlParser x = new XmlParser();
		MapNode cent = x.parseAddress(stadd);
		DistConverter dc = new DistConverter(cent.lat, cent.lon);
		wMin[0] = dc.getLeft(cent.lon);
		wMin[1] = dc.getBott(cent.lat);
		wMax[0] = dc.getRight(cent.lon);
		wMax[1] = dc.getTop(cent.lat);
		File box = Retriever.getBox(wMin[0], wMin[1], wMax[0], wMax[1]);
		//File box = new File("box.xml");
		x.parseBox(box);
		_ways = x.getWays();
		_nodes = x.getNodes();
		_highways = x.getHighs();
		
		// find closest highway node to center
		double dist = Double.MAX_VALUE;
		for(MapWay w : _highways) {
			for(MapNode n : w.getNodes()) {
				double d2 = (n.lon-cent.lon)*(n.lon-cent.lon) + (n.lat-cent.lat)*(n.lat-cent.lat);
				if(d2 < dist) {
					dist = d2;
					_base = n;
				}
			}
		}
		
		try {
			_baseSprite = ImageIO.read(new File("stuff/bldgsprite.png"));
		} catch (IOException e) {
			System.out.println("ERROR: Could not get image (SpriteImp)");
		}
		
	}
	
	public void draw(Graphics2D g) {
		/*
		for(MapNode n : _nodes) {
			g.drawOval(lonToX(n.lon), latToY(n.lat), 1, 1);
		}
		*/
		//g.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		
		
		for(MapWay w : _ways) {
			List<MapNode> nList = w.getNodes();
			for(int i=1; i<nList.size(); i++) {
				g.drawLine(lonToX(nList.get(i-1).lon), latToY(nList.get(i-1).lat), lonToX(nList.get(i).lon), latToY(nList.get(i).lat));
				//g.drawLine((int)nList.get(i-1).lon,(int) nList.get(i-1).lat,(int) nList.get(i).lon, (int)nList.get(i).lat);
			}
		}
		
		g.setColor(java.awt.Color.GREEN);
		for(MapWay h : _highways) {
			List<MapNode> nList = h.getNodes();
			for(int i=1; i<nList.size(); i++) {
				g.drawLine(lonToX(nList.get(i-1).lon), latToY(nList.get(i-1).lat), lonToX(nList.get(i).lon), latToY(nList.get(i).lat));
				//g.drawLine((int)nList.get(i-1).lon,(int) nList.get(i-1).lat,(int) nList.get(i).lon, (int)nList.get(i).lat);
			}
		}
		
		g.setColor(java.awt.Color.BLUE);
		//g.setStroke(new BasicStroke(3));
		for(MapNode n : _srcs) {
			MapNode cur = n;
			MapNode next = cur.getNext();
			while(next!=null) {
				g.drawLine(lonToX(cur.lon), latToY(cur.lat), lonToX(next.lon), latToY(next.lat));
				cur = next;
				next = next.getNext();
			}
			//System.out.println("new path\n");
		}
		
		//g.drawImage(_baseSprite, lonToX(_base.lon), latToY(_base.lat), _baseSprite.getWidth()/2, _baseSprite.getHeight()/2, null);
		g.setColor(java.awt.Color.BLUE);
		g.drawOval(lonToX(_base.lon)-2, latToY(_base.lat)-2, 3, 3);
		
		//g.setColor(java.awt.Color.ORANGE);
		for(MapNode n : _srcs) {
			g.fillOval(lonToX(n.lon)-2, latToY(n.lat)-2, 5, 5);
		}
		
		g.setColor(java.awt.Color.RED);
		for(Zombie z : _ref.getZombies()) {
			g.drawOval(lonToX(z.getCoords().x), latToY(z.getCoords().y), 3, 3);
		}
		
		for(AbstractTower t : _ref.towers()) {
			t.draw(g);
		}
	}
	
	
	private int latToY(double lat) {
		return (int) ((wMax[1]-lat)/(wMax[1]-wMin[1]) * _size.y);
	}
	
	private int lonToX(double lon) {
		System.out.println("+++++++");
		System.out.println(lon);
		System.out.println(wMin[0]);
		System.out.println(wMax[0]);
		System.out.println(_size.x);
		System.out.println("+++++++++");
		return (int) ((lon - wMin[0])/(wMax[0]-wMin[0]) * _size.x);
	}
	
	public void setSize(Vec2i size) {
		_size = size;
	}
	
	public List<MapNode> getSourceList() {
		return Collections.unmodifiableList(_srcs);
	}
	
	
	/* path finding methods */
	
	
	private List<MapNode> potentialSrcs() {
		List<MapNode> results = new LinkedList<MapNode>();
		for(MapNode n : _nodes) {
			if(n.lon <= wMax[0] && n.lon + 0.0001 > wMax[0]) {
				results.add(n);
				continue;
			}
			if(n.lat <= wMax[1] && n.lat + 0.0001 > wMax[1]) {
				results.add(n);
				continue;
			}
			if(n.lon >= wMin[0] && n.lon - 0.0001 < wMin[0]) {
				results.add(n);
				continue;
			}
			if(n.lat >= wMin[1] && n.lat - 0.0001 < wMin[1]) {
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
		List<MapNode> srcs = potentialSrcs();
		this._srcs = findPaths(srcs, _base);
		return this._srcs;
	}
	
	
	
	/* for debugging */
	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==39) {
			wMax[0]+=0.0005;
			wMin[0]+=0.0005;
		}
		if(e.getKeyCode()==37) {
			wMax[0]-=0.0005;
			wMin[0]-=0.0005;
		}
		if(e.getKeyCode()==38) {
			wMax[1]+=0.0005;
			wMin[1]+=0.0005;
		}
		if(e.getKeyCode()==40) {
			wMax[1]-=0.0005;
			wMin[1]-=0.0005;
		}
		if(e.getKeyCode()==81) {if(e.getKeyCode()==39) {
			wMax[0]+=0.0005;
			wMin[0]+=0.0005;
		}
		if(e.getKeyCode()==37) {
			wMax[0]-=0.0005;
			wMin[0]-=0.0005;
		}
		if(e.getKeyCode()==38) {
			wMax[1]+=0.0005;
			wMin[1]+=0.0005;
		}
		if(e.getKeyCode()==40) {
			wMax[1]-=0.0005;
			wMin[1]-=0.0005;
		}
		if(e.getKeyCode()==81) {
			wMax[0]-=0.0005;
			wMin[0]+=0.0005;
			wMax[1]-=0.0005;
			wMin[1]+=0.0005;			
		}
		if(e.getKeyCode()==65) {
			wMax[0]+=0.0005;
			wMin[0]-=0.0005;
			wMax[1]+=0.0005;
			wMin[1]-=0.0005;			
		}
			wMax[0]-=0.0005;
			wMin[0]+=0.0005;
			wMax[1]-=0.0005;
			wMin[1]+=0.0005;			
		}
		if(e.getKeyCode()==65) {
			wMax[0]+=0.0005;
			wMin[0]-=0.0005;
			wMax[1]+=0.0005;
			wMin[1]-=0.0005;			
		}
	}
	
}
