package luyao.wanandroid.model.repository

import luyao.util.ktx.base.BaseRepository
import luyao.wanandroid.model.api.WanRetrofitClient
import luyao.wanandroid.model.bean.ArticleList
import luyao.wanandroid.model.bean.SystemParent
import luyao.util.ktx.bean.Response

/**
 * Created by luyao
 * on 2019/4/10 14:34
 */
class SystemRepository : BaseRepository() {

    suspend fun getSystemTypeDetail(id: Int, page: Int): Response<ArticleList> {
        return apiCall { WanRetrofitClient.service.getSystemTypeDetail(page, id) }
    }

    suspend fun getSystemTypes(): Response<List<SystemParent>> {
        return apiCall { WanRetrofitClient.service.getSystemType() }
    }

    suspend fun collectArticle(articleId: Int): Response<ArticleList> {
        return apiCall { WanRetrofitClient.service.collectArticle(articleId) }
    }

    suspend fun unCollectArticle(articleId: Int): Response<ArticleList> {
        return apiCall { WanRetrofitClient.service.cancelCollectArticle(articleId) }
    }

    suspend fun getBlogArticle(id:Int,page:Int): Response<ArticleList> {
        return apiCall { WanRetrofitClient.service.getBlogArticle(id, page) }
    }
}