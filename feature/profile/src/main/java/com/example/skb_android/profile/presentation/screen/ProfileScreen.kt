package com.example.skb_android.profile.presentation.screen

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Environment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.skb_android.profile.presentation.model.ProfileUiModel
import com.example.skb_android.profile.presentation.viewModel.ProfileViewModel
import com.example.skb_android.uikit.components.ProfileAvatar
import com.example.skb_android.uikit.theme.Spacing
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import androidx.core.net.toUri

@Composable
fun ProfileScreen() {
    val vm = koinViewModel<ProfileViewModel>()
    val profileState by vm.profileState.collectAsStateWithLifecycle()

    ProfileScreenContent(
        profileState = profileState,
        onEditClick = vm::onEditClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreenContent(
    profileState: ProfileUiModel,
    onEditClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Профиль") },
                actions = {
                    IconButton(
                        onClick = onEditClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        ProfileInfo(
            profileState = profileState,
            snackbarHostState = snackbarHostState,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun ProfileInfo(
    profileState: ProfileUiModel,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.small, Alignment.CenterVertically)
    ) {
        ProfileAvatar(avatarURI = profileState.avatarURI)
        Text(
            text = profileState.fullName,
            style = MaterialTheme.typography.titleMedium
        )
        DownloadResume(
            resumeURL = profileState.resumeURL,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
private fun DownloadResume(
    resumeURL: String?,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val myDownloadId = remember { mutableLongStateOf(-1L) }

    DisposableEffect(Unit) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context, intent: Intent) {
                val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
                if (downloadId != myDownloadId.longValue) return

                val dm = ctx.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

                val cursor = dm.query(DownloadManager.Query().setFilterById(downloadId))
                val status = if (cursor.moveToFirst()) {
                    cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                } else null
                cursor.close()
                if (status != DownloadManager.STATUS_SUCCESSFUL) return

                val uri = dm.getUriForDownloadedFile(downloadId) ?: return
                val mime = dm.getMimeTypeForDownloadedFile(downloadId) ?: "application/pdf"

                val openIntent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, mime)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                ctx.startActivity(openIntent)
            }
        }

        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
        } else {
            ContextCompat.registerReceiver(
                context,
                receiver,
                filter,
                ContextCompat.RECEIVER_EXPORTED
            )
        }

        onDispose { context.unregisterReceiver(receiver) }
    }

    Button(
        onClick = {
            if (resumeURL != null) {
                myDownloadId.longValue = enqueueResumeDownload(context, resumeURL)
            } else {
                scope.launch { snackbarHostState.showSnackbar("⚠ Сначала заполните профиль!") }
            }
        }
    ) {
        Text("Скачать резюме")
    }
}

private fun enqueueResumeDownload(context: Context, url: String): Long {
    val request = DownloadManager.Request(url.toUri()).apply {
        setTitle("Резюме")
        setDescription("Загрузка резюме...")
        setMimeType("application/pdf")
        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "resume_skb_android.pdf")
    }
    val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    return dm.enqueue(request)  // ← уникальный ID этой загрузки
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    ProfileScreenContent(
        profileState = ProfileUiModel(
            fullName = "Алексей",
            avatarURI = null,
            resumeURL = null
        ),
        onEditClick = {}
    )
}