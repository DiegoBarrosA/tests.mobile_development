package one.expressdev.testing.mobile_development.modelo

import androidx.compose.runtime.mutableStateListOf
import android.util.Log

data class User(
    val id: String = java.util.UUID.randomUUID().toString(),
    val first_name: String,
    val last_name: String,
    val email: String,
    val password: String
) {
    companion object {
        private val usersList = mutableListOf<User>(
            User("k92839lasje121dkldji", "John", "Doe", "john.doe@example.com", "Password!1John"),
            User("dasddsa923890sadsa", "Jane", "Smith", "jane.smith@example.com", "Password!2Jane"),
            User("j83ed8rhd92fj84e", "Bob", "Johnson", "bob.johnson@example.com", "Password!3Bob")
        )
        private var logged_user: User? = null

        fun addUser(user: User) {
            usersList.add(user)
        }

        fun getUsers(): List<User> = usersList

        fun setLoggedUser(user: User?) {
            logged_user = user
        }

        fun getLoggedUser(): Any = logged_user ?: false

        fun updateUser(updatedUser: User) {
            usersList.indexOfFirst { it.id == updatedUser.id }
                .takeIf { it != -1 }
                ?.let { usersList[it] = updatedUser }
        }

        fun isEmailTaken(email: String, excludeUserId: String? = null): Boolean =
            usersList.any { it.email == email && it.id != excludeUserId }
    }
}
