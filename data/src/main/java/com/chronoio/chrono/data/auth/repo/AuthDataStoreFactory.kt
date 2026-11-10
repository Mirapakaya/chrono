package com.chronoio.chrono.data.auth.repo

class AuthDataStoreFactory(
    val cache: AuthCacheDataStore,
    val remote: AuthRemoteDataStore
)