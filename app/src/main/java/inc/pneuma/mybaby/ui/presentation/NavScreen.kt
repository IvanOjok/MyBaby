package inc.pneuma.mybaby.ui.presentation

sealed class NavScreen(var route:String) {

    object Login: NavScreen("Login")

    object Verify: NavScreen("Verify")

    object Register: NavScreen("Register")

    object UserHome: NavScreen("UserHome")

    object DoctorHome: NavScreen("DoctorHome")

    object Maps: NavScreen("Maps")

    object GetCalls: NavScreen("GetCalls")

    object GetLocations: NavScreen("GetLocations")

    object AddNutrition: NavScreen("AddNutrition")

    object GetNutrition: NavScreen("GetNutrition")
}