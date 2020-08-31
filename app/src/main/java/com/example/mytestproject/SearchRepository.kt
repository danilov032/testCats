package com.example.mytestproject

import io.reactivex.Observable

class SearchRepository(val apiService: ApiService) {
    fun searchUsers(page: Int): Observable<List<Cat>> {
        return apiService.search(page)
    }
}