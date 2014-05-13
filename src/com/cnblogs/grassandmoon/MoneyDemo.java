package com.cnblogs.grassandmoon;

import java.io.*;

class Helper {
    public static void printVersion() {
        System.out.println("欢迎使用汇率转换器");
        System.out.println("该汇率转换器实现了人民币同不同货币之间的换算");
        System.out.println("使用的汇率是现钞买入价");
        System.out.println("即, 向银行换取外币时使用的汇率");
        System.out.println();
        System.out.println("版本: 1.0");
        System.out.println();
        System.out.println("作者: 钟谢伟");
        System.out.println("email: grass-of-sky@163.com");
        System.out.println();
    }

    public static void printHelp() {
        System.out.println("1. 输入待转换量, 用空格隔开");
        System.out.println("   " + getExchangeUsage());
        System.out.println();
        System.out.println("2. 帮助");
        System.out.println();
        System.out.println("3. 察看支持的货币单位");
        System.out.println();
        System.out.println("4. 退出");
        System.out.println();
        printUsage();
        System.out.println();
    }

    public static void printUsage() {
        System.out.println("请选择 (1 输入待转换量, 2 帮助, 3 察看支持的货币单位 或者 4 退出)");
    }

    public static String getExchangeUsage() {
        return "如: 100 USD CNY";
    }
}

class ReadLineTokens {
    public double value;
    public String from;
    public String to;

    ReadLineTokens(double v, String f, String t) {
        value = v;
        from = f;
        to = t;
    }
}

public class MoneyDemo {
    // 对汇率输入, 以及转换输入进行处理, 提取不同的成份
    static ReadLineTokens processReadLine(BufferedReader br)
    throws IOException {
        String inputLine = br.readLine();
        String tokens[] = inputLine.split(" ");
        if (tokens.length == 1 && tokens[0].equals("q")) {
            return null;
        }

        return new ReadLineTokens(new Double(tokens[0]),
                                  tokens[1],
                                  tokens[2]);
    }

    public static void main(String[] args)
    throws IOException {
        String cmdLine = null;
        Money m = new Money();
        Money exchanged = new Money();
        ReadLineTokens tokens;
        boolean inputError = false;
        BufferedReader br = new
            BufferedReader(new InputStreamReader(System.in));
        System.out.println("开始更新汇率表");
        try {
            Rate.update();
        } catch (Exception ex) {
            System.out.println("汇率表更新失败");
            System.exit(-1);
        }
        System.out.println("完成汇率表更新");
        System.out.println();
        Rate.printSupported();
        Helper.printVersion();
        Helper.printHelp();
        System.out.println();

        while (true) {
            cmdLine = br.readLine();
            if (cmdLine.equals("1")) {
                do {
                    try {
                        System.out.print("输入待转换量 " +
                                         Helper.getExchangeUsage() +
                                         " (退出当前选项 \"q\"): ");

                        // input "q" to quit this step
                        tokens = processReadLine(br);
                        if (tokens == null) {
                            break;
                        }

                        m.setMoney(tokens.value, tokens.from);
                        exchanged = Rate.exchangeRate(m, tokens.to);
                        System.out.println(m + " = " + exchanged);
                        System.out.println();
                        inputError = false;
                    } catch (IllegalArgumentException e) {
                        System.out.println("\n\t汇率转换表中没有相应的转换项\n");
                        inputError = true;
                    } catch (Exception e) {
                        System.out.println("\n\t请按照正确的格式进行输入\n");
                        inputError = true;
                    }
                } while (inputError);
            } else if (cmdLine.equals("2")) {
                Helper.printHelp();
            } else if (cmdLine.equals("3")) {
                // 输出支持换算的货币
                Rate.printSupported();
            } else if (cmdLine.equals("4")) {
                break;
            } else {
                Helper.printUsage();
            }
        }
    }
}