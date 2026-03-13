package vacancy.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.skb_android.ui.kit.VacancyCardComponent
import com.example.skb_android.ui.theme.Spacing
import org.koin.androidx.compose.koinViewModel
import vacancy.model.listShortVacanciesInfo
import vacancy.viewModel.VacanciesTrendingViewModel

@Composable
fun VacanciesTrendingScreen(onVacancyClick: (vacancyId: String) -> Unit) {
    val vm: VacanciesTrendingViewModel = koinViewModel()
    val favoritesIds by vm.favoritesIds.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(Spacing.small),
        verticalArrangement = Arrangement.spacedBy(Spacing.medium)
    ) {
        listShortVacanciesInfo.forEach {
            item(key = it.id) {
                VacancyCardComponent(
                    shortVacancy = it,
                    onVacancyClick = { onVacancyClick(it.id) },
                    isFavorite = it.id in favoritesIds,
                    onFavoriteClick = { vm.toggleFavorite(it.id) }
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun VacanciesTrendingPreview() {
    VacanciesTrendingScreen() {}
}
