package inc.pneuma.mybaby.ui.presentation

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import inc.pneuma.mybaby.data.model.LocationInfo
import inc.pneuma.mybaby.data.model.User
import inc.pneuma.mybaby.data.model.userDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class ClientViewModel : ViewModel() {

    private var _navigateToLogin = MutableStateFlow(false)
    val navigateToLogin = _navigateToLogin.asStateFlow()

    private var _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn = _isUserLoggedIn.asStateFlow()

    private var _loggedInUser = MutableStateFlow("")
    val loggedInUser = _loggedInUser.asStateFlow()

    var storedVerificationId: String? = null

    private val _loginState = MutableStateFlow(LoginState())
    val loginState get() = _loginState.asStateFlow()

    val _verifyState = MutableStateFlow(VerifyState())
    val verifyState = _verifyState.asStateFlow()

    val _addMemberState = MutableStateFlow(AddMemberState())
    val addMemberState = _addMemberState.asStateFlow()

    val _getLocationState = MutableStateFlow(AddLocationState())
    val getLocationState = _getLocationState.asStateFlow()

    fun startSplash(context: Context) {
        viewModelScope.launch {
            delay(3000)
            _navigateToLogin.value = true

            val user = getLocalUser(context).first()
            if (!user.id.isNullOrEmpty()) {
                _isUserLoggedIn.value = true
                if (user.title == "User") {
                    _loggedInUser.value = "User"
                } else {
                    _loggedInUser.value = "Doctor"
                }
            }
        }
    }

    fun initializeAuthentication(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    fun initializeDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    fun login(phoneNumber: String, activity: Activity) {

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$credential")
                _loginState.update {
                    it.copy(
                        isRunning = false,
                        isComplete = true
                    )
                }

                signInWithPhoneAuthCredential(credential, activity)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                    // reCAPTCHA verification attempted with null Activity
                }

                // Show a message and update the UI
                _loginState.update {
                    it.copy(
                        isRunning = false,
                        error = e.message
                    )
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                //resendToken = token
                _loginState.update {
                    it.copy(
                        isRunning = false,
                        isComplete = true,
                        verificationId = storedVerificationId ?:""
                    )
                }
            }
        }

        val auth = initializeAuthentication()
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyCode(verificationId: String, code: String, activity: Activity) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential, activity)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential, activity: Activity) {
        _verifyState.update {
            it.copy(
                isRunning = true,
                isComplete = false,
            )
        }

        val auth = initializeAuthentication()
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithCredential:success")

                    val user = task.result?.user
                    if (user != null) {
                        getMember(user.phoneNumber?:"")
                    } else {
                        _verifyState.update {
                            it.copy(
                                isRunning = false,
                                isComplete = false,
                                error = "User doesn't exist"
                            )
                        }
                    }

                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI

                    _verifyState.update {
                        it.copy(
                            isRunning = false,
                            isComplete = false,
                            error = task.exception?.message
                        )
                    }
                }
            }
    }

    fun getMember(userId: String) {
        val db = initializeDatabase().reference
        db.child("users").child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)

                    _verifyState.update {
                        it.copy(
                            isRunning = false,
                            isComplete = true,
                            user = user,
                            newUser = false
                        )
                    }
                } else {
                    _verifyState.update {
                        it.copy(
                            isRunning = false,
                            isComplete = true,
                            newUser = true
                        )
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                _verifyState.update {
                    it.copy(
                        isRunning = false,
                        isComplete = false,
                        error = error.message
                    )
                }
            }

        })

    }


    fun addMember(
        context: Context,
        name:String,
        phone:String,
        dob: String,
        doc: String,
        doDelivery: String,
        location: String) {
        _addMemberState.update {
            it.copy(
                isRunning = true
            )
        }
        val auth = initializeAuthentication().currentUser?.uid ?: "None"
        val db = initializeDatabase().reference
        val user = User(
            id = auth,
            name = name,
            phone = phone,
            dob = dob,
            doc = doc,
            doDelivery = doDelivery,
            location = location, title = "User")
        db.child("users").child(phone).setValue(user).addOnSuccessListener {
            saveUserLocally(context = context, id = auth,
                name = name,
                phone = phone,
                dob = dob,
                doc = doc,
                doDelivery = doDelivery,
                location = location, title = "User")
            _addMemberState.update {
                it.copy(
                    isRunning = false,
                    isComplete = true
                )
            }
        }.addOnFailureListener {
            val message = it.message
            _addMemberState.update {
                it.copy(
                    isRunning = false,
                    isComplete = false,
                    error = message
                )
            }
        }
    }

    private fun saveUserLocally(context:Context, id:String, name: String, phone:String,
                                dob: String,
                                doc: String,
                                doDelivery: String,
                                location: String,
                                title:String) {
        viewModelScope.launch {
            context.userDataStore.updateData {user ->
                user.toBuilder()
                    .setId(id)
                    .setName(name)
                    .setPhone(phone)
                    .setDob(dob)
                    .setDoc(doc)
                    .setDoDelivery(doDelivery)
                    .setLocation(location)
                    .setTitle(title)
                    .build()
            }
        }
    }

    fun getLocalUser(context: Context): Flow<User> {
        return context.userDataStore.data.map { userProto->
            User(
                id = userProto.id.orEmpty(),
                name = userProto.name.orEmpty(),
                phone = userProto.phone.orEmpty(),
                dob = userProto.dob.orEmpty(),
                doc = userProto.doc.orEmpty(),
                doDelivery = userProto.doDelivery.orEmpty(),
                location = userProto.location.orEmpty(),
                title = userProto.title.orEmpty()
            )
        }
    }


    fun saveLocationInformation(
        context: Context,
        latitude: Double,
        longitude: Double,
        time: String,
        status: String) {

        _getLocationState.update {
            it.copy(
                isRunning = true
            )
        }

        viewModelScope.launch {
            val user = getLocalUser(context).first()
            val db = initializeDatabase().reference
            val location = LocationInfo(
                phone = user.phone,
                latitude = latitude,
                longitude = longitude,
                time = time,
                status = status,)
            db.child("location").child(user.phone).setValue(location).addOnSuccessListener {
                _getLocationState.update {
                    it.copy(
                        isRunning = false,
                        isComplete = true
                    )
                }
            }.addOnFailureListener {
                val message = it.message
                _getLocationState.update {
                    it.copy(
                        isRunning = false,
                        isComplete = false,
                        error = message
                    )
                }
            }
        }

    }
}

data class LoginState(
    val isRunning: Boolean = false,
    val isComplete:Boolean = false,
    val verificationId: String = "",
    val error: String? = null
)

data class VerifyState(
    val isRunning: Boolean = false,
    val isComplete:Boolean = false,
    val user: User? = null,
    val newUser: Boolean = false,
    val error: String? = null
)

data class AddMemberState(
    val isRunning: Boolean = false,
    val isComplete:Boolean = false,
    val error: String? = null
)

data class AddLocationState(
    val isRunning: Boolean = false,
    val isComplete:Boolean = false,
    val error: String? = null
)