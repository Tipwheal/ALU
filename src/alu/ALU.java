package alu;

public class ALU {

	public String integerRepresentation(String number, int length) {
		boolean negative = number.startsWith("-");
		if (negative)
			number = number.substring(1);
		int num = Integer.parseInt(number);
		int add = 1;
		String result = "";
		for (int i = 0; i < length; i++) {
			int last = num % 2;
			num /= 2;
			if (negative) {
				last = 1 - last + add;
				add = last == 2 ? add : 0;
				last %= 2;
			}
			result = last + result;
		}
		return result;
	}

	public String floatRepresentation(String number, int eLength, int sLength) {
		int lab = number.startsWith("-") ? 1 : 0;
		int exp = (int) Math.pow(2, eLength - 1) - 1;
		String s = "";
		if (!number.contains("."))
			number += ".0";
		String[] sep = number.split("\\.");
		int pre = Integer.parseInt(sep[0]);
		if (pre >= Math.pow(2, Math.pow(2, eLength - 1)))
			return (lab == 0 ? "+" : "-") + "Inf";
		while (pre > 0) {
			s = pre % 2 + s;
			pre /= 2;
		}
		exp += s.length() - 1;
		if (s.length() >= sLength + 1) {
			s = s.substring(1, 1 + sLength);
			return lab + this.integerRepresentation(exp + "", eLength) + s;
		}
		int[] tail = new int[sep[1].length()];
		for (int i = 0; i < sep[1].length(); i++) {
			tail[i] = Integer.parseInt(sep[1].substring(i, i + 1));
		}
		if (s.length() > 0) {
			while (s.length() < sLength + 1) {
				int add = 0;
				for (int i = tail.length - 1; i > 0; i--) {
					tail[i] = tail[i] * 2 + add;
					add = tail[i] / 10;
					tail[i] %= 10;
				}
				tail[0] = tail[0] * 2 + add;
				s += tail[0] / 10;
				tail[0] %= 10;
			}
			return lab + this.integerRepresentation(exp + "", eLength) + s.substring(1);
		} else {
			while (exp > 0) {
				int add = 0;
				for (int i = tail.length - 1; i > 0; i--) {
					tail[i] = tail[i] * 2 + add;
					add = tail[i] / 10;
					tail[i] %= 10;
				}
				tail[0] = tail[0] * 2 + add;
				s += tail[0] / 10;
				tail[0] %= 10;
				if (!s.contains("1")) {
					exp--;
				} else if (s.substring(s.indexOf("1")).length() == sLength + 1) {
					return lab + this.integerRepresentation(exp + "", eLength)
							+ s.substring(s.indexOf("1") + 1, s.indexOf("1") + 1 + sLength);
				}
			}
			s = "";
			for (int index = 0; index < sLength; index++) {
				int add = 0;
				for (int i = tail.length - 1; i > 0; i--) {
					tail[i] = tail[i] * 2 + add;
					add = tail[i] / 10;
					tail[i] %= 10;
				}
				tail[0] = tail[0] * 2 + add;
				s += tail[0] / 10;
				tail[0] %= 10;
			}
			return lab + this.integerRepresentation(0 + "", eLength) + s;
		}
	}

	public String ieee754(String number, int length) {
		return length == 32 ? this.floatRepresentation(number, 8, 23) : this.floatRepresentation(number, 11, 52);
	}

	public String integerTrueValue(String operand) {
		int number = 0;
		if (operand.startsWith("1"))
			number -= Math.pow(2, operand.length() - 1);
		for (int i = 1; i < operand.length(); i++) {
			number += Integer.parseInt(operand.substring(i, i + 1)) * (int) Math.pow(2, operand.length() - i - 1);
		}
		return number + "";
	}

	public String floatTrueValue(String operand, int eLength, int sLength) {
		String e = operand.substring(1, 1 + eLength);
		int exp = Integer.parseInt(this.integerTrueValue(e)) - 127;
		int[] s = new int[sLength];
		int dvd = 1;
		if (operand.substring(1 + eLength, 2 + eLength).equals(1)) {
			dvd = 0;
			s[0] = 5;
		}
		for (int i = 1; i < sLength; i++) {
			if (operand.substring(1 + eLength + i, 2 + eLength + i).equals("1")) {
				for (int j = i - 1; j >= 0; j--) {
					// s[j] +=
				}
			}
		}
		return null;
	}

	public String negation(String operand) {
		String result = "";
		for (int i = 0; i < operand.length(); i++)
			result += this.Not(operand.substring(i, i + 1));
		return result;
	}

	public String leftShift(String operand, int n) {
		return null;
	}

	public String logRightShift(String operand, int n) {
		return null;
	}

	public String ariRightShift(String operand, int n) {
		return null;
	}

	public String fullAdder(char x, char y, char c) {
		return null;
	}

	public String claAdder(String operand1, String operand2, char c) {
		return null;
	}

	public String oneAdder(String operand) {
		return null;
	}

	public String adder(String operand1, String operand2, char c, int length) {
		return null;
	}

	public String integerAddition(String operand1, String operand2, int length) {
		return null;
	}

	public String integerSubtraction(String operand1, String operand2, int length) {
		return null;
	}

	public String integerMultiplication(String operand1, String operand2, int length) {
		return null;
	}

	public String integerDivision(String operand1, String operand2, int length) {
		return null;
	}

	public String signedAddition(String operand1, String operand2, int length) {
		return null;
	}

	public String floatAddition(String operand1, String operand2, int eLength, int sLength, int gLength) {
		return null;
	}

	public String floatSubtraction(String operand1, String operand2, int eLength, int sLength, int gLength) {
		return null;
	}

	public String floatMultiplication(String operand1, String operand2, int eLength, int sLength) {
		return null;
	}

	public String floatDivision(String operand1, String operand2, int eLength, int sLength) {
		return null;
	}

	/**
	 * Not gate.
	 * 
	 * @param input
	 *            All inputs.
	 * @return "0" or "1".
	 */
	public String Not(String... input) {
		for (String s : input)
			if (s.equals("1"))
				return "0";
		return "1";

	}

	/**
	 * Or gate.
	 * 
	 * @param input
	 *            All inputs.
	 * @return "0" or "1".
	 */
	public String Or(String... input) {
		for (String s : input)
			if (s.equals("1"))
				return "1";
		return "0";
	}

	/**
	 * And gate.
	 * 
	 * @param input
	 *            All inputs.
	 * @return "0" or "1".
	 */
	public String And(String... input) {
		for (String s : input)
			if (s.equals("0"))
				return "0";
		return "1";
	}
}
