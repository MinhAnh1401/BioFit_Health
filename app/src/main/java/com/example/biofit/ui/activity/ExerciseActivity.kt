package com.example.biofit.ui.activity

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.biofit.R
import com.example.biofit.navigation.MainActivity
import com.example.biofit.ui.components.ExerciseItem
import com.example.biofit.ui.components.TopBar
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.theme.BioFitTheme

class ExerciseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                ExerciseScreen()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun ExerciseScreen() {
    val context = LocalContext.current
    val activity = context as? Activity

    val standardPadding = getStandardPadding().first
    val modifier = getStandardPadding().second

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding(),
                    start = standardPadding,
                    end = standardPadding,
                ),
            verticalArrangement = Arrangement.spacedBy(standardPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(
                onBackClick = { activity?.finish() },
                title = stringResource(R.string.exercise),
                middleButton = null,
                rightButton = {
                    IconButton(
                        onClick = {
                            activity?.let {
                                val intent = Intent(it, CreateAndUpdateExerciseActivity::class.java)
                                intent.putExtra("EXERCISE", "")
                                it.startActivity(intent)
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_plus),
                            contentDescription = "Add button",
                            modifier = Modifier.size(standardPadding * 1.5f),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                standardPadding = standardPadding
            )

            ExerciseContent(
                standardPadding = standardPadding,
                modifier = modifier
            )
        }
    }
}

@Composable
fun ExerciseContent(
    standardPadding: Dp,
    modifier: Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity

    var search by rememberSaveable { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(standardPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            modifier = modifier,
            textStyle = MaterialTheme.typography.bodySmall,
            placeholder = {
                Text(
                    text = stringResource(R.string.search),
                    style = MaterialTheme.typography.bodySmall
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = { TODO() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { /*TODO*/ }
            ),
            singleLine = true,
            shape = MaterialTheme.shapes.large,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                unfocusedBorderColor = Color.Transparent
            )
        )

        LazyColumn {
            val groupedExercises = exerciseList
                .sorted()
                .groupBy { it.first().uppercaseChar() }

            groupedExercises.forEach { (letter, exercises) ->
                item {
                    Text(
                        text = letter.toString(),
                        modifier = modifier.padding(top = standardPadding * 2),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }

                items(exercises) { exercise ->
                    ExerciseItem(
                        exercise = exercise,
                        onClick = {
                            activity?.let {
                                val intent = Intent(it, CreateAndUpdateExerciseActivity::class.java)
                                intent.putExtra("EXERCISE", exercise)
                                it.startActivity(intent)
                            }
                        },
                        standardPadding = standardPadding,
                        modifier = modifier
                    )
                }
            }

            item {
                Spacer(
                    modifier = Modifier.padding(
                        bottom = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateBottomPadding() * 2
                                + standardPadding
                    )
                )
            }
        }
    }
}

val exerciseList = listOf(
    "Push-ups",
    "Pull-ups",
    "Squats",
    "Lunges",
    "Plank",
    "Deadlifts",
    "Bench Press",
    "Overhead Press",
    "Bicep Curls",
    "Triceps Dips",
    "Jump Rope",
    "Burpees",
    "Mountain Climbers",
    "Sit-ups",
    "Crunches",
    "Russian Twists",
    "Leg Raises",
    "Jump Squats",
    "Box Jumps",
    "Calf Raises",
    "Kettlebell Swings",
    "Medicine Ball Slams",
    "Dumbbell Rows",
    "Lat Pulldown",
    "Face Pulls",
    "Side Plank",
    "Flutter Kicks",
    "Bicycle Crunches",
    "Superman Exercise",
    "Reverse Crunches",
    "Glute Bridges",
    "Hip Thrusts",
    "Wall Sit",
    "Farmers Walk",
    "Arnold Press",
    "Seated Cable Rows",
    "Chest Fly",
    "Incline Bench Press",
    "Hamstring Curls",
    "Step-ups",
    "Bear Crawls",
    "Skater Jumps",
    "High Knees",
    "Toe Taps",
    "Battle Ropes",
    "Sled Push",
    "Turkish Get-Up",
    "Hanging Leg Raises",
    "Hollow Body Hold",
    "Jumping Jacks"
)

@Preview(
    device = "id:pixel",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    locale = "vi"
)
@Composable
private fun ExerciseScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        ExerciseScreen()
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun ExerciseScreenPreviewInLargePhone() {
    BioFitTheme {
        ExerciseScreen()
    }
}

@Preview(
    device = "spec:parent=Nexus 10,orientation=portrait",
    locale = "vi",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun ExerciseScreenPreviewInTablet() {
    BioFitTheme {
        ExerciseScreen()
    }
}

@Preview(
    device = "spec:parent=pixel,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    locale = "vi"
)
@Composable
private fun ExerciseScreenLandscapeDarkModePreviewInSmallPhone() {
    BioFitTheme {
        ExerciseScreen()
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun ExerciseScreenLandscapePreviewInLargePhone() {
    BioFitTheme {
        ExerciseScreen()
    }
}

@Preview(
    device = "spec:parent=Nexus 10",
    locale = "vi",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun ExerciseScreenLandscapePreviewInTablet() {
    BioFitTheme {
        ExerciseScreen()
    }
}