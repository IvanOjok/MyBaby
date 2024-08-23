package inc.pneuma.mybaby.ui.presentation

sealed class NavScreen(var route:String) {

    object Login: NavScreen("Login")

    object Verify: NavScreen("Verify")

    object Register: NavScreen("Register")

    object UserHome: NavScreen("UserHome")

    object DoctorHome: NavScreen("DoctorHome")
}