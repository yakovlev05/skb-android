package com.example.skb_android.vacancy.presentation.screen


import android.text.Html
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.skb_android.R
import com.example.skb_android.ui.kit.EmployerLogo
import com.example.skb_android.ui.kit.ErrorScreen
import com.example.skb_android.ui.kit.MiniTextCard
import com.example.skb_android.ui.kit.MyCircularLoader
import com.example.skb_android.ui.kit.OpenOnHhButton
import com.example.skb_android.ui.theme.Spacing
import com.example.skb_android.util.toReadableDate
import com.example.skb_android.vacancy.presentation.model.VacanciesTrendingState
import com.example.skb_android.vacancy.presentation.model.VacancyFullState
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import com.example.skb_android.vacancy.presentation.model.VacancyFullUiModel
import com.example.skb_android.vacancy.presentation.model.listFullVacanciesInfo
import com.example.skb_android.vacancy.presentation.viewModel.VacancyFullScreenViewModel

@Composable
fun VacancyFullScreen(vacancyId: String) {
    val vm = koinViewModel<VacancyFullScreenViewModel>(key = vacancyId) { parametersOf(vacancyId) }
    val state by vm.viewState.collectAsStateWithLifecycle()

    VacancyFullScreenContent(
        state = state.state,
        onClickBack = vm::onClickBack
    )
}

@Composable
private fun VacancyFullScreenContent(
    state: VacancyFullState.State,
    onClickBack: () -> Unit
) {
    when (state) {
        is VacancyFullState.State.Loading -> MyCircularLoader()

        is VacancyFullState.State.Error -> ErrorScreen(state.message)

        is VacancyFullState.State.Success -> VacancyInfo(
            fullVacancy = state.vacancy,
            onClickBack = onClickBack
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VacancyInfo(
    fullVacancy: VacancyFullUiModel,
    onClickBack: () -> Unit
) {
    Column {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(
                    onClick = onClickBack
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.material_icon_arrow_back_ios),
                        contentDescription = "Назад"
                    )
                }
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = Spacing.medium)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = fullVacancy.name,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )
            SalaryInfo(fullVacancy.prettySalary)
            AdditionalInfo(fullVacancy)
            Text(
                modifier = Modifier.padding(top = Spacing.small),
                text = "Опубликована ${toReadableDate(fullVacancy.publishedAt)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            EmployerCard(fullVacancy)
            VacancyDescription(fullVacancy)
            Skills(fullVacancy.skills)
            OpenOnHhButton(
                url = fullVacancy.vacancyUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Spacing.small)
            )
        }
    }
}

@Composable
private fun SalaryInfo(salary: String?) {
    if (salary != null) {
        Text(
            text = salary,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            ),
        )
    } else {
        Text(
            text = "з/п не указана",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun AdditionalInfo(fullVacancy: VacancyFullUiModel) {
    val text = listOf(fullVacancy.areaName, fullVacancy.experienceName)
        .filter { it != null }
        .joinToString(" - ")

    Text(
        modifier = Modifier.padding(top = Spacing.small),
        text = text,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun EmployerCard(fullVacancy: VacancyFullUiModel) {
    Card(
        modifier = Modifier.padding(top = Spacing.medium)
    ) {
        Column(
            modifier = Modifier
                .padding(Spacing.small)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = fullVacancy.employerName,
                    style = MaterialTheme.typography.bodyLarge
                )
                EmployerLogo(
                    url = fullVacancy.employerLogoUrl,
                    size = Spacing.xLarge
                )
            }

            if (fullVacancy.employerUrl != null) {
                OpenOnHhButton(
                    url = fullVacancy.employerUrl,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

    }
}

@Composable
private fun VacancyDescription(fullVacancy: VacancyFullUiModel) {
    val spanned = Html.fromHtml(fullVacancy.description, Html.FROM_HTML_MODE_LEGACY)
    Card(
        modifier = Modifier.padding(top = Spacing.medium)
    ) {
        Column(
            modifier = Modifier.padding(Spacing.small)
        ) {
            Text(
                modifier = Modifier.padding(bottom = Spacing.medium),
                text = "Описание вакансии:",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = spanned.toString()
            )
        }
    }

}

@Composable
private fun Skills(skills: List<String>) {
    if (skills.isNotEmpty()) {
        FlowRow(
            modifier = Modifier.padding(top = Spacing.medium),
            horizontalArrangement = Arrangement.spacedBy(Spacing.xSmall),
            verticalArrangement = Arrangement.spacedBy(Spacing.xSmall)
        ) {
            skills.forEach {
                MiniTextCard(it)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VacancyFullPreview() {
    VacancyFullScreenContent(
        state = VacancyFullState.State.Success(listFullVacanciesInfo.last()),
        onClickBack = {}
    )
}
