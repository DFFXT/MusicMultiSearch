package com.simple.module.main

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import com.simple.R
import com.simple.base.BaseFragmentStatePagerAdapter
import com.simple.base.BaseNavFragment
import com.simple.base.setUpWithViewPager2
import com.simple.module.main.subFragmnet.LocalMusicFragment
import com.simple.module.search.related.SearchActivity
import com.simple.tools.ResUtil
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : BaseNavFragment() {
    override var fitId = R.id.topBar

    override fun layoutId() = R.layout.fragment_main
    private val title: ArrayList<String> = ArrayList(2)
    private val fragments = ArrayList<Fragment>(2)
    private lateinit var v: View

    init {
        title.add(ResUtil.getString(R.string.song))
        fragments.add(LocalMusicFragment())
        title.add(ResUtil.getString(R.string.singer))
        fragments.add(LocalMusicFragment())
    }

    override fun initView() {

        rootView.topBar.setEndImageListener(View.OnClickListener {
            v = it
            prepareCall(object :ActivityResultContract<Void,String>(){
                override fun createIntent(input: Void?): Intent {
                    return Intent(context, SearchActivity::class.java)
                }

                override fun parseResult(resultCode: Int, intent: Intent?): String {
                    return intent?.getStringExtra(SearchActivity.DATA)?:""
                }
            }){result->
                val b = Bundle()
                b.putString("keyword", result)
                Navigation.findNavController(v).navigate(R.id.action_gotToSearchResult, b)
            }.launch(null)

        })

        rootView.vp_local.adapter = BaseFragmentStatePagerAdapter(fragments, context as FragmentActivity)
        rootView.tabLayout.setUpWithViewPager2(rootView.vp_local, title)


    }


    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != RESULT_OK) return
        if (requestCode == 200) {
            rootView.post {
                val b = Bundle()
                b.putString("keyword", data!!.getStringExtra(SearchActivity.DATA))
                Navigation.findNavController(v).navigate(R.id.action_gotToSearchResult, b)
            }
        }
    }*/
}