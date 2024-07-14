package top.kkoishi.pml.io

import java.io.IOException
import java.io.Reader

internal class ReaderCharIterator(private val reader: Reader): CharIterator() {
    private var nxtChar: Char? = null
    override fun hasNext(): Boolean {
        if (nxtChar != null)
            return true
        return try {
            val cb = CharArray(1)
            if (reader.read(cb, 0, 1) != -1) {
                nxtChar = cb[0]
                true
            } else false
        } catch (ioe: IOException) {
            false
        }
    }

    override fun nextChar(): Char {
        if (nxtChar != null) {
            val c = nxtChar!!
            nxtChar = null
            return c
        } else {
            try {
                val cb = CharArray(1)
                if (reader.read(cb, 0, 1) != -1) {
                    return cb[0]
                }
            } catch (ioe: IOException) {
                throw NoSuchElementException(ioe)
            }
            throw NoSuchElementException()
        }
    }
}