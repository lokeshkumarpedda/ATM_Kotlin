
import Abstraction.ATMInteractions
import implementation.*
import ui.ATMInteractionsImpl
import ui.MenuDisplay
import java.lang.Exception

fun main(args: Array<String>) {
    val atmMoney = MoneyStrategyImpl()
    val denominations = DenominationStrategyImpl()
    val customerTransactions = CustomerStrategyImpl()

    val atm = ATMInteractionsImpl(atmMoney, denominations,customerTransactions)
    displayMainMenu(atm)
}
private typealias ATM = ATMInteractions<Double, Customer, Denomination>
fun displayMainMenu(atm: ATM) {
    val menuList = listOf<MenuItem>(
        MenuItemImpl(1, "Load Cash to ATM"),
        MenuItemImpl(2, "Show Customer Details"),
        MenuItemImpl(3, "Show ATM Operations")
    )
    while (true) {
        val menu = MenuDisplay("Welcome to ATM", menuList)
        menu.display()
        val userEntered = readLine()!!.toIntOrNull()
        if (userEntered == null || userEntered > menuList.count() || userEntered <= 0) {
            println("Please enter valid input from above list")
            continue
        }
        when (userEntered) {
            1 -> {
                loadCashToATM(atm)
                blockUntilPressedEnter()
            }
            2 -> {
                showCustomerDetails(atm)
                blockUntilPressedEnter()
            }
            3 -> {
                showATMProcess(atm)
            }
            else -> {
                throw IllegalStateException("Please handle user entered inputs")
            }
        }
    }
}
private fun blockUntilPressedEnter(){
    println()
    print("press Enter to continue")
    readLine()
}


private fun loadCashToATM(atm: ATM) {
    println("Please Enter multiples of 1,00,000 to load")
    val userEnteredAmount = readLine()!!.toIntOrNull()
    if (userEnteredAmount == null || userEnteredAmount <= 0) {
        println("Please enter valid multiple")
        return
    }
    try {
        atm.addAmount(userEnteredAmount!!)
        println("Total Balance: ${atm.getTotalAmount()} \u20B9")
        println("Denominations:")
        for (denomination: Denomination in atm.getTotalDenominations()) {
            println(denomination.displayName() + ": " + denomination.count)
        }
    }catch (e: Exception){
        println(e.message)
    }
}

private fun showCustomerDetails(atm: ATM){
    println("Customer Details")
    println("Acc No\t Account Holder\t Pin Number \t Account Balance")
    for (customer: Customer in atm.getCustomers()){
        println("${customer.accNo}\t\t ${customer.name} \t\t ${customer.pin}\t\t\t ${customer.balance} ₹")
    }
}

private fun validateAccount(atm: ATM) : Customer?{
    print("Please Enter Account Number: ")
    val accNumber = readLine()!!.toLongOrNull() ?: return null
    print("Please Enter Pin Number: ")
    val pinEntered = readLine()!!.toIntOrNull() ?: return null
    return atm.getCustomer(accNumber,pinEntered)
}

private fun showATMProcess(atm: ATM){
    var customer = validateAccount(atm)
    if(customer == null){
        println("Please enter valid Account and Pin numbers")
        return
    }
    var withdrawCount = 0
    val menuList = listOf<MenuItem>(
        MenuItemImpl(1, "Check Balance"),
        MenuItemImpl(2, "Withdraw Money"),
        MenuItemImpl(3, "Transfer Money"),
        MenuItemImpl(4, "Mini Statement"),
        MenuItemImpl(5, "Back to Main Menu")
    )
    while (true) {
        val menu = MenuDisplay("ATM Process", menuList)
        menu.display()
        val userEntered = readLine()!!.toIntOrNull()
        if (userEntered == null || userEntered > menuList.count() || userEntered <= 0) {
            println("Please enter valid input from above list")
            continue
        }
        when (userEntered!!) {
            1 -> {
                println("Your Account Balance: ${customer.balance} ₹")
                blockUntilPressedEnter()
            }
            2 -> {
                if(withdrawCount == 2){
                    print("Please Enter Pin Number: ")
                    val pinEntered = readLine()!!.toIntOrNull()
                    if (pinEntered == null || customer.pin != pinEntered) {
                        println("Entered Pin is Invalid")
                        break
                    }
                    withdrawCount = 0
                }
                withdrawMoney(atm, customer)
                withdrawCount += 1
            }
            3 -> {
                tranferMoney(atm, customer)
            }
            4 -> {
                showMiniStatement(customer)
                blockUntilPressedEnter()
            }
            5 -> {
                break
            }
            else -> {
                throw IllegalStateException("Please handle user entered inputs")
            }
        }
    }
}

private fun withdrawMoney(atm:ATM, customer: Customer){
    print("Please enter amount to withdraw:")
    val userEntered = readLine()!!.toDoubleOrNull()
    if (userEntered == null || userEntered <= 0) {
        println("Please enter valid amount")
        return
    }
    try {
        val denominations = atm.withdrawAmount(userEntered!!,customer)
        println("Denominations:")
        for (denomination: Denomination in denominations) {
            println(denomination.displayName() + ": " + denomination.count)
        }
        blockUntilPressedEnter()

    }catch (e: Exception){
        println(e.message)
    }
}

private fun tranferMoney(atm: ATM, fromCustomer: Customer){
    print("Account Number to Transfer money: ")
    val accNumber = readLine()!!.toLongOrNull()
    if(accNumber == null){
        println("Please enter valid account number")
        return
    }
    val toCustomer = atm.getCustomer(accNumber)
    if(toCustomer == null){
        println("Enter account number is invalid.")
        return
    }
    println("Transferring to ${toCustomer.name}")
    print("Enter amount: ")
    val amount = readLine()!!.toDoubleOrNull()
    if (amount == null || amount <= 0) {
        println("Please enter valid amount")
        return
    }
    try {
        atm.transferAmount(fromCustomer, toCustomer,amount)
        println("Transferred $amount ₹ to $accNumber")
        println("Your account balance ${fromCustomer.balance} ₹")
        blockUntilPressedEnter()
    }catch (e: Exception){
        println(e.message)
    }
}

private fun showMiniStatement(customer: Customer){
    val transactions = customer.getRecentTransactions(5)
    if(transactions.isEmpty()){
        println("Doesn't have transactions to view.")
        return
    }
    println()
    println("*${"*".repeat(20)}*")
    println("*${" ".repeat(20)}*")
    println("*${" ".repeat(3)}Mini Statement${" ".repeat(3)}*")
    println("*${" ".repeat(20)}*")
    println("*${"*".repeat(20)}*")
    println()
    println("Transaction ID\t Transaction Remarks\t Transaction Type\t Transaction Amt")
    for (transaction: Transaction in transactions){
        println("\t${transaction.id}\t\t\t ${getRemarks(transaction)}\t\t ${getTransactionType(transaction)}\t\t\t ${transaction.amount} ₹")
    }
}
fun getRemarks(transaction: Transaction): String{
    return when(transaction.type){
        is TransactionType.DebitFromATM ->{
            "Debited ${transaction.amount} ₹ from ATM"
        }
        is TransactionType.TransferredFromAccount -> {
            "Funds transferred from Acc ${transaction.type.accNo}"
        }
        is TransactionType.TransferredToAccount -> {
            "Funds transferred to Acc ${transaction.type.accNo}"
        }
    }
}
fun getTransactionType(transaction: Transaction): String{
    return when(transaction.type){
        is TransactionType.DebitFromATM ->{
            "Debit"
        }
        is TransactionType.TransferredFromAccount -> {
            "Credit"
        }
        is TransactionType.TransferredToAccount -> {
            "Debit"
        }
    }
}


