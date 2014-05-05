package mapbuilder;

import java.awt.geom.Point2D;

public class DistConverter {
	
	private double left;
	private double bott;
	private double right;
	private double top;
	private double latrat;
	private double lonrat;
	public final double DLAT;
	public final double DLON;
	public final double EARTH_RADIUS = 6371;

	
	public DistConverter(double centlat, double centlon) {
		DLON = Math.toDegrees(.3/EARTH_RADIUS/Math.cos(Math.toRadians(centlat)));
		DLAT = Math.toDegrees(.3/EARTH_RADIUS);
		
	}
	

	
	
	public double latRat(double toplat, double bottlat, double mapheight) {
		latrat = (mapheight)/(toplat - bottlat);
		return latrat;
	}
	
	public double lonRat(double leftlon, double rightlon, double mapwidth) {
		lonrat = (mapwidth)/(rightlon - leftlon);
		return lonrat;
	}

	
	
	public double getBott(double centlat) {
		bott = centlat - DLAT;
		return bott;
	}
	
	public double getTop(double centlat) {
		top = centlat + DLAT;
		return top;
	}
	
	public double getLeft(double centlon) {
		left = centlon - DLON;
		return left;
	}
	
	public double getRight(double centlon) {
		right = centlon + DLON;
		return right;
	}

}
