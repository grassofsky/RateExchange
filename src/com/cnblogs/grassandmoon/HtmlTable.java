package com.cnblogs.grassandmoon;

import java.util.regex.*;
import java.io.*;

/**
   从网页中爬取特定表单的各个元素信息
*/
public class HtmlTable {
    /** 表单内不同元素之间的分隔符 */
    final static String ELEMENT_SEPARATOR = "\001";

    /** 表单内不同行之间的分隔符 */
    final static String ROW_SEPARATOR = "\002";

    /** 匹配表单的正则表达式 */
    final static String REGEX_TABLE = "<table.*?>\\s*?((<tr.*?>.*?</tr>)+?)\\s*?</table>";

    /** 匹配表单中行的正则表达式 */
    final static String REGEX_ROW = "<tr.*?>\\s*?(.*?)\\s*?</tr>";

    /** 匹配行中每一项元素的正则表达式 */
    final static String REGEX_ELE = "(?:<th.*?>|<td.*?>)(?:\\s*<.*?>)*(?:&nbsp;)?(.*?)(?:&nbsp;)?(?:\\s*<.*?>)*?\\s*(?:</th>|</td>)";

    /**
       从html特定行中匹配获取表单内各个元素的信息
       @param nStartLine 表单区域开始的行数
       @param nEndLine 表单区域结束的行数
       @param br html文档的{@link java.io.BufferedReader}
       @throws IOException 网页内容读取中出现的异常
       @return 各个元素的字符串, 其中不同行的元素用行分隔符隔开, {@link #ROW_SEPARATOR}
               同一行的不同元素用元素分隔符隔开 {@link #ELEMENT_SEPARATOR}
    */
    public static String extract(int nStartLine, int nEndLine, BufferedReader br)
    throws IOException {
        String line;
        String target = "";
        String elements = "";
        int i = 0;

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