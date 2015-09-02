package zy.core.distance;

public class EuclideanDistance implements DistanceMeasure {

	private static final long serialVersionUID = -8883613265648603783L;

	public double compute(double[] a, double[] b) {
		if (a.length != b.length)
			throw new IllegalArgumentException();
		
		double sum = 0;
		for (int i = 0; i < a.length; i++) {
			final double dp = a[i] - b[i];
			sum += dp * dp;
		}
		return Math.sqrt(sum);
	}

}
