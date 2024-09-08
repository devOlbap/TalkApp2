package com.example.talkapp2

object UserManager {
    private val users = mutableListOf<User>(
        User("1-2", "123", "admin", 1),
        User("1-3", "pauli123", "pauli", 2),
        User("1-9", "olbap123", "olbap", 3)
    )


    private var user_log: User = User()

    private var id_user_recover : Int = 0


    fun getRecoverIdUser():Int{
        return id_user_recover
    }

    fun setUserLog(user:User){
        user_log = user
    }

    fun setUserRecoverId(user:User){
        id_user_recover = user.id
    }

    fun getUserLog():User{
        return user_log
    }

    fun getUsers(): List<User> {
        return users
    }
    fun getUserByUsernameAndEmail(email: String, username: String): User? {
        return users.find { it.email == email && it.username == username }
    }
    fun getCountUsers():Int{
        return users.size
    }
    fun setNewPassword(id: Int, newPassword: String): Boolean {
        val user = getUserById(id)
        return if (user != null) {
            val updatedUser = user.copy(password = newPassword)
            val index = users.indexOf(user)
            if (index != -1) {
                users[index] = updatedUser
                true
            } else {
                false
            }
        } else {
            false
        }
    }


    fun getUserById(id:Int): User? {
        return users.find { it.id == id  }
    }

    fun findUser(email: String, password: String): User? {
        return users.find { it.email == email && it.password == password }
    }

    fun addUser(email: String, password: String, username: String, id : Int) {
        val newUser = User(email, password, username, id)
        users.add(newUser)
    }
    fun delUserLog(){
        user_log = User()
    }



}

data class User(
    val email: String ="",
    val password: String="",
    val username: String="",
    val id: Int =0
)
