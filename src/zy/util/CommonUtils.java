package zy.util;

import java.util.LinkedList;
import java.util.List;

public class CommonUtils {

	public static List<Integer> getColumnIndex(String s) {
		String[] parts = s.split(",");
		List<Integer> buf = new LinkedList<Integer>();
		for (int i = 0; i < parts.length; i++) {
			String p = parts[i];
			if (p.contains(":")) {
				int start = Integer.parseInt(p.split(":")[0]);
				int end = Integer.parseInt(p.split(":")[1]);
				for (int j = start; j <= end; j++) {
					buf.add(j);
				}
			} else {
				buf.add(Integer.parseInt(p));
			}
		}
		return buf;
	}
}
