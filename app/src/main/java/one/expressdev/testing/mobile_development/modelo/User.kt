package one.expressdev.testing.mobile_development.modelo

data class User(val nombre: String, val apellidos: String, val correo: String) {
    companion object {
        private val usersList = mutableListOf<User>(
            User("John", "Doe", "john.doe@example.com"),
            User("Jane", "Smith", "jane.smith@example.com"),
            User("Bob", "Johnson", "bob.johnson@example.com")
        )

        fun addUser(user: User) {
            usersList.add(user)
        }

        fun getUsers(): List<User> {
            return usersList
        }
    }
}