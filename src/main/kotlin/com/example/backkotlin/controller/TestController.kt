package com.example.backkotlin.controller

import com.example.backkotlin.model.IntAndDouble
import com.fasterxml.jackson.annotation.JsonIgnore
import org.apache.poi.openxml4j.opc.internal.ContentType
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.core.io.InputStreamResource
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream
import java.lang.Exception
import javax.servlet.http.HttpServletResponse

@RestController
class TestController {

    @GetMapping("/test1")
    fun getString(): String {
        return "This is an example!!!"
    }

    @PostMapping("/binding")
    fun showData(@RequestBody iads: List<IntAndDouble>) {
        for (iad in iads) {
            println(iad)
        }
    }

    // https://grobmeier.solutions/generating-excel-sheets-with-spring-and-apache-poi.html
    @PostMapping("/excel")
    fun getExcelFile(@RequestBody iads: List<IntAndDouble>): ResponseEntity<InputStreamResource> {
        for (iad in iads) {
            println(iad)
        }
        println("Excel Start!")
        val file = File.createTempFile("testExcel_", ".xlsx")
        val wb = XSSFWorkbook()
        val sheet = wb.createSheet("sheet1")
        var row: Row?
//        var cell: Cell?
        var rowNum = 0

        // Header
        row = sheet.createRow(rowNum++)
        row.createCell(0).setCellValue("Mode")
        row.createCell(1).setCellValue("Affinity")
        row.createCell(2).setCellValue("lb")
        row.createCell(3).setCellValue("ub")
        row.createCell(4).setCellValue("url1")
        row.createCell(5).setCellValue("url2")

        // Body
        for(iad in iads) {
            row = sheet.createRow(rowNum++)
            row.createCell(0).setCellValue(iad.mode.toDouble())
            row.createCell(1).setCellValue(iad.affinity)
            row.createCell(2).setCellValue(iad.lb)
            row.createCell(3).setCellValue(iad.ub)
            row.createCell(4).setCellValue(iad.pUrl)
            row.createCell(5).setCellValue(iad.qUrl)
        }

        wb.write(file.outputStream())
        wb.close()

        println(file.absolutePath)

        val excelHeader = HttpHeaders().apply {
            contentType = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            contentDisposition = ContentDisposition.builder("attatchment").filename("example.xlsx").build()
        }

        return ResponseEntity.ok()
            .headers(excelHeader)
            .body(InputStreamResource(FileInputStream(file)))
    }

    @PostMapping("/excel2")
    fun getExcelFile2(@RequestBody iads: List<IntAndDouble>, response: HttpServletResponse): ResponseEntity<Any> {
        for (iad in iads) {
            println(iad)
        }
        println("Excel Start-2!")

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

        for(iad in iads) {
            row = sheet.createRow(rowNum++)
            row.createCell(0).setCellValue(iad.mode.toDouble())
            row.createCell(1).setCellValue(iad.affinity)
            row.createCell(2).setCellValue(iad.lb)
            row.createCell(3).setCellValue(iad.ub)
            row.createCell(4).setCellValue(iad.pUrl)
            row.createCell(5).setCellValue(iad.qUrl)
        }

        response.contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        response.setHeader("Content-Disposition", "attachment; filename=example.xlsx")

//        return ResponseEntity.ok()
//            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=example.xlsx")
//            .header(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
//            .body(wb.write(response.outputStream))

        try {
            println("Before Send!")
            return ResponseEntity.ok()
                .body(wb.write(response.outputStream))
        } catch (e: Exception) {
            println(e)
            throw e
        } finally {
            println("After Send!")
            wb.close()
        }
    }
}