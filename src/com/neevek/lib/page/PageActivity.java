package com.neevek.lib.page;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.neevek.lib.page.annotation.InjectPageAnimator;
import com.neevek.lib.page.exception.InjectFailedException;
import com.neevek.lib.page.util.AnnotationUtils;
import com.neevek.lib.page.util.ViewFinder;

public class PageActivity extends Activity {
    private PageManager mPageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageManager = new PageManager((ViewGroup)findViewById(android.R.id.content));

        try {
            initAnnotatedFields();

        } catch (Exception e) {
            e.printStackTrace();
            throw new InjectFailedException(e);
        }
    }

    private void initAnnotatedFields() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        Class clazz = getClass();

        List<Class> list = new ArrayList<Class>();
        do {
            list.add(clazz);

            if (mPageManager.getPageAnimator() == null) {
                InjectPageAnimator paAnnotation = (InjectPageAnimator)clazz.getAnnotation(InjectPageAnimator.class);
                if (paAnnotation != null) {
                    mPageManager.setPageAnimator(paAnnotation.value().newInstance());
                }
            }
        } while ((clazz = clazz.getSuperclass()) != PageActivity.class);

        ViewFinder viewFinder = new ViewFinder() {
            public View findViewById(int id) { return PageActivity.this.findViewById(id); }
        };
        for (int i = list.size() - 1; i >= 0; --i) {
            AnnotationUtils.initAnnotatedFields(list.get(i), this, viewFinder);
        }
    }

    public PageManager getPageManager() {
        return mPageManager;
    }

    //**************** methods to show a new page ****************//
    public void showPage(Class <? extends Page > pageClass, boolean animated) {
        showPage(pageClass, animated, null);
    }

    public void showPage(Class <? extends Page > pageClass, boolean animated, Object arg) {
        try {
            Page page = pageClass.getConstructor(PageActivity.class).newInstance(this);
            page.show(arg, animated);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideTopPage() {
        getPageManager().popPage(false, false);
    }

    public int getPageCount() {
        return mPageManager.getPageCount();
    }

    @Override
    public void onBackPressed() {
        if (!mPageManager.onBackPressed()) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPageManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPageManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPageManager.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPageManager.onDestroy();
    }


}
