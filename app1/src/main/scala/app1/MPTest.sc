
object session {

  case class User(val name: String)

  class UserRepository {
    def authenticate(user: User): User = user
    def create(newuser: String): Unit = println("Creating new User " + newuser)
    def delete(newuser: String): Unit = println("Deleting new User " + newuser)
  }

  class UserService {
    def authorization(user: User, service: String): Unit = {
      println(s"Authorizing user ${user} for service ${service}")
    }
  }

  trait UserRepositoryComponent {
    val userRepository: UserRepository
  }

  trait UserServiceComponent {
    this: UserRepositoryComponent =>
    val userService: UserService
  }


  object ComponentRegistry extends UserServiceComponent with UserRepositoryComponent {
    val userRepository = new UserRepository
    val userService = new UserService

    //userRepository.authenticate(User("Alex"))
  }

  ComponentRegistry.userRepository.authenticate(User("Alex"))

  val a: UserService = new UserService

  val predef

  val x: User = User("1")
  x.getClass.getMethods.toList.filter{
    n =>
      (n.getParameterTypes.size == 0)
  }

  case class AnotherUser(val name: String, val age: Int, val gender: String)

  //val b = AnotherUser("2", 2, "m")
  //val biters = b.productIterator
  //for (x <- b.productIterator) println (x)
  //for (x <- b.productPrefix) println (x)

  def cv(input: AnotherUser): Map[String, Any] = {
    val fieldNames = input.getClass.getDeclaredFields.map(_.getName)
    val fieldVals =  AnotherUser.unapply(input).get.productIterator.toSeq
    fieldNames.zip(fieldVals) toMap
  }

  val b = AnotherUser("2", 2, "m")
  val result = cv(b)

  result.keys


  //val m = AnotherUser.getClass.getDeclaredFields.map(_.getName -> biters.next()).toMap
  //println(s"${m}")
}