package com.example.skb_android.vacancy.impl.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.skb_android.vacancy.impl.presentation.component.VacancyCardComponent
import com.example.skb_android.uikit.components.ErrorScreen
import com.example.skb_android.uikit.components.MyCircularLoader
import com.example.skb_android.uikit.theme.Spacing
import com.example.skb_android.vacancy.impl.presentation.model.ShortVacancyUiModel
import com.example.skb_android.vacancy.impl.presentation.model.VacanciesSearchQueryState
import com.example.skb_android.vacancy.impl.presentation.model.VacanciesTrendingState
import com.example.skb_android.vacancy.impl.presentation.model.VacancyExperience
import com.example.skb_android.vacancy.impl.presentation.model.listShortVacanciesInfo
import com.example.skb_android.vacancy.impl.presentation.viewModel.VacanciesTrendingViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun VacanciesTrendingScreen() {
    val vm = koinViewModel<VacanciesTrendingViewModel>()
    val vacanciesState by vm.vacanciesState.collectAsStateWithLifecycle()
    val searchState by vm.searchState.collectAsStateWithLifecycle()
    val hasActiveFilters by vm.hasActiveFilters.collectAsStateWithLifecycle()

    VacanciesTrendingContent(
        vacanciesState = vacanciesState.state,
        searchState = searchState,
        hasActiveFilters = hasActiveFilters,
        onVacancyClick = vm::onVacancyClick,
        onFavoriteClick = vm::onFavoriteClick,
        onSearchClick = vm::onSearchClick,
        onSearchQueryInput = vm::onSearchQueryInput,
        onSelectExperience = vm::onSelectExperience,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VacanciesTrendingContent(
    vacanciesState: VacanciesTrendingState.State,
    searchState: VacanciesSearchQueryState,
    hasActiveFilters: Boolean,
    onVacancyClick: (vacancy: ShortVacancyUiModel) -> Unit,
    onFavoriteClick: (vacancy: ShortVacancyUiModel) -> Unit,
    onSearchClick: () -> Unit,
    onSearchQueryInput: (text: String) -> Unit,
    onSelectExperience: (vacancyExperience: VacancyExperience) -> Unit,
) {

    val globalModifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(Spacing.small)

    Column {
        TopAppBar(
            title = { Text("Горячее") },
            actions = {
                IconButton(
                    onClick = {}
                ) {
                    BadgedBox(
                        badge = {
                            if (hasActiveFilters) {
                                Badge()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu"
                        )
                    }
                }
            }
        )

        when (vacanciesState) {
            is VacanciesTrendingState.State.Loading -> VacanciesTrendingOtherState(
                modifier = globalModifier,
                searchState = searchState,
                onSearchQueryInput = onSearchQueryInput,
                onSelectExperience = onSelectExperience,
                onSearchClick = onSearchClick
            ) {
                MyCircularLoader()
            }

            is VacanciesTrendingState.State.Error -> VacanciesTrendingOtherState(
                modifier = globalModifier,
                searchState = searchState,
                onSearchQueryInput = onSearchQueryInput,
                onSelectExperience = onSelectExperience,
                onSearchClick = onSearchClick
            ) {
                ErrorScreen(vacanciesState.message)
            }

            is VacanciesTrendingState.State.Success -> VacanciesTrendingSuccessState(
                modifier = globalModifier,
                vacancies = vacanciesState.vacancies,
                searchState = searchState,
                onVacancyClick = onVacancyClick,
                onFavoriteClick = onFavoriteClick,
                onSearchClick = onSearchClick,
                onSearchQueryInput = onSearchQueryInput,
                onSelectExperience = onSelectExperience,
            )
        }
    }
}

@Composable
private fun VacanciesTrendingSuccessState(
    modifier: Modifier,
    vacancies: List<ShortVacancyUiModel>,
    searchState: VacanciesSearchQueryState,
    onVacancyClick: (vacancy: ShortVacancyUiModel) -> Unit,
    onFavoriteClick: (vacancy: ShortVacancyUiModel) -> Unit,
    onSearchClick: () -> Unit,
    onSearchQueryInput: (text: String) -> Unit,
    onSelectExperience: (vacancyExperience: VacancyExperience) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.medium)
    ) {
        item {
            VacanciesSearch(
                searchState = searchState,
                onSearchQueryInput = onSearchQueryInput,
                onSelectExperience = onSelectExperience,
                onSearchClick = onSearchClick
            )
        }

        items(items = vacancies, key = { it.id }) { vacancy ->
            VacancyCardComponent(
                shortVacancy = vacancy,
                onVacancyClick = { onVacancyClick(vacancy) },
                isFavorite = vacancy.isFavorite,
                onFavoriteClick = { onFavoriteClick(vacancy) }
            )
        }
    }
}

@Composable
private fun VacanciesTrendingOtherState(
    modifier: Modifier,
    searchState: VacanciesSearchQueryState,
    onSearchQueryInput: (text: String) -> Unit,
    onSelectExperience: (vacancyExperience: VacancyExperience) -> Unit,
    onSearchClick: () -> Unit,
    customComponent: @Composable () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        VacanciesSearch(
            searchState = searchState,
            onSearchQueryInput = onSearchQueryInput,
            onSelectExperience = onSelectExperience,
            onSearchClick = onSearchClick
        )

        customComponent()
    }
}

@Composable
private fun VacanciesSearch(
    searchState: VacanciesSearchQueryState,
    onSearchQueryInput: (text: String) -> Unit,
    onSelectExperience: (vacancyExperience: VacancyExperience) -> Unit,
    onSearchClick: () -> Unit,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchState.text,
                onValueChange = onSearchQueryInput,
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = Spacing.medium)
            )

            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                onClick = onSearchClick
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Поиск"
                )
            }
        }

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(Spacing.small)
        ) {
            VacancyExperience.entries.forEach {
                FilterChip(
                    selected = searchState.experience?.title == it.title,
                    onClick = { onSelectExperience(it) },
                    label = { Text(it.title) }
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Success")
@Composable
private fun PreviewSuccess() {
    VacanciesTrendingContent(
        vacanciesState = VacanciesTrendingState.State.Success(listShortVacanciesInfo),
        searchState = VacanciesSearchQueryState("Kotlin", VacancyExperience.NO_EXPERIENCE),
        hasActiveFilters = true,
        onVacancyClick = {},
        onFavoriteClick = {},
        onSearchQueryInput = {},
        onSelectExperience = {},
        onSearchClick = {}
    )
}
