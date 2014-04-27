package mapbuilder;

import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.util.List;

public class Building {
	
	private Polygon _p;
	
	public Building(MapWay mw) {
		List<MapNode> nList = mw.getNodes();
		int size = nList.size();
		int[] xpoints = new int[size];
		int[] ypoints = new int[size];
		for(int i = 0; i < size; i++) {
			MapNode mn = nList.get(i);
			xpoints[i] = (int) mn._coords.x;
		}
	}

}