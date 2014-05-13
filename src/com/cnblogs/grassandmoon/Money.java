package com.cnblogs.grassandmoon;

/*
 * 用于存储货币的单位, 以及货币的量
 */
public class Money {
    double amount;
    String unit;

    public String toString() {
        return amount + " " + unit;
    }

    public Money() {
        setMoney(0.0, null);
    }

    public Money(double amount, String unit) {
        setMoney(amount, unit);
    }

    public Money(Money m) {
        setMoney(m.amount, m.unit);
    }

    public void setMoney(double amount, String unit) {
        this.amount = amount;
        this.unit = unit;
    }

    public boolean equals(Object obj) {
        Money other = (Money) obj;
        return amount == other.amount
            && unit.equals(other.unit);
    }
}