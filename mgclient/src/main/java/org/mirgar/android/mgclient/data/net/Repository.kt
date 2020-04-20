package org.mirgar.android.mgclient.data.net

import org.mirgar.android.mgclient.data.net.models.Appeal
import retrofit2.await
import org.mirgar.android.mgclient.data.entity.Category as DBCategory

class Repository {
    private val retrofitFactory = RetrofitFactory()

    suspend fun getCategories() =
        retrofitFactory.default.create(RestClient::class.java).getAllCategories().await()
            .let { result ->
                if (result.err_code?.isNotBlank() == true || result.err_msg?.isNotBlank() == true)
                    TODO("Say to user what happen")
                result.data
            }.let { container ->
                container.groups.asSequence().flatMap { group ->
                    group.items.asSequence()
                        .map { category ->
                            DBCategory(
                                category.id,
                                category.name,
                                if (group.parentId != 0L) group.parentId else null,
                                if (category.hasIcon) container.template.format(category.id)
                                else null
                            )
                        }
                }
            }.asIterable()

    suspend fun authorize(username: String, password: String) =
        retrofitFactory.default.create(RestClient::class.java).authorize(username, password).await()
            .let { response ->
                if (response.err_code?.isNotBlank() == true
                    || response.err_msg?.isNotBlank() == true
                )
                    TODO("Say to user what happen")
                response.data
            }

    suspend fun send(auth: String, appeal: Appeal): Int {
        return retrofitFactory.default.create(RestClient::class.java)
            .sendAppeal("Bearer $auth", appeal)
            .await()
    }
}