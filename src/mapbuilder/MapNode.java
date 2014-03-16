package mapbuilder;




public class MapNode {
	
	public final double lat;
	public final double lon;
	public final String id;
	private MapNode next;
	
	public MapNode(String id, double lat, double lon) {
		this.id = id;
		this.lat = lat;
		this.lon = lon;
	}
	
	public void setNext(MapNode next) {
		this.next = next;
	}
	
	
	
	

}
