package com.example.epoxydemo.modelview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.example.epoxydemo.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ItemViewTest @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context) {
    val holder: FileListHolder = FileListHolder()

    init {
        inflate(context, R.layout.item_view_test, this)
        holder.bindView(this)
    }

    @TextProp
    fun setName(name:CharSequence?){
        holder.mTvName.text=name
    }

    @TextProp
    fun setDate(date:CharSequence?){
        holder.mTvDate.text=date
    }

    @TextProp
    fun setSize(size:CharSequence?){
        holder.mTvSize.text=size
    }
//
    @ModelProp
    fun setIcon(res:Int){
        holder.mIvIcon.setImageResource(res)
    }


    class FileListHolder {

//        lateinit var mBtCheck: CheckBox
        lateinit var mIvIcon: ImageView
        lateinit var mTvName: TextView
        lateinit var mTvDate: TextView
        lateinit var mTvSize: TextView
        lateinit var mRootView: View


        fun bindView(itemView: View) {
            mRootView = itemView
            mIvIcon = itemView.findViewById(R.id.navigation_view_item_icon)
            mTvName = itemView.findViewById(R.id.navigation_view_item_name)
            mTvDate = itemView.findViewById(R.id.navigation_view_item_summary)
            mTvSize = itemView.findViewById(R.id.navigation_view_item_size)
//            mBtCheck = itemView.findViewById(com.zql.filepickerlib.R.id.selected_icon)
        }
    }
}