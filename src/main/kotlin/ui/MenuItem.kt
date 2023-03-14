interface MenuItem{
    fun displayName(): String
}
data class MenuItemImpl constructor(val id: Int, val name: String): MenuItem{
    override fun displayName(): String {
        return "$id.$name"
    }
}