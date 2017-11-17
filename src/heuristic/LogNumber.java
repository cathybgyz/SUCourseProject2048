package heuristic;

public class LogNumber {
	//This method is used to return log(n)
	public static int LogReturn(int num) {
		switch(num) {
		case 2:
			return 1;
		case 4:
			return 2;
		case 8:
			return 3;
		case 16:
			return 4;
		case 32:
			return 5;
		case 64:
			return 6;
		case 128:
			return 7;
		case 256:
			return 8;
		case 512:
			return 9;
		case 1024:
			return 10;
		case 2048:
			return 11;
		case 4096:
			return 12;
		default:
			return 0;  //indicate the number is no the power of 2
		}
	}

}
