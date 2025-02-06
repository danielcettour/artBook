package com.example.artbook.roomdb

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.atilsamancioglu.artbookhilttesting.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class ArtDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("testDataBase") // nombre usado para inyectar este en particular, el de test, y no el original
    lateinit var database : ArtDatabase

    // private lateinit var database : ArtDatabase -> se comenta al testear DI
    @Inject
    lateinit var dao : ArtDao

    @Before
    fun setup(){
        // inMemoryDatabaseBuilder -> temporal, sólo para fines de testing. Corre en main thread
        /*database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), ArtDatabase::class.java
        ).allowMainThreadQueries().build() -> se comenta al testear DI*/

        hiltRule.inject()

        //dao = database.artDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertArtTesting() = runBlocking { // runBlockingTest deprecated
        val exampleArt = Art("Mona Lisa", "Da Vinci", 1700, "test.com", 1)
        dao.insertArt(exampleArt)
        val list = dao.observeArts().getOrAwaitValue() // espera el valor del live data
        assertThat(list).contains(exampleArt)
    }

    @Test
    fun deleteArtTesting() = runBlocking {
        //el id puede ser el mismo porque es una inMemoryDatabaseBuilder y se borra tras cada test
        val exampleArt = Art("Mona Lisa", "Da Vinci", 1700, "test.com", 1)
        dao.insertArt(exampleArt)
        var list = dao.observeArts().getOrAwaitValue() // espera el valor del live data
        assertThat(list).contains(exampleArt)

        dao.deleteArt(exampleArt)
        list = dao.observeArts().getOrAwaitValue()
        assertThat(list).doesNotContain(exampleArt)
    }
}