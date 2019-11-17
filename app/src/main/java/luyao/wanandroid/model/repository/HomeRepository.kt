package luyao.wanandroid.model.repository

import luyao.util.ktx.base.BaseRepository
import luyao.wanandroid.model.api.WanRetrofitClient
import luyao.wanandroid.model.bean.ArticleList
import luyao.wanandroid.model.bean.Banner
import luyao.util.ktx.bean.Response

/**
 * Created by luyao
 * on 2019/4/10 14:09
 */
class HomeRepository : BaseRepository() {

    suspend fun getBanners(): Response<List<Banner>> {
        return apiCall { WanRetrofitClient.service.getBanner() }
    }

    suspend fun getArticleList(page: Int): Response<ArticleList> {
        return apiCall { WanRetrofitClient.service.getHomeArticles(page) }
    }

    suspend fun collectArticle(articleId: Int): Response<ArticleList> {
        return apiCall { WanRetrofitClient.service.collectArticle(articleId) }
    }

    suspend fun unCollectArticle(articleId: Int): Response<ArticleList> {
        return apiCall { WanRetrofitClient.service.cancelCollectArticle(articleId) }
    }
}