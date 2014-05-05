package mapbuilder;

import gameEngine.Base;
import gameEngine.Referee;
import gameEngine.towers.AbstractTower;
import gameEngine.zombie.Zombie;
import gui.SpriteImp;

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
import java.util.concurrent.TimeoutException;

import javax.imageio.ImageIO;

import ztdpac.ImageRet;
import gui.TestFrontEnd;

import cs195n.Vec2f;
import cs195n.Vec2i;
import mapbuilder.MapWay;
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
		try {
			_tf = tf;
			_ref = ref;

			wMin = new double[2];
			wMax = new double[2];

			File stadd = Retriever.getFromAddress(address);
			//File stadd = new File("stadd.xml");
			if (stadd == null) {
				_tf.dataError(1);
				_sentDataError = true;
			}
			else {
				XmlParser x = new XmlParser(this);
				Point2D.Double cent = x.parseAddress(stadd);
				if ((cent == null) && (!_sentDataError)) {
					_tf.dataError(2);
					_sentDataError = true;
				}
				else {
					DistConverter dc = new DistConverter(cent.y, cent.x);
					wMin[0] = dc.getLeft(cent.x);
					wMin[1] = dc.getBott(cent.y);
					wMax[0] = dc.getRight(cent.x);
					wMax[1] = dc.getTop(cent.y);
					File box = Retriever.getBox(wMin[0], wMin[1], wMax[0], wMax[1]);
					//File box = new File("box.xml");
					if ((box == null) && (!_sentDataError)) {
						_tf.dataError(1);
						_sentDataError = true;
					}
					else {
						if ((!x.parseBox(box)) && (!_sentDataError)) {
							_tf.dataError(2);
							_sentDataError = true;
						}
						else {
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
					}
				}
			}
		}
		catch(TimeoutException e) {
			_tf.dataError(1);
			_sentDataError = true;
		}
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
		return  (float) ((wMax[1]-lat)/(wMax[1]-wMin[1]) * 10000);
	}
	
	public float lonToX(double lon) {
		return  (float) ((lon - wMin[0])/(wMax[0]-wMin[0]) * 10000);
	}
	
}
