package com.neevek.lib.page;

/**
 * Created with IntelliJ IDEA.
 * User: neevek
 * Date: 12/7/13
 * Time: 3:58 PM
 */
public interface PageDataCallback<T> {
    void onPageDataReturned(T data);
}
