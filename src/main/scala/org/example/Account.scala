package org.example
import java.text.NumberFormat
import java.util.Locale
import java.text.NumberFormat

class Account(var number: String = null, var owner: String = null, var balance: Float = 0.0f, var debt: Float = 0.0f, var username: String = null, var currencyInstance: NumberFormat = NumberFormat.getCurrencyInstance())
{
  def getNumber() : String=
  {
    return number
  }
  def setNumber(number : String) : Unit=
  {
    this.number = number
  }
  def getOwner() : String=
  {
    return owner
  }
  def setOwner(owner : String) : Unit=
  {
    this.owner = owner
  }
  def getBalance(): Float = {
    val currencyInstance = NumberFormat.getCurrencyInstance(new Locale("es", "CO"))
    println("Su saldo actual es de " + currencyInstance.format(balance))
    balance
  }
  def setBalance(balance : Float) : Unit=
  {
    this.balance = balance
  }
  def getDebt(): Float = {
    println("Su crédito actual es de " + debt)
    debt
  }
  def setDebt(debt : Float) : Unit=
  {
    this.debt = debt
  }
  def getUsername() : String=
  {
    return username
  }
  def setUsername(username : String) : Unit=
  {
    this.username = username
  }
  def this(number : String, owner : String, username : String, balance : Float, debt : Float)
  {
    this(number, owner, balance, debt, username, null)
  }
}
