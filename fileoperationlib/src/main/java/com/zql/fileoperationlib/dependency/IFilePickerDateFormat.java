package com.zql.fileoperationlib.dependency;

import android.content.Context;
import android.os.Parcelable;

public interface IFilePickerDateFormat extends Parcelable {
    String getFormatTime(Context context, long timeStamp);
}

