package org.sbrf.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final String SQL_PARAMETER_SEARCH_PATTERN = "SET (@.*?) =";

    public static List<String> parseSQLToFindParameters(String sqlString) {
        Matcher matcher = Pattern.compile(SQL_PARAMETER_SEARCH_PATTERN).matcher(sqlString);

        List<String> result = new ArrayList<>();
        while (matcher.find()) {
            result.add(matcher.group(1));
        }

        return result;
    }
}
