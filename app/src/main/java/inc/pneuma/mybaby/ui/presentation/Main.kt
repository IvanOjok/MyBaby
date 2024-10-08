package inc.pneuma.mybaby.ui.presentation


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Output
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material.icons.rounded.FoodBank
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material.icons.rounded.PhoneCallback
import androidx.compose.material.icons.rounded.PhoneInTalk
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SportsGymnastics
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import inc.pneuma.mybaby.R
import inc.pneuma.mybaby.data.model.User
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(activity: Activity, viewModel: ClientViewModel) {
    val navController = rememberNavController()
    val isLoggedIn by viewModel.isUserLoggedIn.collectAsState()
    val loggedInUser by viewModel.loggedInUser.collectAsState()
    val startRoute = if (isLoggedIn) {
        if (loggedInUser == "User") {
            NavScreen.UserHome.route
        } else {
            NavScreen.DoctorHome.route
        }
    } else NavScreen.Login.route
    
    NavHost(navController = navController, startDestination = startRoute) {

        composable(NavScreen.Login.route) {
            SignInScreen(navController, activity, viewModel)
        }

        composable(NavScreen.Verify.route+"?phone={phone}&verificationId={verificationId}", arguments = listOf(navArgument(name = "phone"){
            type = NavType.StringType
            defaultValue = ""
        }, navArgument(name="verificationId") {
            type = NavType.StringType
            defaultValue = ""
        })) {
            val phone = it.arguments?.getString("phone")
            val verificationId = it.arguments?.getString("verificationId")
            VerifyCodeScreen(navController, activity, viewModel, phone?:"", verificationId?:"")
        }

        composable(NavScreen.Register.route+"?phone={phone}", arguments= listOf(navArgument("phone") {
            type = NavType.StringType
            defaultValue = ""
        })) {
            val phone = it.arguments?.getString("phone")
            RegisterUserScreen(navController, activity, viewModel, phone?:"")
        }

        composable(NavScreen.UserHome.route) {
            UserHomeScreen(navController, activity, viewModel)
        }

        composable(NavScreen.DoctorHome.route) {
            DoctorHomeScreen(navController, activity, viewModel)
        }

        composable(NavScreen.Maps.route) {
            UserMapActivity(navController, activity, viewModel)
        }

        composable(NavScreen.GetCalls.route) {
            GetCallsScreen(navController, activity, viewModel)
        }

        composable(NavScreen.GetLocations.route) {
            GetLocationScreen(navController, activity, viewModel)
        }

        composable(NavScreen.ViewLocations.route+"?lat={lat}&long={long}", arguments= listOf(navArgument("lat") {
            type = NavType.FloatType
            defaultValue = 0.0
        },  navArgument(name="long") {
            type = NavType.FloatType
            defaultValue = 0.0
        })) {
            val lat = it.arguments?.getFloat("lat") ?: 0.0f
            val long = it.arguments?.getFloat("long") ?: 32.0f
            MapDetailScreen(navController, activity, viewModel, lat, long)
        }

        composable(NavScreen.AddNutrition.route) {
            AddNutritionInfo(navController, activity, viewModel)
        }

        composable(NavScreen.GetNutrition.route) {
            GetNutritionInfo(navController, activity, viewModel)
        }

        composable(NavScreen.AddExercise.route) {
            AddExerciseInfo(navController, activity, viewModel)
        }

        composable(NavScreen.GetExercise.route) {
            GetExerciseInfo(navController, activity, viewModel)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(navController: NavController, activity: Activity, viewModel: ClientViewModel) {
    val loginState by viewModel.loginState.collectAsState()
    var userPhone by rememberSaveable { mutableStateOf("") }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    Log.d("Login State 1", "${loginState.isRunning}")
    Log.d("Login State 2", "${loginState.isComplete}")
    Log.d("Login State 3", "${loginState.error}")
    Log.d("Login State 4", loginState.verificationId)
//    if (loginState.isRunning) {
//        Box(modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Transparent), contentAlignment = Alignment.Center) {
//            CircularProgressIndicator(modifier = Modifier
//                .width(70.dp)
//                .background(color = Color.Transparent), color = MaterialTheme.colorScheme.primary)
//        }
//
//    } else
        if (loginState.isComplete) {
        navController.navigate(NavScreen.Verify.route+"?phone=$userPhone&verificationId=${loginState.verificationId}")
    } else if (!loginState.error.isNullOrEmpty()) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            // Sheet content

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 50.dp, start = 10.dp, end = 10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

                Icon(imageVector = Icons.Rounded.Error, contentDescription = null, tint = Color.Red, modifier = Modifier
                    .width(70.dp)
                    .height(70.dp)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = loginState.error?: "An error occurred")
                Spacer(modifier = Modifier.height(5.dp))
                Button(onClick = { showBottomSheet = false }) {
                    Text(text = "Retry", color = Color.White, fontSize = 16.sp)
                }
            }

        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp), contentAlignment = Alignment.TopCenter) {
        Column(modifier =  Modifier.fillMaxWidth()) {
            //Image(imageVector = Icons.Rounded.AccountCircle, contentDescription = null, modifier = Modifier.height(250.dp))
            Image(painter = painterResource(id = R.drawable.preg_logo), contentDescription = null, modifier = Modifier
                .fillMaxWidth()
                .height(300.dp))
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Enter Your Phone Number e.g 704253811")
            Spacer(modifier = Modifier.height(5.dp))
            TextField(value = userPhone, onValueChange = { userPhone = it }, prefix = { Text(text = "256") }, singleLine= true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(50.dp))
            Button(onClick = {
                viewModel.login("+256${userPhone}", activity)
                //navController.navigate(NavScreen.Verify.route)
                             }, modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)) {
                if (loginState.isRunning) {
                    CircularProgressIndicator(modifier = Modifier
                        .width(70.dp)
                        )
                } else {
                    Text(text = "Sign In", color = Color.White, fontSize = 18.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyCodeScreen(navController: NavController, activity: Activity, viewModel: ClientViewModel, phone: String, verificationId:String) {
    val verifyState by viewModel.verifyState.collectAsState()
    var userCode by rememberSaveable { mutableStateOf("") }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }


//    if (verifyState.isRunning) {
//        CircularProgressIndicator(modifier = Modifier
//            .width(70.dp)
//            .background(color = Color.Transparent), color = MaterialTheme.colorScheme.secondary)
//    } else
        if (verifyState.newUser) {
        navController.navigate(NavScreen.Register.route+"?phone=$phone")
    } else if (verifyState.user != null) {
        if (verifyState.user?.title == "User") {
            navController.navigate(NavScreen.UserHome.route)
        } else {
            navController.navigate(NavScreen.DoctorHome.route)
        }
    } else if (!verifyState.error.isNullOrEmpty()) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            // Sheet content

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 50.dp, start = 10.dp, end = 10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

                Icon(imageVector = Icons.Rounded.Error, contentDescription = null, tint = Color.Red, modifier = Modifier
                    .width(70.dp)
                    .height(70.dp)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = verifyState.error?: "An error occurred")
                Spacer(modifier = Modifier.height(5.dp))
                Button(onClick = {
                    showBottomSheet = false
                    viewModel.verifyCode(phone, verificationId, userCode, activity)
                }) {
                    Text(text = "Retry", color = Color.White, fontSize = 18.sp)
                }
            }

        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp), contentAlignment = Alignment.TopCenter) {
        Column(modifier =  Modifier.fillMaxWidth()) {
            //Image(imageVector = Icons.Rounded.AccountCircle, contentDescription = null, modifier = Modifier.height(250.dp))
            Image(painter = painterResource(id = R.drawable.preg_logo), contentDescription = null, modifier = Modifier
                .fillMaxWidth()
                .height(300.dp))
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "An OTP has been sent to Your number. Enter the code to continue:")
            Spacer(modifier = Modifier.height(5.dp))
            TextField(value = userCode, onValueChange = { userCode = it }, singleLine= true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(50.dp))
            Button(onClick = {
                viewModel.verifyCode(phone, verificationId, userCode, activity)
                //navController.navigate(NavScreen.Register.route)
                             }, modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)) {
                if (verifyState.isRunning) {
                    CircularProgressIndicator()
                } else {
                    Text(text = "Verify Code", color = Color.White, fontSize = 18.sp)
                }

            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterUserScreen(navController: NavController, activity: Activity, viewModel: ClientViewModel, phone: String) {
    val addMemberState by viewModel.addMemberState.collectAsState()

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    var showCalender by remember { mutableStateOf(false) }
    var showDoBCalender by remember { mutableStateOf(false) }
    var showDoCCalender by remember { mutableStateOf(false) }

    var userName by rememberSaveable { mutableStateOf("") }
    var dob by rememberSaveable { mutableStateOf("") }
    var doc by rememberSaveable { mutableStateOf("") }
    var doDelivery by rememberSaveable { mutableStateOf("") }
    var residence by rememberSaveable { mutableStateOf("") }

//    if (addMemberState.isRunning) {
//        CircularProgressIndicator(modifier = Modifier
//            .width(70.dp)
//            .background(color = Color.Transparent), color = MaterialTheme.colorScheme.secondary)
//    } else
        if (addMemberState.isComplete) {
        navController.navigate(NavScreen.UserHome.route)
    } else if (!addMemberState.error.isNullOrEmpty()) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            // Sheet content

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 50.dp, start = 10.dp, end = 10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

                Icon(imageVector = Icons.Rounded.Error, contentDescription = null, tint = Color.Red, modifier = Modifier
                    .width(70.dp)
                    .height(70.dp)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = addMemberState.error?: "An error occurred")
                Spacer(modifier = Modifier.height(5.dp))
                Button(onClick = {
                    showBottomSheet = false
                    viewModel.addMember(context = activity, name = userName, phone = phone, dob = dob, doc=doc, doDelivery = doDelivery, location = residence)
                }) {
                    Text(text = "Retry", color = Color.White, fontSize = 18.sp)
                }
            }

        }
    }

    if (showCalender) {
        val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)

        val millisToLocalDate = datePickerState.selectedDateMillis?.let {
            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
        }

        val dateToString = millisToLocalDate?.let {
            val formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM, yyyy", Locale.getDefault())
            val dateInMillis = LocalDate.parse(it.format(formatter), formatter).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val localDate = Instant.ofEpochMilli(dateInMillis).atZone(ZoneId.systemDefault()).toLocalDate()
            formatter.format(localDate)
        } ?: "Choose Date"

        val dateOfDelivery = millisToLocalDate?.plusMonths(9)?.let {
            val formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM, yyyy", Locale.getDefault())
            val dateInMillis = LocalDate.parse(it.format(formatter), formatter).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val localDate = Instant.ofEpochMilli(dateInMillis).atZone(ZoneId.systemDefault()).toLocalDate()
            formatter.format(localDate)
        } ?: "Choose Date"

        if (showDoBCalender) dob = dateToString
        if (showDoCCalender)  {
            doc = dateToString
            doDelivery = dateOfDelivery
        }


            DatePickerDialog(
                onDismissRequest = {
                    showCalender = false
                    showDoBCalender = false
                    showDoCCalender = false},
                confirmButton = {
                    TextButton(onClick = {
                        //onDateSelected(datePickerState.selectedDateMillis)
                        showCalender = false
                        showDoBCalender = false
                        showDoCCalender = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showCalender = false
                        showDoBCalender = false
                        showDoCCalender = false}) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState, showModeToggle = true)
            }

    }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp), contentAlignment = Alignment.TopStart) {
        Column(modifier =  Modifier.fillMaxWidth()) {
            //Image(imageVector = Icons.Rounded.AccountCircle, contentDescription = null, modifier = Modifier.height(150.dp))
            Image(painter = painterResource(id = R.drawable.preg_logo), contentDescription = null, modifier = Modifier
                .fillMaxWidth()
                .height(150.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Enter Your Name")
            Spacer(modifier = Modifier.height(2.dp))
            TextField(value = userName, onValueChange = { userName = it }, placeholder = { Text(text = "Enter Your Name") }, singleLine= true, shape= RoundedCornerShape(5.dp), keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, keyboardType = KeyboardType.Text, imeAction = ImeAction.Next), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Enter Your Date Of Birth")
            Spacer(modifier = Modifier.height(2.dp))
            TextField(value = dob, onValueChange = { dob = it }, placeholder={ Text(text = "Enter Your Date Of Birth") }, singleLine= true, shape= RoundedCornerShape(5.dp), enabled = false, readOnly = true, modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showCalender = true
                    showDoBCalender = true
                }, trailingIcon = {
                Icon(
                    imageVector = Icons.Rounded.CalendarMonth,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        showCalender = true
                        showDoBCalender = true
                    })
            })
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Enter the Date Of Conception")
            Spacer(modifier = Modifier.height(2.dp))
            TextField(value = doc , onValueChange = { doc = it }, placeholder={ Text(text = "Enter the Date Of Conception") }, singleLine= true, shape= RoundedCornerShape(5.dp), enabled = false, readOnly = true, modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showCalender = true
                    showDoCCalender = true
                }, trailingIcon = {
                Icon(imageVector = Icons.Rounded.CalendarMonth, contentDescription = null, modifier = Modifier.clickable {
                    showCalender = true
                    showDoCCalender = true
                })
            })
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Enter Your Place Of Residence")
            Spacer(modifier = Modifier.height(2.dp))
            TextField(value = residence, onValueChange = { residence = it }, placeholder={ Text(text = "Enter Your Place Of Residence") }, singleLine= true, shape= RoundedCornerShape(5.dp), keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, keyboardType = KeyboardType.Text, imeAction = ImeAction.Next), modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(30.dp))
            Button(onClick = {
                             when {
                                 userName.isEmpty() -> { Toast.makeText(activity, "Name cannot be empty", Toast.LENGTH_SHORT).show()}
                                 dob.isEmpty() -> { Toast.makeText(activity, "Date Of Birth cannot be empty", Toast.LENGTH_SHORT).show() }
                                 doc.isEmpty() -> { Toast.makeText(activity, "Date Of Conception cannot be empty", Toast.LENGTH_SHORT).show() }
                                 residence.isEmpty() -> { Toast.makeText(activity, "Place Of Residence cannot be empty", Toast.LENGTH_SHORT).show() }
                                 else -> {
                                     viewModel.addMember(context = activity, name = userName, phone = phone, dob = dob, doc=doc, doDelivery = doDelivery, location = residence)
                                     //navController.navigate(NavScreen.UserHome.route)
                                 }
                             }
            }, modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)) {
                if (addMemberState.isRunning) {
                    CircularProgressIndicator()
                } else {
                    Text(text = "Continue" , color = Color.White, fontSize = 18.sp)
                }

            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserHomeScreen(navController: NavController, context: Context, viewModel: ClientViewModel) {
    var menuState by remember { mutableStateOf(false) }
    var pressOffset by remember { mutableStateOf(DpOffset(0.dp, 0.dp)) }
    val user = viewModel.getLocalUser(context).collectAsState(null).value
    val time = LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))


    Box(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp), contentAlignment = Alignment.TopCenter)
    {
        if (menuState) {
            DropdownMenu(
                expanded = menuState,
                onDismissRequest = { menuState = false },
                offset = pressOffset.copy(
                    y = pressOffset.y
                )
            ) {
                DropdownMenuItem(
                    text = { Text("Sign Out") },
                    onClick = {
                        viewModel.removeLocalUser(context)
                        navController.navigate(NavScreen.Login.route, navOptions = NavOptions.Builder().setPopUpTo(NavScreen.Login.route, true,
                            saveState = false
                        ).build())
                              },
                    leadingIcon = { Icon(Icons.Outlined.Output, contentDescription = null) }
                )
            }
        }

        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.Start) {
            Row {
                Icon(imageVector = Icons.Rounded.AccountCircle, contentDescription = null, modifier = Modifier
                    .height(50.dp)
                    .width(45.dp), tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(5.dp))
                Column {
                    Text(text = "Welcome, ${user?.name?.substringBefore(" ")}")
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(text = "0${user?.phone}")
                }

                Column(modifier = Modifier
                    .fillMaxWidth(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.End) {
                    Icon(imageVector = Icons.Rounded.Settings, contentDescription = null, modifier = Modifier
                        .height(45.dp)
                        .width(45.dp)
                        .pointerInput(true) {
                            detectTapGestures {
//                                menuState = true
//                                pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                            }
                        }
                        .clickable {
                            //menuState = true
                            viewModel.removeLocalUser(context)
                            navController.navigate(
                                NavScreen.Login.route,
                                navOptions = NavOptions
                                    .Builder()
                                    .setPopUpTo(
                                        NavScreen.Login.route, true,
                                        saveState = false
                                    )
                                    .build()
                            )
                        }
                    )
                }
            }
            
            Card(onClick = { /*TODO*/ }, modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(10.dp), shape = RoundedCornerShape(20.dp)) {
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Expected Delivery Date")
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = "~ ${user?.doDelivery}")
                }
            }


            Row(modifier = Modifier
                .fillMaxWidth()
                .height(150.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Card(onClick = {
                    val u = Uri.parse("tel:" + "0704253811")
                    val i = Intent(Intent.ACTION_DIAL, u)
                    try {
                        context.startActivity(i)
                    } catch (s: SecurityException) {
                        Toast.makeText(context, "An error occurred ${s.message}", Toast.LENGTH_LONG)
                            .show()
                    }

                    viewModel.saveCallInformation(context = context, time = time)
                    navController.navigateUp()
                }, modifier = Modifier
                    .weight(1F)
                    .padding(10.dp), shape = RoundedCornerShape(20.dp)) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Rounded.PhoneInTalk, contentDescription = null, modifier = Modifier
                            .height(70.dp)
                            .width(45.dp))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "Call")
                    }
                }
                Spacer(modifier = Modifier.width(5.dp))
                Card(onClick = { navController.navigate(NavScreen.Maps.route) }, modifier = Modifier
                    .weight(1F)
                    .padding(10.dp), shape = RoundedCornerShape(20.dp)) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Rounded.LocationOn, contentDescription = null, modifier = Modifier
                            .height(70.dp)
                            .width(45.dp))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "Location")
                    }
                }
            }

            Row(modifier = Modifier
                .fillMaxWidth()
                .height(150.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Card(onClick = { navController.navigate(NavScreen.GetNutrition.route) }, modifier = Modifier
                    .weight(1F)
                    .padding(10.dp), shape = RoundedCornerShape(20.dp)) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Rounded.FoodBank, contentDescription = null, modifier = Modifier
                            .height(70.dp)
                            .width(45.dp))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "Nutrition")
                    }
                }
                Spacer(modifier = Modifier.width(5.dp))
                Card(onClick = { navController.navigate(NavScreen.GetExercise.route)  }, modifier = Modifier
                    .weight(1F)
                    .padding(10.dp), shape = RoundedCornerShape(20.dp)) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Rounded.SportsGymnastics, contentDescription = null, modifier = Modifier
                            .height(70.dp)
                            .width(45.dp))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "Exercise")
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorHomeScreen(navController: NavController, context: Context, viewModel: ClientViewModel) {
    val user = viewModel.getLocalUser(context).collectAsState(null).value
    viewModel.getCallInfo(context)
    val callState by viewModel.getCallState.collectAsState()

    viewModel.getLocationInfo(context)
    val locationState by viewModel.getLocationState.collectAsState()

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp), contentAlignment = Alignment.TopCenter)
    {

        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.Start) {
            Row {
                Icon(imageVector = Icons.Rounded.AccountCircle, contentDescription = null, modifier = Modifier
                    .height(45.dp)
                    .width(45.dp))
                Spacer(modifier = Modifier.width(5.dp))
                Column {
                    Text(text = "Welcome, ${user?.name?.substringBefore(" ")}")
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(text = "0${user?.phone}")
                }

                Column(modifier = Modifier
                    .fillMaxWidth(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.End) {
                    Icon(imageVector = Icons.Rounded.Settings, contentDescription = null, modifier = Modifier
                        .height(45.dp)
                        .width(45.dp)
                        .clickable {
                            viewModel.removeLocalUser(context)
                            navController.navigate(
                                NavScreen.Login.route,
                                navOptions = NavOptions
                                    .Builder()
                                    .setPopUpTo(
                                        NavScreen.Login.route, true,
                                        saveState = false
                                    )
                                    .build()
                            )
                        }
                    )
                }
            }

            Card(onClick = { /*TODO*/ }, modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(10.dp), shape = RoundedCornerShape(20.dp)) {
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Total Calls: ${callState.call?.size ?: 0}")
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = "Total Locations Received: ${locationState.location?.size ?: 0}")
                }
            }


            Row(modifier = Modifier
                .fillMaxWidth()
                .height(150.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Card(onClick = { navController.navigate(NavScreen.GetCalls.route) }, modifier = Modifier
                    .weight(1F)
                    .padding(10.dp), shape = RoundedCornerShape(20.dp)) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Rounded.PhoneInTalk, contentDescription = null, modifier = Modifier.height(70.dp))
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(text = "Calls")
                    }
                }
                Spacer(modifier = Modifier.width(5.dp))
                Card(onClick = { navController.navigate(NavScreen.GetLocations.route) }, modifier = Modifier
                    .weight(1F)
                    .padding(10.dp), shape = RoundedCornerShape(20.dp)) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Rounded.LocationOn, contentDescription = null, modifier = Modifier.height(70.dp))
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(text = "Location")
                    }
                }
            }

            Row(modifier = Modifier
                .fillMaxWidth()
                .height(150.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Card(onClick = { navController.navigate(NavScreen.GetNutrition.route) }, modifier = Modifier
                    .weight(1F)
                    .padding(10.dp), shape = RoundedCornerShape(20.dp)) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Rounded.Fastfood, contentDescription = null, modifier = Modifier.height(70.dp))
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(text = "Nutrition Info")
                    }
                }
                Spacer(modifier = Modifier.width(5.dp))
                Card(onClick = { navController.navigate(NavScreen.GetExercise.route) }, modifier = Modifier
                    .weight(1F)
                    .padding(10.dp), shape = RoundedCornerShape(20.dp)) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Rounded.SportsGymnastics, contentDescription = null, modifier = Modifier.height(70.dp))
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(text = "Exercise Info")
                    }
                }
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun UserMapActivity(navController: NavController, activity: Activity, viewModel: ClientViewModel) {

    var latitude by remember { mutableStateOf<Double?>(null) }
    var longitude by remember { mutableStateOf<Double?>(null) }
    val uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = true)) }
    val properties by remember { mutableStateOf(MapProperties(mapType = MapType.TERRAIN)) }
    val time = LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))

    val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)

    // Determine the accuracy priority based on the 'priority' parameter
    val accuracy =  Priority.PRIORITY_HIGH_ACCURACY
    //else Priority.PRIORITY_BALANCED_POWER_ACCURACY

    // Check if location permissions are granted
    // Initialize the state for managing multiple location permissions.
    val permissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    // Use LaunchedEffect to handle permissions logic when the composition is launched.
    LaunchedEffect(key1 = permissionState) {
        // Check if all previously granted permissions are revoked.
        val allPermissionsRevoked =
            permissionState.permissions.size == permissionState.revokedPermissions.size

        // Filter permissions that need to be requested.
        val permissionsToRequest = permissionState.permissions.filter {
            !it.status.isGranted
        }

        // If there are permissions to request, launch the permission request.
        if (permissionsToRequest.isNotEmpty()) permissionState.launchMultiplePermissionRequest()

        // Execute callbacks based on permission status.
        if (allPermissionsRevoked || !permissionState.allPermissionsGranted) {
            return@LaunchedEffect
        } else {
            if (permissionState.allPermissionsGranted) {
                if (ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return@LaunchedEffect
                }
                fusedLocationProviderClient.getCurrentLocation(
                    accuracy, CancellationTokenSource().token,
                ).addOnSuccessListener { location ->
                    location?.let {
                        latitude = it.latitude
                        longitude = it.longitude

                    }
                }.addOnFailureListener { exception ->
                   Toast.makeText(activity, "An error occurred $exception", Toast.LENGTH_LONG).show()
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null, modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .clickable {
                        navController.navigateUp()
                    } )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Send Location")
            }

        }) },
        floatingActionButton = {
            Column {
                FloatingActionButton(onClick = {
                    fusedLocationProviderClient.getCurrentLocation(
                        accuracy, CancellationTokenSource().token,
                    ).addOnSuccessListener { location ->
                        location?.let {
                            latitude = it.latitude
                            longitude = it.longitude

                        }
                    }.addOnFailureListener { exception ->
                        Toast.makeText(activity, "An error occurred $exception", Toast.LENGTH_LONG).show()
                        navController.navigateUp()
                    }
                }) {
                    Icon(imageVector = Icons.Rounded.MyLocation, contentDescription = null)
                }
                Spacer(modifier = Modifier.height(2.dp))
                FloatingActionButton(onClick = {
                    if (latitude == null || longitude == null) {
                        Toast.makeText(activity, "Enable Your Location To continue, May take time", Toast.LENGTH_LONG).show()
                    } else {
                        viewModel.saveLocationInformation(
                            context = activity,
                            latitude = latitude ?: 0.0 ,
                            longitude = longitude ?: 32.0,
                            time = time,
                            status = "Pending",
                        )
                        navController.navigateUp()

                    }
                }) {
                    Icon(imageVector = Icons.Rounded.Send, contentDescription = null)
                }
            }

        }
    ) {padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)) {
            val point = LatLng(latitude?: 0.0, longitude ?: 32.0)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(point, 15f)
            }
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = uiSettings,
                properties = properties
            ) {
                Marker(
                    state = MarkerState(position = point),
                    title = "My Location"
                )
            }
            }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GetCallsScreen(navController: NavController, context: Context, viewModel: ClientViewModel) {
    viewModel.getCallInfo(context)
    val callState = viewModel.getCallState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null, modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .clickable {
                        navController.navigateUp()
                    } )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Calls")
            }

        }) }
    ) {padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp), contentAlignment = Alignment.Center) {
            if (callState.value.isRunning) {
                CircularProgressIndicator()
            }

                LazyColumn() {
                    if (callState.value.call != null) {
                        items(callState.value.call!!) {element ->
                            Card(modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .padding(5.dp), shape = RoundedCornerShape(20.dp)) {
                                Row {
                                    Icon(imageVector = Icons.Rounded.PhoneCallback, contentDescription = null, modifier = Modifier
                                        .width(50.dp)
                                        .height(50.dp), tint = Color.Green)
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Column {
                                        Text(text = element?.phone ?: "")
                                        Spacer(modifier = Modifier.height(1.dp))
                                        Text(text = element?.time ?:"")
                                    }
                                }
                            }
                        }
                    }

                }

            }


    }
    
    
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GetLocationScreen(navController: NavController, context: Context, viewModel: ClientViewModel) {
    viewModel.getLocationInfo(context)
    val locationState by viewModel.getLocationState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null, modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .clickable {
                        navController.navigateUp()
                    } )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Location Updates")
            }

        }) }
    ) {padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp), contentAlignment = Alignment.TopStart) {
            if (locationState.isRunning) {
                CircularProgressIndicator()
            }

            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp, start = 10.dp, bottom = 10.dp, end = 10.dp)) {
                if (locationState.location != null) {
                    items(locationState.location!!) { element ->
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(5.dp).clickable {
                                                     navController.navigate(NavScreen.ViewLocations.route+"?lat=${element?.latitude}&long=${element?.longitude}")
                            }, shape = RoundedCornerShape(20.dp)) {
                            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Rounded.LocationOn, contentDescription = null, modifier = Modifier
                                    .width(70.dp)
                                    .height(50.dp), tint = Color.Blue)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(text = "0${element?.phone}")
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(text = element?.time ?: "")
                                }
                            }
                        }
                    }
                }

            }
        }
    }


}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MapDetailScreen(navController: NavController, activity: Activity, viewModel: ClientViewModel, latitude:Float, longitude:Float) {

    val uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = true)) }
    val properties by remember { mutableStateOf(MapProperties(mapType = MapType.TERRAIN)) }

    Scaffold(
        topBar = { TopAppBar(title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null, modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .clickable {
                        navController.navigateUp()
                    } )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Location Info")
            }

        }) }
    ) {padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)) {
            val point = LatLng(latitude.toDouble() ?: 0.0, longitude.toDouble() ?: 32.0)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(point, 15f)
            }
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = uiSettings,
                properties = properties
            ) {
                Marker(
                    state = MarkerState(position = point),
                    title = "My Location"
                )
            }
        }
    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNutritionInfo(navController: NavController, context: Context, viewModel: ClientViewModel) {

    val addNutritionState by viewModel.addNutritionState.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    //val image by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var quantity by rememberSaveable { mutableStateOf("") }
    var benefit by rememberSaveable { mutableStateOf("") }


    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        //contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uri ->
            uri.let {
                imageUri = it
            }
        }
    )

//    if (addNutritionState.isRunning) {
//        CircularProgressIndicator(modifier = Modifier
//            .width(70.dp)
//            .background(color = Color.Transparent), color = MaterialTheme.colorScheme.secondary)
//    } else
        if (addNutritionState.isComplete) {
        navController.navigate(NavScreen.DoctorHome.route)
    } else if (!addNutritionState.error.isNullOrEmpty()) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            // Sheet content

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 50.dp, start = 10.dp, end = 10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

                Icon(imageVector = Icons.Rounded.Error, contentDescription = null, tint = Color.Red, modifier = Modifier
                    .width(70.dp)
                    .height(70.dp)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = addNutritionState.error?: "An error occurred")
                Spacer(modifier = Modifier.height(5.dp))
                Button(onClick = {
                    showBottomSheet = false
                    viewModel.saveNutritionInformation(context = context, name = name, image = imageUri!!, quantity = quantity, benefit = benefit)
                }) {
                    Text(text = "Retry", color = Color.White, fontSize = 18.sp)
                }
            }

        }
    }

    Scaffold(
        topBar = { TopAppBar(title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null, modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .clickable {
                        navController.navigateUp()
                    } )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Add Nutrition")
            }

        }) }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(20.dp), contentAlignment = Alignment.TopStart) {
            Column(modifier =  Modifier.fillMaxWidth()) {
                //Image(imageVector = Icons.Rounded.AccountCircle, contentDescription = null, modifier = Modifier.height(150.dp))
                AsyncImage(model = imageUri, contentDescription = null, contentScale = ContentScale.Fit, modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable {
                        galleryLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }, alignment = Alignment.Center)
                Button(onClick = {
                    galleryLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                ) }) {
                    Text(text = "Add Image")
                }
//                Image(painter = painterResource(id = R.drawable.preg_logo), contentDescription = null, modifier = Modifier
//                    .fillMaxWidth()
//                    .height(150.dp)
//                    )
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Enter Name Of Product")
                Spacer(modifier = Modifier.height(2.dp))
                TextField(value = name, onValueChange = { name = it }, placeholder = { Text(text = "Enter Name Of Product") }, singleLine= true, shape= RoundedCornerShape(5.dp), keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, keyboardType = KeyboardType.Text, imeAction = ImeAction.Next), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "Enter Quantity To Be Consumed")
                Spacer(modifier = Modifier.height(2.dp))
                TextField(value = quantity, onValueChange = { quantity = it }, placeholder = { Text(text = "Enter Quantity To Be Consumed") }, singleLine= true, shape= RoundedCornerShape(5.dp), keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None, keyboardType = KeyboardType.Text, imeAction = ImeAction.Next), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "Enter Nutritional Benefit")
                Spacer(modifier = Modifier.height(2.dp))
                TextField(value = benefit, onValueChange = { benefit = it }, placeholder={ Text(text = "Enter Nutritional Benefit") }, singleLine= true, shape= RoundedCornerShape(5.dp), keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, keyboardType = KeyboardType.Text, imeAction = ImeAction.Next), modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(30.dp))
                Button(onClick = {
                    when {
                        imageUri == null -> { Toast.makeText(context, "Product Image cannot be empty", Toast.LENGTH_SHORT).show()}
                        name.isEmpty() -> { Toast.makeText(context, "Product Name cannot be empty", Toast.LENGTH_SHORT).show() }
                        quantity.isEmpty() -> { Toast.makeText(context, "Product Quantity cannot be empty", Toast.LENGTH_SHORT).show() }
                        benefit.isEmpty() -> { Toast.makeText(context, "Product Benefit cannot be empty", Toast.LENGTH_SHORT).show() }
                        else -> {
                            viewModel.saveNutritionInformation(context = context, name = name, image = imageUri!!, quantity = quantity, benefit = benefit)
                            //navController.navigate(NavScreen.UserHome.route)
                        }
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)) {
                    if (addNutritionState.isRunning) {
                        CircularProgressIndicator()
                    } else {
                        Text(text = "Add Product" , color = Color.White, fontSize = 18.sp)
                    }

                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GetNutritionInfo(navController: NavController, context: Context, viewModel: ClientViewModel) {
    val user = viewModel.getLocalUser(context).collectAsState(null).value
    viewModel.getNutritionInfo(context)
    val nutritionState by viewModel.getNutritionState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null, modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .clickable {
                        navController.navigateUp()
                    } )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Nutrition")
            }

        }) },
        floatingActionButton = {
            if (user?.title == "Doctor") {
                FloatingActionButton(onClick = {
                    navController.navigate(NavScreen.AddNutrition.route)
                }) {
                    Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
                }
            }
        }
    ) {padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp), contentAlignment = Alignment.Center) {

            if (nutritionState.isRunning) {
                CircularProgressIndicator()
            }

            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp, start = 10.dp, bottom = 10.dp, end = 10.dp)) {
                if (nutritionState.nutrition != null) {
                    items(nutritionState.nutrition!!) { element ->
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .padding(5.dp), shape = RoundedCornerShape(20.dp)) {
                            Row {
                                AsyncImage(model = element?.image, contentDescription = null, contentScale = ContentScale.Fit, modifier = Modifier
                                    .width(100.dp)
                                    .height(90.dp))
                                Spacer(modifier = Modifier.width(5.dp))
                                Column {
                                    Text(text = element?.name ?:"")
                                    Spacer(modifier = Modifier.height(1.dp))
                                    Text(text = "Quantity: ${element?.quantity}")
                                    Spacer(modifier = Modifier.height(1.dp))
                                    Text(text = "Benefit: ${element?.benefit}", maxLines = 3, overflow = TextOverflow.Ellipsis)
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExerciseInfo(navController: NavController, context: Context, viewModel: ClientViewModel) {

    val addExerciseState by viewModel.addExerciseState.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    //val image by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        //contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uri ->
            uri.let {
                imageUri = it
            }
        }
    )

//    if (addExerciseState.isRunning) {
//        CircularProgressIndicator(modifier = Modifier
//            .width(70.dp)
//            .background(color = Color.Transparent), color = MaterialTheme.colorScheme.secondary)
//    } else
        if (addExerciseState.isComplete) {
        navController.navigate(NavScreen.DoctorHome.route)
    } else if (!addExerciseState.error.isNullOrEmpty()) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            // Sheet content

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 50.dp, start = 10.dp, end = 10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

                Icon(imageVector = Icons.Rounded.Error, contentDescription = null, tint = Color.Red, modifier = Modifier
                    .width(70.dp)
                    .height(70.dp)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = addExerciseState.error?: "An error occurred")
                Spacer(modifier = Modifier.height(5.dp))
                Button(onClick = {
                    showBottomSheet = false
                    viewModel.saveExerciseInformation(context = context, name = name, image = imageUri!!, description = description)
                }) {
                    Text(text = "Retry", color = Color.White, fontSize = 18.sp)
                }
            }

        }
    }

    Scaffold(
        topBar = { TopAppBar(title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null, modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .clickable {
                        navController.navigateUp()
                    } )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Add Nutrition")
            }

        }) }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(20.dp), contentAlignment = Alignment.TopStart) {
            Column(modifier =  Modifier.fillMaxWidth()) {
                //Image(imageVector = Icons.Rounded.AccountCircle, contentDescription = null, modifier = Modifier.height(150.dp))
                AsyncImage(model = imageUri, contentDescription = null, contentScale = ContentScale.Fit, modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable {
                        galleryLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }, alignment = Alignment.Center)
                Button(onClick = {
                    galleryLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    ) }) {
                    Text(text = "Add Image")
                }
//                Image(painter = painterResource(id = R.drawable.preg_logo), contentDescription = null, modifier = Modifier
//                    .fillMaxWidth()
//                    .height(150.dp)
//                    )
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Enter Name Of Exercise")
                Spacer(modifier = Modifier.height(2.dp))
                TextField(value = name, onValueChange = { name = it }, placeholder = { Text(text = "Enter Name Of Exercise") }, singleLine= true, shape= RoundedCornerShape(5.dp), keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, keyboardType = KeyboardType.Text, imeAction = ImeAction.Next), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "Enter Description Of Exercise")
                Spacer(modifier = Modifier.height(2.dp))
                TextField(value = description, onValueChange = { description = it }, placeholder = { Text(text = "Enter Description Of Exercise") }, singleLine= true, shape= RoundedCornerShape(5.dp), keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None, keyboardType = KeyboardType.Text, imeAction = ImeAction.Next), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(5.dp))

                Spacer(modifier = Modifier.height(30.dp))
                Button(onClick = {
                    when {
                        imageUri == null -> { Toast.makeText(context, "Exercise Image cannot be empty", Toast.LENGTH_SHORT).show()}
                        name.isEmpty() -> { Toast.makeText(context, "Name Of Exercise cannot be empty", Toast.LENGTH_SHORT).show() }
                        description.isEmpty() -> { Toast.makeText(context, "Product Quantity cannot be empty", Toast.LENGTH_SHORT).show() }
                        else -> {
                            viewModel.saveExerciseInformation(context = context, name = name, image = imageUri!!, description = description)
                        }
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)) {
                    if (addExerciseState.isRunning) {
                        CircularProgressIndicator()
                    }
                    Text(text = "Add Exercise" , color = Color.White, fontSize = 18.sp)
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GetExerciseInfo(navController: NavController, context: Context, viewModel: ClientViewModel) {
    val user = viewModel.getLocalUser(context).collectAsState(null).value
    viewModel.getExerciseInfo(context)
    val exerciseState by viewModel.getExerciseState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null, modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .clickable {
                        navController.navigateUp()
                    } )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Exercises")
            }

        }) },
        floatingActionButton = {
            if (user?.title == "Doctor") {
                FloatingActionButton(onClick = {
                    navController.navigate(NavScreen.AddExercise.route)
                }) {
                    Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
                }
            }
        }
    ) {padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp), contentAlignment = Alignment.Center) {

            if (exerciseState.isRunning) {
                CircularProgressIndicator()
            }

            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp, start = 10.dp, bottom = 10.dp, end = 10.dp)) {
                if (exerciseState.exercise != null) {
                    items(exerciseState.exercise!!) { element ->
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .padding(5.dp), shape = RoundedCornerShape(20.dp)) {
                            Row {
                                AsyncImage(model = element?.image, contentDescription = null, contentScale = ContentScale.Fit, modifier = Modifier
                                    .width(100.dp)
                                    .height(90.dp))
                                Spacer(modifier = Modifier.width(5.dp))
                                Column {
                                    Text(text = element?.name ?:"")
                                    Spacer(modifier = Modifier.height(1.dp))
                                    Text(text = "Description: ${element?.description}", maxLines = 3, overflow = TextOverflow.Ellipsis)
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}



