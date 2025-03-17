package com.crypto.interview.core.database

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 14:14<br>
 * Desc:
 * A Room DataBase,just a demo class,no implementations <br>
 */

class CryptoDataBase private constructor()
//    :RoomDatabase()
{
    companion object {
        @Volatile
        private var INSTANCE: CryptoDataBase? = null

        @JvmStatic
        fun getInstance(): CryptoDataBase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CryptoDataBase().also { INSTANCE = it }
            }
        }
    }

    fun getSettingData(): Flow<Any> {
        return flowOf(Any())
    }

    fun saveSettingData() {

    }
}