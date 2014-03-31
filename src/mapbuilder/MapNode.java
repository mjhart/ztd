package mapbuilder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;




public class MapNode {
	
	public final double lat;
	public final double lon;
	public final String id;
	private MapNode next;
	private List<MapWay> ways;
	
	public MapNode(String id, double lat, double lon) {
		this.id = id;
		this.lat = lat;
		this.lon = lon;
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
	
	@Override
	public String toString() {
		//return "Lat: " + lat + " Lon: " + lon;
		return id;
	}
	

}
