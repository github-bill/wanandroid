package luyao.wanandroid.model.repository

import luyao.util.ktx.base.BaseRepository
import luyao.wanandroid.model.api.WanRetrofitClient
import luyao.wanandroid.model.bean.Navigation
import luyao.util.ktx.bean.Response

/**
 * Created by luyao
 * on 2019/4/10 14:15
 */
class NavigationRepository : BaseRepository() {


    suspend fun getNavigation(): Response<List<Navigation>> {
        return apiCall { WanRetrofitClient.service.getNavigation() }
    }
}