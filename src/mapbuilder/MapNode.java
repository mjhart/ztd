package mapbuilder;

import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;




public class MapNode {
	
	public final double lat;
	public final double lon;
	public final double x;
	public final double y;
	public final String id;
	private MapNode next;
	private List<MapWay> ways;
	
	public MapNode(String id, double lat, double lon) {
		this.id = id;
		this.lat = lat;
		this.lon = lon;
		//TODO: fill in logic for transforming into pixels
		//We could house this in DistConverter, and it could be done in the parser
		this.x = (lon + 80)*(100000) - 859500;
		this.y = (lat)*(100000) - 4182500;
		ways = new LinkedList<MapWay>();
	}
	
	public void setNext(MapNode next) {
		this.next = next;
	}
	
	public MapNode getNext() {
		return next;
	}
	
	public void addWay(MapWay way) {
		ways.add(way);
	}
	
	public List<MapWay> getWays() {
		return Collections.unmodifiableList(ways);
	}
	
	public Point2D getPoint() {
		return new Point2D.Double(x,y);
	}
	
	@Override
	public String toString() {
		//return "Lat: " + lat + " Lon: " + lon;
		return id;
	}
	

}
