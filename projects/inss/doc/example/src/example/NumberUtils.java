package example;
public class NumberUtils {

	public int nextOdd(int x) {   // 0
		if (x % 2 != 0) {         // 0
			x++;                  // 6
		}
		x++;                      // 9
		return x;                 // 9
	}
	
//	public int max(int[] array) {  // 0
//		int i = 0;                 // 0
//		int max = array[++i];      // 0
//		while (i < array.length) { // 9
//			if(array[i] > max)     // 15
//				max = array[i];    // 22
//			i++;                   // 26
//		}
//		return max;                // 32
//	}

}
