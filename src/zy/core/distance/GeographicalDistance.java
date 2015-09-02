package zy.core.distance;

public class GeographicalDistance implements DistanceMeasure {

	private static final long serialVersionUID = -1885360458003161243L;

	@Override
	public double compute(double[] a, double[] b) {
		if (a.length != 2 || b.length != 2)
			throw new IllegalArgumentException("");

		double R = 6378.137d;
		double distance = 0.0;
		double dLat = (a[0] - b[0]) * Math.PI / 180;
		double dLon = (a[1] - b[1]) * Math.PI / 180;
		double t = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(a[0] * Math.PI / 180)
				* Math.cos(b[0] * Math.PI / 180) * Math.sin(dLon / 2)
				* Math.sin(dLon / 2);
		distance = (2 * Math.atan2(Math.sqrt(t), Math.sqrt(1 - t))) * R;
		return distance;
	}

}
