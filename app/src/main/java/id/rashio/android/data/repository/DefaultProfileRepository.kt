package id.rashio.android.data.repository

import id.rashio.android.api.ApiService
import javax.inject.Inject

class DefaultProfileRepository @Inject constructor(
    private val api: ApiService,
) : ProfileRepository {

}
