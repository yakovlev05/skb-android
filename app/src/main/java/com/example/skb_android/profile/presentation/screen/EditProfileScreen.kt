package com.example.skb_android.profile.presentation.screen

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.skb_android.R
import com.example.skb_android.profile.presentation.model.EditProfileState
import com.example.skb_android.profile.presentation.viewModel.EditProfileViewModel
import com.example.skb_android.ui.kit.ProfileAvatar
import com.example.skb_android.ui.theme.Spacing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.io.File

@Composable
fun EditProfileScreen() {
    val vm = koinViewModel<EditProfileViewModel>()
    val editState by vm.editProfileState.collectAsStateWithLifecycle()

    EditProfileScreenContent(
        editState = editState,
        onBackClick = vm::onBackClick,
        onSaveClick = vm::onSaveClick,
        onInputFullName = vm::onInputFullName,
        onInputResumeUrl = vm::onInputResumeUrl,
        onCloseAlertChooseSource = vm::onCloseAlertChooseSource,
        onImageSelected = vm::onImageSelected,
        onClickAvatar = vm::onClickAvatar,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditProfileScreenContent(
    editState: EditProfileState,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onInputFullName: (String) -> Unit,
    onInputResumeUrl: (String) -> Unit,
    onCloseAlertChooseSource: () -> Unit,
    onImageSelected: (Uri?) -> Unit,
    onClickAvatar: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Редактирование") },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.material_icon_arrow_back_ios),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onSaveClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Save"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        EditProfileData(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = Spacing.medium),
            editState = editState,
            onInputFullName = onInputFullName,
            onInputResumeUrl = onInputResumeUrl,
            onCloseAlertChooseSource = onCloseAlertChooseSource,
            onImageSelected = onImageSelected,
            onClickAvatar = onClickAvatar,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
private fun EditProfileData(
    modifier: Modifier,
    editState: EditProfileState,
    onInputFullName: (String) -> Unit,
    onInputResumeUrl: (String) -> Unit,
    onCloseAlertChooseSource: () -> Unit,
    onImageSelected: (Uri?) -> Unit,
    onClickAvatar: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var cameraTempUri by remember { mutableStateOf<Uri?>(null) }

    val pickImage = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri -> onImageSelected(uri) }

    val takePicture = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success -> if (success) onImageSelected(cameraTempUri) }

    val requestCameraPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            cameraTempUri = createCameraUri(context)
            cameraTempUri?.let { takePicture.launch(it) }
        } else {
            scope.launch { snackbarHostState.showSnackbar("Необходим доступ к камере") }
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.small, Alignment.CenterVertically)
    ) {
        ProfileAvatar(
            avatarURI = editState.avatarURI,
            onClick = onClickAvatar
        )

        FullNameTextField(
            fullName = editState.fullName,
            onInputFullName = onInputFullName
        )

        ResumeUrlTextField(
            resumeURL = editState.resumeURL,
            onInputResumeUrl = onInputResumeUrl,
        )

        if (editState.isShowAlertChooseSource) {
            AlertChooseImageSource(
                onCloseAlertChooseSource = onCloseAlertChooseSource,
                onCameraClick = {
                    onCloseAlertChooseSource()
                    requestCameraPermission.launch(Manifest.permission.CAMERA)
                },
                onGalleryClick = {
                    onCloseAlertChooseSource()
                    pickImage.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            )
        }
    }
}

private fun createCameraUri(context: Context): Uri {
    val tmpFile = File.createTempFile("tmp_camera_avatar", ".jpg", context.cacheDir)
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", tmpFile)
}

@Composable
private fun FullNameTextField(
    fullName: String,
    onInputFullName: (String) -> Unit
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = fullName,
        onValueChange = { onInputFullName(it) },
        singleLine = true,
        label = { Text("ФИО") }
    )
}

@Composable
private fun ResumeUrlTextField(
    resumeURL: String,
    onInputResumeUrl: (String) -> Unit
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = resumeURL,
        onValueChange = { onInputResumeUrl(it) },
        singleLine = true,
        label = { Text("Резюме") }
    )
}

@Composable
private fun AlertChooseImageSource(
    onCloseAlertChooseSource: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCloseAlertChooseSource,
        title = { Text("Изменение аватара") },
        text = {
            Column {
                ListItem(
                    headlineContent = { Text("Камера") },
                    modifier = Modifier
                        .clickable { onCameraClick() }
                )
                ListItem(
                    headlineContent = { Text("Галерея") },
                    modifier = Modifier
                        .clickable { onGalleryClick() }
                )
            }
        },
        confirmButton = { },
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    EditProfileScreenContent(
        editState = EditProfileState("Алексей", null, ""),
        onBackClick = {},
        onSaveClick = {},
        onInputFullName = {},
        onInputResumeUrl = {},
        onCloseAlertChooseSource = {},
        onImageSelected = {},
        onClickAvatar = {}
    )
}