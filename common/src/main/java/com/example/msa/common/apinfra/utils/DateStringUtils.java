package com.example.msa.common.apinfra.utils;

import java.sql.Timestamp;
import java.util.Date;

public interface DateStringUtils {
  public static String now() {
    return (new Timestamp(System.currentTimeMillis()).toString());
  }

  public static Date nowDate() {
    return new Date(System.currentTimeMillis());
  }
}
