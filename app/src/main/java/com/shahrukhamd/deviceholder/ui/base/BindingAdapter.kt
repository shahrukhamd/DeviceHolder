package com.shahrukhamd.deviceholder.ui.base

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.shahrukhamd.deviceholder.R

@BindingAdapter(value = ["dividerItemDecorationOrientation"])
fun RecyclerView.setDividerItemDecoration(dividerItemDecorationOrientation: Int) {
    addItemDecoration(DividerItemDecoration(context, dividerItemDecorationOrientation))
}

@BindingAdapter(value = ["loadImage", "placeholder", "circleCropEnabled"], requireAll = false)
fun ImageView.loadImage(url: String?, placeholder: Drawable?, circleCropEnabled: Boolean?) {
    Glide.with(context).load(url).also { glide ->
        placeholder?.let {
            glide.placeholder(it)
            glide.error(it)
        }
        if (circleCropEnabled == true) {
            glide.circleCrop()
        }
    }.into(this)
}

@BindingAdapter(value = ["stickers"])
fun ChipGroup.addStickers(stickers: List<String?>?) {
    stickers?.filterNotNull()?.forEach {
        val chip = Chip(context).apply {
            text = it
            setTextAppearance(R.style.StickerChipAppearance)
            chipEndPadding = 2f
            chipStartPadding = 2f
            chipStrokeWidth = 0.4f
            setChipBackgroundColorResource(if (it.equals("ban", true)) R.color.chip_bg_red else R.color.chip_bg_grey)
        }

        addView(chip)
    }
}