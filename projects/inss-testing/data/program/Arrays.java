package foo.bar;

public class Arrays {

	public static int max(int[] array) {
		int i = 0;
		int max = array[i++];
		while (i < array.length) {
			if (array[i] > max)
				max = array[i];
			i++;
		}
		return max;
	}
}
