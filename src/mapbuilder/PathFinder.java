package mapbuilder;

import java.util.LinkedList;
import java.util.List;

import cs195n.Vec2f;

public class PathFinder {
	
	public static List<MapNode> findSrcs(List<MapNode> nodeList, Vec2f max, Vec2f min) {
		List<MapNode> results = new LinkedList<MapNode>();
		for(MapNode n : nodeList) {
			if(n.lon >= max.x) {
				results.add(n);
				continue;
			}
			if(n.lat >= max.y) {
				results.add(n);
				continue;
			}
			if(n.lon <= min.x) {
				results.add(n);
				continue;
			}
			if(n.lat <= min.y) {
				results.add(n);
				continue;
			}
		}
		return results;
	}
	
	
	
}
