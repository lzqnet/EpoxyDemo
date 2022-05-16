package com.example.epoxydemo;

import com.airbnb.epoxy.EpoxyDataBindingLayouts;
import com.airbnb.epoxy.PackageModelViewConfig;

@PackageModelViewConfig(rClass = R.class)
@EpoxyDataBindingLayouts(R.layout.item_view_databinding)
interface EpoxyConfig {}

