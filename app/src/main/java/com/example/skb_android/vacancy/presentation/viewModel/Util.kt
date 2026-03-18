package com.example.skb_android.vacancy.presentation.viewModel

fun toPrettySalary(salaryFrom: Int?, salaryTo: Int?, salaryMode: String?): String? {
    if (salaryFrom == null || salaryTo == null || salaryMode == null) return null

    return "$salaryFrom - $salaryTo ${salaryMode.lowercase()}"
}
