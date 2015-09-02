package zy.core.distance;

public class CosineDistance implements DistanceMeasure {

	private static final long serialVersionUID = 8663550907767983108L;

	@Override
	public double compute(double[] a, double[] b) {
		if (a.length != b.length)
			throw new IllegalArgumentException();

		double numerator = 0;
		double len1 = 0, len2 = 0;
		for (int i = 0; i < a.length; i++) {
			len1 += Math.pow(a[i], 2);
		}
		for (int i = 0; i < b.length; i++) {
			len2 += Math.pow(b[i], 2);
		}
		for (int i = 0; i < a.length; i++) {
			numerator += a[i] * b[i];
		}
		double denominator = Math.sqrt(len1) * Math.sqrt(len2);
		return 1 - numerator / denominator;
	}
}
