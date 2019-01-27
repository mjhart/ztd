package mapbuilder;

import gameEngine.Referee;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.TimeoutException;

import gui.TestFrontEnd;

import cs195n.Vec2f;
import cs195n.Vec2i;
//import mapbuilder.PathFinder.MyComparator;

public class Map {
	private List<MapWay> _ways;
	private List<MapWay> _highways;
	private List<MapNode> _nodes;
	private List<Building> _buildings;
	private List<Building> _landuse;
	private List<Building> _waterways;
	private List<MapWay> _footways;
	private List<MapWay> _residential;
	private List<MapWay> _secondary;
	private List<MapWay> _tertiary;
	private List<Relation> _waterrels;
	private List<MapWay> _streams;

	
	private static final int MIN_DIST = 400000;

	private double[] wMin;
	private double[] wMax;
	private Vec2i _size;
	//private PathFinder _pf;
	private MapNode _baseNode;
	private List<MapNode> _srcs;
	private Referee _ref;
	private BufferedImage _img;
	private TestFrontEnd _tf;
	private boolean _sentDataError = false;
	
	public Map(String address, Referee ref, TestFrontEnd tf) {
		
		_tf = tf;
		_ref = ref;

		wMin = new double[2];
		wMax = new double[2];

    XmlParser x = new XmlParser(this);
    try (InputStream box = getBoxInputStream(address, x)){
			if ((box == null) && (!_sentDataError)) {
				_tf.dataError(1);
				_sentDataError = true;
				return;
			}
			if ((!x.parseBox(box)) && (!_sentDataError)) {
				_tf.dataError(2);
				_sentDataError = true;
				return;
			}
			_ways = x.getWays();
			_nodes = x.getNodes();
			_highways = x.getHighs();
			_buildings = x.getBuildings();
			_landuse = x.getLanduse();
			_waterways = x.getWaterways();
			_footways = x.getFootways();
			_residential = x.getResidential();
			_secondary = x.getSecondary();
			_tertiary = x.getTertiary();
			_waterrels = x.getWaterrels();
			_streams = x.getStreams();

			// find closest highway node to center
			double dist = Double.MAX_VALUE;
			for(MapWay w : _highways) {
				for(MapNode n : w.getNodes()) {
					double d2 = (n.getX()-5000)*(n.getX()-5000) + (n.getY()-5000)*(n.getY()-5000);
					if(d2 < dist) {
						dist = d2;
						_baseNode = n;
					}
				}
			}
		}
		catch(TimeoutException e) {
			_tf.dataError(1);
			_sentDataError = true;
		} catch (IOException e) {
      e.printStackTrace();
    }
  }

	private InputStream getBoxInputStream(String address, XmlParser x) throws TimeoutException {
		if (isStored(address)) {
			String formatted = address.replace(" ", "_");
			try {
				FileInputStream inputStream =
								new FileInputStream(new File("src/main/resources/maps/" + formatted + ".xml"));
				setW(address);
				return inputStream;
			} catch (FileNotFoundException e) {
				throw new IllegalStateException("Cannot find stored file", e);
			}
		}

		Point2D.Double cent = null;
		try (InputStream stadd = Retriever.getFromAddress(address)) {
			if (stadd == null) {
				_tf.dataError(1);
				_sentDataError = true;
				cent = null;
			}
			cent = x.parseAddress(stadd);
		} catch (IOException e) {
			System.out.println("Problem closing address input stream: " + e.getMessage());
		}
		if ((cent == null) && (!_sentDataError)) {
			_tf.dataError(2);
			_sentDataError = true;
			return null;
		}
		DistConverter dc = new DistConverter(cent.y, cent.x);
		wMin[0] = dc.getLeft(cent.x);
		wMin[1] = dc.getBott(cent.y);
		wMax[0] = dc.getRight(cent.x);
		wMax[1] = dc.getTop(cent.y);

		return Retriever.getBox(wMin[0], wMin[1], wMax[0], wMax[1]);
	}
	
	
	public void setSize(Vec2i size) {
		_size = size;
	}
	
	public List<MapNode> getSourceList() {
		return Collections.unmodifiableList(_srcs);
	}
	
	public BufferedImage getImage() {
		return _img;
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
	
	public List<Building> getBuildings() {
		return Collections.unmodifiableList(_buildings);
	}
	
	public List<Building> getLanduse() {
		return Collections.unmodifiableList(_landuse);
	}
	
	public List<Building> getWaterways() {
		return Collections.unmodifiableList(_waterways);
	}
	
	public List<MapWay> getFootways() {
		return Collections.unmodifiableList(_footways);
	}
	
	public List<MapWay> getResidential() {
		return Collections.unmodifiableList(_residential);
	}
	
	public List<MapWay> getSecondary() {
		return Collections.unmodifiableList(_secondary);
	}
	
	public List<MapWay> getTertiary() {
		return Collections.unmodifiableList(_tertiary);
	}
	
	public List<Relation> getWaterrels() {
		return Collections.unmodifiableList(_waterrels);
	}
	
	public List<MapWay> getStreams() {
		return Collections.unmodifiableList(_streams);
	}
	
	
	/* path finding methods */
	
	///*
	private List<MapNode> potentialSrcs() {
		List<MapNode> results = new LinkedList<MapNode>();
		for(MapWay w : _highways) {
			for(MapNode n : w.getNodes()) {
				if(n.getX() >= 10000 && n.getX() - 500 < 10000 && n.getY() < 10500 && n.getY() > -500) {
					boolean farEnough = true;
					for(MapNode n2 : results) {
						if(n._coords.dist2(n2._coords) < MIN_DIST) {
							farEnough = false;
							break;
						}
					}
					if(farEnough) {
						results.add(n);
					}
					continue;
				}
				if(n.getY() >= 10000 && n.getY() - 500 < 10000 && n.getX() < 10500 && n.getX() > -500) {
					boolean farEnough = true;
					for(MapNode n2 : results) {
						if(n._coords.dist2(n2._coords) < MIN_DIST) {
							farEnough = false;
							break;
						}
					}
					if(farEnough) {
						results.add(n);
					}
					continue;
				}
				if(n.getX() <= 0 && n.getX() + 500 > 0 && n.getY() < 10500 && n.getY() > -500) {
					boolean farEnough = true;
					for(MapNode n2 : results) {
						if(n._coords.dist2(n2._coords) < MIN_DIST) {
							farEnough = false;
							break;
						}
					}
					if(farEnough) {
						results.add(n);
					}
					continue;
				}
				if(n.getY() <= 0 && n.getY() + 500 > 0 && n.getX() < 10500 && n.getX() > -500) {
					boolean farEnough = true;
					for(MapNode n2 : results) {
						if(n._coords.dist2(n2._coords) < MIN_DIST) {
							farEnough = false;
							break;
						}
					}
					if(farEnough) {
						results.add(n);
					}
					continue;
				}
			}
		}
		if(results.size() == 0) {
			for(MapWay w : _highways) {
				for(MapNode n : w.getNodes()) {
					if(n.getX() >= 10000 && n.getX() - 1000 < 10000 && n.getY() < 11000 && n.getY() > -1000) {
						boolean farEnough = true;
						for(MapNode n2 : results) {
							if(n._coords.dist2(n2._coords) < MIN_DIST) {
								farEnough = false;
								break;
							}
						}
						if(farEnough) {
							results.add(n);
						}
						continue;
					}
					if(n.getY() >= 10000 && n.getY() - 1000 < 10000 && n.getX() < 11000 && n.getX() > -1000) {
						boolean farEnough = true;
						for(MapNode n2 : results) {
							if(n._coords.dist2(n2._coords) < MIN_DIST) {
								farEnough = false;
								break;
							}
						}
						if(farEnough) {
							results.add(n);
						}
						continue;
					}
					if(n.getX() <= 0 && n.getX() + 1000 > 0 && n.getY() < 11000 && n.getY() > -1000) {
						boolean farEnough = true;
						for(MapNode n2 : results) {
							if(n._coords.dist2(n2._coords) < MIN_DIST) {
								farEnough = false;
								break;
							}
						}
						if(farEnough) {
							results.add(n);
						}
						continue;
					}
					if(n.getY() <= 0 && n.getY() + 1000 > 0 && n.getX() < 11000 && n.getX() > -1000) {
						boolean farEnough = true;
						for(MapNode n2 : results) {
							if(n._coords.dist2(n2._coords) < MIN_DIST) {
								farEnough = false;
								break;
							}
						}
						if(farEnough) {
							results.add(n);
						}
						continue;
					}
				}
			}
		}
		return results;
	}
	
	private List<MapNode> findPaths(List<MapNode> srcList, MapNode base) {
		LinkedList<MapNode> spawns = new LinkedList<MapNode>();
		HashSet<MapNode> visited = new HashSet<MapNode>();
		HashMap<MapNode, Double> dist = new HashMap<MapNode, Double>();
		
		///*
		for(MapNode n : _nodes) {
			dist.put(n, Double.MAX_VALUE);
		}
		//*/
		
		visited.add(base);
		dist.put(base, 0d);
		//base.setDist(0);
		
		PriorityQueue<MapNode> pq = new PriorityQueue<MapNode>(10, new MyComparator(dist));
		//PriorityQueue<MapNode> pq = new PriorityQueue<MapNode>();
		
		pq.add(base);
		
		
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
					float d = nv.dist(nv2);
					//System.out.println(nbor.id);
					//System.out.println("distance to: " + Double.toString((dist.get(node) + d)));
					///*
					if(dist.get(node) + d < dist.get(nbor)) {
						dist.put(nbor, dist.get(node)+d);
						pq.remove(nbor);
						pq.add(nbor);
						nbor.setNext(node);
						nbor.setDist(dist.get(nbor));
					}
					//*/
					/*
					System.out.println("Node + d: " + node.getDist() + d);
					System.out.println("nbor: " + nbor.getDist());
					if(node.getDist() + d < nbor.getDist()) {
						nbor.setDist(node.getDist()+d);
						pq.remove(nbor);
						pq.add(nbor);
						nbor.setNext(node);
					}
					*/
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
	
	public List<MapNode> calculatePath() {
		List<MapNode> srcs = potentialSrcs();
		_srcs = findPaths(srcs, _baseNode);
		return _srcs;
	}
	
	public double[] getwMax() {
		return wMax;
	}
	
	public double[] getwMin() {
		return wMin;
	}
	
	public float latToY(double lat) {
		return  (float) ((wMax[1]-lat)/(wMax[1]-wMin[1]) * 10000);
	}
	
	public float lonToX(double lon) {
		return  (float) ((lon - wMin[0])/(wMax[0]-wMin[0]) * 10000);
	}
	
	private boolean isStored(String addr) {
		return addr.equalsIgnoreCase("Brown University") ||
			addr.equalsIgnoreCase("Wall Street") ||
			addr.equalsIgnoreCase("White House") ||
			addr.equalsIgnoreCase("Eiffel Tower") ||
			addr.equalsIgnoreCase("San Francisco") ||
			addr.equalsIgnoreCase("Philadelphia Museum of Art") ||
			addr.equalsIgnoreCase("London") ||
			addr.equalsIgnoreCase("Amsterdam");
	}
	
	public void setW(String word1) {
		if (word1.equals("Brown University")) {
			setWBrown();
		}
		if (word1.equals("Wall Street")) {
			setWWall();
		}
		if (word1.equals("White House")) {
			setWWhite();
		}
		if (word1.equals("Eiffel Tower")) {
			setWEiffel();
		}
		if (word1.equals("San Francisco")) {
			setWSan();
		}
		if (word1.equals("Philadelphia Museum of Art")) {
			setWPhiladelphia();
		}
		if (word1.equals("London")) {
			setWLondon();
		}
		if (word1.equals("Amsterdam")) {
			setWAmsterdam();
		}
	}
	
	public void setWBrown() {
		wMin[0] = -71.40492698993405;
		wMin[1] = 41.824158135182245;
		wMax[0] = -71.39768572051334;
		wMax[1] = 41.829554064817756;
	}
	
	public void setWWall() {
		wMin[0] = -74.01239001118442;
		wMin[1] = 40.70331963518225;
		wMax[0] = -74.00527198881558;
		wMax[1] = 40.70871556481776;
	}
	
	public void setWWhite() {
		wMin[0] = -77.04001980797291;
		wMin[1] = 38.89500243518224;
		wMax[0] = -77.03308655721868;
		wMax[1] = 38.900398364817754;
	}
	
	public void setWEiffel() {
		wMin[0] = 2.2903993633314004;
		wMin[1] = 48.85556293518224;
		wMax[0] = 2.29860081119386;
		wMax[1] = 48.86095886481775;
	}
	
	public void setWSan() {
		wMin[0] = -122.42261250327931;
		wMin[1] = 37.77626213518224;
		wMax[0] = -122.4157854967207;
		wMax[1] = 37.781658064817755;
	}
	
	public void setWPhiladelphia() {
		wMin[0] = -75.18503682899697;
		wMin[1] = 39.96286273518224;
		wMax[0] = -75.17799649274744;
		wMax[1] = 39.96825866481775;
	}
	
	public void setWLondon() {
		wMin[0] = -0.13199437017418134;
		wMin[1] = 51.50457793518225;
		wMax[0] = -0.12332502982581865;
		wMax[1] = 51.50997386481776;
	}
	
	public void setWAmsterdam() {
		wMin[0] = 4.8956925620078575;
		wMin[1] = 52.36831083518224;
		wMax[0] = 4.904530437992143;
		wMax[1] = 52.373706764817754;
	}
	
	
	
}
