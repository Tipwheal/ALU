package alu;

import java.util.ArrayList;

public class ALUTest {
	private ALU alu = new ALU();

	public static void main(String[] args) {
		ALUTest test = new ALUTest();
		test.testIntegerRepresentation();
		test.testFloatRepresentation();
		test.testLeftShift();
		test.testFullAdder();
		test.testClaAdder();
	}

	public void testIntegerRepresentation() {
		for (int length = 1; length < 32; length++) {
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
