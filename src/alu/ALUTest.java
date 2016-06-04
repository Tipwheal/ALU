package alu;

public class ALUTest {
	private ALU alu = new ALU();

	public static void main(String[] args) {
		ALUTest test = new ALUTest();
		test.test();
		 test.testIntegerRepresentation();
		// test.testFloatRepresentation();
	}

	public void test() {
		int i = 1<<31;
		System.out.println(i+"");
//		System.out.println(Math.pow(2, 32));
//		System.out.println(alu.integerRepresentation("-2147483648", 32));
	}
	
	public void testIntegerRepresentation() {
		for (int length = 1; length < 32; length++) {
			System.out.println("length:\t" + length);
			System.out.println("dec\tbin");
			for (int i = -(int) Math.pow(2, length - 1); i < Math.pow(2, length - 1); i++) {
				String res = alu.integerRepresentation(i + "", length);
				System.out.print(i + "\t" + res);
				System.out.println("\t" + alu.integerTrueValue(res));
				break;
			}
		}
	}

	public void testFloatRepresentation() {
		System.out.println("dec\tbin");
		for (int i = 0; i < 400; i += 6) {
			for (int j = 0; j < 200; j += 9) {
				System.out.println(i + "." + j + "\t" + alu.floatRepresentation(i + "." + j, 4, 10));
			}
		}
	}
}
