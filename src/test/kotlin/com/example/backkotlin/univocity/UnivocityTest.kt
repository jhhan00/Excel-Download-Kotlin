package com.example.backkotlin.univocity

import com.univocity.parsers.tsv.TsvParser
import com.univocity.parsers.tsv.TsvParserSettings
import org.junit.jupiter.api.Test
import java.io.BufferedReader
import java.io.FileReader

class UnivocityTest {

    @Test
    fun univocityTest() {
        val startTime = System.currentTimeMillis()
        val fileReader = FileReader("C:\\test1\\drug_id_to_gene_full_202101291704.csv")
        val bufferedReader = BufferedReader(fileReader)

        val tsvParserSetting = TsvParserSettings()
        tsvParserSetting.format.setLineSeparator("\r\n")
        val tsvParser = TsvParser(tsvParserSetting)

        val list = tsvParser.parseAll(bufferedReader)
//        for (data in list) {
//            println(data[0])
//        }
        val endTime = System.currentTimeMillis()
        println("Execute Time(ms) : " + (endTime - startTime))
        // 10.5MB 정도 되는 파일은 120~130ms 정도가 걸리는 듯 하다.
    }
}