package alu;

import java.util.ArrayList;

public class ALUTest {
	private ALU alu = new ALU();

	public static void main(String[] args) {
		ALUTest test = new ALUTest();
		// test.testIntegerRepresentation();
		// test.testFloatRepresentation();
		// test.testLeftShift();
		// test.testFullAdder();
		// test.testClaAdder();
		// test.testIntegerSubtraction();
		// test.testAdder();
		test.test();
	}

	public void testAdder() {
		System.out.println(alu.adder("0001", "0011", '0', 4));
	}

	public void test() {
//		System.out.println(alu.negation("0001"));
//		System.out.println(alu.adder(alu.negation("0001"), "0000", '1', 4));
//		String result = alu.integerMultiplication("1101", "0010", 8);
//		System.out.println(result);
//		System.out.println(alu.integerTrueValue(result.substring(1)));
//		System.out.println(alu.ariRightShift("11111110", 1));
//		for (int i = 4; i < 5; i++) {
//			for (int a = -(int) Math.pow(2, i - 1); a < Math.pow(2, i - 1); a++) {
//				for (int b = -(int) Math.pow(2, i - 1); b < Math.pow(2, i - 1); b++) {
//					String s = alu.integerMultiplication(alu.integerRepresentation(a + "", 4),
//							alu.integerRepresentation(b + "", 4), 4);
//					System.out.println(a + "\t" + b + "\t" + s + "\t" + alu.integerTrueValue(s.substring(1)) + "\t"
//							+ (a * b + ""));
//				}
//			}
//		}
//		System.out.println(alu.floatMultiplication("000100101", "001011000", 4, 4));
		System.out.println(alu.floatAddition("001101111", "001101111", 4, 4, 4));
//		System.out.println(alu.signedAddition("0101", "0010", 4));
//		for (int i = 4; i < 5; i++) {
//			for (int a = -(int) Math.pow(2, i - 1); a < Math.pow(2, i - 1); a++) {
//				for (int b = -(int) Math.pow(2, i - 1); b < Math.pow(2, i - 1); b++) {
//					String s = alu.adder(alu.integerRepresentation(a + "", 4),
//							alu.integerRepresentation(b + "", 4),'0' ,8);
//					System.out.println(a + "\t" + b + "\t" + s + "\t" + alu.integerTrueValue(s.substring(1)) + "\t"
//							+ (a * b + ""));
//				}
//			}
//		}
//		alu.integerDivision("0111", "0111", 4);
//		System.out.println(alu.adder("1101", "0", '1', 4));
//		System.out.println(alu.leftShift("10011", 1));
	}

	public void testIntegerSubtraction() {
		for (int i = 4; i < 5; i++) {
			for (int a = -(int) Math.pow(2, i - 1); a < Math.pow(2, i - 1); a++) {
				for (int b = -(int) Math.pow(2, i - 1); b < Math.pow(2, i - 1); b++) {
					String s = alu.integerSubtraction(alu.integerRepresentation(a + "", 4),
							alu.integerRepresentation(b + "", 4), i);
					System.out.println(a + "\t" + b + "\t" + s + "\t" + alu.integerTrueValue(s.substring(1)) + "\t"
							+ (a - b + ""));
				}
			}
		}
	}

	public void testIntegerRepresentation() {
		for (int length = 1; length < 8; length++) {
			System.out.println("length:\t" + length);
			System.out.println("dec\tbin");
			for (int i = -(int) Math.pow(2, length - 1); i < Math.pow(2, length - 1); i++) {
				String res = alu.integerRepresentation(i + "", length);
				System.out.print(i + "\t" + res);
				System.out.println("\t" + alu.integerTrueValue(res));
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

	public void testLeftShift() {
		System.out.println(alu.leftShift("11111", 6));
	}

	public void testFullAdder() {
		char x = '0';
		char y = '0';
		char c = '0';
		for (int i = 0; i < 1 << 3; i++) {
			x = (char) ('0' + (i & 1));
			y = (char) ('0' + ((i & (1 << 1)) >> 1));
			c = (char) ('0' + ((i & (1 << 2)) >> 2));
			System.out.println(i + "\t" + alu.fullAdder(x, y, c));
			System.out.println(c + "" + y + "" + x);
		}
	}

	public void testClaAdder() {
		char a = '0';
		char b = '0';
		char c = '0';
		char d = '0';
		ArrayList<String> strs = new ArrayList<>();
		for (int i = 0; i < 1 << 4; i++) {
			a = (char) ('0' + (i & 1));
			b = (char) ('0' + ((i & (1 << 1)) >> 1));
			c = (char) ('0' + ((i & (1 << 2)) >> 2));
			d = (char) ('0' + ((i & (1 << 3)) >> 3));
			strs.add(d + "" + c + "" + b + "" + a);
		}
		for (String sa : strs) {
			for (String sb : strs) {
				System.out.println(sa + "\t" + sb + "\t" + c + "\t" + alu.claAdder(sa, sb, '0'));
				System.out.println(sa + "\t" + sb + "\t" + c + "\t" + alu.claAdder(sa, sb, '1'));
			}
		}
	}
}
