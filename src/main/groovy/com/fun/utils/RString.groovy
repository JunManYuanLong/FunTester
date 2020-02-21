package com.fun.utils


import com.fun.frame.SourceCode

class RString extends SourceCode {

    static char[] chars = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', (char) 65, (char) 66, (char) 67, (char) 68, (char) 69, (char) 70, (char) 71, (char) 72, (char) 73, (char) 74, (char) 75, (char) 76, (char) 77, (char) 78, (char) 79, (char) 80, (char) 81, (char) 82, (char) 83, (char) 84, (char) 85, (char) 86, (char) 87, (char) 88, (char) 89, (char) 90, (char) 97, (char) 98, (char) 99, (char) 100, (char) 101, (char) 102, (char) 103, (char) 104, (char) 105, (char) 106, (char) 107, (char) 108, (char) 109, (char) 110, (char) 111, (char) 112, (char) 113, (char) 114, (char) 115, (char) 116, (char) 117, (char) 118, (char) 119, (char) 120, (char) 121, (char) 122]

    static String[] chineses = ["〇", "一", "二", "三", "四", "五", "六", "七", "八", "九"]

    static String[] capeChineses = ["零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"]

/**
 * 获取随机字符串
 * @param i
 * @return
 */
    static String getString(int i) {
        def re = new StringBuffer()
        if (i < 1) return re
        for (int j in 1..i) {
            re.append(getChar())
        }
        re.toString()
    }

/**
 * 获取随机字符
 * @return
 */
    static char getChar() {
        chars[getRandomInt(62) - 1]
    }

/**
 * 获取随机字母，区分大小写
 *
 * @return
 */
    static char getWord() {
        chars[getRandomInt(52) + 9];
    }

/**
 * 获取随机字符串，没有数字
 * @param i
 * @return
 */
    static String getStringWithoutNum(int i) {
        def re = new StringBuffer()
        if (i < 1) return re
        for (int j in 1..i) {
            re.append(getWord())
        }
        re.toString()
    }

/**
 * 获取所有小写字母
 * @return
 */
    static String getAllLowWord() {
        "abcdefghijklmnopqrstuvwxyz"
    }
/**
 * 获取所有大写字母
 * @return
 */
    static String getAllUpWord() {
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    }

/**
 * 获取所有的数字
 * @return
 */
    static String getAllNumber() {
        "0123456789"
    }

/**
 * 将int类型转化为汉子数字，对于3位数的数字自动补零
 * @param i
 * @return
 */
    static String getChinese(int i) {
        if (i <= 0) return "〇〇〇"
        String num = (i + EMPTY).collect {x -> chineses[changeStringToInt(x)]}.join()
        num.length() > 2 ? num : getManyString(chineses[0] + EMPTY, 3 - num.length()) + num
    }

/**
 * 将int类型转化汉字大写数字表示，对于3位数的数字自动补零
 * @param i
 * @return
 */
    static String getCapeChinese(int i) {
        if (i <= 0) return "零零零"
        def num = (i + EMPTY).collect {x -> capeChineses[changeStringToInt(x)]}.join()
        num.length() > 2 ? num : getManyString(capeChineses[0] + EMPTY, 3 - num.length()) + num

    }
}