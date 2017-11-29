import java.io.FileWriter;
import java.util.Random;

import game2048.Tile;
import heuristic.Heuristic;
import heuristic.*;

public class test {

	public static void main(String args[]) throws Exception {
		Tile[] input = new Tile[16];
		int testTime = 100;
		Integer[] element = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,2,2,2,4,4,4,4,4,4,8,8,8,16,16,16,32,32,32,64,64,128,128,256,512,1024,2048};
		Random rand = new Random();
		FileWriter fw = new FileWriter("score.csv");
		
		// Change the function object below
		Heuristic func = new AverageNum();
		double score = 0;

		for (int cur = 0; cur < testTime; cur++) {
			System.out.println("Case "+cur);
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					input[i*4+j] = new Tile(i,j,element[(rand.nextInt(1000)*11+23)%element.length]);
					System.out.print(input[i*4+j].getValue()+"\t");
				}
				System.out.println();
			}
			
			// feed input to your function
			score = func.function(input);
			System.out.println("Score: "+score);
			System.out.println("===========================================");
			fw.append(Integer.toString(cur));
			fw.append(",");
			fw.append(Double.toString(score));
			fw.append("\n");
			
		}
		fw.close();
	}
}
