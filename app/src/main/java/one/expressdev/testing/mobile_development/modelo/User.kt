package one.expressdev.testing.mobile_development.modelo

import androidx.compose.runtime.mutableStateListOf
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import kotlinx.coroutines.tasks.await

data class User(
    val id: String = java.util.UUID.randomUUID().toString(), // Default value for id
    val first_name: String = "", // Default value for first_name
    val last_name: String = "",  // Default value for last_name
    val email: String = "",       // Default value for email
    val password: String = ""     // Default value for password
) {
    companion object {
        private val database: DatabaseReference = Firebase.database.reference.child("users")
        private val usersList = mutableStateListOf<User>()
        private var loggedUser: User? = null
        suspend fun fetchUsers() {
            try {
                val snapshot = database.get().await()
                usersList.clear()
                snapshot.children.forEach { child ->
                    val user = child.getValue<User>()
                    user?.let { usersList.add(it) }
                }

                // Log the fetched users
                Log.d("User", "Users fetched successfully: ${usersList.joinToString { it.toString() }}")
            } catch (e: Exception) {
                Log.e("User", "Error fetching users: ${e.message}")
            }
        }

         // Push a single user to Firebase
        private fun pushUser(user: User) {
            database.child(user.id).setValue(user).addOnSuccessListener {
                Log.d("User", "User ${user.first_name} added successfully!")
            }.addOnFailureListener { error ->
                Log.e("User", "Error adding user: ${error.message}")
            }
        }

        // Add a new user
        fun addUser(user: User) {
            usersList.add(user)
            pushUser(user)
        }

        // Get the list of users
        fun getUsers(): List<User> = usersList

        // Set the logged-in user
        fun setLoggedUser(user: User?) {
            loggedUser = user
        }

        // Get the logged-in user
        fun getLoggedUser(): User? = loggedUser

        // Update an existing user
        fun updateUser(updatedUser: User) {
            val index = usersList.indexOfFirst { it.id == updatedUser.id }
            if (index != -1) {
                usersList[index] = updatedUser
                pushUser(updatedUser)
            }
        }

        // Check if an email is already taken
        fun isEmailTaken(email: String, excludeUserId: String? = null): Boolean =
            usersList.any { it.email == email && it.id != excludeUserId }
    }
}
