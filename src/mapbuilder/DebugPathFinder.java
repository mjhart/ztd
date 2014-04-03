package mapbuilder;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import cs195n.Vec2f;

public class DebugPathFinder {
	
	private List<MapNode> spawns;
	public HashMap<MapNode, Double> dist;
	public HashSet<MapNode> visited;
	private PriorityQueue<MapNode> pq;
	public boolean run;
	
	public DebugPathFinder(MapNode base, List<MapNode> nodes) {
		spawns = new LinkedList<MapNode>();
		dist = new HashMap<MapNode, Double>();
		visited = new HashSet<MapNode>();
		for(MapNode n : nodes) {
			dist.put(n, Double.MAX_VALUE);
		}
		
		visited.add(base);
		dist.put(base, 0d);
		
		pq = new PriorityQueue<MapNode>(10, new MyComparator(dist));
		
		pq.add(base);
	}
	
	public List<MapNode> findSrcs(List<MapNode> nodeList, Vec2f max, Vec2f min) {
		List<MapNode> results = new LinkedList<MapNode>();
		for(MapNode n : nodeList) {
			if(n.lon <= max.x && n.lon + 0.0001 > max.x) {
				results.add(n);
				continue;
			}
			if(n.lat <= max.y && n.lat + 0.0001 > max.y) {
				results.add(n);
				continue;
			}
			if(n.lon >= min.x && n.lon - 0.0001 < min.x) {
				results.add(n);
				continue;
			}
			if(n.lat >= min.y && n.lat - 0.0001 < min.y) {
				results.add(n);
				continue;
			}
		}
		return results;
	}
	
	public List<MapNode> findPaths(List<MapNode> srcList, MapNode base, List<MapNode> nodes, List<MapWay> ways) {
		
		
		MapNode node;
		while(!pq.isEmpty() && run) {
			node = pq.poll();
			System.out.println("Popped: " + node);
			System.out.println("Popped dist: " + dist.get(node));
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
					System.out.println(nbor.id);
					System.out.println("distance to: " + Double.toString((dist.get(node) + d)));
					
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
	
}
