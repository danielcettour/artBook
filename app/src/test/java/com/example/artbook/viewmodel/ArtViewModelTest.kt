package com.example.artbook.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.atilsamancioglu.artbookhilttesting.MainCoroutineRule
import com.atilsamancioglu.artbookhilttesting.getOrAwaitValueTest
import com.example.artbook.repo.FakeArtRepository
import com.example.artbook.utils.Status
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ArtViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule() // no testeamos threading, porque estamos en la carpeta Test y no AndroidTest, si estuvieramos en AndroidTest esto no se necesita

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    private lateinit var viewModelTest: ArtViewModel

    @Before
    fun setup(){
        /**
         * Test Doubles (fake repository)
         * Se llama doble porque es una copia
         * inicializamos el viewmodel, como no nos importa el repositorio, creamos uno fake,
         * ya que no testeamos el repositorio sino el viewmodel
        */

        viewModelTest = ArtViewModel(FakeArtRepository())
    }

    @Test
    fun `insert art without year should return an error`(){
        viewModelTest.makeArt("Mona Lisa", "Da Vinci", "")
        val value = viewModelTest.insertArtMessage.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert art without name should return an error`(){
        viewModelTest.makeArt("", "Da Vinci", "1900")
        val value = viewModelTest.insertArtMessage.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert art without artistName should return an error`(){
        viewModelTest.makeArt("Mona Lisa", "", "1900")
        val value = viewModelTest.insertArtMessage.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }
}