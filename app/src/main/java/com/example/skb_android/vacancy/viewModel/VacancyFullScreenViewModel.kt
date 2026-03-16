package com.example.skb_android.vacancy.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import com.example.skb_android.vacancy.model.FullVacancyModel
import com.example.skb_android.vacancy.model.listFullVacanciesInfo
import java.util.concurrent.CompletableFuture

data class VacancyFullScreenState(
    val isLoading: Boolean,
    val fullVacancy: FullVacancyModel?
)

class VacancyFullScreenViewModel(
    private val vacancyId: String
) : ViewModel() {

    private val _state = MutableStateFlow(VacancyFullScreenState(true, null))
    val state: StateFlow<VacancyFullScreenState> = _state

    init {
        load()
    }

    private fun load() {
        CompletableFuture.supplyAsync {
            Thread.sleep(1000)
            _state.update {
                VacancyFullScreenState(
                    false,
                    listFullVacanciesInfo.find { it.id == vacancyId }
                )
            }
        }
    }
}
