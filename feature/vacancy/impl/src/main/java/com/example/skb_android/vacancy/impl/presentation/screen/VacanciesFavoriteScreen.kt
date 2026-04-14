package com.example.skb_android.vacancy.impl.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.skb_android.vacancy.impl.presentation.component.VacancyCardComponent
import com.example.skb_android.uikit.components.ErrorScreen
import com.example.skb_android.uikit.components.MyCircularLoader
import com.example.skb_android.uikit.theme.Spacing
import com.example.skb_android.vacancy.impl.presentation.model.ShortVacancyUiModel
import com.example.skb_android.vacancy.impl.presentation.model.VacanciesFavoriteState
import com.example.skb_android.vacancy.impl.presentation.model.listShortVacanciesInfo
import com.example.skb_android.vacancy.impl.presentation.viewModel.VacanciesFavoriteViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun VacanciesFavoriteScreen() {
    val vm = koinViewModel<VacanciesFavoriteViewModel>()
    val vacanciesState by vm.vacanciesState.collectAsStateWithLifecycle()

    VacanciesFavoriteContent(
        vacanciesState = vacanciesState.state,
        onVacancyClick = vm::onVacancyClick,
        onFavoriteClick = vm::onFavoriteClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VacanciesFavoriteContent(
    vacanciesState: VacanciesFavoriteState.State,
    onVacancyClick: (vacancy: ShortVacancyUiModel) -> Unit,
    onFavoriteClick: (vacancy: ShortVacancyUiModel) -> Unit,
) {
    Column {
        TopAppBar(
            title = { Text("Избранное") }
        )

        when (vacanciesState) {
            is VacanciesFavoriteState.State.Loading -> MyCircularLoader()
            is VacanciesFavoriteState.State.Error -> ErrorScreen(vacanciesState.message)
            is VacanciesFavoriteState.State.Success -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(Spacing.small),
                verticalArrangement = Arrangement.spacedBy(Spacing.medium)
            ) {
                items(items = vacanciesState.vacancies, key = { it.id }) { vacancy ->
                    VacancyCardComponent(
                        shortVacancy = vacancy,
                        onVacancyClick = { onVacancyClick(vacancy) },
                        isFavorite = true,
                        onFavoriteClick = { onFavoriteClick(vacancy) }
                    )
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    VacanciesFavoriteContent(
        vacanciesState = VacanciesFavoriteState.State.Success(vacancies = listShortVacanciesInfo),
        onVacancyClick = {},
        onFavoriteClick = {}
    )
}
