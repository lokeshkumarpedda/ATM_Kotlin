package implementation
import Abstraction.DenominationStrategy
import kotlin.jvm.Throws
import kotlin.math.roundToInt



data class Denomination(val actualValue: Int, var count: Int){
    fun displayName(): String{
        return actualValue.toString()
    }
}

class DenominationStrategyImpl: DenominationStrategy<Denomination,Double> {
    var denomination_1000 = Denomination(1000,0)
    var denomination_500 = Denomination(500,0)
    var denomination_100 = Denomination(100,0)

    override fun addDenominations(count: Int):  List<Denomination>{
        denomination_1000.count += (count * 20.0).roundToInt()
        denomination_500.count += (count * 50.0).roundToInt()
        denomination_100.count += (count * 30.0).roundToInt()
        return getAllDenominations()
    }

    override fun getAllDenominations(): List<Denomination> {
        return listOf(denomination_1000, denomination_500, denomination_100)
    }

    @Throws
    override fun withDrawDenominations(amount: Double): List<Denomination>{
        val denominations = getDenominations(amount)
        for (denomination: Denomination in denominations){
            if(denomination.actualValue == denomination_1000.actualValue){
                if(denomination.count > denomination_1000.count){
                    throw IllegalStateException("Insufficient balance.")
                }
            }
            if(denomination.actualValue == denomination_500.actualValue){
                if(denomination.count > denomination_1000.count){
                    throw IllegalStateException("Insufficient balance.")
                }
            }
            if(denomination.actualValue == denomination_1000.actualValue){
                if(denomination.count > denomination_1000.count){
                    throw IllegalStateException("Insufficient balance.")
                }
            }
        }
        for (denomination: Denomination in denominations){
            if(denomination.actualValue == denomination_1000.actualValue){
                denomination_1000.count -= denomination.count
            }
            if(denomination.actualValue == denomination_500.actualValue){
                denomination_500.count -= denomination.count
            }
            if(denomination.actualValue == denomination_100.actualValue){
                denomination_100.count -= denomination.count
            }
        }
        return denominations
    }

    private fun getDenominations(amount: Double): List<Denomination>{
        if(amount <= 5000){
            if(amount <= 1000){
                return listOf(Denomination(denomination_100.actualValue,
                    amount.toInt() / denomination_100.actualValue))
            }
            var denominations: MutableList<Denomination> = mutableListOf()
            denominations.add(Denomination(denomination_1000.actualValue, 1))
            var remainingAmount = amount - 1000
            if (remainingAmount-1000 < 0){
                denominations.add(Denomination(denomination_100.actualValue,
                    remainingAmount.toInt() / denomination_100.actualValue))
                return denominations
            }
            remainingAmount = amount - 1000
            val neededHundredNotes = (remainingAmount % 100).toInt()
            denominations.add(Denomination(denomination_100.actualValue, 10 + neededHundredNotes))
            denominations.add(Denomination(denomination_500.actualValue,
                (remainingAmount - (neededHundredNotes*100)).toInt() / denomination_500.actualValue))
            return denominations
        }else{
            var thousandNotes = 3
            var fiveHundredNotes = 2
            var hundredNotes = 10
            var remainingAmount = amount - 5000
            val neededHundredNotes = (remainingAmount % 100).toInt()
            hundredNotes += neededHundredNotes
            fiveHundredNotes += (remainingAmount - (neededHundredNotes*100)).toInt() / denomination_500.actualValue

            return listOf(Denomination(denomination_1000.actualValue, thousandNotes),
                Denomination(denomination_500.actualValue, fiveHundredNotes),
                Denomination(denomination_100.actualValue, hundredNotes))
        }
    }
}
