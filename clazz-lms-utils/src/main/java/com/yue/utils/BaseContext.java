package com.yue.utils;

/**
 * base context class
 * used to store the current user id in the thread local
 * and get the current user id from the thread local
 * and remove the current user id from the thread local
 */
public class BaseContext {

    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Integer id) {
        threadLocal.set(id);
    }

    public static Integer getCurrentId() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }
}
