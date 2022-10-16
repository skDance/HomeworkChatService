package Exception

import java.lang.RuntimeException

class MessageNotFoundException (message: String): RuntimeException(message)