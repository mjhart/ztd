package mapbuilder;

public class Constants {
	
	
	
	public final double DLAT = .0021739;
	public final double DLON;
	
	public Constants(double centlat, double centlon) {
		DLON = (.1)/(Math.cos(centlat)*(69.172));
	}
	
	

}
