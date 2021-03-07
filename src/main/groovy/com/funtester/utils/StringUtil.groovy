package com.funtester.utils

import com.funtester.frame.SourceCode

import java.util.stream.Collectors

/**
 * 处理各种字符串的工具类
 */
class StringUtil extends SourceCode {

    private static final char[] chars = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', (char) 65, (char) 66, (char) 67, (char) 68, (char) 69, (char) 70, (char) 71, (char) 72, (char) 73, (char) 74, (char) 75, (char) 76, (char) 77, (char) 78, (char) 79, (char) 80, (char) 81, (char) 82, (char) 83, (char) 84, (char) 85, (char) 86, (char) 87, (char) 88, (char) 89, (char) 90, (char) 97, (char) 98, (char) 99, (char) 100, (char) 101, (char) 102, (char) 103, (char) 104, (char) 105, (char) 106, (char) 107, (char) 108, (char) 109, (char) 110, (char) 111, (char) 112, (char) 113, (char) 114, (char) 115, (char) 116, (char) 117, (char) 118, (char) 119, (char) 120, (char) 121, (char) 122]

    /**
     * emoji表情
     */
    private static final String[] EMOJIS = ["☢", "💸", "💷", "💶", "💵", "💼", "💻", "💰", "💮", "💴", "💳", "💨", "💧", "💦", "💪", "💡", "📘", "📗", "📖", "📕", "📜", "📚", "📙", "📐", "📏", "📎", "📍", "📔", "📓", "📑", "📈", "📇", "📆", "📅", "📌", "📋", "📊", "📉", "📀", "💿", "💾", "💽", "📄", "📃", "📂", "📁", "📷", "📼", "📻", "📺", "📹", "📰", "📯", "📮", "📭", "📲", "📱", "📨", "📧", "📦", "📥", "📬", "📫", "📪", "📩", "📠", "📟", "📞", "📝", "📤", "📡", "🔗", "🔖", "🔐", "🔏", "🔎", "🔍", "🔓", "🔒", "🔑", "🔌", "🔋", "🔮", "🔭", "🔨", "🔧", "🔦", "🔥", "🔬", "🔫", "🔪", "🔩", "🦋", "😘", "😗", "😖", "😕", "😜", "😛", "😚", "😙", "😐", "😏", "😎", "😍", "😔", "😓", "😒", "😑", "😇", "😆", "😅", "😌", "😋", "😊", "😉", "😀", "😄", "😃", "😂", "😁", "😸", "😷", "😶", "😵", "😼", "😻", "😺", "😹", "😰", "😯", "😮", "😭", "😴", "😳", "😲", "😱", "😨", "😧", "😦", "😥", "😬", "😫", "😪", "😠", "😟", "😞", "😝", "😤", "😣", "😢", "😡", "🙏", "🙎", "🙍", "🙈", "🙇", "🙆", "🙅", "🙌", "🙋", "🙊", "🙉", "🙀", "😿", "😾", "😽", "🚘", "🚗", "🚖", "🚕", "🚜", "🚛", "🚚", "✏✒", "🚐", "🚏", "🚎", "🚍", "🚔", "🚓", "🚒", "🚑", "🚈", "🚌", "🚋", "🚊", "🚉", "☀", "☁", "🚶", "🚵", "🚴", "🚲", "☎", "🚨", "🚥", "🚬", "☔", "☕", "🚪", "🚩", "🚞", "🚝", "🚣", "🛀", "🚿", "🚽", "🛁", "🌗", "🌖", "🌕", "🌔", "🌛", "🌚", "🌙", "🌘", "♈", "🌏", "♉", "🌎", "♊", "🌍", "♋", "♌", "🌓", "♍", "🌒", "♎", "🌑", "♏", "🌐", "♐", "♑", "♒", "♓", "🌊", "🌂", "🌷", "🌵", "🌴", "🌻", "🌺", "🌹", "🌸", "🌳", "🌲", "🌱", "🌰", "🌟", "🌞", "🌝", "🌜", "🌠", "🍗", "🍖", "🍕", "🍔", "🍛", "🍚", "🍙", "🍘", "🍏", "🍎", "🍍", "🍌", "🍓", "🍒", "🍑", "🍐", "🍇", "🍆", "🍅", "🍄", "🍋", "🍊", "🍉", "🍈", "🌿", "🌾", "🌽", "🌼", "🍃", "🍂", "🍁", "🍀", "🍷", "🍶", "⚡", "🍵", "🍴", "🍻", "🍺", "🍹", "🍸", "🍯", "🍮", "🍭", "🍬", "🍳", "🍲", "🍱", "🍰", "🍧", "🍦", "🍥", "🍤", "🍫", "🍪", "🍩", "🍨", "🍟", "🍞", "🍝", "🍜", "🍣", "🍢", "⚽", "🍡", "⚾", "🍠", "⛅", "🎏", "🎎", "🎌", "🎓", "🎒", "⛎", "🎐", "🎅", "🎊", "🎉", "🎈", "🍼", "🎂", "🎁", "🎀", "🎷", "🎻", "🎺", "🎸", "🎯", "🎮", "🎭", "🎬", "🎳", "🎲", "🎱", "🎰", "🎥", "🎫", "🎪", "🎩", "🎨", "🎣", "✂", "✉", "✊", "✋", "✌", "🏇", "🏆", "🏄", "🏊", "🏉", "🏈", "🎿", "🎾", "🎽", "⌚", "⌛", "🏃", "🏂", "🏀", "🏮", "❄", "⭐", "🐘", "🐗", "🐖", "🐕", "🐜", "🐛", "🐚", "🐙", "🐐", "🐏", "🐎", "🐍", "🐔", "🐓", "🐒", "🐑", "🐈", "🐇", "🐆", "🐅", "🐌", "🐋", "🐊", "🐉", "🐀", "🐄", "🐃", "🐂", "🐁", "🐸", "🐷", "🐶", "🐵", "🐼", "🐻", "🐺", "🐹", "🐰", "🐯", "🐮", "🐭", "🐴", "🐳", "🐲", "🐱", "🐨", "🐧", "🐦", "🐥", "🐬", "🐫", "🐪", "🐩", "🐠", "🐟", "🐞", "🐝", "🐤", "🐣", "🐢", "🐡", "👘", "👗", "👖", "👕", "👜", "👛", "👚", "👙", "👐", "👏", "👎", "👍", "👔", "👓", "👒", "👑", "👈", "👇", "👆", "👅", "👌", "👋", "👊", "👉", "👀", "🐾", "🐽", "👄", "👃", "👂", "👸", "👷", "👶", "👵", "👼", "👰", "👯", "👮", "👭", "👴", "👳", "👲", "👱", "👨", "👧", "👦", "👥", "👬", "👫", "👪", "👩", "👠", "👟", "👞", "👝", "👤", "👣", "👢", "👡", "💐", "💏", "💎", "💍", "💑", "⏰", "💈", "💇", "💆", "💅", "⏳", "💌", "💋", "💊", "💉", "💄", "💃", "💂", "💁"]

    /**
     * 序号
     */
    private static final String[] SERIAL = ["⓪", "①", "②", "③", "④", "⑤", "⑥", "⑦", "⑧", "⑨", "⑩", "⑪", "⑫", "⑬", "⑭", "⑮", "⑯", "⑰", "⑱", "⑲", "⑳"]

    /**
     * 小写汉字数字
     */
    private static final String[] chineses = ["〇", "一", "二", "三", "四", "五", "六", "七", "八", "九"]

    /**
     * 大写汉字数字
     */
    private static final String[] capeChineses = ["零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"]

    /**
     * 获取随机字符串
     * @param i
     * @return
     */
    static String getString(int i) {
        def re = new StringBuffer()
        if (i < 1) return re
        for (int j in 1..i) {
            re << getChar()
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
            re << getWord()
        }
        re.toString()
    }

    /**
     * 获取所有小写字母
     * @return
     */
    static String getLowWords() {
        "abcdefghijklmnopqrstuvwxyz"
    }

    /**
     * 获取所有大写字母
     * @return
     */
    static String getUpWords() {
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    }

    /**
     * 获取所有的数字
     * @return
     */
    static String getNumbers() {
        "0123456789"
    }

    /**
     * 将int类型转化为汉子数字，对于3位数的数字自动补零
     * @param i
     * @return
     */
    static String getChinese(int i) {
        if (i <= 0) return "〇〇〇"
        String num = (i + EMPTY).collect { x -> chineses[changeStringToInt(x)] }.join()
        num.length() > 2 ? num : getManyString(chineses[0] + EMPTY, 3 - num.length()) + num
    }

    /**
     * 将int类型转化汉字大写数字表示，对于3位数的数字自动补零
     * @param i
     * @return
     */
    static String getCapeChinese(int i) {
        if (i <= 0) return "零零零"
        def num = (i + EMPTY).collect { x -> capeChineses[changeStringToInt(x)] }.join()
        num.length() > 2 ? num : getManyString(capeChineses[0] + EMPTY, 3 - num.length()) + num
    }

    /**
     * 随机获取emoji表情数
     *
     * @param size
     * @return
     */
    static String getEmojis(int size) {
        range(size).map { x -> getEmojis() }.collect(Collectors.toString());
    }

    /**
     * 获取序号符号
     *
     * @param i
     * @return
     */
    static String getSerialEmoji(int i) {
        (i < 0 || i > 20) ? EMOJIS[0] : SERIAL[i];
    }

    /**
     * 随机获取emoji表情
     *
     * @return
     */
    static String getEmojis() {
        EMOJIS[getRandomInt(EMOJIS.length - 1)];
    }


    /**
     * 返回一个居中的字符串
     * @param str
     * @param size
     * @return
     */
    static String center(String str, int size) {
        str.center(size)
    }

    /**
     * 返回一个居左的文本
     * @param str
     * @param size
     * @return
     */
    static String left(String str, int size) {
        str.padLeft(size)
    }

    /**
     * 返回一个居右的文本
     * @param str
     * @param size
     * @return
     */
    static String right(String str, int size) {
        str.padRight(size)
    }


//这个是添加新的的emoji表情的方法
//    static void main(String[] args) {
//        String aa = "";
//        String aaa = EMPTY;
//        for (int i = 0; i < aa.length(); i += 2) {
//            String abc = aa.substring(i, i + 2);
//            aaa = aaa + "\"" + aa.substring(i, i + 2) + "\",";
//        }
//        output(aaa);
//        aaa = EMPTY;
//        int length = EMOJIS.length;
//        HashSet<String> strings = new HashSet<>(Arrays.asList(EMOJIS));
//        for (String string : strings) {
//            aaa = aaa + "\"" + string + "\",";
//        }
//        output(aaa);
//        output(length, strings.size());
//    }
}