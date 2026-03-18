package com.example.skb_android.vacancy.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.skb_android.ui.kit.ErrorScreen
import com.example.skb_android.ui.kit.MyCircularLoader
import com.example.skb_android.ui.kit.VacancyCardComponent
import com.example.skb_android.ui.theme.Spacing
import com.example.skb_android.vacancy.presentation.model.ShortVacancyUiModel
import com.example.skb_android.vacancy.presentation.model.VacanciesTrendingState
import com.example.skb_android.vacancy.presentation.model.listShortVacanciesInfo
import com.example.skb_android.vacancy.presentation.viewModel.VacanciesTrendingViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun VacanciesTrendingScreen() {
    val vm = koinViewModel<VacanciesTrendingViewModel>()
    val state by vm.viewState.collectAsStateWithLifecycle()

    VacanciesTrendingContent(
        state = state.state,
        onVacancyClick = vm::onVacancyClick,
        onFavoriteClick = vm::onFavoriteClick
    )
}

@Composable
private fun VacanciesTrendingContent(
    state: VacanciesTrendingState.State,
    onVacancyClick: (vacancy: ShortVacancyUiModel) -> Unit,
    onFavoriteClick: (vacancy: ShortVacancyUiModel) -> Unit
) {
    when (state) {
        is VacanciesTrendingState.State.Loading -> MyCircularLoader()

        is VacanciesTrendingState.State.Error -> ErrorScreen(state.message)

        is VacanciesTrendingState.State.Success -> LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(Spacing.small),
            verticalArrangement = Arrangement.spacedBy(Spacing.medium)
        ) {
            items(items = state.vacancies, key = { it.id }) { vacancy ->
                VacancyCardComponent(
                    shortVacancy = vacancy,
                    onVacancyClick = { onVacancyClick(vacancy) },
                    isFavorite = vacancy.isFavorite,
                    onFavoriteClick = { onFavoriteClick(vacancy) }
                )
            }
        }
    }
}


@Preview(showBackground = true, name = "Success")
@Composable
private fun PreviewSuccess() {
    VacanciesTrendingContent(
        state = VacanciesTrendingState.State.Success(listShortVacanciesInfo),
        onVacancyClick = {},
        onFavoriteClick = {}
    )
}
