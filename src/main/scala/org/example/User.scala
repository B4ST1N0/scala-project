package org.example

class User( var nit: String, var name: String, var surname: String, var email: String) {

  def getNit: String = nit

  def setNit(nit: String): Unit = {
    this.nit = nit
  }

  def getName: String = name

  def setName(name: String): Unit = {
    this.name = name
  }

  def getSurname: String = surname

  def setSurname(surname: String): Unit = {
    this.surname = surname
  }

}

object User {
  def apply(nit: String, name: String, surname: String, email: String): User = new User(email, nit, name, surname)
}
