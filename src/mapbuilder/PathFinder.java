package mapbuilder;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import cs195n.Vec2f;

public class PathFinder {
	
	private List<MapNode> spawns;
	
	public PathFinder() {
		spawns = new LinkedList<MapNode>();
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
		//HashMap<MapNode, MapNode> prev = new HashMap<MapNode, MapNode>();
		HashSet<MapNode> visited = new HashSet<MapNode>();
		HashMap<MapNode, Integer> dist = new HashMap<MapNode, Integer>();
		
		for(MapNode n : nodes) {
			dist.put(n, Integer.MAX_VALUE);
		}
		
		visited.add(base);
		dist.put(base, 0);
		
		PriorityQueue<MapNode> pq = new PriorityQueue<MapNode>(10, new MyComparator(dist));
		
		pq.add(base);
		
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
					
					Vec2f nv2 = new Vec2f((float)node.lon,(float) node.lat);
					float d = nv.dist2(nv2);
					
					if(dist.get(node) + d < dist.get(nbor)) {
						dist.put(nbor, (int) (dist.get(node)+d));
						pq.remove(nbor);
						pq.add(nbor);
						nbor.setNext(node);
					}
				}
			}
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
		System.out.println("adj: " + results);
		return results;
	}
	
	class MyComparator implements Comparator<MapNode> {
		
		private HashMap<MapNode, Integer> dist;
		
		public MyComparator(HashMap<MapNode, Integer> distMap) {
			dist = distMap;
		}

		@Override
		public int compare(MapNode o1, MapNode o2) {
			return dist.get(o1).compareTo(dist.get(o2));
		}
		
	}
	
}
