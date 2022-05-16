package com.example.epoxydemo.modelview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.example.epoxydemo.R

@ModelView(defaultLayout = R.layout.item_view_test)
class ItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context) {

    @TextProp
    fun setName(name: CharSequence?) {
        findViewById<TextView>(R.id.navigation_view_item_name).text = name
    }

    @TextProp
    fun setDate(date: CharSequence?) {
        findViewById<TextView>(R.id.navigation_view_item_summary).text = date
    }

    @TextProp
    fun setSize(size: CharSequence?) {
        findViewById<TextView>(R.id.navigation_view_item_size).text = size
    }

    //
    @ModelProp
    fun setIcon(res: Int) {
        findViewById<ImageView>(R.id.navigation_view_item_icon).setImageResource(res)
    }

}