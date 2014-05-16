package com.cnblogs.grassandmoon;

import java.util.*;
import java.io.*;
import java.net.*;
import java.text.NumberFormat;

/**
   实现汇率的实时更新, 汇率换算表的建立, 以及汇率的换算
*/
public class Rate {
    /** 从网站:<a href="http://www.usd-cny.com/">http://www.usd-cny.com/</a>中获取最新的汇率信息 */
    final static String webSite = "http://www.usd-cny.com/";

    /** 利用Hashtable存储不同货币之间的利率 */
    private static Hashtable rateTable = new Hashtable();

    /** 记录人民币能够和其他哪些货币能够相互转换 */
    private static String toCurrency = null;

    /**
       从网站中爬取汇率信息, 并自动更新汇率信息
       @throws Exception 网页连接, 网页内容读取可能出现的异常
       @see com.cnblogs.grassandmoon.HtmlTable HtmlTable
    */
    public static void update() throws Exception {
        // 网页连接, 并获取相应的BufferedReader
        URL hp = new URL(webSite);
        URLConnection hpCon = hp.openConnection();

        InputStream input = (InputStream)hpCon.getInputStream();
        BufferedReader br = new BufferedReader(new
                                               InputStreamReader(input, "gb2312"));

        // 爬取html网页中第78行到303行的表中的元素信息
        String result = HtmlTable.extract(78, 303, br);

        String strRows[] = result.split(HtmlTable.ROW_SEPARATOR);
        String strCur, strRate;
        NumberFormat numFormat = NumberFormat.getNumberInstance();
        Number numb = null;

        // 遍历各行, 并将汇率信息存入rateTable中
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

    /**
       获取从货币1转换为货币2时的汇率
       @param from 需要转换的货币单位
       @param to 转换到的目标货币单位
       @return 汇率
    */
    public static Double getRate(String from, String to) {
        return (Double) rateTable.get(from + to);
    }

    /**
       将一定量的货币, 转变成另一货币单位的货币量
       @param m 待转换的货币
       @param to 转换到的货币的目标单位
       @return 转换后的货币
       @see com.cnblogs.grassandmoon.Money Money;
    */
    public static Money exchangeRate(Money m, String to) {
        if (m.unit.equals(to)) return new Money(m);
        Double rate = getRate(m.unit, to);

        if (rate == null) {
            throw new IllegalArgumentException();
        }

        return new Money(m.amount*rate.doubleValue(), to);
    }

    /** 输出支持相互转换的货币信息 */
    public static void printSupported() {
        System.out.println("支持人民币 CNY 和以下货币之间的转换\n" + toCurrency);
    }
}