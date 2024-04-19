package org.study.kotlinmemberservice.common.enums

enum class RoleEnum(
  val action: String
) {
  CREATE("create"),
  READ("read"),
  UPDATE("update"),
  DELETE("delete");
  
  companion object{
    val CREATION = "create"
    fun action(roleEnum: RoleEnum): String{
      return roleEnum.action
    }
  }
}