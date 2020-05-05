/* CS448 프로그래밍 과제 3. DES XDT & LDT
 * 20206030 배나영
 * Environment:JDK 1.8
 * */


package DES_XDTLDT;

public class DES_XDT {
	
	static final int[][] S4_Box = {
			{7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
			{13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
			{10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
			{3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}};
	
	static final int[][] S5_Box = {
			{2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
			{14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
			{4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
			{11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}};
	
	//convert given String to decimal int value
	private static int convertDecimal(String x) {
		int dec = 0;
		char one = '1';
		for(int i = 0; i <=x.length()-1; i++) {
			if(x.charAt(i) == one) {
				dec = (int) (dec + Math.pow(2, x.length()-i-1));
			}
		}
		return dec;
	}
	
	private static String addZeroX(String x) {
		//make the binary into 6 bits by adding 0 in the front of the string
		int length = x.length();
		String zero = "";
		
		if(length<6) {
			int diff = 6-length;
			for(int i = 0; i<diff; i++) {
				//based on the differences, add 0
				zero=zero.concat("0");
			}
		}
		//append zero string to the argument string
		x=zero.concat(x);
		return x;
	}
	
	private static String addZero4(String x) {
		int length = x.length();
		String zero = "";
		
		if(length<6) {
			int diff = 4-length;
			for(int i = 0; i<diff; i++) {
				//based on the differences, add 0
				zero=zero.concat("0");
			}
		}
		//append zero string to the argument string
		x=zero.concat(x);
		return x;
	}
	
	private static void XDT() {
		int[][] OutputXOR = new int[16][64];	//to store the value
		//x' = {0,1,...,63}		y' = {0,1,...,15}
		
		//1. Convert 0 ~ 63 to binary (store in x)
		for(int i = 0; i<64; i++) {
			for(int j = i; j<64; j++) {
				
				//two input X and X*
				int x = i;
				int xm = j; //x*
				
				int xPr = x ^ xm;	//result of XOR
				
				String firstX = Integer.toBinaryString(x);
				int fX = Integer.parseInt(firstX);
				firstX = addZeroX(firstX);
				
				//pre-compute the number of count of X'
				//find S(x)
				
				//2. evaluate S4(x)
				//2.1. calculate the position in S4-box
				//2.1.1. take the first and the last element to calculate the row
				String s_rowS = "", s_colS = "";
				int s_row = convertDecimal(s_rowS + firstX.charAt(0) + firstX.charAt(firstX.length()-1));
			
				//2.1.2. take the middle 4 elements to calculate the position
				int s_col =convertDecimal(s_colS + firstX.charAt(1)+firstX.charAt(2)+firstX.charAt(3)+firstX.charAt(4));
				int y = S4_Box[s_row][s_col];

				
				//---------------------------
				//find S(x*)
				String secondX = Integer.toBinaryString(xm);
				int sX = Integer.parseInt(secondX);
				secondX = addZeroX(secondX);
				
				String p_rowS = "", p_colS = "";
				int p_row = convertDecimal(p_rowS + secondX.charAt(0) + secondX.charAt(secondX.length()-1));
			
				//2.1.2. take the middle 4 elements to calculate the position
				int p_col =convertDecimal(p_colS + secondX.charAt(1)+secondX.charAt(2)+secondX.charAt(3)+secondX.charAt(4));
				int ym = S4_Box[p_row][p_col];
				
				//3. calculate output xor
				int yPr = y ^ ym;
				//4. put the value
				
				if(xPr == yPr) {
					OutputXOR[yPr][xPr] = OutputXOR[yPr][xPr] + 1;
				}
				//for the pairs e.g) (2,3) and (3,2)
				else {OutputXOR[yPr][xPr] = OutputXOR[yPr][xPr] + 2;};
			}
		}
		//print
		System.out.println("Input" + "\t|\t\t\t\t\t\t\t" + "Output XOR" + "\t\t\t\t\t\t\t\t|");
		System.out.println("---------------------------------------------------------------------------------------------------------------------------------------");
		System.out.printf("XOR" + "\t|");
		for(int i = 0; i<OutputXOR.length; i++) {
			System.out.printf(Integer.toHexString(i).toUpperCase() + "x\t|");
		}
		System.out.println();
		System.out.println("---------------------------------------------------------------------------------------------------------------------------------------");
		for(int j = 0; j<64; j++) {
			System.out.print(Integer.toHexString(j).toUpperCase() + "x\t|");
			for(int i = 0; i<OutputXOR.length; i++) {
				System.out.print(OutputXOR[i][j] + "\t|");
			}
			System.out.println();
		}

	}

	
	private static int LDT(int input, int output) {
//		int input = 0;	//~16 	
//		int output = 0;	//~64
		
		int freq = 0;	//store frequency
		
		for(int i = 0; i<64; i++) {
			//i = plaintext;
			
			//1. compute 	x & α
			int alpha = i & input;
			
			//1.1. match the 6 bits
			String al = Integer.toBinaryString(i);
			al = addZeroX(al);
			
			String alphabinary = Integer.toBinaryString(alpha);
			alphabinary = addZeroX(alphabinary);

			
			//2. for s(x) and β
			//2.1. find s5(x) (row and col in S5_Box)
			String s_rowS = "", s_colS = "";
			int s_row = convertDecimal(s_rowS + al.charAt(0) + al.charAt(al.length()-1));
			int s_col =convertDecimal(s_colS + al.charAt(1)+al.charAt(2)+al.charAt(3)+al.charAt(4));
			
			//2.2. compute 	s(x) & 	β
			int beta = S5_Box[s_row][s_col] & output;
			
			//3. count the number of 1 in the x & α
			int count = 0;
			for(int j = 0; j<al.length(); j++) {
				if(alphabinary.charAt(j) == '1') {
					count++;
				}
			}
						
			//3.1. count the number of 1 in the s(x) & 	β
			int cnt = 0;
			String bebinary = Integer.toBinaryString(beta);
			bebinary = addZero4(bebinary);
			for(int j = 0; j<bebinary.length(); j++) {
				if(bebinary.charAt(j) == '1') {
					cnt++;
				}
			}
			//4. calculate the difference & if they are same, increase the freq
			//the value should be even number
			if((count - cnt)%2 == 0) {
				freq++;
			}
		}
		return freq;
	}
			
	public static void main(String[] args) {
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println( "|\t\t\t\t\t\t\t\t\t" + "XDT" + "\t\t\t\t\t\t\t\t|");
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------------\n");
		XDT();
		
		
		
		
		int[][] ret = new int[64][16];
		for(int i = 1; i<64; i++) {
			for(int j = 1; j<16; j++) {
				ret[i-1][j-1] = LDT(i,j);
			}
		}
		System.out.println("\n\n");
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
		System.out.println( "|\t\t\t\t\t\t\t\t\t" + "LDT" + "\t\t\t\t\t\t\t|");
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------\n");

		System.out.println("Input" + "\t|\t\t\t\t\t\t" + "LDT" + "\t\t\t\t\t\t\t\t\t|");
		System.out.println("-------------------------------------------------------------------------------------------------------------------------------");
		System.out.printf("Value" + "\t|");
		for(int i = 1; i<16; i++) {
			System.out.printf(i + "\t|");
		}
		System.out.println("\n-------------------------------------------------------------------------------------------------------------------------------");

		for(int i = 1; i<64; i++) {
			System.out.print(i + "\t|");
			for(int j = 1; j<16; j++) {
				//subtract 32 bias
				System.out.print(ret[i-1][j-1] -32 + "\t|");
			}
			System.out.println();
		}

	}
}

