package com.cnblogs.grassandmoon;

import java.util.*;
import java.io.*;
import java.net.*;
import java.text.NumberFormat;

/*
 * 主要功能:
 *   1. 实现汇率的实时更新
 *   2. 汇率换算表的建立
 *   3. 汇率的换算
 */
public class Rate {
    // 从网站:http://www.usd-cny.com/中获取最新的汇率信息
    final static String webSite = "http://www.usd-cny.com/";

    // 利用hashtable对不同货币之间的利率进行存储
    //   key: $from+$to, value: $rate
    private static Hashtable rateTable = new Hashtable();

    private static String toCurrency = null;

    // 从网上自动更新汇率信息
    public static void update() throws Exception {
        URL hp = new URL(webSite);
        URLConnection hpCon = hp.openConnection();

        InputStream input = (InputStream)hpCon.getInputStream();
        BufferedReader br = new BufferedReader(new
                                               InputStreamReader(input, "gb2312"));
        String result = HtmlTable.extract(78, 303, br);
        String strRows[] = result.split(HtmlTable.ROW_SEPARATOR);
        String strCur, strRate;
        NumberFormat numFormat = NumberFormat.getNumberInstance();
        Number numb = null;
        for (int i = 1; i < strRows.length; i++) {
            String strEle[] = strRows[i].split(HtmlTable.ELEMENT_SEPARATOR);
            if (strEle[3].equals("")) break;
            strCur = strEle[0].split(" ")[1];
            strRate = strEle[3];
            if (i == 1)
                toCurrency = strEle[0] + "; ";
            else
                toCurrency = toCurrency + strEle[0] + "; ";
            numb = numFormat.parse(strRate);
            strRate = numb.toString();
            System.out.println(strCur + strRate);
            rateTable.put("CNY" + strCur, Double.valueOf(strRate)/100.0);
            rateTable.put(strCur+"CNY", 1.0/Double.valueOf(strRate));
        }
        br.close();
    }

    public static Double getRate(String from, String to) {
        return (Double) rateTable.get(from + to);
    }

    // 将一定量的货币$m, 转变成单位为$to的货币量
    // return: 相应的货币值
    public static Money exchangeRate(Money m, String to) {
        if (m.unit.equals(to)) return new Money(m);
        Double rate = getRate(m.unit, to);

        if (rate == null) {
            throw new IllegalArgumentException();
        }

        return new Money(m.amount*rate.doubleValue(), to);
    }

    // 输出支持的货币转换
    public static void printSupported() {
        System.out.println("支持人民币 CNY 和以下货币之间的转换\n" + toCurrency);
    }
}