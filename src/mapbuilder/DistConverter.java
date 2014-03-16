package mapbuilder;

public class DistConverter {
	
	
	public static void convertLat(double lat) {
		
		//There are 111 km to a degree of latitude, same regardless of where on the earth (basically)
		double km = 111*lat;

	}
	
	public static void convertLon(double lat, double lon) {
		//Distance for lon depends on lat
		double km = 111.320*Math.cos(lat)*lon;
	}
	
	
	
	
	
	
	
	

}
