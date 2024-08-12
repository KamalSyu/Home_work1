
import java.io.File

sealed interface Command {
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

data class ExportCommand(val path: String) : Command {
    override fun isValid(): Boolean {
        return path.isNotEmpty()
    }
}

data class Person(var name: String, val phones: MutableList<String> = mutableListOf(), val emails: MutableList<String> = mutableListOf())

val contacts = mutableListOf<Person>()

fun main() {
    println("Введите номер команды (введите '1' для просмотра доступных команд):")

    while (true) {
        val choice = readLine()?.trim()?.lowercase() ?: ""
        when {
            choice == "1" -> showHelp()
            choice.startsWith("2") -> addPhone()
            choice.startsWith("3") -> addEmail()
            choice.startsWith("4") -> showContacts()
            choice.startsWith("5") -> findPerson()
            choice.startsWith("6") -> exportToFile()
            choice == "7" -> exitProgram()
            else -> println("Неизвестный выбор. Введите '1' для помощи.")
        }
    }
}

fun showHelp() {
    println("Доступные команды:")
    println("1. help - список доступных команд")
    println("2. add <Имя> phone <Номер телефона> - добавить номер телефона")
    println("3. add <Имя> email <Адрес электронной почты> - добавить адрес электронной почты")
    println("4. show <Имя> - показать данные о человеке")
    println("5. find <Телефон или Email> - найти людей по телефону или email")
    println("6. export <path> - экспортировать данные в файл")
    println("7. exit - выход из программы")
}

fun addPhone() {
    println("Введите команду в формате: add <Имя> phone <Номер телефона> (Пример: add Petr phone +7987987987987987987)")
    val input = readLine() ?: ""
    val parts = input.trim().split(" ")

    if (parts.size == 4 && parts[0] == "add" && parts[2] == "phone") {
        val command = AddPhoneCommand(parts[1], parts[3])
        if (command.isValid()) {
            val person = contacts.find { it.name.equals(command.name, ignoreCase = true) }
                ?: Person(command.name).also { contacts.add(it) }
            person.phones.add(command.phone)
            println("Добавлен телефон для ${command.name}: ${command.phone}")
        } else {
            println("Ошибка: Неправильный формат номера телефона.")
        }
    } else {
        println("Неверный ввод. Убедитесь, что команда введена правильно.")
    }


}

fun addEmail() {
    println("Введите команду в формате: add <Имя> email <Email> (Пример: add Petr email Petr@petr.com)")
    val input = readLine() ?: ""
    val parts = input.trim().split(" ")

    if (parts.size == 4 && parts[0] == "add" && parts[2] == "email") {
        val command = AddEmailCommand(parts[1], parts[3])
        if (command.isValid()) {
            val person = contacts.find { it.name.equals(command.name, ignoreCase = true) }
                ?: Person(command.name).also { contacts.add(it) }
            person.emails.add(command.email)
            println("Добавлен email для ${command.name}: ${command.email}")
        } else {
            println("Ошибка: Неправильный формат адреса электронной почты.")
        }
    } else {
        println("Неверный ввод. Убедитесь, что команда введена правильно.")
    }

}

fun showContacts() {
    println("Введите имя человека для отображения данных:")
    val name = readLine() ?: ""
    val person = contacts.find { it.name.equals(name, ignoreCase = true) }

    if (person != null) {
        println("Данные для ${person.name}:")
        println("Телефоны: ${if (person.phones.isNotEmpty()) person.phones.joinToString(", ") else "нет"}")
        println("Email: ${if (person.emails.isNotEmpty()) person.emails.joinToString(", ") else "нет"}")
    } else {
        println("Человек с именем '$name' не найден.")
    }
}

fun findPerson() {
    println("Введите телефон или email:")
    val query = readLine()?.trim() ?: ""

    val foundPersons = contacts.filter { person ->
        person.phones.contains(query) || person.emails.contains(query)
    }

    if (foundPersons.isNotEmpty()) {
        println("Найдены лица:")
        foundPersons.forEach {
            println("Имя: ${it.name}, Телефоны: ${it.phones.joinToString(", ")}, Email: ${it.emails.joinToString(", ")}")
        }
    } else {
        println("Люди с таким телефоном или email не найдены.")
    }
}

fun exportToFile() {
    println("Введите путь к файлу для экспорта: (Пример: export /Users/user/myfile.json)")
    val path = readLine()?.trim() ?: ""

    val command = ExportCommand(path)
    if (command.isValid()) {
        val jsonString = contacts.toJson()
        File(path).writeText(jsonString)  // Записываем текст в файл
        println("Данные успешно экспортированы в $path")
    } else {
        println("Ошибка: Неверный путь к файлу.")
    }

}

fun exitProgram() {
    println("Выход из программы.")
}

fun List<Person>.toJson(): String {
    val jsonBuilder = StringBuilder()
    jsonBuilder.append("[")

    this.forEachIndexed { index, person ->
        jsonBuilder.append("{")
        jsonBuilder.append("\"name\": \"${person.name}\",")
        jsonBuilder.append("\"phones\": [${person.phones.joinToString(", ") { "\"$it\"" }}],")
        jsonBuilder.append("\"emails\": [${person.emails.joinToString(", ") { "\"$it\"" }}]")
        jsonBuilder.append("}")
        if (index < this.size - 1) jsonBuilder.append(",")
    }

    jsonBuilder.append("]")
    return jsonBuilder.toString()
}

// Проверка формата номера телефона
fun isValidPhone(phone: String): Boolean {
    return phone.matches(Regex("^[+]?[0-9]+$")) // Простой формат для проверки
}

// Проверка формата почты
fun isValidEmail(email: String): Boolean {
    return email.matches(Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
}