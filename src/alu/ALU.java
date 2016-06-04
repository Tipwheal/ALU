package alu;

public class ALU {

	/**
	 * 生成十进制整数的二进制补码表示。
	 * 
	 * @param number
	 *            十进制整数。若为负数；则第一位为“-”；若为正数或 0，则无 符号位
	 * @param length
	 *            二进制补码表示的长度
	 * @return number的二进制表示，长度为 length
	 */
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

	/**
	 * 生成十进制浮点数的二进制表示。需要考虑 0、反规格化、正负无穷（“+Inf” 和“-Inf”）、 NaN 等因素，具体借鉴 IEEE
	 * 754。舍入策略为向 0 舍入。
	 * 
	 * @param number
	 *            十进制浮点数（可能为不包含小数点的整数，例如 5）。若为负 数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param eLength
	 *            指数的长度，取值大于等于 4
	 * @param sLength
	 *            尾数的长度，取值大于等于 4
	 * @return number 的二进制表示，长度为 1+eLength+sLength。从左向右，依次为 符号、指数（移码表示）、尾数（首位隐藏）
	 */
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

	/**
	 * 生成十进制浮点数的 IEEE 754 表示，要求调用 floatRepresentation 实现。
	 * 
	 * @param number
	 *            十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为 正数或 0，则无符号位
	 * @param length
	 *            二进制表示的长度，为 32 或 64
	 * @return number 的 IEEE 754 表示，长度为 length。从左向右，依次为符号、指数 （移码表示）、尾数（首位隐藏）
	 */
	public String ieee754(String number, int length) {
		return length == 32 ? this.floatRepresentation(number, 8, 23) : this.floatRepresentation(number, 11, 52);
	}

	/**
	 * 计算二进制补码表示的整数的真值。
	 * 
	 * @param operand
	 *            二进制补码表示的操作数
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 */
	public String integerTrueValue(String operand) {
		int number = 0;
		if (operand.startsWith("1"))
			number -= Math.pow(2, operand.length() - 1);
		for (int i = 1; i < operand.length(); i++) {
			number += Integer.parseInt(operand.substring(i, i + 1)) * (int) Math.pow(2, operand.length() - i - 1);
		}
		return number + "";
	}

	/**
	 * 计算二进制原码表示的浮点数的真值。
	 * 
	 * @param operand
	 *            二进制表示的操作数
	 * @param eLength
	 *            指数的长度，取值大于等于 4
	 * @param sLength
	 *            尾数的长度，取值大于等于 4
	 * @return
	 */
	public String floatTrueValue(String operand, int eLength, int sLength) {
		// String e = operand.substring(1, 1 + eLength);
		// int exp = Integer.parseInt(this.integerTrueValue(e)) - 127;
		// int[] s = new int[sLength];
		// int dvd = 1;
		// if (operand.substring(1 + eLength, 2 + eLength).equals(1)) {
		// dvd = 0;
		// s[0] = 5;
		// }
		// for (int i = 1; i < sLength; i++) {
		// if (operand.substring(1 + eLength + i, 2 + eLength + i).equals("1"))
		// {
		// for (int j = i - 1; j >= 0; j--) {
		// // s[j] +=
		// }
		// }
		// }
		return null;
	}

	/**
	 * 按位取反操作，需要采用非门模拟。
	 * 
	 * @param operand
	 *            二进制表示的操作数
	 * @return operand 按位取反的结果
	 */
	public String negation(String operand) {
		String result = "";
		for (int i = 0; i < operand.length(); i++)
			result += this.Not(operand.substring(i, i + 1));
		return result;
	}

	/**
	 * 左移操作。
	 * 
	 * @param operand
	 *            二进制表示的操作数
	 * @param n
	 *            左移的位数
	 * @return operand左移 n 位的结果
	 */
	public String leftShift(String operand, int n) {
		StringBuilder sb = new StringBuilder(operand);
		sb.delete(0, n);
		for (int i = 0; i < n; i++)
			sb.append("0");
		return sb.toString();
	}

	/**
	 * 逻辑右移操作。
	 * 
	 * @param operand
	 *            二进制表示的操作数
	 * @param n
	 *            右移的位数
	 * @return operand逻辑右移 n 位的结果
	 */
	public String logRightShift(String operand, int n) {
		for (int i = 0; i < n; i++)
			operand = "0" + operand;
		return operand.substring(operand.length() - n);
	}

	/**
	 * 逻辑右移操作。
	 * 
	 * @param operand
	 *            二进制表示的操作数
	 * @param n
	 *            右移的位数
	 * @return operand 逻辑右移 n 位的结果
	 */
	public String ariRightShift(String operand, int n) {
		String s = operand.substring(0, 1);
		for (int i = 0; i < n; i++)
			operand = s + operand;
		return operand.substring(operand.length() - n);
	}

	/**
	 * 全加器，对两位以及进位进行加法运算，需要采用与门、或门、异或门等模 拟。
	 * 
	 * @param x
	 *            被加数的某一位，取 0 或 1
	 * @param y
	 *            加数的某一位，取 0 或 1
	 * @param c
	 *            低位对当前位的进位，取 0 或 1
	 * @return 相加的结果，用长度为 2 的字符串表示，第 1 位表示进位，第 2 位表示和
	 */
	public String fullAdder(char x, char y, char c) {
		return null;
	}

	/**
	 * 4 位先行进位加法器，要求采用 fullAdder 来实现。
	 * 
	 * @param operand1
	 *            4 位二进制表示的被加数
	 * @param operand2
	 *            4 位二进制表示的加数
	 * @param c
	 *            低位对当前位的进位，取 0 或 1
	 * @return 长度为 5 的字符串表示的计算结果，其中第 1 位是最高位进位，后 4 位 是二进制表示的相加结果，其中进位不可以由循环获得
	 */
	public String claAdder(String operand1, String operand2, char c) {
		return null;
	}

	/**
	 * 加一器，实现操作数加 1 的运算。需要采用与门、或门、异或门等模拟，不 可以直接调用
	 * fullAdder、claAdder、adder、integerAddition 方法。
	 * 
	 * @param operand
	 *            二进制补码表示的操作数
	 * @return operand 加 1 的结果，长度为 operand 的长度加 1，其中第 1 位指示是否 溢出（溢出为 1，否则为
	 *         0），其余位为相加结果
	 */
	public String oneAdder(String operand) {
		return null;
	}

	/**
	 * 加法器，要求调用 claAdder 方法实现。
	 * 
	 * @param operand1
	 *            二进制补码表示的被加数
	 * @param operand2
	 *            二进制补码表示的加数
	 * @param c
	 *            最低位进位
	 * @param length
	 *            存放操作数的寄存器的长度，为 4 的倍数。length 不小于操作数 的长度，当某个操作数的长度小于 length
	 *            时，需要在高位补符号位
	 * @return 长度为 length+1 的字符串表示的计算结果，其中第 1 位指示是否溢出 （溢出为 1，否则为 0），后 length
	 *         位是相加结果
	 */
	public String adder(String operand1, String operand2, char c, int length) {
		return null;
	}

	/**
	 * 整数加法，要求调用 adder 方法实现。
	 * 
	 * @param operand1
	 *            二进制补码表示的被加数
	 * @param operand2
	 *            二进制补码表示的加数
	 * @param length
	 *            存放操作数的寄存器的长度，为 4 的倍数。length 不小于操作数 的长度，当某个操作数的长度小于 length
	 *            时，需要在高位补符号位
	 * @return 长度为 length+1 的字符串表示的计算结果，其中第 1 位指示是否溢出 （溢出为 1，否则为 0），后 length
	 *         位是相加结果
	 */
	public String integerAddition(String operand1, String operand2, int length) {
		return null;
	}

	/**
	 * 整数减法，要求调用 adder 方法实现。
	 * 
	 * @param operand1
	 *            二进制补码表示的被减数
	 * @param operand2
	 *            二进制补码表示的减数
	 * @param length
	 *            存放操作数的寄存器的长度，为 4 的倍数。length 不小于操作数 的长度，当某个操作数的长度小于 length
	 *            时，需要在高位补符号位
	 * @return 长度为 length+1 的字符串表示的计算结果，其中第 1 位指示是否溢出 （溢出为 1，否则为 0），后 length
	 *         位是相减结果
	 */
	public String integerSubtraction(String operand1, String operand2, int length) {
		return null;
	}

	/**
	 * 整数乘法，使用 Booth 算法实现，可调用 adder 等方法。
	 * 
	 * @param operand1
	 *            二进制补码表示的被乘数
	 * @param operand2
	 *            二进制补码表示的乘数
	 * @param length
	 *            存放操作数的寄存器的长度，为 4 的倍数。length 不小于操作数 的长度，当某个操作数的长度小于 length
	 *            时，需要在高位补符号位
	 * @return 长度为 length+1 的字符串表示的计算结果，其中第 1 位指示是否溢出 （溢出为 1，否则为 0），后 length
	 *         位是相乘结果
	 */
	public String integerMultiplication(String operand1, String operand2, int length) {
		return null;
	}

	/**
	 * 整数的不恢复余数除法，可调用 adder 等方法实现。
	 * 
	 * @param operand1
	 *            二进制补码表示的被除数
	 * @param operand2
	 *            二进制补码表示的除数
	 * @param length
	 *            存放操作数的寄存器的长度，为 4 的倍数。length 不小于操作数 的长度，当某个操作数的长度小于 length
	 *            时，需要在高位补符号位
	 * @return 长度为 2*length+1 的字符串表示的相除结果，其中第 1 位指示是否溢出 （溢出为 1，否则为 0）， 其后 length
	 *         位为商，最后 length 位为余数
	 */
	public String integerDivision(String operand1, String operand2, int length) {
		return null;
	}

	/**
	 * 带符号整数加法，可以调用 adder 等方法，但不能直接将操作数转换为补码 后使用
	 * integerAddition、integerSubtraction 来实现。
	 * 
	 * @param operand1
	 *            二进制原码表示的被加数，其中第 1 位为符号位
	 * @param operand2
	 *            二进制原码表示的加数，其中第 1 位为符号位
	 * @param length
	 *            存放操作数的寄存器的长度，为 4 的倍数。length 不小于操作数 的长度（不包含符号），当某个操作数的长度小于
	 *            length 时，需要将其长 度扩展到 length
	 * @return 长度为 length+2 的字符串表示的计算结果，其中第 1 位指示是否溢出 （溢出为 1，否则为 0）， 第 2 位为符号位，后
	 *         length 位是相加结果
	 */
	public String signedAddition(String operand1, String operand2, int length) {
		return null;
	}

	/**
	 * 浮点数加法，要求调用 signedAddition 等方法实现。
	 * 
	 * @param operand1
	 *            二进制表示的被加数
	 * @param operand2
	 *            二进制表示的加数
	 * @param eLength
	 *            指数的长度，取值大于等于 4
	 * @param sLength
	 *            尾数的长度，取值大于等于 4
	 * @param gLength
	 *            保护位的长度
	 * @return 长度为 2+eLength+sLength 的字符串表示的相加结果，其中第 1 位指示 是否指数上溢（溢出为 1，否则为
	 *         0），其余位从左到右依次为符号、指 数（移码表示）、尾数（首位隐藏）。舍入策略为向 0 舍入
	 */
	public String floatAddition(String operand1, String operand2, int eLength, int sLength, int gLength) {
		return null;
	}

	/**
	 * 浮点数减法，要求调用 floatAddition 方法实现。
	 * 
	 * @param operand1
	 *            二进制表示的被减数
	 * @param operand2
	 *            二进制表示的减数
	 * @param eLength
	 *            指数的长度，取值大于等于 4
	 * @param sLength
	 *            尾数的长度，取值大于等于 4
	 * @param gLength
	 *            保护位的长度
	 * @return 长度为 2+eLength+sLength 的字符串表示的相减结果，其中第 1 位指示 是否指数上溢（溢出为 1，否则为
	 *         0），其余位从左到右依次为符号、指 数（移码表示）、尾数（首位隐藏）。舍入策略为向 0 舍入
	 */
	public String floatSubtraction(String operand1, String operand2, int eLength, int sLength, int gLength) {
		return null;
	}

	/**
	 * 浮点数乘法，可调用 integerMultiplication 等方法实现。
	 * 
	 * @param operand1
	 *            二进制表示的被乘数
	 * @param operand2
	 *            二进制表示的乘数
	 * @param eLength
	 *            指数的长度，取值大于等于 4
	 * @param sLength
	 *            尾数的长度，取值大于等于 4
	 * @return 长度为 2+eLength+sLength 的字符串表示的相乘结果，其中第 1 位指示 是否指数上溢（溢出为 1，否则为
	 *         0），其余位从左到右依次为符号、指 数（移码表示）、尾数（首位隐藏）。舍入策略为向 0 舍入
	 */
	public String floatMultiplication(String operand1, String operand2, int eLength, int sLength) {
		return null;
	}

	/**
	 * 浮点数除法，可调用 integerDivision 等方法实现。
	 * 
	 * @param operand1
	 *            二进制表示的被除数
	 * @param operand2
	 *            二进制表示的除数
	 * @param eLength
	 *            指数的长度，取值大于等于 4
	 * @param sLength
	 *            尾数的长度，取值大于等于 4
	 * @return 长度为 2+eLength+sLength 的字符串表示的相除结果，其中第 1 位指示 是否指数上溢（溢出为 1，否则为
	 *         0），其余位从左到右依次为符号、指 数（移码表示）、尾数（首位隐藏）。舍入策略为向 0 舍入
	 */
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
