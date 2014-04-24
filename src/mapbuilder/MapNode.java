package mapbuilder;

import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cs195n.Vec2f;




public class MapNode  implements Comparable<MapNode> {
	
	public final Vec2f _coords;
	public final String id;
	private MapNode next;
	private List<MapWay> ways;
	private double _dist;
	
	public MapNode(String id, float lat, float lon) {
		this.id = id;
		_coords = new Vec2f(lon, lat);
		ways = new LinkedList<MapWay>();
		_dist = Double.MAX_VALUE;
	}
	
	public MapNode(String id, double lat, double lon) {
		this.id = id;
		_coords = new Vec2f((float)lon, (float)lat);
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
	
	public float getX() {
		return _coords.x;
	}
	
	public float getY() {
		return _coords.y;
	}
	
	public double getDist() {
		return _dist;
	}
	
	public void setDist(double dist) {
		_dist = dist;
	}
	
	@Override
	public String toString() {
		//return "Lat: " + lat + " Lon: " + lon;
		return id;
	}

	@Override
	public int compareTo(MapNode o) {
		return -Double.compare(this._dist, o._dist);
	}
	

}
