package example;

import example.NumberUtils;

public class Test {
	
	public static void main(String[] args) {
		NumberUtils utils = new NumberUtils();
		
		assert 3 == utils.nextOdd(1);
		assert 3 == utils.nextOdd(2);
		
//		int[] array = new int[] {1,2,3,4,5};
//		assert 5 == utils.max(array);
//		
//		array = new int[] {1,2,5,4,3};
//		assert 5 == utils.max(array);
//		
//		array = new int[] {5,2,1,4,3};
//		assert 5 == utils.max(array);
//		
//		array = new int[] {5};
//		assert 5 == utils.max(array);
	}

}
