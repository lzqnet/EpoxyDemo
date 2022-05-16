package com.zql.frameworks.base.mvp;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Liu Hanhong on 2017/4/18.
 */

public interface LifeCycleReceiver {
    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onSaveInstance(Bundle outState);

    void onActivityResult(int requestCode, int resultCode, Intent data);
}
