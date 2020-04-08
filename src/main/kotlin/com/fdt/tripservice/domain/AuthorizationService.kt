

class AuthorizationService(private val roleService : RoleService) {

    fun canCreateTrip(val authInfo: String): Boolean {
        val roles = roleService.findRoleFrom(authInfo)
        //TODO
        return canCreateTripWith(roles)
    }

    private fun canCreateTripWith(val roles: : List<Role>): Boolean {
        return !listOf(DRIVER).intersect(roles).empty()
    }
}