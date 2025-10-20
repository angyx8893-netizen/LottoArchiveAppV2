package com.example.lottoarchive.scripting

import com.example.lottoarchive.repo.LottoRepository
import kotlinx.coroutines.runBlocking
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable

class ScriptHost(private val repo: LottoRepository) {

    fun runJs(code: String): String {
        val cx = Context.enter()
        return try {
            val scope: Scriptable = cx.initStandardObjects()

            val helper = Helper(repo)
            scope.put("print", scope) { args: Array<out Any?> -> helper.print(args) }
            scope.put("countOccurrences", scope) { args: Array<out Any?> -> helper.countOccurrences(args) }
            scope.put("randomNumbers", scope) { args: Array<out Any?> -> helper.randomNumbers(args) }
            scope.put("frequency", scope) { args: Array<out Any?> -> helper.frequency(args) }
            scope.put("lastSeen", scope) { args: Array<out Any?> -> helper.lastSeen(args) }
            scope.put("topFrequencies", scope) { args: Array<out Any?> -> helper.topFrequencies(args) }
            scope.put("comboFrequency", scope) { args: Array<out Any?> -> helper.comboFrequency(args) }

            cx.evaluateString(scope, code, "user.js", 1, null)
            helper.getOutput()
        } catch (t: Throwable) {
            "Errore: ${t.message}"
        } finally {
            Context.exit()
        }
    }

    fun runVbsLike(code: String): String {
        var js = code.replace("\r\n", "\n")

        // comments
        js = js.replace(Regex("(?m)^\\s*'"), "//")
        js = js.replace("'", "//")

        // MsgBox / WScript.Echo -> print
        js = js.replace(Regex("\\bMsgBox\\b"), "print")
        js = js.replace(Regex("\\bWScript\\.Echo\\b"), "print")

        // String concatenation & -> +
        js = js.replace("&", "+")

        // CInt/CLng -> parseInt
        js = js.replace(Regex("\\bCInt\\("), "parseInt(")
        js = js.replace(Regex("\\bCLng\\("), "parseInt(")

        // Split -> .split
        js = js.replace(Regex("\\bSplit\\("), "String(") // Split(a,b) => String(a).split(b) - handle simply
        js = js.replace(Regex("\\)\\s*,\\s*"), ").split(")

        // UBound/LBound (very naive)
        js = js.replace(Regex("\\bUBound\\(([^)]+)\\)"), "(($1).length-1)")
        js = js.replace(Regex("\\bLBound\\(([^)]+)\\)"), "(0)")

        // Replace VB True/False/Nothing
        js = js.replace(Regex("\\bTrue\\b"), "true")
        js = js.replace(Regex("\\bFalse\\b"), "false")
        js = js.replace(Regex("\\bNothing\\b"), "null")

        // Remove Dim/Set keywords
        js = js.replace(Regex("\\bDim\\b"), "")
        js = js.replace(Regex("\\bSet\\b"), "")

        return runJs(js)
    }

    class Helper(private val repo: LottoRepository) {
        private val sb = StringBuilder()

        fun getOutput(): String = sb.toString()
        fun print(args: Array<out Any?>): Any? { sb.append(args.joinToString(" ")).append("\n"); return null }

        fun countOccurrences(args: Array<out Any?>): Any? {
            val number = (args.getOrNull(0) as? Number)?.toInt() ?: return "NaN"
            val wheel = args.getOrNull(1)?.toString()
            val count = runBlocking { repo.countOccurrences(number, wheel) }
            sb.append(count).append("\n")
            return count
        }

        fun frequency(args: Array<out Any?>): Any? = countOccurrences(args)

        fun lastSeen(args: Array<out Any?>): Any? {
            val number = (args.getOrNull(0) as? Number)?.toInt() ?: return "NaN"
            val wheel = args.getOrNull(1)?.toString()
            val last = runBlocking { repo.lastDateForNumber(number, wheel) } ?: "mai"
            sb.append(last).append("\n")
            return last
        }

        fun topFrequencies(args: Array<out Any?>): Any? {
            val n = (args.getOrNull(0) as? Number)?.toInt() ?: 10
            val wheel = args.getOrNull(1)?.toString()
            val list = runBlocking { repo.topFrequencies(n, wheel) }
            val text = list.joinToString("\n") { "${it.first}: ${it.second}" }
            sb.append(text).append("\n")
            return text
        }

        fun comboFrequency(args: Array<out Any?>): Any? {
            val arr = (args.getOrNull(0) as? String)?.split(",")?.mapNotNull { it.trim().toIntOrNull() }
                ?: args.mapNotNull { (it as? Number)?.toInt() }
            val wheel = args.getOrNull(1)?.toString()
            val c = runBlocking { repo.comboFrequency(arr, wheel) }
            sb.append(c).append("\n")
            return c
        }

        fun randomNumbers(args: Array<out Any?>): Any? {
            val n = (args.getOrNull(0) as? Number)?.toInt() ?: 5
            val pool = (1..90).toMutableList()
            pool.shuffle()
            val nums = pool.take(n).sorted()
            sb.append(nums.joinToString(",")).append("\n")
            return nums.joinToString(",")
        }
    }
}
