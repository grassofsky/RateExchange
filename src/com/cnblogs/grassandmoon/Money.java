package com.cnblogs.grassandmoon;

/**
   用于存储货币的单位, 以及货币的量
*/
public class Money {
    double amount;
    String unit;

    /**
       @return "数量 单位"的形式输出, 如: <code>100 USD</code>
    */
    public String toString() {
        return amount + " " + unit;
    }

    /**
       默认构造函数, 另货币的量为0, 单位为null
    */
    public Money() {
        setMoney(0.0, null);
    }

    /**
       根据货币量和货币单位建立一个新的对象
       @param amount 货币量
       @param unit 货币单位
    */
    public Money(double amount, String unit) {
        setMoney(amount, unit);
    }

    /**
       根据货币Money创建新的对象
       @param m 货币
    */
    public Money(Money m) {
        setMoney(m.amount, m.unit);
    }

    /**
       设置货币量以及货币单位
       @param amount 货币量
       @param unit 货币单位
    */
    public void setMoney(double amount, String unit) {
        this.amount = amount;
        this.unit = unit;
    }

    /**
       当货币单位以及货币量都相等时, 两个货币对象就相等
       @param obj 货币
       @return 相等为<code>true</code>, 不相等为<code>false</code>
    */
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null) return false;

        if (getClass() != obj.getClass()) return false;

        Money other = (Money) obj;
        return amount == other.amount
            && unit.equals(other.unit);
    }
}