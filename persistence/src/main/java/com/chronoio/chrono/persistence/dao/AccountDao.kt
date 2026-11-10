package com.chronoio.chrono.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chronoio.chrono.persistence.common.Config
import com.chronoio.chrono.persistence.model.AccountTable

@Dao
abstract class AccountDao : BaseDao<AccountTable> {

    // Override BaseDao.insert with REPLACE so saveAccount is idempotent.
    // SelectAccount calls saveAccount on every login; without REPLACE,
    // re-selecting an existing account hits a UNIQUE PK violation.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override fun insert(obj: AccountTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override fun insert(objects: List<AccountTable>)

    @Query(Config.CLEAR_ACCOUNT_TABLE)
    abstract fun clear()

    @Query(Config.QUERY_LAST_ACCOUNT)
    abstract fun lastAccount(): List<AccountTable>

    @Query(Config.QUERY_ACCOUNT_BY_ID)
    abstract fun getAccount(id: String): AccountTable?

    @Query(Config.GET_ACCOUNTS)
    abstract fun getAccounts(): List<AccountTable>
}