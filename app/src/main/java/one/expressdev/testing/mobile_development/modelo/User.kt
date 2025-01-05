package one.expressdev.testing.mobile_development.modelo

import android.util.Log

data class User(val first_name: String, val last_name: String, val email: String, val password: String) {
    companion object {
        private val usersList = mutableListOf<User>(
            User("John", "Doe", "john.doe@example.com","Password!1John"),
            User("Jane", "Smith", "jane.smith@example.com","Password!2Jane"),
            User("Bob", "Johnson", "bob.johnson@example.com","Password!3Bob")
        )

        fun addUser(user: User) {
            usersList.add(user)
        }

        fun getUsers(): List<User> {
            return usersList
        }
    }
}