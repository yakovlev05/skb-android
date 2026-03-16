package com.example.skb_android.vacancy.screen


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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.skb_android.ui.kit.EmployerLogo
import com.example.skb_android.ui.kit.MiniTextCard
import com.example.skb_android.ui.kit.MyCircularLoader
import com.example.skb_android.ui.kit.OpenOnHhButton
import com.example.skb_android.ui.theme.Spacing
import com.example.skb_android.util.toReadableDate
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import com.example.skb_android.vacancy.model.FullVacancyModel
import com.example.skb_android.vacancy.model.listFullVacanciesInfo
import com.example.skb_android.vacancy.viewModel.VacancyFullScreenViewModel

@Composable
fun VacancyFullScreen(vacancyId: String) {
    val vm: VacancyFullScreenViewModel = koinViewModel { parametersOf(vacancyId) }
    val state by vm.state.collectAsState()

    if (!LocalInspectionMode.current && state.isLoading || state.fullVacancy == null) {
        MyCircularLoader()
    } else {
        VacancyInfo(state.fullVacancy!!)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VacancyInfo(fullVacancy: FullVacancyModel) {
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
        SalaryInfo(fullVacancy)
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

@Composable
private fun SalaryInfo(fullVacancy: FullVacancyModel) {
    val isExists = fullVacancy.salaryFrom != null
            && fullVacancy.salaryTo != null
            && fullVacancy.salaryModeName != null
    if (isExists) {
        Text(
            text = "${fullVacancy.salaryFrom} - ${fullVacancy.salaryTo} ${fullVacancy.salaryModeName.lowercase()}",
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
private fun AdditionalInfo(fullVacancy: FullVacancyModel) {
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
private fun EmployerCard(fullVacancy: FullVacancyModel) {
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
private fun VacancyDescription(fullVacancy: FullVacancyModel) {
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
    VacancyInfo(listFullVacanciesInfo.last())
}
