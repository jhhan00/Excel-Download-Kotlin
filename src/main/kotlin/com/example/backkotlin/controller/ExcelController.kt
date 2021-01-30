package com.example.backkotlin.controller

import com.example.backkotlin.model.IntAndDouble
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.lang.Exception
import javax.servlet.http.HttpServletResponse

@RestController
class ExcelController {

    @PostMapping("/excel3")
    fun getExcelFile3(@RequestBody iadList: List<IntAndDouble>, response: HttpServletResponse) {
        println("Excel Start-3!")
        for (iad in iadList) {
            println(iad)
        }

        val wb = XSSFWorkbook()
        val sheet = wb.createSheet("sheet1")
        var row: Row?
        var rowNum = 0

        row = sheet.createRow(rowNum++)
        row.createCell(0).setCellValue("Mode")
        row.createCell(1).setCellValue("Affinity")
        row.createCell(2).setCellValue("lb")
        row.createCell(3).setCellValue("ub")
        row.createCell(4).setCellValue("url1")
        row.createCell(5).setCellValue("url2")

        for(iad in iadList) {
            row = sheet.createRow(rowNum++)
            row.createCell(0).setCellValue(iad.mode.toDouble())
            row.createCell(1).setCellValue(iad.affinity)
            row.createCell(2).setCellValue(iad.lb)
            row.createCell(3).setCellValue(iad.ub)
            row.createCell(4).setCellValue(iad.pUrl)
            row.createCell(5).setCellValue(iad.qUrl)
        }

        try {
            response.contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            response.setHeader("Content-Disposition", "attachment; filename=\"example.xlsx\"")
            wb.write(response.outputStream)
        } catch (e: Exception) {
            println(e)
            throw e
        } finally {
            wb.close()
        }
    }
}