package org.example

import java.sql._
import scala.util.control.Breaks._

class Database {
  private val JDBC_URL: String = "jdbc:mysql://localhost:3306/users"
  private val JDBC_USER: String = "root"
  private val JDBC_PASSWORD: String = ""

  private var connection: Connection = _

  try {
    connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)
  } catch {
    case e: SQLException => e.printStackTrace()
  }

  def closeConnection(): Unit = {
    try {
      if (connection != null) connection.close()
    } catch {
      case e: SQLException => e.printStackTrace()
    }
  }

  def getAccount(username: String, password: String): Account = {
    val cryptedPassword = Crypto.encrypt(password)
    var account: Account = null
    val selectQuery = s"SELECT number,owner_nit,debt,balance,active FROM accounts WHERE username = '${username.toUpperCase}' AND password = '$cryptedPassword'"
    try {
      val preparedStatement = connection.prepareStatement(selectQuery)
      val resultSet = preparedStatement.executeQuery()

      if (resultSet.next() && resultSet.getString("active") == "1") {
        val number = resultSet.getString("number")
        val owner_nit = resultSet.getString("owner_nit")
        val debt = resultSet.getFloat("debt")
        val balance = resultSet.getFloat("balance")
        account = new Account(number, owner_nit, username, balance, debt)
      }

      resultSet.close()
      preparedStatement.close()
    } catch {
      case e: SQLException => e.printStackTrace()
    }
    account
  }

  def changePassword(username: String, password: String): Unit = {
    val cryptedPassword = Crypto.encrypt(password)
    val insertQuery = "UPDATE accounts SET password = ? WHERE username = ?"
    try {
      val preparedStatement = connection.prepareStatement(insertQuery)
      preparedStatement.setString(1, cryptedPassword)
      preparedStatement.setString(2, username.toUpperCase())
      preparedStatement.executeUpdate()
    } catch {
      case e: SQLException => e.printStackTrace()
    }
  }

  def deleteAccount(accountNumber: String): Unit = {
    val insertQuery = "UPDATE accounts SET active = 0 WHERE number = ?"
    try {
      val preparedStatement = connection.prepareStatement(insertQuery)
      preparedStatement.setString(1, accountNumber)
      preparedStatement.executeUpdate()
    } catch {
      case e: SQLException => e.printStackTrace()
    }
  }

  def getAccountNumber(): String = {
    var randomNumber: Long = 0L
    var randomNumberString: String = ""
    do {
      randomNumber = Math.round((Math.random() * 9000000000L + 1000000000L))
      randomNumberString = randomNumber.toString
    } while (accountNumberExist(randomNumberString))
    randomNumberString
  }

  def PQR(nit: String, context: String): Unit = {
    val insertQuery = "INSERT INTO pqrs (nit, context) VALUES (? , ?)"
    try {
      val preparedStatement = connection.prepareStatement(insertQuery)
      preparedStatement.setString(1, nit)
      preparedStatement.setString(2, context)
      preparedStatement.executeUpdate()
    } catch {
      case e: SQLException => e.printStackTrace()
    }
  }

  def createAccount(nit: String, username: String, password: String): Account = {
    var account: Account = null
    val number = getAccountNumber()
    val insertQuery = "INSERT INTO accounts (number, owner_nit, username, password) VALUES (? , ? , ?, ?)"
    try {
      val preparedStatement = connection.prepareStatement(insertQuery)
      preparedStatement.setString(1, number)
      preparedStatement.setString(2, nit)
      preparedStatement.setString(3, username.toUpperCase())
      preparedStatement.setString(4, password)

      if (preparedStatement.executeUpdate() > 0) {
        account = new Account(number, nit, username, 0, 0)
      }
    } catch {
      case e: SQLException =>
        e.printStackTrace()
        return null
    }
    account
  }

  def usernameExist(username: String): Boolean = {
    val selectQuery = s"SELECT username FROM accounts WHERE username = '${username.toUpperCase}'"
    var exist = false
    try {
      val preparedStatement = connection.prepareStatement(selectQuery)
      val resultSet = preparedStatement.executeQuery()
      exist = resultSet.next()
      resultSet.close()
      preparedStatement.close()
    } catch {
      case e: SQLException => e.printStackTrace()
    }
    exist
  }

  def accountNumberExist(accountNumber: String): Boolean = {
    val selectQuery = s"SELECT number FROM accounts WHERE number = '$accountNumber'"
    var exist = false
    try {
      val preparedStatement = connection.prepareStatement(selectQuery)
      val resultSet = preparedStatement.executeQuery()
      exist = resultSet.next()
      resultSet.close()
      preparedStatement.close()
    } catch {
      case e: SQLException => e.printStackTrace()
    }
    exist
  }

  def getUserById(nit: String): User = {
    var user: User = null
    val selectQuery = s"SELECT name,surname,email FROM users WHERE nit = $nit"
    try {
      val preparedStatement = connection.prepareStatement(selectQuery)
      val resultSet = preparedStatement.executeQuery()

      if (resultSet.next()) {
        val name = resultSet.getString("name")
        val surname = resultSet.getString("surname")
        val email = resultSet.getString("email")
        user = new User(nit, name, surname, email)
      }

      resultSet.close()
      preparedStatement.close()
    } catch {
      case e: SQLException => e.printStackTrace()
    }
    user
  }

  def createUser(nit: String, name: String, surname: String, email: String): User = {
    var user: User = null
    val insertQuery = "INSERT INTO users (nit,name,surname,email) VALUES (? , ? , ?, ?)"
    try {
      val preparedStatement = connection.prepareStatement(insertQuery)
      preparedStatement.setString(1, nit)
      preparedStatement.setString(2, name.toUpperCase())
      preparedStatement.setString(3, surname.toUpperCase())
      preparedStatement.setString(4, email.toUpperCase())

      if (preparedStatement.executeUpdate() > 0) {
        user = new User(nit.toUpperCase(), name.toUpperCase(), surname.toUpperCase(), email.toUpperCase())
      }

    } catch {
      case e: SQLException =>
        e.printStackTrace()
        return null
    }
    user
  }

  // Resto del código para las operaciones de la base de datos...

  def main(args: Array): Unit = {
    // Main method content, if needed
  }
}
