package org.example

import java.io.File
import kotlin.math.ceil

fun main(args: Array<String>) {

    validateInput(args = args)?.let { inputData ->
        println("dateFrom: ${inputData.dateFrom}")
        println("dateTo: ${inputData.dateTo}")
        println("interval: ${inputData.interval}")
        println("directory: ${inputData.dir}")
    }

}

fun validateInput(args: Array<String>): InputData? =
    //-from -to -interval -dir
    try {
        val dateFrom = args[args.indexOf("-from") + 1]
        val dateTo = args[args.indexOf("-to") + 1]
        val interval = args[args.indexOf("-interval") + 1]
        val dir = args[args.indexOf("-dir") + 1]

        val regexTimestamp = """\d{10}""".toRegex()
        val diff = dateTo.toLong() - dateFrom.toLong()
        val numberOfDirs = ceil((diff / interval.toLong()).toDouble()).toInt()
        val file = File(dir)

        if (!regexTimestamp.matches(dateFrom) && regexTimestamp.matches(dateTo)) {
            throw Exception("\"from\" and to should be a UNIX timestamp in seconds, something like 1727077308 which is Sep 23 2024 07:41:48 GMT+0000")
        }
        if (diff < 0) {
            throw Exception("\"from\" should be earlier(smaller) than \"to\"")
        }
        if (interval.toLong() > diff){
            throw Exception("\"interval\" overlaps \"to\"")
        }
        if(!file.canRead() && !file.canWrite()){
            throw Exception("Can't read/write to \"dir\"")
        }
        println("$numberOfDirs directories will be created.")


        InputData(
            dateFrom = dateFrom,
            dateTo = dateTo,
            interval = interval,
            dir = dir
        )

    } catch (e: Exception) {
        println("$e\n")
        null
    }

data class InputData(
    val dateFrom: String,
    val dateTo: String,
    val interval: String,
    val dir: String
)
