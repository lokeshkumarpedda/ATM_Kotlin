package Abstraction


interface ATMInteractions<M,C,D>: ATMMoneyOperations<M>,
    DenominationOperations<D>,
    CustomerOperations<C>,
    ATMOperations<M,C,D>{}

interface ATMMoneyOperations<M>{
    @kotlin.jvm.Throws
    fun addAmount(count: Int)
    fun getTotalAmount(): M
}

interface DenominationOperations<D>{
    fun getTotalDenominations(): List<D>
}

interface CustomerOperations<C>{
    fun getCustomers(): List<C>
    fun getCustomer(accNo: Long, pin: Int): C?
    fun getCustomer(accNo: Long): C?
}

interface ATMOperations<M,C,D>{
    @kotlin.jvm.Throws
    fun withdrawAmount(amount: M, customer: C): List<D>
    @kotlin.jvm.Throws
    fun transferAmount(from: C, to: C, amount: M)

}




interface MoneyStrategy<M>{
    @Throws
    fun add(money: M)
    @Throws
    fun deduct(money: M)
    fun getMoney(): M
}

interface CustomerStrategy<C,M>{
    fun getCustomerList(): List<C>
    fun getCustomer(accNo: Long): C?
    fun getCustomer(accNo: Long, pin: Int): C?
    @kotlin.jvm.Throws
    fun deductAmount(amount: M,customer: C)
    @kotlin.jvm.Throws
    fun transferAmount(from: C, to: C, amount: M)
}

interface DenominationStrategy<D,M>{
    fun addDenominations(count: Int) : List<D>
    @kotlin.jvm.Throws
    fun withDrawDenominations(amount: M): List<D>
    fun getAllDenominations(): List<D>
}