package com.cnblogs.grassandmoon;

import java.util.regex.*;
import java.io.*;

public class HtmlTable {
    final static String ELEMENT_SEPARATOR = "\001";
    final static String ROW_SEPARATOR = "\002";

    final static String REGEX_TABLE = "<table.*?>\\s*?((<tr.*?>.*?</tr>)+?)\\s*?</table>";
    final static String REGEX_ROW = "<tr.*?>\\s*?(.*?)\\s*?</tr>";
    final static String REGEX_ELE = "(?:<th.*?>|<td.*?>)(?:\\s*<.*?>)*(?:&nbsp;)?(.*?)(?:&nbsp;)?(?:\\s*<.*?>)*?\\s*(?:</th>|</td>)";


    public static String extract(int nStartLine, int nEndLine, BufferedReader br)
    throws IOException {
        String line;
        String target = "";
        String elements = "";
        int i = 0;
        // iStartLine[0] = 78;
        // iEndLine[0] = 303;

        while ((line = br.readLine()) != null) {
            ++i;
            if (i < nStartLine) continue;
            line.trim();
            target = target + line;
            if (i >= nEndLine) break;
        }

        // 正则表达式
        Pattern r = Pattern.compile(REGEX_TABLE, Pattern.CASE_INSENSITIVE);

        // 表单的匹配
        Matcher mTable = r.matcher(target);

        if (mTable.find()) {
            String strRows = mTable.group(1).trim();

            // 表单中每一行得匹配
            Matcher mRow = Pattern.compile(REGEX_ROW, Pattern.CASE_INSENSITIVE).matcher(strRows);
            while (mRow.find()) {
                boolean firstEle = true;
                String strEle = mRow.group(1).trim();
                // System.out.println("\nTh or td: " + strEle);

                // 每一行中每个元素得匹配
                Matcher mEle = Pattern.compile(REGEX_ELE, Pattern.CASE_INSENSITIVE).matcher(strEle);

                if (!elements.equals(""))
                    elements = elements + ROW_SEPARATOR;
                while (mEle.find()) {
                    String result = mEle.group(1).trim();
                    if (firstEle)
                        elements = elements + result;
                    else
                        elements = elements + ELEMENT_SEPARATOR + result;
                    firstEle = false;
                    // System.out.println("\nElement: " + result);
                }
                if (!elements.equals("")) {
                    int len = elements.length();
                    elements = elements.substring(0, len-2);
                }
            }
        }

        return new String(elements);
    }
}