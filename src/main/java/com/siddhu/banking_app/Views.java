package com.siddhu.banking_app;

public class Views {
    public static class Public {}    // Basic view for public data
    public static class Internal extends Public {} // Internal view includes Public fields + internal data
    public static class Full extends Internal {}   // Full view includes all fields
}
