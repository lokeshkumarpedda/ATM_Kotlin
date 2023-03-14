package ui

import Abstraction.*
import implementation.*
import kotlin.jvm.Throws

class ATMInteractionsImpl constructor(private val moneyRepo: MoneyStrategy<Double>,
                      private val denominationRepo: DenominationStrategy<Denomination,Double>,
                      private val customerRepository: CustomerStrategy<Customer, Double>
): ATMInteractions<Double,Customer, Denomination> {
    private val WITHDRAW_MIN_AMOUNT = 100
    private val WITHDRAW_MAX_AMOUNT = 10000
    private val TRANSFER_MIN_AMOUNT = 100
    private val TRANSFER_MAX_AMOUNT = 10000

    @Throws
    override fun addAmount(count: Int){
        val money : Double = count * 100000.0
        moneyRepo.add(money)
        denominationRepo.addDenominations(count)
    }

    override fun getTotalAmount(): Double{
        return moneyRepo.getMoney()
    }

    override fun getTotalDenominations(): List<Denomination>{
        return denominationRepo.getAllDenominations()
    }

    override fun getCustomers(): List<Customer>{
        return customerRepository.getCustomerList()
    }

    override fun getCustomer(accNo: Long, pin: Int): Customer?{
        return customerRepository.getCustomer(accNo, pin)
    }
    override fun getCustomer(accNo: Long): Customer?{
        return customerRepository.getCustomer(accNo)
    }

    @Throws
    override fun withdrawAmount(amount: Double, customer: Customer): List<Denomination>{
        if(amount % 100 != 0.0){
            throw IllegalArgumentException("Please enter multiples of hundred")
        }
        if(amount > WITHDRAW_MAX_AMOUNT){
            throw IllegalArgumentException("Withdraw limit per transaction is $WITHDRAW_MAX_AMOUNT," +
                    " Please withdraw according to Limit.")
        }
        if(amount < WITHDRAW_MIN_AMOUNT){
            throw IllegalArgumentException("Minimum Withdraw amount is $WITHDRAW_MIN_AMOUNT")
        }

        if(moneyRepo.getMoney() < amount){
            throw IllegalArgumentException("Insufficient Balance in the ATM.")
        }

        if(customer.balance < amount){
            throw IllegalArgumentException("Your account doesn't have enough funds to Withdraw.")
        }
        val denominations = denominationRepo.withDrawDenominations(amount)
        moneyRepo.deduct(amount)
        customerRepository.deductAmount(amount,customer)
        return denominations
    }

    @Throws
    override fun transferAmount(from: Customer, to: Customer, amount: Double){
        if(amount > TRANSFER_MAX_AMOUNT){
            throw IllegalArgumentException("Transfer limit per transaction is $WITHDRAW_MAX_AMOUNT," +
                    " Please transfer according to Limit.")
        }
        if(amount < TRANSFER_MIN_AMOUNT){
            throw IllegalArgumentException("Minimum Transfer amount is $WITHDRAW_MIN_AMOUNT")
        }
        customerRepository.transferAmount(from,to, amount)
    }
}