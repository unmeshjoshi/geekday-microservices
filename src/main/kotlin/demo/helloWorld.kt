package demo

import com.geekday.accounting.account.domain.Account
import java.util.*

class CustomerNames(val account: Account) {

    val names = ArrayList<String?>()

    fun addName(name: String?) {
        names.add(name)
    }

    fun toCsv(): String {
        return "${account.customerName} ${names.filterNotNull().joinToString(separator = " and ")}"
    }
}