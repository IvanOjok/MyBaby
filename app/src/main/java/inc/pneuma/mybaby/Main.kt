package inc.pneuma.mybaby


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.PhoneInTalk
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "") {
        composable("") {
            SplashScreen()
        }
    }

}

@Composable
fun SplashScreen() {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp), contentAlignment = Alignment.Center) {
        Icon(imageVector = Icons.Rounded.AccountCircle, contentDescription = null, modifier = Modifier
            .width(200.dp)
            .height(250.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen() {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp), contentAlignment = Alignment.Center) {
        Column(modifier =  Modifier.fillMaxWidth()) {
            Image(imageVector = Icons.Rounded.AccountCircle, contentDescription = null, modifier = Modifier.height(250.dp))
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Enter Your Phone Number e.g 704253811")
            Spacer(modifier = Modifier.height(5.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(5.dp)
                .background(color = Color.White, shape = RoundedCornerShape(5.dp)), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                Text(text = "256")
                Spacer(modifier = Modifier.width(2.dp))
                TextField(value = "", onValueChange = {}, singleLine= true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
            }

            Spacer(modifier = Modifier.height(50.dp))
            Button(onClick = { /*TODO*/ }, modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(10.dp)) {
                Text(text = "Sign In")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterUserScreen() {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp), contentAlignment = Alignment.Center) {
        Column(modifier =  Modifier.fillMaxWidth()) {
            Image(imageVector = Icons.Rounded.AccountCircle, contentDescription = null, modifier = Modifier.height(150.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Enter Your Name")
            Spacer(modifier = Modifier.height(2.dp))
            TextField(value = "", onValueChange = {}, label={ Text(text = "Enter Your Name") }, placeholder = {}, singleLine= true, shape= RoundedCornerShape(5.dp), keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, keyboardType = KeyboardType.Text, imeAction = ImeAction.Next))
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Enter Your Date Of Birth")
            Spacer(modifier = Modifier.height(2.dp))
            TextField(value = "", onValueChange = {}, label={ Text(text = "Enter Your Date Of Birth") }, singleLine= true, shape= RoundedCornerShape(5.dp), keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None, keyboardType = KeyboardType.Text, imeAction = ImeAction.Next))
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Enter the Date Of Conception")
            Spacer(modifier = Modifier.height(2.dp))
            TextField(value = "", onValueChange = {}, label={ Text(text = "Enter the Date Of Conception") }, singleLine= true, shape= RoundedCornerShape(5.dp), keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None, keyboardType = KeyboardType.Text, imeAction = ImeAction.Next))
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Enter Your Place Of Residence")
            Spacer(modifier = Modifier.height(2.dp))
            TextField(value = "", onValueChange = {}, label={ Text(text = "Enter Your Place Of Residence") }, singleLine= true, shape= RoundedCornerShape(5.dp), keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, keyboardType = KeyboardType.Text, imeAction = ImeAction.Next))

            Spacer(modifier = Modifier.height(30.dp))
            Button(onClick = { /*TODO*/ }, modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(10.dp)) {
                Text(text = "Continue")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserHomeScreen() {
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
                    Text(text = "Mr. Ivan Ojok")
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "256 704253811")
                }

                Column(modifier = Modifier
                    .fillMaxWidth(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.End) {
                    Icon(imageVector = Icons.Rounded.Settings, contentDescription = null, modifier = Modifier
                        .height(45.dp)
                        .width(45.dp)
                    )
                }
            }
            
            Card(onClick = { /*TODO*/ }, modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(10.dp), shape = RoundedCornerShape(20.dp)) {
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Period Left")
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = "~ 5 months, 9 days")
                }
            }


            Row(modifier = Modifier.fillMaxWidth().height(150.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Card(onClick = { /*TODO*/ }, modifier = Modifier.weight(1F)
                    .padding(10.dp), shape = RoundedCornerShape(20.dp)) {
                    Column(modifier = Modifier.fillMaxWidth().padding(10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Rounded.PhoneInTalk, contentDescription = null, modifier = Modifier.height(70.dp))
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(text = "Call")
                    }
                }
                Spacer(modifier = Modifier.width(5.dp))
                Card(onClick = { /*TODO*/ }, modifier = Modifier.weight(1F)
                    .padding(10.dp), shape = RoundedCornerShape(20.dp)) {
                    Column(modifier = Modifier.fillMaxWidth().padding(10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Rounded.LocationOn, contentDescription = null, modifier = Modifier.height(70.dp))
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(text = "Location")
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth().height(150.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Card(onClick = { /*TODO*/ }, modifier = Modifier.weight(1F)
                    .padding(10.dp), shape = RoundedCornerShape(20.dp)) {
                    Column(modifier = Modifier.fillMaxWidth().padding(10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Rounded.PhoneInTalk, contentDescription = null, modifier = Modifier.height(70.dp))
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(text = "Nutrition")
                    }
                }
                Spacer(modifier = Modifier.width(5.dp))
                Card(onClick = { /*TODO*/ }, modifier = Modifier.weight(1F)
                    .padding(10.dp), shape = RoundedCornerShape(20.dp)) {
                    Column(modifier = Modifier.fillMaxWidth().padding(10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Rounded.LocationOn, contentDescription = null, modifier = Modifier.height(70.dp))
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(text = "Exercise")
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorHomeScreen() {
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
                    Text(text = "Mr. Ivan Ojok")
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "256 704253811")
                }

                Column(modifier = Modifier
                    .fillMaxWidth(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.End) {
                    Icon(imageVector = Icons.Rounded.Settings, contentDescription = null, modifier = Modifier
                        .height(45.dp)
                        .width(45.dp)
                    )
                }
            }

            Card(onClick = { /*TODO*/ }, modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(10.dp), shape = RoundedCornerShape(20.dp)) {
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Number Of Patients")
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = "50")
                }
            }


            Row(modifier = Modifier.fillMaxWidth().height(150.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Card(onClick = { /*TODO*/ }, modifier = Modifier.weight(1F)
                    .padding(10.dp), shape = RoundedCornerShape(20.dp)) {
                    Column(modifier = Modifier.fillMaxWidth().padding(10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Rounded.PhoneInTalk, contentDescription = null, modifier = Modifier.height(70.dp))
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(text = "Calls")
                    }
                }
                Spacer(modifier = Modifier.width(5.dp))
                Card(onClick = { /*TODO*/ }, modifier = Modifier.weight(1F)
                    .padding(10.dp), shape = RoundedCornerShape(20.dp)) {
                    Column(modifier = Modifier.fillMaxWidth().padding(10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Rounded.LocationOn, contentDescription = null, modifier = Modifier.height(70.dp))
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(text = "Location Updates")
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth().height(150.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Card(onClick = { /*TODO*/ }, modifier = Modifier.weight(1F)
                    .padding(10.dp), shape = RoundedCornerShape(20.dp)) {
                    Column(modifier = Modifier.fillMaxWidth().padding(10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Rounded.PhoneInTalk, contentDescription = null, modifier = Modifier.height(70.dp))
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(text = "Nutrition Info")
                    }
                }
                Spacer(modifier = Modifier.width(5.dp))
                Card(onClick = { /*TODO*/ }, modifier = Modifier.weight(1F)
                    .padding(10.dp), shape = RoundedCornerShape(20.dp)) {
                    Column(modifier = Modifier.fillMaxWidth().padding(10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Rounded.LocationOn, contentDescription = null, modifier = Modifier.height(70.dp))
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(text = "Exercise Info")
                    }
                }
            }

        }
    }
}