package com.example.demo.controller

import com.example.demo.view.MainView
import tornadofx.*
import java.io.BufferedReader
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class MainViewController: Controller() {

    val view: MainView by inject()

    /**
    * Open every file for AST parsing and send class breakdown for test generation
    */
    fun walk(path: String) {
        Files.walk(Paths.get(path)).use { allFiles ->
            allFiles.filter { path -> path.toString().endsWith(".kt") }
                    .forEach {path ->
                        val file = File(path.toUri())
                        readFiles(file, path.toUri().path)
                    }
        }
    }

    /**
     * Read file and start analyzing file with AST parsing
     */
    private fun readFiles(file: File, path: String) {
        val fileText = file.bufferedReader().use(BufferedReader::readText)


        if (filterFiles(fileText)) {
            view.console.items.add(fileText)
            view.console.items.add("===================================================================")
        }
    }

    /**
     * Filter Kotlin files that are not Test files or Styles
     */
    private fun filterFiles(fileText: String): Boolean {
        return !fileText.contains("ApplicationTest()")
                && !fileText.contains("src/test")
                && !fileText.contains("@Test")
                && !fileText.contains("class Styles")
    }
}