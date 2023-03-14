package ui

import MenuItem

class MenuDisplay constructor(private val title: String, private val menuList: List<MenuItem>){
    fun display(){
        val length = if(title.length < 30) 30 else title.length
        val spaceForName = (length-title.length)/2
        println()
        println("*${"*".repeat(length)}*")
        println("*${" ".repeat(length)}*")
        println("*${" ".repeat(spaceForName)}$title${" ".repeat(spaceForName)}*")
        println("*${" ".repeat(length)}*")
        println("*${"*".repeat(length)}*")
        println()
        for(menu in menuList){
            println(menu.displayName())
        }
        println("Please choose to continue..")
    }
}