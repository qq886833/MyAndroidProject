package com.bsoft.module_ndk;

public class Test {

   static {
      System.loadLibrary("hello");
   }
   public static native String sayHello();
}
