package com.demo.util;

import java.text.DecimalFormat;

/**
 * 金额格式化工具（每次新建实例，避免 DecimalFormat 线程安全问题）。
 */
public final class Fmt {

    private Fmt() {
    }

    public static String money(double value) {
        return new DecimalFormat("#,##0.00").format(value);
    }

    public static String moneyShort(double value) {
        return new DecimalFormat("#,##0.##").format(value);
    }
}
