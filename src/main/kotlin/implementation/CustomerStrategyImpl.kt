package implementation

import Abstraction.CustomerStrategy
import java.util.TreeMap
import kotlin.jvm.Throws

data class Customer(val accNo: Long,
                    val name: String,
                    val pin: Int,
                    var balance: Double){
    var transactions: MutableList<Transaction> = mutableListOf()

    fun getRecentTransactions(count: Int) : List<Transaction>{
        if (transactions.size <= count){
            return transactions
        }
        return transactions.subList(transactions.size - count, transactions.size)
    }
}
sealed class TransactionType{
    object DebitFromATM:TransactionType()
    class TransferredToAccount(val accNo: Long): TransactionType()
    class TransferredFromAccount(val accNo: Long): TransactionType()
}
data class Transaction(val id: Long, val amount: Double, val type: TransactionType)



class CustomerStrategyImpl : CustomerStrategy<Customer, Double> {
    private var customerList: TreeMap<Long,Customer> = TreeMap<Long,Customer>()
    private var transactionId = 1L

    init {
        customerList[101] =   Customer(101, "Suresh", 2343,25234.0)
        customerList[102] =   Customer(102, "Ganesh", 5432,24123.0)
        customerList[103] =   Customer(103, "Magesh", 7854,26100.0)
        customerList[104] =   Customer(104, "Naresh", 2345,80000.0)
        customerList[105] =   Customer(105, "Harish", 1907,103400.0)
    }

    override fun getCustomerList(): List<Customer> = customerList.values.toList()

    override fun getCustomer(accNo: Long): Customer?{
        return customerList[accNo]
    }
    override fun getCustomer(accNo: Long, pin: Int): Customer?{
        if(getCustomer(accNo)?.pin == pin){
            return getCustomer(accNo)
        }
        return null
    }

    @Throws
    override fun deductAmount(amount: Double,customer: Customer){
        if(customer.balance < amount){
            throw IllegalArgumentException("Your account doesn't have enough funds to Withdraw.")
        }
        customer.balance -= amount
        customer.transactions.add(Transaction(transactionId, amount,TransactionType.DebitFromATM))
        transactionId += 1
    }

    @Throws
    override fun transferAmount(from: Customer, to: Customer, amount: Double){
        if(from.balance < amount){
            throw IllegalArgumentException("Your account doesn't have enough funds to this transfer.")
        }
        from.balance -= amount
        to.balance += amount

        from.transactions.add(Transaction(transactionId, amount,TransactionType.TransferredToAccount(to.accNo)))
        to.transactions.add(Transaction(transactionId, amount,TransactionType.TransferredFromAccount(from.accNo)))
        transactionId += 1
    }
}