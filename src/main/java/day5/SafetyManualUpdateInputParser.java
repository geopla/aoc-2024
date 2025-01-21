package day5;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SafetyManualUpdateInputParser {

    static Pattern pageOrderRulePattern = Pattern.compile("^\\d+\\|\\d+$");
    static Pattern updatePattern = Pattern.compile("^\\d+(,\\d+)*$");

    static boolean isPageOrderRule(String input) {
        Matcher matcher = pageOrderRulePattern.matcher(input.trim());
        return matcher.matches();
    }

    static boolean isUpdate(String input) {
        Matcher matcher = updatePattern.matcher(input.trim());
        return matcher.matches();
    }
}
