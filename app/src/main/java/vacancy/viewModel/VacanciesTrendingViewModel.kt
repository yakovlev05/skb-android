package vacancy.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import vacancy.repository.VacanciesFavoritesRepository

class VacanciesTrendingViewModel(
    private val vacanciesFavoritesRepository: VacanciesFavoritesRepository
) : ViewModel() {
    val favoritesIds: StateFlow<Set<String>> = vacanciesFavoritesRepository.favoritesIds

    fun toggleFavorite(vacancyId: String) = vacanciesFavoritesRepository.toggle(vacancyId)
}