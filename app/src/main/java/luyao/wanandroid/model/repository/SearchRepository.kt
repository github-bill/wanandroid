package luyao.wanandroid.model.repository

import luyao.util.ktx.base.BaseRepository
import luyao.wanandroid.model.api.WanRetrofitClient
import luyao.wanandroid.model.bean.ArticleList
import luyao.wanandroid.model.bean.Hot
import luyao.util.ktx.bean.Response

/**
 * Created by luyao
 * on 2019/4/10 14:26
 */
class SearchRepository : BaseRepository() {
    suspend fun searchHot(page: Int, key: String): Response<ArticleList> {
        return apiCall { WanRetrofitClient.service.searchHot(page, key) }
    }

    suspend fun getWebSites(): Response<List<Hot>> {
        return apiCall { WanRetrofitClient.service.getWebsites() }
    }

    suspend fun getHotSearch(): Response<List<Hot>> {
        return apiCall { WanRetrofitClient.service.getHot() }
    }

    suspend fun collectArticle(articleId: Int): Response<ArticleList> {
        return apiCall { WanRetrofitClient.service.collectArticle(articleId) }
    }

    suspend fun unCollectArticle(articleId: Int): Response<ArticleList> {
        return apiCall { WanRetrofitClient.service.cancelCollectArticle(articleId) }
    }
}