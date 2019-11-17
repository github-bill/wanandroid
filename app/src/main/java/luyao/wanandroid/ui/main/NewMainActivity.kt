package luyao.wanandroid.ui.main

import android.Manifest
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_new_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import luyao.util.ktx.base.BaseActivity
import luyao.util.ktx.ext.goToAppInfoPage
import luyao.util.ktx.ext.listener.onPageSelected
import luyao.util.ktx.ext.permission.PermissionRequest
import luyao.util.ktx.ext.permission.request
import luyao.util.ktx.ext.startKtxActivity
import luyao.util.ktx.ext.toast
import luyao.wanandroid.R
import luyao.wanandroid.model.api.WanRetrofitClient
import luyao.wanandroid.ui.BrowserNormalActivity
import luyao.wanandroid.ui.about.AboutActivity
import luyao.wanandroid.ui.collect.MyCollectActivity
import luyao.wanandroid.ui.home.HomeFragment
import luyao.wanandroid.ui.login.LoginActivity
import luyao.wanandroid.ui.navigation.NavigationFragment
import luyao.wanandroid.ui.project.ProjectActivity
import luyao.wanandroid.ui.project.ProjectTypeFragment
import luyao.wanandroid.ui.search.SearchActivity
import luyao.wanandroid.ui.share.ShareActivity
import luyao.wanandroid.ui.square.SquareFragment
import luyao.wanandroid.ui.system.SystemFragment
import luyao.wanandroid.util.LoginPrefsHelper
import luyao.wanandroid.util.TOOL_URL

/**
 * Created by luyao
 * on 2019/5/7 15:36
 */
class NewMainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val titleList = arrayOf("首页", "广场", "最新项目", "体系", "导航")
    private val fragmentList = arrayListOf<Fragment>()
    private val homeFragment by lazy { HomeFragment() } // 首页
    private val squareFragment by lazy { SquareFragment() } // 广场
    private val lastedProjectFragment by lazy { ProjectTypeFragment.newInstance(0, true) } // 最新项目
    private val systemFragment by lazy { SystemFragment() } // 体系
    private val navigationFragment by lazy { NavigationFragment() } // 导航


    override fun getLayoutResId() = R.layout.activity_new_main

    init {
        fragmentList.add(homeFragment)
        fragmentList.add(squareFragment)
        fragmentList.add(lastedProjectFragment)
        fragmentList.add(systemFragment)
        fragmentList.add(navigationFragment)
    }

    override fun initView() {
        initViewPager()
        mainToolBar.setNavigationOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.menu.findItem(R.id.nav_exit).isVisible = LoginPrefsHelper.instance.isLogin
        addFab.setOnClickListener {
            if (!LoginPrefsHelper.instance.isLogin) startKtxActivity<LoginActivity>()
            else startKtxActivity<ShareActivity>()
        }
    }

    override fun initData() {
        mainSearch.setOnClickListener { startKtxActivity<SearchActivity>() }


        request(Manifest.permission.WRITE_EXTERNAL_STORAGE) {
            onGranted { toast("onGranted") }
            onDenied { toast("onDenied") }
            onShowRationale { showRationale(it) }
            onNeverAskAgain { goToAppInfoPage() }
        }
    }

    private fun showRationale(request: PermissionRequest) {
        MaterialDialog(this).show {
            title = "Request Permission"
            message(text = "We need permission to provide normal service. Do you agree ?")
            positiveButton(R.string.agree) { request.retry() }
            negativeButton(R.string.disagree)
        }
    }

    private fun initViewPager() {
        viewPager.offscreenPageLimit = titleList.size
        viewPager.adapter = object : androidx.fragment.app.FragmentPagerAdapter(
            supportFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
            override fun getItem(position: Int) = fragmentList[position]

            override fun getCount() = titleList.size

            override fun getPageTitle(position: Int) = titleList[position]

        }
        viewPager.onPageSelected { newPosition -> if (newPosition == 1) addFab.show() else addFab.hide() }
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_blog -> startKtxActivity<ProjectActivity>(value = ProjectActivity.BLOG_TAG to true)
            R.id.nav_project_type -> startKtxActivity<ProjectActivity>(value = ProjectActivity.BLOG_TAG to false)
            R.id.nav_tool -> switchToTool()
            R.id.nav_collect -> switchCollect()
            R.id.nav_about -> switchAbout()
            R.id.nav_exit -> exit()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun exit() {
        MaterialDialog(this).show {
            title = "退出"
            message(text = "是否确认退出登录？")
            positiveButton(text = "确认") {
                launch(Dispatchers.Default) {
                    WanRetrofitClient.service.logOut()
                }
                LoginPrefsHelper.instance.isLogin = false
                LoginPrefsHelper.instance.userJson = ""
                refreshView()
            }
            negativeButton(text = "取消")
        }
    }

    private fun refreshView() {
        navigationView.menu.findItem(R.id.nav_exit).isVisible = LoginPrefsHelper.instance.isLogin
        homeFragment.refresh()
        lastedProjectFragment.refresh()
    }

    private fun switchAbout() {
        startKtxActivity<AboutActivity>()
    }

    private fun switchCollect() {
        if (LoginPrefsHelper.instance.isLogin) {
            startKtxActivity<MyCollectActivity>()
        } else {
            startKtxActivity<LoginActivity>()
        }
    }

    private fun switchToTool() {
        startKtxActivity<BrowserNormalActivity>(value = BrowserNormalActivity.URL to TOOL_URL)
    }
}