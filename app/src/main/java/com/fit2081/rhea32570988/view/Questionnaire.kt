package com.fit2081.rhea32570988.view

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fit2081.rhea32570988.R
import com.fit2081.rhea32570988.model.entity.Patient
import com.fit2081.rhea32570988.view.theme.Rhea32570988Theme
import com.fit2081.rhea32570988.viewmodel.QuestionnaireViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

class Questionnaire : ComponentActivity() {
    private val questionnaireViewModel: QuestionnaireViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Rhea32570988Theme {
                Scaffold(
                    topBar = { MyTopBar() },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    QuestionnaireScreen(questionnaireViewModel, innerPadding)
                }
            }
        }
    }
}

// Questionnaire Screen UI
@Composable
fun QuestionnaireScreen(questionnaireViewModel: QuestionnaireViewModel, innerPadding: PaddingValues) {
    // State Variables for Food Category Checkboxes
    val fruitsChk = remember { mutableStateOf(false) }
    val vegetablesChk = remember { mutableStateOf(false) }
    val grainsChk = remember { mutableStateOf(false) }
    val redMeatChk = remember { mutableStateOf(false) }
    val seafoodChk = remember { mutableStateOf(false) }
    val poultryChk = remember { mutableStateOf(false) }
    val fishChk = remember { mutableStateOf(false) }
    val eggsChk = remember { mutableStateOf(false) }
    val nutsChk = remember { mutableStateOf(false) }

    // State Variables for Persona
    val selectedPersona = remember { mutableStateOf("") }

    // State Variables for Timings
    val eatTime = remember { mutableStateOf("") }
    val sleepTime = remember { mutableStateOf("") }
    val wakeTime = remember { mutableStateOf("") }

    // Get the current user
    var user by remember { mutableStateOf<Patient?>(null) }
    var userID by remember { mutableStateOf(0) }

    // Current Context
    val context = LocalContext.current

    // Coroutine scope for launching coroutines
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        // Get the current user from the ViewModel
        user = questionnaireViewModel.getCurrentUser()
        userID = user?.id ?: 0

        // Load previously saved preferences based on userID
        if (user?.hasCompletedQuestionnaire == true) {
            val questionnaireData = questionnaireViewModel.getQuestionnaireByPatientID(userID)

            // If questionnaire data exists, populate the checkboxes and other fields
            questionnaireData?.let {
                fruitsChk.value = it.fruitsChk
                vegetablesChk.value = it.vegetablesChk
                grainsChk.value = it.grainsChk
                redMeatChk.value = it.redMeatChk
                seafoodChk.value = it.seafoodChk
                poultryChk.value = it.poultryChk
                fishChk.value = it.fishChk
                eggsChk.value = it.eggsChk
                nutsChk.value = it.nutsChk
                selectedPersona.value = it.selectedPersona
                eatTime.value = it.eatTime
                sleepTime.value = it.sleepTime
                wakeTime.value = it.wakeTime
            }
        }
    }

    // LazyColumn for scroll view
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // Food Category
        item {
            // Food Category Text
            Text(
                text = "Tick all the food categories you can eat",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            // Food Category Checkboxes
            FoodCategoryCheckboxes(
                fruitsChk, vegetablesChk, grainsChk,
                redMeatChk, seafoodChk, poultryChk,
                fishChk, eggsChk, nutsChk
            )
        }

        // Section Divider
        item {
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Persona Modals
        item {
            // Persona Modals Text
            Text(
                text = "Your Persona",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "People can be broadly classified into 6 different types based on their eating preferences. " +
                        "Click on each button below to find out the different types and select the type that best fits you!",
                fontSize = 14.sp,
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Persona Modals
            PersonaModals()
        }

        // Section Divider
        item {
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Persona Selection
        item {
            // Persona Text
            Text(
                text = "Which persona best fits you?",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Persona Dropdown
            PersonaDropdown(selectedPersona)
        }

        // Section Divider
        item {
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Timings
        item {
            // Timings Text
            Text(
                text = "Timings",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Timings
            Timings(eatTime, sleepTime, wakeTime)
        }

        // Section Divider
        item {
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Save Button
        item {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            // Check if the questionnaire has been filled in
                            if ( selectedPersona.value == "" || eatTime.value == "" || sleepTime.value == "" || wakeTime.value == "" ) {
                                Toast.makeText(context, "Please complete the Questionnaire", Toast.LENGTH_SHORT).show()
                            } else {

                                // Check if all three time values are the same
                                if (eatTime.value == sleepTime.value || eatTime.value == wakeTime.value || sleepTime.value == wakeTime.value) {
                                    Toast.makeText(context, "Eat, Sleep, and Wake times must be different", Toast.LENGTH_SHORT).show()
                                } else {

                                    // insert the questionnaire into the database
                                    questionnaireViewModel.insertQuestionnaire(
                                        patientId = userID,
                                        fruitsChk = fruitsChk.value,
                                        vegetablesChk = vegetablesChk.value,
                                        grainsChk = grainsChk.value,
                                        redMeatChk = redMeatChk.value,
                                        seafoodChk = seafoodChk.value,
                                        poultryChk = poultryChk.value,
                                        fishChk = fishChk.value,
                                        eggsChk = eggsChk.value,
                                        nutsChk = nutsChk.value,
                                        selectedPersona = selectedPersona.value,
                                        eatTime = eatTime.value,
                                        sleepTime = sleepTime.value,
                                        wakeTime = wakeTime.value
                                    )

                                    // Go to the main app - Home Screen
                                    context.startActivity(Intent(context, Navigation::class.java))
                                }
                            }
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(55.dp)
                        .align(Alignment.Center)
                ) {
                    Text("Save", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(25.dp))
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar() {
    // Create a TopAppBarState object to control the behavior of the TopAppBar.
    // rememberTopAppBarState() is a composable function that creates a TopAppBarState that is remembered across compositions.
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    // The onBackPressedDispatcher is used to handle the back button press in the app.
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    CenterAlignedTopAppBar(
        // the colors property is used to customise the appearance of the TopAppBar
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor =  MaterialTheme.colorScheme.primary,
        ),

        // Title displayed in the center of the app bar
        title = {
            Text(
                "Food Intake Questionnaire",
                maxLines = 1,
                // the Ellipsis property is used to truncate the text if it exceeds the available space
                overflow = TextOverflow.Ellipsis
            )
        },

        // Navigation icon (back button) with appropriate behaviour
        navigationIcon = {
            IconButton(onClick = {
                // onBackPressDispatcher is used to handle the back button press in the app
                // it takes the current activity out of the back stack and shows the previous activity
                onBackPressedDispatcher?.onBackPressed()
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Localised description"
                )
            }
        },

        scrollBehavior = scrollBehavior,
    )
}

@Composable
fun FoodCategoryCheckboxes(fruitsChk: MutableState<Boolean>,
                           vegetablesChk: MutableState<Boolean>,
                           grainsChk: MutableState<Boolean>,
                           redMeatChk: MutableState<Boolean>,
                           seafoodChk: MutableState<Boolean>,
                           poultryChk: MutableState<Boolean>,
                           fishChk: MutableState<Boolean>,
                           eggsChk: MutableState<Boolean>,
                           nutsChk: MutableState<Boolean>,) {
    // Checkboxes
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Column 1 Checkboxes
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = fruitsChk.value, onCheckedChange = { fruitsChk.value = it })
                Text("Fruits")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = redMeatChk.value, onCheckedChange = { redMeatChk.value = it })
                Text("Red Meat")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = fishChk.value, onCheckedChange = { fishChk.value = it })
                Text("Fish")
            }
        }

        // Column 2 Checkboxes
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = vegetablesChk.value, onCheckedChange = { vegetablesChk.value = it })
                Text("Vegetables")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = seafoodChk.value, onCheckedChange = { seafoodChk.value = it })
                Text("Seafood")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = eggsChk.value, onCheckedChange = { eggsChk.value = it })
                Text("Eggs")
            }
        }

        // Column 3 Checkboxes
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = grainsChk.value, onCheckedChange = { grainsChk.value = it })
                Text("Grains")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = poultryChk.value, onCheckedChange = { poultryChk.value = it })
                Text("Poultry")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = nutsChk.value, onCheckedChange = { nutsChk.value = it })
                Text("Nuts/Seeds")
            }
        }
    }
}

@Composable
fun PersonaModals() {
    // State to control the visibility of the AlertDialog
    var healthDevotee by remember { mutableStateOf(false) }
    var mindfulEater by remember { mutableStateOf(false) }
    var wellnessStriver by remember { mutableStateOf(false) }
    var balanceSeeker by remember { mutableStateOf(false) }
    var healthProcrastinator by remember { mutableStateOf(false) }
    var foodCarefree by remember { mutableStateOf(false) }

    // Modals
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { healthDevotee = true },
                shape = RectangleShape
            ) {
                Text("Health Devotee")
            }

            Button(
                onClick = { mindfulEater = true },
                shape = RectangleShape
            ) {
                Text("Mindful Eater")
            }

            Button(
                onClick = { wellnessStriver = true },
                shape = RectangleShape
            ) {
                Text("Wellness Striver")
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { balanceSeeker = true },
                shape = RectangleShape
            ) {
                Text("Balance Seeker")
            }

            Button(
                onClick = { healthProcrastinator = true },
                shape = RectangleShape
            ) {
                Text("Health Procrastinator")
            }

            Button(
                onClick = { foodCarefree = true },
                shape = RectangleShape
            ) {
                Text("Food Carefree")
            }
        }
    }

    // Health Devotee
    if (healthDevotee) {
        AlertDialog(
            onDismissRequest = { healthDevotee = false },
            title = { Text("Health Devotee")},
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.persona_1),
                        contentDescription = "Persona 1",
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(250.dp)
                            .padding(16.dp)
                    )
                    Text("I’m passionate about healthy eating & health plays a big part in my life. " +
                            "I use social media to follow active lifestyle personalities or get new recipes/exercise ideas. " +
                            "I may even buy superfoods or follow a particular type of diet. I like to think I am super healthy.")
                }
            },
            dismissButton = {
                Button(
                    onClick = { healthDevotee = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Dismiss")
                }
            },
            confirmButton = {}
        )
    }

    // Mindful Eater
    if (mindfulEater) {
        AlertDialog(
            onDismissRequest = { mindfulEater = false },
            title = { Text("Mindful Eater")},
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.persona_2),
                        contentDescription = "Persona 2",
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(250.dp)
                            .padding(16.dp)
                    )
                    Text("I’m health-conscious and being healthy and eating healthy is important to me. " +
                            "Although health means different things to different people, " +
                            "I make conscious lifestyle decisions about eating based on what I believe healthy means. " +
                            "I look for new recipes and healthy eating information on social media.")
                }
            },
            dismissButton = {
                Button(
                    onClick = { mindfulEater = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Dismiss")
                }
            },
            confirmButton = {}
        )
    }

    // Wellness Striver
    if (wellnessStriver) {
        AlertDialog(
            onDismissRequest = { wellnessStriver = false },
            title = { Text("Wellness Striver")},
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.persona_3),
                        contentDescription = "Persona 3",
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(250.dp)
                            .padding(16.dp)
                    )
                    Text("I aspire to be healthy (but struggle sometimes). Healthy eating is hard work! " +
                            "I’ve tried to improve my diet, but always find things that make it difficult to stick with the changes. " +
                            "Sometimes I notice recipe ideas or healthy eating hacks, and if it seems easy enough, I’ll give it a go.")
                }
            },
            dismissButton = {
                Button(
                    onClick = { wellnessStriver = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Dismiss")
                }
            },
            confirmButton = {}
        )
    }

    // Balance Seeker
    if (balanceSeeker) {
        AlertDialog(
            onDismissRequest = { balanceSeeker = false },
            title = { Text("Balance Seeker")},
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.persona_4),
                        contentDescription = "Persona 4",
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(250.dp)
                            .padding(16.dp)
                    )
                    Text("I try and live a balanced lifestyle, and I think that all foods are okay in moderation. " +
                            "I shouldn’t have to feel guilty about eating a piece of cake now and again. " +
                            "I get all sorts of inspiration from social media like finding out about new restaurants, " +
                            "fun recipes and sometimes healthy eating tips.")
                }
            },
            dismissButton = {
                Button(
                    onClick = { balanceSeeker = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Dismiss")
                }
            },
            confirmButton = {}
        )
    }

    // Health procrastinator
    if (healthProcrastinator) {
        AlertDialog(
            onDismissRequest = { healthProcrastinator = false },
            title = { Text("Health Procrastinator")},
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.persona_5),
                        contentDescription = "Persona 5",
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(250.dp)
                            .padding(16.dp)
                    )
                    Text("I’m contemplating healthy eating but it’s not a priority for me right now. " +
                            "I know the basics about what it means to be healthy, but it doesn’t seem relevant to me right now. " +
                            "I have taken a few steps to be healthier but I am not motivated to make it a high priority " +
                            "because I have too many other things going on in my life.")
                }
            },
            dismissButton = {
                Button(
                    onClick = { healthProcrastinator = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Dismiss")
                }
            },
            confirmButton = {}
        )
    }

    // Food Carefree
    if (foodCarefree) {
        AlertDialog(
            onDismissRequest = { foodCarefree = false },
            title = { Text("Food Carefree")},
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.persona_6),
                        contentDescription = "Persona 6",
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(250.dp)
                            .padding(16.dp)
                    )
                    Text("I’m not bothered about healthy eating. " +
                            "I don’t really see the point and I don’t think about it. " +
                            "I don’t really notice healthy eating tips or recipes and I don’t care what I eat.")
                }
            },
            dismissButton = {
                Button(
                    onClick = { foodCarefree = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Dismiss")
                }
            },
            confirmButton = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonaDropdown(selectedPersona: MutableState<String>) {
    // State variables
    var expanded by remember { mutableStateOf(false) }

    // List of the Personas
    val personaList = listOf(
        "Health Devotee",
        "Mindful Eater",
        "Wellness Striver",
        "Balance Seeker",
        "Health Procrastinator",
        "Food Carefree"
    )

    // Dropdown
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedPersona.value,
            onValueChange = { },
            label = { Text("Select Option") },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = OutlinedTextFieldDefaults.colors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            personaList.forEach { persona ->
                DropdownMenuItem(
                    text = { Text(persona) },
                    onClick = {
                        selectedPersona.value = persona
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun Timings(eatTime: MutableState<String>, sleepTime: MutableState<String>, wakeTime: MutableState<String>) {
    // Call the function to return a TimePickerDialog
    val eatTimePickerDialog = TimePickerFun(eatTime)
    val sleepTimePickerDialog = TimePickerFun(sleepTime)
    val wakeTimePickerDialog = TimePickerFun(wakeTime)

    Column(
        verticalArrangement = Arrangement.Center
    ) {
        // Question 1
        Text(
            text = "What time do you eat your biggest meal?",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Time Picker 1
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        )  {
            Button(onClick = { eatTimePickerDialog.show() }) {
                Text(text = "Open Time Picker")
            }
            Text(
                text = "Selected Time: ${eatTime.value}",
                fontSize = 14.sp)
        }

        // Question 2
        Text(
            text = "What time do you go to sleep?",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Time Picker 2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        )  {
            Button(onClick = { sleepTimePickerDialog.show() }) {
                Text(text = "Open Time Picker")
            }
            Text(
                text = "Selected Time: ${sleepTime.value}",
                fontSize = 14.sp)
        }

        // Question 3
        Text(
            text = "What time do you wake up?",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Time Picker 3
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        )  {
            Button(onClick = { wakeTimePickerDialog.show() }) {
                Text(text = "Open Time Picker")
            }
            Text(
                text = "Selected Time: ${wakeTime.value}",
                fontSize = 14.sp)
        }
    }
}

@Composable
fun TimePickerFun(mTime: MutableState<String>): TimePickerDialog {
    // Get the current context
    val mContext = LocalContext.current

    // Get a calendar instance
    val mCalendar = Calendar.getInstance()

    // Get the current hour and minute from the calendar
    val mHour = mCalendar.get(Calendar.HOUR_OF_DAY)
    val mMinute = mCalendar.get(Calendar.MINUTE)

    // Set the calendar's time to the current time
    mCalendar.time = Calendar.getInstance().time

    // Return a TimePickerDialog
    return TimePickerDialog(
        // Context
        // Listener to be invoked when the time is set
        // Initial hour and minute
        // Whether to use 24-hour format

        mContext,
        { _, mHour: Int, mMinute: Int ->
            mTime.value = String.format("%02d:%02d", mHour, mMinute)
        }, mHour, mMinute, false
    )
}