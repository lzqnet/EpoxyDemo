package com.example.epoxydemo.viewholder

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.epoxydemo.R


@EpoxyModelClass(layout = R.layout.drivefilepicker_item_file)
abstract class ItemViewModelWithHolder : EpoxyModelWithHolder<ItemViewModelWithHolder.FileListHolder>() {

    @EpoxyAttribute
    var iconRes: Int = 0

    @EpoxyAttribute
    lateinit var name: String

    @EpoxyAttribute
    lateinit var date: String

    @EpoxyAttribute
    lateinit var size: String

    @EpoxyAttribute
    var selected: Boolean = false


//    override fun getDefaultLayout(): Int {
//        return R.layout.drivefilepicker_item_file
//    }

    override fun bind(holder: ItemViewModelWithHolder.FileListHolder) {
        super.bind(holder)
//        holder.mBtCheck.setClickable(false)
//        holder.mBtCheck.setChecked(selected)
        holder.mIvIcon.setImageResource(iconRes)
        holder.mTvDate.setText(date)
        holder.mTvName.setText(name)
//        holder.mRootView.setOnClickListener(object :View.OnClickListener{
//            override fun onClick(v: View?) {
//                holder.mBtCheck.isChecked = !holder.mBtCheck.isChecked
//                clickListener.OnClick(holder.mBtCheck.isChecked)
//            }
//
//        })


    }

    class FileListHolder : EpoxyHolder() {

        lateinit var mIvIcon: ImageView
        lateinit var mTvName: TextView
        lateinit var mTvDate: TextView
        lateinit var mTvSize: TextView
        lateinit var mRootView: View


        override fun bindView(itemView: View) {
            mRootView = itemView
            mIvIcon = itemView.findViewById(R.id.navigation_view_item_icon)
            mTvName = itemView.findViewById(R.id.navigation_view_item_name)
            mTvDate = itemView.findViewById(R.id.navigation_view_item_summary)
            mTvSize = itemView.findViewById(R.id.navigation_view_item_size)
//            mBtCheck = itemView.findViewById(R.id.selected_icon)
        }
    }

}