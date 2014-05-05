package mapbuilder;

import java.awt.Polygon;
import java.util.List;
import java.util.ArrayList;

public class Relation {
	
	private Polygon _p;
	private String _name;
	
	public Relation(List<MapWay> lmw) {
		List<Integer> xparr = new ArrayList<>(0);
		List<Integer> yparr = new ArrayList<>(0);

		for (MapWay mw: lmw) {
			List<MapNode> nList = mw.getNodes();
			int size = nList.size();
			for(int i = 0; i < size; i++) {
				MapNode mn = nList.get(i);
				xparr.add((int) mn._coords.x);
				yparr.add((int) mn._coords.y);
			}
		}
		int[] xpoints = new int[xparr.size()];
		int[] ypoints = new int[yparr.size()];
		for (int i = 0; i < xparr.size(); i++) {
			xpoints[i] = xparr.get(i);
			ypoints[i] = yparr.get(i);
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
