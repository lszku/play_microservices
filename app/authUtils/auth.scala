package authUtils


case class User(name: String,
                password: String,
                textArea: String = "")

object auth {


  def isValidUser(user: String) = {

    user == "admin"
  }

  def check(user: String, password: String) = {
    true
  }

}

