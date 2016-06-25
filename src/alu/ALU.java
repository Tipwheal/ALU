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
		int num = Integer.parseInt(number);
		if (number.startsWith("-"))
			return this.integerRepresentation(((long) (1 << length) + num) + "", length);
		String result = "";
		for (int i = 0; i < length; i++) {
			int last = num % 2;
			num /= 2;
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
			number -= 1 << operand.length() - 1;
		for (int i = 1; i < operand.length(); i++)
			number += Integer.parseInt(operand.substring(i, i + 1)) * (int) Math.pow(2, operand.length() - i - 1);
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
		boolean sign = operand.charAt(0) == '0' ? true : false;
		String exponent = operand.substring(1, 1 + eLength);
		String significant = operand.substring(1 + eLength);
		if (!exponent.contains("0"))
			return exponent.contains("0") ? "NaN" : (sign ? "+" : "-") + "Inf";
		int expDec = Integer.parseInt(this.integerTrueValue(exponent)) - (1 << eLength - 1) - 1;
		significant = (exponent.contains("1") ? "1" : "0") + significant;
		double sig = 0;
		for (int i = 0; i <= sLength; i++)
			sig += Math.pow(2, -i);
		return Math.pow(sig, expDec) + "";
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
			result += this.not(operand.substring(i, i + 1));
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
		return operand.substring(0, operand.length() - n);
	}

	/**
	 * 算术右移操作。
	 * 
	 * @param operand
	 *            二进制表示的操作数
	 * @param n
	 *            右移的位数
	 * @return operand 算术右移 n 位的结果
	 */
	public String ariRightShift(String operand, int n) {
		String s = operand.substring(0, 1);
		for (int i = 0; i < n; i++)
			operand = s + operand;
		return operand.substring(0, operand.length() - n);
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
		return this.or(this.and(x + "", y + ""), this.and(y + "", c + ""), this.and(c + "", x + ""))
				+ this.xor(this.xor(x + "", y + ""), c + "");
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
		String result = "";
		for (int i = 3; i >= 0; i--) {
			String add = this.fullAdder(operand1.charAt(i), operand2.charAt(i), c);
			c = add.charAt(0);
			result = add.charAt(1) + result;
		}
		return c + result;
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
		String add = "1";
		String result = "";
		for (int i = operand.length() - 1; i >= 0; i--) {
			result = this.xor(operand.substring(i, i + 1), add) + result;
			add = this.and(operand.substring(i, i + 1), add);
		}
		result = add + result;
		return result;
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
		String add1 = operand1.substring(0, 1);
		String add2 = operand2.substring(0, 1);
		String result = "";
		String over = "0";
		for (int i = operand1.length(); i < length; i++)
			operand1 = add1 + operand1;
		for (int i = operand2.length(); i < length; i++)
			operand2 = add2 + operand2;
		for (int i = length / 4 - 1; i >= 0; i--) {
			String s = this.claAdder(operand1.substring(i * 4, (i + 1) * 4), operand2.substring(i * 4, (i + 1) * 4), c);
			result = s.substring(1) + result;
			c = s.charAt(0);
		}
		if (operand1.startsWith(operand2.substring(0, 1)) && !operand1.startsWith(result.substring(0, 1)))
			over = "1";
		return over + result;
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
		return this.adder(operand1, operand2, '0', length);
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
		return this.adder(operand1, this.negation(operand2), '1', length);
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
		boolean zero = !(operand1.contains("1") && operand2.contains("1"));
		char sign1 = operand1.charAt(0);
		char sign2 = operand2.charAt(0);
		while (operand1.length() < length)
			operand1 = sign1 + operand1;
		while (operand2.length() < length)
			operand2 = sign2 + operand2;
		String product = "";
		while (product.length() < length)
			product += '0';
		char last = '0';
		char first = '0';
		for (int i = 1; i <= length; i++) {
			int sub;
			sub = last - operand2.charAt(length - 1);
			last = operand2.charAt(length - 1);
			String temp = "";
			switch (sub) {
			case 0:
				break;
			default:
				product = adder(product, sub == 1 ? operand1 : negation(operand1), sub == 1 ? '0' : '1', length)
						.substring(1);
				break;
			}
			temp = this.ariRightShift(product + operand2, 1);
			product = temp.substring(0, length);
			operand2 = temp.substring(length);
		}
		String temp = sign1 == sign2 ? "1" : "0";
		if ((product + operand2.charAt(0)).contains(temp))
			first = '1';
		if (zero)
			first = '0';
		return first + operand2;
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
		char sign1 = operand1.charAt(0);
		char sign2 = operand2.charAt(0);
		while (operand1.length() < length)
			operand1 = sign1 + operand1;
		while (operand2.length() < length)
			operand2 = sign2 + operand2;
		if (!operand1.contains("1"))
			return "0";
		if (!operand2.contains("1"))
			return "NaN";
		String remainder = "";
		String quotient = operand1;
		System.out.println(quotient);
		while (remainder.length() < length) {
			remainder = sign1 + remainder;
		}
		for (int i = 0; i < length; i++) {
			if (remainder.charAt(0) == sign2) {
				remainder = this.integerSubtraction(remainder, operand2, length).substring(1);
			} else {
				remainder = this.integerAddition(remainder, operand2, length).substring(1);
			}
			quotient += remainder.charAt(0) == sign2 ? '1' : '0';
			String temp = this.leftShift(remainder + quotient, 1);
			remainder = temp.substring(0, length);
			quotient = temp.substring(length, length * 2);
			System.out.println("remainder\t" + remainder);
			System.out.println("quotient\t" + quotient);
		}
		if (remainder.charAt(0) == sign2) {
			remainder = this.integerSubtraction(remainder, operand2, length).substring(1);
		} else {
			remainder = this.integerAddition(remainder, operand2, length).substring(1);
		}
		if (sign1 != sign2) {
			quotient += remainder.charAt(0) == sign2 ? '1' : '0';
			remainder = this.integerSubtraction(remainder, operand2, length).substring(1);
			quotient = this.adder(this.leftShift(quotient, 1).substring(0, length), "0", '1', length).substring(1);
		}
		System.out.println("remainder\t" + remainder);
		System.out.println("quotient\t" + quotient);
		return "0" + remainder + quotient;
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
		char sign1 = operand1.charAt(0);
		char sign2 = operand2.charAt(0);
		while (operand1.length() < length)
			operand1 = sign1 + operand1;
		while (operand2.length() < length)
			operand2 = sign2 + operand2;
		String sum = "";
		String first = "0";
		if (sign1 == sign2) {
			sum = this.adder("0" + operand1.substring(1), "0" + operand2.substring(1), '0', length + 4);
			first = sum.charAt(4) + "";
			sum = sum.substring(5);
			return first + sign1 + sum;
		} else {
			String larger = "0";
			for (int i = 1; i < length; i++) {
				if (operand1.charAt(i) != operand2.charAt(i)) {
					larger = operand1.charAt(i) == '1' ? "1" : "-1";
					break;
				}
			}
			if (larger.equals("0")) {
				sum = this.integerSubtraction("0" + operand1.substring(1), "0" + operand2.substring(1), 4 + length)
						.substring(5);
				return "00" + sum;
			}
			if (larger.equals("1")) {
				sum = this.integerSubtraction("0" + operand1.substring(1), "0" + operand2.substring(1), 4 + length)
						.substring(5);
				return "0" + sign1 + sum;
			} else {
				sum = this.integerSubtraction("0" + operand2.substring(1), "0" + operand1.substring(1), 4 + length)
						.substring(5);
				return "1" + sign2 + sum;
			}
		}
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
		if (operand1.substring(1).equals(operand2.substring(1)))
			if (operand1.charAt(0) != operand2.charAt(0))
				return "0"+this.floatRepresentation("0", eLength, sLength);
			else
				return "0"+operand1.charAt(0)
						+ this.adder(operand1.substring(1, eLength + 1), "0", '1', eLength).substring(1)
						+ operand1.substring(eLength + 1);
		else
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
		if (operand1.substring(1).equals(operand2.substring(1)))
			if (operand1.charAt(0) == operand2.charAt(0))
				return "0"+this.floatRepresentation("0", eLength, sLength);
			else
				return "0"+operand1.charAt(0)
						+ this.adder(operand1.substring(1, eLength + 1), "0", '1', eLength).substring(1)
						+ operand1.substring(eLength + 1);
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
		if (this.floatTrueValue(operand1, eLength, sLength).equals("0"))
			return this.floatRepresentation("0", eLength, sLength);
		if (this.floatTrueValue(operand2, eLength, sLength).equals("0"))
			return this.floatRepresentation("0", eLength, sLength);
		int e1;
		int e2;
		if (this.integerTrueValue(operand1.substring(1, eLength + 1)).equals("0")) {
			e1 = -((1 << eLength) / 2) - 2;
		}
		if (this.integerTrueValue(operand2.substring(1, eLength + 1)).equals("0")) {
			e2 = -((1 << eLength) / 2) - 2;
		}
		char overFlow = '0';
		e1 = Integer.parseInt("0" + integerTrueValue(operand1.substring(1, eLength + 1))) - (1 << (eLength - 1)) + 1;
		e2 = Integer.parseInt("0" + integerTrueValue(operand2.substring(1, eLength + 1))) - (1 << (eLength - 1)) + 1;
		int e = e1 + e2;
		double s1 = Double.parseDouble(this.floatTrueValue("00011" + operand1.substring(eLength + 1), 4, sLength));
		double s2 = Double.parseDouble(this.floatTrueValue("00011" + operand2.substring(eLength + 1), 4, sLength));
		String s = this.floatRepresentation((float) s1 * s2 + "", 4, sLength);
		e += Integer.parseInt("0" + s.substring(1, 5));
		if (e > (1 << eLength) - 1)
			overFlow = '1';
		if (operand1.startsWith(operand2.charAt(0) + "")) {
			return overFlow + "0" + this.integerRepresentation(e + "", eLength) + s.substring(5);
		} else {
			return overFlow + "1" + this.integerRepresentation(e + "", eLength) + s.substring(5);
		}
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
		if (this.floatTrueValue(operand1, eLength, sLength).equals("0"))
			return this.floatRepresentation("0", eLength, sLength);
		if (this.floatTrueValue(operand2, eLength, sLength).equals("0"))
			return "NaN";
		return null;
	}

	/**
	 * 非门。
	 * 
	 * @param input
	 *            所有输入
	 * @return 有1则0，否则1
	 */
	public String not(String... input) {
		for (String s : input)
			if (s.equals("1"))
				return "0";
		return "1";

	}

	/**
	 * 或门。
	 * 
	 * @param input
	 *            所有输入
	 * @return 有1则1，否则0
	 */
	public String or(String... input) {
		for (String s : input)
			if (s.equals("1"))
				return "1";
		return "0";
	}

	/**
	 * 与门。
	 * 
	 * @param a
	 *            一个输入
	 * @param b
	 *            另一个输入
	 * @return 都为一则1，否则0
	 */
	public String and(String a, String b) {
		return a.equals(b) && a.equals("1") ? "1" : "0";
	}

	/**
	 * 异或门。
	 * 
	 * @param a
	 *            一个输入
	 * @param b
	 *            另一个输入
	 * @return 相同为0，相异为1
	 */
	public String xor(String a, String b) {
		return a.equals(b) ? "0" : "1";
	}
}
