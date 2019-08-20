package com.simple.module.download

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.simple.R
import com.simple.base.BaseActivity
import com.simple.base.BaseFragmentStatePagerAdapter
import com.simple.base.setUpWithViewPager2
import com.simple.tools.ResUtil
import kotlinx.android.synthetic.main.activity_download.*

class DownloadActivity : BaseActivity() {
    override fun layoutId() = R.layout.activity_download
    private val fragments = ArrayList<Fragment>()
    private val titles = ArrayList<String>()

    init {
        fragments.add(DownloadingFragment())
        fragments.add(CompleteFragment())
        titles.add(ResUtil.getString(R.string.downloading))
        titles.add(ResUtil.getString(R.string.completeDownload))
    }

    override fun initView(savedInstanceState: Bundle?) {
        vp_download.adapter = BaseFragmentStatePagerAdapter(fragments, this)
        tabLayout.setUpWithViewPager2(vp_download, titles)
    }

    companion object {
        @JvmStatic
        fun actionStart(ctx: Context) {
            ctx.startActivity(Intent(ctx, DownloadActivity::class.java))
        }
    }
}