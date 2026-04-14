package com.example.skb_android.vacancy.impl.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.skb_android.uikit.components.EmployerLogo
import com.example.skb_android.uikit.components.OpenOnHhButton
import com.example.skb_android.uikit.theme.Spacing
import com.example.skb_android.core.util.toReadableDate
import com.example.skb_android.vacancy.impl.presentation.model.ShortVacancyUiModel
import com.example.skb_android.vacancy.impl.presentation.model.listShortVacanciesInfo

@Composable
fun VacancyCardComponent(
    shortVacancy: ShortVacancyUiModel,
    onVacancyClick: () -> Unit,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        onClick = onVacancyClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.small)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "id: ${shortVacancy.id}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = shortVacancy.name,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    SalaryInfo(shortVacancy.prettySalary)
                }
                IconButton(
                    onClick = onFavoriteClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Избранное",
                        tint = if (isFavorite) Color.Red else Color.Black
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Spacing.small)
                    .padding(end = Spacing.small),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = shortVacancy.employerName)
                    Text(
                        text = shortVacancy.areaName,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                EmployerLogo(shortVacancy.employerLogoUrl, Spacing.large)
            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = toReadableDate(shortVacancy.publishedAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }


            OpenOnHhButton(
                url = shortVacancy.vacancyUrl,
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
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun VacancyCardPreview() {
    VacancyCardComponent(listShortVacanciesInfo[0], {}, true) {}
}