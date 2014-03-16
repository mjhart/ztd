package mapbuilder;

import java.util.ArrayList;
import java.util.List;

public class MapWay {
	
	private List<MapNode> mapnodes;
	public final String id;
	
	public MapWay(String id) {
		this.id = id;
		mapnodes = new ArrayList<MapNode>(0);
	}
	
	public void addNode(MapNode mn) {
		mapnodes.add(mn);
	}
	
	public List<MapNode> getNodes() {
		return mapnodes;
	}
	

}
