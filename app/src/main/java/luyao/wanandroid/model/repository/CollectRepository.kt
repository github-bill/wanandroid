package luyao.wanandroid.model.repository

import luyao.util.ktx.base.BaseRepository
import luyao.wanandroid.model.api.WanRetrofitClient
import luyao.wanandroid.model.bean.ArticleList
import luyao.util.ktx.bean.Response

/**
 * Created by luyao
 * on 2019/4/10 14:01
 */
class CollectRepository : BaseRepository(){

    suspend fun getCollectArticles(page: Int): Response<ArticleList> {
        return apiCall { WanRetrofitClient.service.getCollectArticles(page) }
    }

    suspend fun collectArticle(articleId: Int): Response<ArticleList> {
        return apiCall { WanRetrofitClient.service.collectArticle(articleId) }
    }

    suspend fun unCollectArticle(articleId: Int): Response<ArticleList> {
        return apiCall { WanRetrofitClient.service.cancelCollectArticle(articleId) }
    }
}