import java.util.Random;

import game2048.Tile;
import heuristic.Heuristic;
import heuristic.SameNumberDistance;

public class test {

	public static void main(String args[]) {
		Tile[] input = new Tile[16];
		int testTime = 100;
		int[] element = {2,2,2,2,2,2,2,2,2,4,4,4,4,4,4,8,8,8,16,16,16,32,32,32,64,64,128,128,256,512,1024,2048};
		Random rand = new Random();
		
		// Change the function object below
		Heuristic func = new SameNumberDistance();
		double score = 0;

		for (int cur = 0; cur < testTime; cur++) {
			System.out.println("Case "+cur);
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					input[i*4+j] = new Tile(i,j,element[(rand.nextInt(1000)*11+23)%element.length]);
					System.out.print(input[i*4+j].getValue()+" | ");
				}
				System.out.println();
			}
			
			// feed input to your function
			score = func.function(input);
			System.out.println("Score: "+score);
			System.out.println("===========================================");
		}
	}
}
