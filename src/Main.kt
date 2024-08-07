gitsealed interface Command {
    fun isValid(): Boolean
}

// Команды для добавления телефона или электронной почты
data class AddPhoneCommand(val name: String, val phone: String) : Command {
    override fun isValid(): Boolean {
        return isValidPhone(phone)
    }
}

data class AddEmailCommand(val name: String, val email: String) : Command {
    override fun isValid(): Boolean {
        return isValidEmail(email)
    }
}

object ShowCommand : Command {
    override fun isValid(): Boolean = true
}

object HelpCommand : Command {
    override fun isValid(): Boolean = true
}

object ExitCommand : Command {
    override fun isValid(): Boolean = true
}

data class Person(var name: String, var phone: String? = null, var email: String? = null)

val contacts = mutableListOf<Person>()

val commands = listOf(
    "1. help - список доступных команд",
    "2. add <Имя> phone <Номер телефона> - добавить номер телефона",
    "3. add <Имя> email <Адрес электронной почты> - добавить адрес электронной почты",
    "4. show - показать последние сохраненные данные",
    "5. exit - выход из программы"
)

fun main() {
    println("Введите номер команды (введите '1' для просмотра доступных команд):")

    while (true) {
        val choice = readLine()?.trim()?.lowercase() ?: ""
        when (choice) {
            "1" -> showHelp()
            "2" -> addPhone()
            "3" -> addEmail()
            "4" -> showContacts()
            "5" -> exitProgram()
            else -> println("Неизвестный выбор. Введите '1' для помощи.")
        }
    }
}

fun showHelp() {
    println("Доступные команды:")
    commands.forEach { println(it) }
}

fun addPhone() {
    println("Введите команду в формате: add <Имя> phone <Номер телефона> (пример: add Petr phone +7987987987")
    val input = readLine() ?: ""
    val parts = input.trim().split(" ")

    if (parts.size == 4 && parts[0] == "add" && parts[2] == "phone") {
        val command = AddPhoneCommand(parts[1], parts[3])
        if (command.isValid()) {
            contacts.add(Person(command.name, command.phone))
            println("Добавлен телефон для ${command.name}: ${command.phone}")
        } else {
            println("Ошибка: Неправильный формат номера телефона.")
        }
    } else {
        println("Неверный ввод. Убедитесь, что команда введена правильно.")
    }
}

fun addEmail() {
    println("Введите команду в формате: add <Имя> email <Email>")
    val input = readLine() ?: ""
    val parts = input.trim().split(" ")

    if (parts.size == 4 && parts[0] == "add" && parts[2] == "email") {
        val command = AddEmailCommand(parts[1], parts[3])
        if (command.isValid()) {
            contacts.add(Person(command.name, email = command.email))
            println("Добавлен email для ${command.name}: ${command.email}")
        } else {
            println("Ошибка: Неправильный формат адреса электронной почты.")
        }
    } else {
        println("Неверный ввод. Убедитесь, что команда введена правильно.")
    }
}

fun showContacts() {
    if (contacts.isNotEmpty()) {
        println("Сохраненные данные:")
        contacts.forEach { println("Имя: ${it.name}, Телефон: ${it.phone ?: "не задан"}, Email: ${it.email ?: "не задан"}") }
    } else {
        println("Данные не были добавлены.")
    }
}

fun exitProgram() {
    println("Выход из программы.")
    return
}

// Проверка формата номера телефона
fun isValidPhone(phone: String): Boolean {
    return phone.matches(Regex("^\\+?\\d+$"))
}

// Проверка формата почты
fun isValidEmail(email: String): Boolean {
    return email.matches(Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
}