package com.Kenji.pagadvisor

import android.app.Application
import com.Kenji.pagadvisor.data.local.UserPreferencesRepository
import com.Kenji.pagadvisor.data.remote.RetrofitClient
import com.Kenji.pagadvisor.data.repository.PagAdvisorRepositoryImpl
import com.Kenji.pagadvisor.data.repository.SalesRepositoryImpl
import com.Kenji.pagadvisor.domain.repository.PagAdvisorRepository
import com.Kenji.pagadvisor.domain.repository.SalesRepository

class PagAdvisorApp : Application() {

    lateinit var userPreferencesRepository: UserPreferencesRepository
    lateinit var salesRepository: SalesRepository
    lateinit var pagAdvisorRepository: PagAdvisorRepository

    override fun onCreate() {
        super.onCreate()

        userPreferencesRepository = UserPreferencesRepository(this)

        salesRepository = SalesRepositoryImpl(userPreferencesRepository)
        pagAdvisorRepository = PagAdvisorRepositoryImpl(RetrofitClient.instance)
    }
}