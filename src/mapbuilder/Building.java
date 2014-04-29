package mapbuilder;

import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.util.List;

public class Building {
	
	private Polygon _p;
	private String _name;
	
	public Building(MapWay mw) {
		List<MapNode> nList = mw.getNodes();
		int size = nList.size();
		System.out.println("size " + size);
		int[] xpoints = new int[size];
		int[] ypoints = new int[size];
		for(int i = 0; i < size; i++) {
			MapNode mn = nList.get(i);
			xpoints[i] = (int) mn._coords.x;
			ypoints[i] = (int) mn._coords.y;
		}
		_p = new Polygon(xpoints, ypoints, xpoints.length);
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	public String getName() {
		return _name;
	}
	
	public Polygon getPolygon() {
		return _p;
	}

}