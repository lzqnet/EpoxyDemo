package com.zql.frameworks.app.context;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Liu Hanhong on 16/9/14.
 */

public final class ContextUtil {

    private ContextUtil() {
    }

    public static void startActivityForResult(Context context, Intent intent, int requestCode) {
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, requestCode);
        } else if (context instanceof FragmentContext) {
            ((FragmentContext) context).startActivityForResult(intent, requestCode);
        } else {
            context.startActivity(intent);
        }
    }
}
