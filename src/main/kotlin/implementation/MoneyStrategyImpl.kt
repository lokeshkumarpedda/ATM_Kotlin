import Abstraction.MoneyStrategy

class MoneyStrategyImpl : MoneyStrategy<Double> {
    private var totalMoney: Double = 0.0

    @Throws
    override fun add(money: Double){
        if (money <= 0){
            throw IllegalArgumentException("Please enter positive amount of money.")
        }
        if(money % 100 != 0.0){
            throw IllegalArgumentException("Please enter multiples of hundred")
        }
        totalMoney += money
    }

    @Throws
    override fun deduct(money: Double){
        if (money <= 0){
            throw IllegalArgumentException("Please enter  positive amount of money.")
        }
        if(money > totalMoney){
            throw IllegalArgumentException("Insufficient Balance.")
        }
        totalMoney -= money

    }

    override fun getMoney(): Double = totalMoney

}