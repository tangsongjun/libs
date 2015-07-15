package com.neevek.lib.page;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.neevek.lib.page.annotation.InnerPageContainerLayoutResId;
import com.neevek.lib.page.exception.InjectFailedException;

/**
 * An InnerPageContainer is a Page that is used to contain & manage InnerPages
 *
 * Date: 1/1/14
 * Time: 11:06 AM
 *
 * @author i@neevek.net
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class InnerPageContainer extends Page {
    private InnerPageManager mInnerPageManager;

    public InnerPageContainer(PageActivity pageActivity) {
        super(pageActivity);

        Class clazz = getClass();

        InnerPageContainerLayoutResId resIdAnnotation = null;

        try {
            do {
                if (clazz.isAnnotationPresent(InnerPageContainerLayoutResId.class)) {
                    resIdAnnotation = (InnerPageContainerLayoutResId)clazz.getAnnotation(InnerPageContainerLayoutResId.class);
                    break;
                }
            } while ((clazz = clazz.getSuperclass()) != InnerPageContainer.class);

        } catch (Exception e) {
            e.printStackTrace();
            throw new InjectFailedException(e);
        }

        if (resIdAnnotation == null) {
            throw new IllegalStateException("Must specify a layout for InnerPageContainer with the @InnerPageContainerLayoutResId annotation.");
        }

        View container = getView().findViewById(resIdAnnotation.value());
        if (container == null) {
            throw new IllegalStateException("Can not find the layout with the specified resource ID: " + resIdAnnotation.value());
        }
        if (!(container instanceof ViewGroup)) {
            throw new IllegalStateException("The specified layout for InnerPageContainer is not of type ViewGroup.");
        }
        mInnerPageManager = new InnerPageManager((ViewGroup)container);
    }

    public InnerPageManager getInnerPageManager() {
        return mInnerPageManager;
    }


    public void setInnerPage(InnerPage newPage, Object data) {
        mInnerPageManager.setPage(newPage, data);
    }

    public void unsetInnerPage() {
        mInnerPageManager.unsetPage();
    }

    public InnerPage getCurrentInnerPage() {
        return mInnerPageManager.getCurrentPage();
    }

    @Override
    public void onResume() {
        mInnerPageManager.onResume();
    }

    @Override
    public void onPause() {
        mInnerPageManager.onPause();
    }

    @Override
    public boolean onBackPressed() {
        return mInnerPageManager.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mInnerPageManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onShown(Object arg) {
        mInnerPageManager.onShown(arg);
    }

    @Override
    public void onHidden() {
        mInnerPageManager.onHidden();
    }

    @Override
    public void onCovered() {
        mInnerPageManager.onCovered();
    }

    @Override
    public void onUncovered(Object arg) {
        mInnerPageManager.onUncovered(arg);
    }
}
