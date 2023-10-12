package org.example
import java.util.Scanner
import scala.io.StdIn

object Main {
  val scanner: Scanner = new Scanner(System.in)
  var attempts: Int = 10
  val db: Database = new Database
  var user: User = _
  var account: Account = _

  // Función para saludar al usuario
  def sayHi(): Unit = {
    println("Hola, yo soy, tu asistente virtual, ¿ya estás registrado en Nu Bank?")
    println("1.Si")
    println("2.No")
    println("Otro.Salir")
  }

  // Función principal
  def main(args: Array[String]): Unit = {
    sayHi()
    val isRegistered: Int = scanner.nextInt()

    isRegistered match {
      case 1 => logIn()
      case 2 => signIn()
      case _ =>
    }

    sayBye()
  }

  // Función para despedirse
  def sayBye(): Unit = {
    println("\nGracias por usar nuestro servicio de asistente virtual, ¡nos vemos pronto!")
  }

  // Función para registro de nuevo usuario
  def signIn(): Boolean = {
    val personalData: scala.collection.mutable.ArrayBuffer[String] = scala.collection.mutable.ArrayBuffer.empty
    val accountData: scala.collection.mutable.ArrayBuffer[String] = scala.collection.mutable.ArrayBuffer.empty
    var value: String = null
    val questionsPersonalData: Array[String] = Array("numero de identificación", "primer y segundo nombre (si tiene)", "apellidos", "correo electronico")
    var tries: String = ""

    scanner.nextLine()

    for (question <- questionsPersonalData) {
      do {
        println(s"Ingrese su $question (Obligatorio)")
        value = scanner.nextLine()
      } while (value.isEmpty)
      personalData += value
    }

    user = db.createUser(personalData(0), personalData(1), personalData(2), personalData(3))

    if (user == null) {
      println("Algo salió mal, ¿Desea intentarlo de nuevo?")
      println("1.Si")
      println("Otro.No")
      tries = scanner.next()
      if (tries == "1") signIn()
      return false
    }

    println("Usuario correctamente creado, ahora digite la siguiente información para la creación de su cuenta")
    println("Ingrese un nombre de usuario sin espacios")
    accountData += scanner.next()

    while (db.usernameExist(accountData(0))) {
      println("Usuario ya en uso, digite uno nuevo")
      accountData(0) = scanner.next()
    }

    println("Digite una contraseña numerica de 4 digitos")
    accountData += scanner.next()

    while (!accountData(1).matches("[0-9]+") || accountData(1).length != 4) {
      println("Contraseña debe ser de 4 digitos y solo numeros")
      accountData(1) = scanner.next()
    }

    account = db.createAccount(user.nit, accountData(0), accountData(1))
    menu()
    true
  }

  // Función para inicio de sesión
  def logIn(): Boolean = {
    val questionsLogIn: Array[String] = Array("su usuario", "su contraseña")
    val data: scala.collection.mutable.ArrayBuffer[String] = scala.collection.mutable.ArrayBuffer.empty
    var repeatLogIn: String = ""

    for (question <- questionsLogIn) {
      println(s"Ingrese por favor $question")
      data += scanner.next()
    }

    account = db.getAccount(data(0), data(1))

    if (account == null) {
      if (attempts > 2) {
        println("Numero maximo de intentos!")
        return false
      }
      println("Usuario no encontrado, ¿Desea intentarlo de nuevo?")
      println("1.Si")
      println("Otro.No")
      repeatLogIn = scanner.next()
      if (repeatLogIn == "1") {
        attempts += 1
        return logIn()
      }
    }

    user = db.getUserById(account.owner)

    if (user == null) {
      println("Algo salió mal, usuario no encontrado")
      return false
    }

    menu()
    true
  }

  // Función para realizar un PQR
  def makePQR(): Unit = {
    scanner.nextLine() //Consume the last line
    println("Ingrese su PQR")
    val context: String = scanner.nextLine()
    println("Lamentamos mucho que haya tenido este inconveniente, lo resolveremos y nos comunicaremos tan rápido como sea posible con usted")
    db.PQR(user.nit, context)
  }

  // Función para cambiar la contraseña
  def changePassword(): Unit = {
    var newPassword: String = null
    do {
      println("Escribe tu nueva contraseña (Recuerda que deben ser solo numeros y de 4 cifras")
      newPassword = scanner.next()
    } while (!newPassword.matches("[0-9]+") || newPassword.length != 4)
    db.changePassword(account.username, newPassword)
    println("Contraseña correctamente guardada")
  }

  // Función para eliminar la cuenta
  def deleteAccount(): Unit = {
    var answer: Int = 0
    do {
      println("¿Está seguro que desea borrar su cuenta y toda su información")
      println("1.Si")
      println("2.No")
      answer = scanner.nextInt()
    } while (answer != 1 && answer != 2)
    if (answer == 1) {
      db.deleteAccount(account.number)
      println("Cuenta correctamente borrada")
      account = null
    } else {
      println("Proceso interrumpido")
    }
  }

  // Función para mostrar el menú de opciones
  def menu(): Boolean = {
    var option: Int = 0
    var exitLoop = false // Declaración de exitLoop

    do {
      println(s"Bienvenido ${user.name} ${user.surname}, estoy aquí para resolver tus dudas y inquietudes, selecciona una de las siguientes opciones:")
      println("0.Salir")
      println("1.Saldo en mi cuenta")
      println("2.Saldo pendiente")
      println("3.Realizar PQR")
      println("4.Cambiar contraseña")
      println("5.Cerrar cuenta")

      option = scanner.nextInt()

      if (option == 0) {
        exitLoop = true // Salir del bucle cuando option es 0
      } else {
        // Resto del código según las opciones seleccionadas
        option match {
          case 1 => account.getBalance()
          case 2 => account.getDebt()
          case 3 => makePQR()
          case 4 => changePassword()
          case 5 => deleteAccount()
          case _ =>
        }

        if (account == null) {
          exitLoop = true // Salir si la cuenta es nula
        } else {
          println("¿Hay algo más que pueda hacer por ti?")
          println("1.Si")
          println("Otro.No")

          option = scanner.nextInt()
          if (option != 1) {
            exitLoop = true // Salir si la opción no es 1
          }
        }
      }
    } while (!exitLoop)

    false
  }
}


