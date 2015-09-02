package zy.core.distance;

public class ChebyshevDistance implements DistanceMeasure {

	private static final long serialVersionUID = 8145753447277483830L;

	@Override
	public double compute(double[] a, double[] b) {
		if (a.length != b.length)
			throw new IllegalArgumentException();

		double max = 0;
		for (int i = 0; i < a.length; i++) {
			max = Math.max(max, Math.abs(a[i] - b[i]));
		}
		return max;
	}

}
