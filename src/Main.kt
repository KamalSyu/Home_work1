fun main() {
    val commands = listOf(
        "1. help - список доступных команд",
        "2. add <Имя> phone <Номер телефона> - добавить номер телефона",
        "3. add <Имя> email <Адрес электронной почты> - добавить адрес электронной почты",
        "4. exit - выход из программы"
    )

    println("Введите номер команды (введите '1' help для просмотра доступных команд):")

    while (true) {
        val input = readLine() ?: continue

        when (input) {
            "1" -> {
                println("Доступные команды:")
                commands.forEach { println(it) }
            }
            "2" -> {
                println("Введите команду в формате: add <Имя> phone <Номер телефона> (пример: add Иван phone +1234567890)")
                val addInput = readLine() ?: continue
                handleAddCommand(addInput)
            }
            "3" -> {
                println("Введите команду в формате: add <Имя> email <Адрес электронной почты> (пример: add Иван email ivan@example.com)")
                val addInput = readLine() ?: continue
                handleAddCommand(addInput)
            }
            "4" -> {
                println("Выход из программы.")
                break
            }
            else -> {
                println("Неизвестная команда. Пожалуйста, попробуйте снова.")
            }
        }
    }
}

fun handleAddCommand(command: String) {
    val parts = command.split(" ")

    // Проверяем, что в команде достаточно параметров
    if (parts.size != 4) {
        println("Неверный формат команды. Используйте: add <Имя> phone <Номер телефона> или add <Имя> email <Адрес электронной почты>")
        return
    }

    val name = parts[1]
    val type = parts[2]
    val value = parts[3]

    when (type) {
        "phone" -> {
            if (isValidPhone(value)) {
                println("Добавлен телефон для $name: $value")
            } else {
                println("Ошибка: Неправильный формат номера телефона.")
            }
        }
        "email" -> {
            if (isValidEmail(value)) {
                println("Добавлен email для $name: $value")
            } else {
                println("Ошибка: Неправильный формат адреса электронной почты.")
            }
        }
        else -> {
            println("Ошибка: Неверный тип. Используйте 'phone' или 'email'.")
        }
    }
}

fun isValidPhone(phone: String): Boolean {
    return phone.matches(Regex("^\\+?\\d+$"))
}

fun isValidEmail(email: String): Boolean {
    return email.matches(Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
}