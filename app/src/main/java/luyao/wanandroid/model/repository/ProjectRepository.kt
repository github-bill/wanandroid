package luyao.wanandroid.model.repository

import luyao.util.ktx.base.BaseRepository
import luyao.wanandroid.model.api.WanRetrofitClient
import luyao.wanandroid.model.bean.ArticleList
import luyao.wanandroid.model.bean.SystemParent
import luyao.util.ktx.bean.Response

/**
 * Created by luyao
 * on 2019/4/10 14:18
 */
class ProjectRepository : BaseRepository() {

    suspend fun getProjectTypeDetailList(page: Int, cid: Int): Response<ArticleList> {
        return apiCall { WanRetrofitClient.service.getProjectTypeDetail(page, cid) }
    }

    suspend fun getProjectTypeList(): Response<List<SystemParent>> {
        return apiCall { WanRetrofitClient.service.getProjectType() }
    }

    suspend fun collectArticle(articleId: Int): Response<ArticleList> {
        return apiCall { WanRetrofitClient.service.collectArticle(articleId) }
    }

    suspend fun unCollectArticle(articleId: Int): Response<ArticleList> {
        return apiCall { WanRetrofitClient.service.cancelCollectArticle(articleId) }
    }

    suspend fun getLastedProject(page: Int): Response<ArticleList> {
        return apiCall { WanRetrofitClient.service.getLastedProject(page) }
    }

    suspend fun getBlog(): Response<List<SystemParent>> {
        return apiCall { WanRetrofitClient.service.getBlogType() }
    }


}