import java.util.Scanner;

public class LexicalAnalyzer {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Statement For Lexical");
        String inp_stmt = scanner.nextLine();

        int[] pos = {0};
        while (pos[0] < inp_stmt.length()) {
            System.out.print(analyze(inp_stmt, pos));
        }
    }

    private static boolean isDigit(char ch) {
        return (ch >= '0' && ch <= '9');
    }

    private static boolean isLetter(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
    }

    private static boolean isRelopChar(char ch) {
        return (ch == '>' || ch == '<' || ch == '=');
    }

    private static boolean isWhitespace(char ch) {
        return (ch == ' ' || ch == '\t' || ch == '\n');
    }

    private static String stripWhitespaces(String str, int[] p) {
        while (p[0] < str.length() && isWhitespace(str.charAt(p[0])))
            ++p[0];

        return "";
    }

    private static String matchKeywordOrId(String str, int[] p) {
        String[] keywords = {"if", "then", "else"};
        for (String word : keywords) {
            if (p[0] + word.length() <= str.length()) {
                if (str.substring(p[0], p[0] + word.length()).equals(word)
                        && (p[0] + word.length() == str.length() || isWhitespace(str.charAt(p[0] + word.length())))) {
                    String ans = "( ";
                    ans += word;
                    ans += " )\n";
                    p[0] += word.length();

                    return ans;
                }
            }
        }

        int init = p[0]++;
        while (p[0] < str.length() && (isDigit(str.charAt(p[0])) || isLetter(str.charAt(p[0]))))
            ++p[0];

        String res = "( id, ";
        res += str.substring(init, Math.min(p[0], str.length()));
        res += " )\n";

        return res;
    }

    private static String matchNumber(String str, int[] p) {
        int init = p[0];
        while (p[0] < str.length() && isDigit(str.charAt(p[0])))
            ++p[0];

        if (p[0] < str.length()) {
            boolean flag = false;
            if (str.charAt(p[0]) == '.') {
                ++p[0];
                if (p[0] >= str.length() || !isDigit(str.charAt(p[0])))
                    flag = false;
                else
                    while (p[0] < str.length() && isDigit(str.charAt(p[0])))
                        ++p[0];
            }

            if (!flag && str.charAt(p[0]) == 'E') {
                ++p[0];
                if (p[0] < str.length() && (str.charAt(p[0]) == '+' || str.charAt(p[0]) == '-'))
                    ++p[0];

                while (p[0] < str.length() && isDigit(str.charAt(p[0])))
                    ++p[0];
            }
        }

        String res = "( number, ";
        res += str.substring(init, Math.min(p[0], str.length()));
        res += " )\n";

        return res;
    }

    private static String matchRelop(String str, int[] p) {
        String res = "( relop, ";
        if (str.charAt(p[0]) == '<') {
            ++p[0];
            if (p[0] < str.length() && str.charAt(p[0]) == '=') {
                ++p[0];
                res += "<= )\n";
            } else if (p[0] < str.length() && str.charAt(p[0]) == '>') {
                ++p[0];
                res += "<> )\n";
            } else
                res += "< )\n";
        } else if (p[0] < str.length() && str.charAt(p[0]) == '=') {
            ++p[0];
            res += "= )\n";
        } else if (p[0] < str.length() && str.charAt(p[0]) == '>') {
            ++p[0];
            if (p[0] < str.length() && str.charAt(p[0]) == '=') {
                ++p[0];
                res += ">= )\n";
            } else
                res += "> )\n";
        }

        return res;
    }

    private static String matchIf(String str, int[] p) {
        String res = "( if )\n";
        p[0] += 2; // Skip "if"
        return res;
    }

    private static String matchElse(String str, int[] p) {
        String res = "( else )\n";
        p[0] += 4; // Skip "else"
        return res;
    }

    private static String analyze(String str, int[] p) {
        if (p[0] >= str.length())
            return "";

        char first = str.charAt(p[0]);
        if (isWhitespace(first)) {
            return stripWhitespaces(str, p);
        } else if (isLetter(first)) {
            return matchKeywordOrId(str, p);
        } else if (isDigit(first)) {
            return matchNumber(str, p);
        } else if (isRelopChar(first)) {
            return matchRelop(str, p);
        } else if (first == 'i' && p[0] + 1 < str.length() && str.charAt(p[0] + 1) == 'f') {
            return matchIf(str, p);
        } else if (first == 'e' && p[0] + 3 < str.length() && str.substring(p[0], p[0] + 4).equals("else")) {
            return matchElse(str, p);
        } else {
            String ans = "Operation ";
            ans += str.charAt(p[0]++);
            ans += "\n";
            return ans;
        }
    }
}
