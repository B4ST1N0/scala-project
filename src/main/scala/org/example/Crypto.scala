package org.example

import java.security.{MessageDigest, Security}
import javax.crypto.spec.SecretKeySpec
import org.apache.commons.codec.binary.Base64
import java.util.Arrays
import javax.crypto.Cipher


object Crypto {
  private val secretKey = "nubanksecretkey"

  def encrypt(text: String): String = {
    var base64EncryptedString = ""

    try {
      val md = MessageDigest.getInstance("MD5")
      val digestOfPassword = md.digest(secretKey.getBytes("utf-8"))
      val keyBytes = Arrays.copyOf(digestOfPassword, 24)

      val key = new SecretKeySpec(keyBytes, "DESede")
      val cipher = Cipher.getInstance("DESede")
      cipher.init(Cipher.ENCRYPT_MODE, key)

      val plainTextBytes = text.getBytes("utf-8")
      val buf = cipher.doFinal(plainTextBytes)
      val base64Bytes = Base64.encodeBase64(buf)
      base64EncryptedString = new String(base64Bytes, "utf-8")

    } catch {
      case ex: Exception => println(ex)
    }
    base64EncryptedString
  }

  def decrypt(encryptedText: String): String = {
    var decryptedString = ""

    try {
      val message = Base64.decodeBase64(encryptedText.getBytes("utf-8"))
      val md = MessageDigest.getInstance("MD5")
      val digestOfPassword = md.digest(secretKey.getBytes("utf-8"))
      val keyBytes = Arrays.copyOf(digestOfPassword, 24)
      val key = new SecretKeySpec(keyBytes, "DESede")

      val decipher = Cipher.getInstance("DESede")
      decipher.init(Cipher.DECRYPT_MODE, key)

      val plainText = decipher.doFinal(message)
      decryptedString = new String(plainText, "utf-8")

    } catch {
      case ex: Exception => println(ex)
    }
    decryptedString
  }

  def main(args: Array[String]): Unit = {
    // Main method content, if needed
  }
}
