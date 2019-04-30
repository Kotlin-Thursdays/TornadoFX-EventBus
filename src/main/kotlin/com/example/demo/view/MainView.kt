package com.example.demo.view

import com.example.demo.app.Styles
import com.example.demo.controller.MainViewController
import javafx.geometry.Pos
import javafx.scene.control.ListView
import javafx.scene.layout.HBox
import tornadofx.*
import java.io.File

class ReadFilesRequest(val file: File) : FXEvent(EventBus.RunOn.BackgroundThread)

class MainView : View() {

    val controller: MainViewController by inject()

    val consolePath = System.getProperty("os.name") + " ~ " + System.getProperty("user.name") + ": "
    lateinit var console: ListView<String>
    lateinit var overlay: HBox

    override val root = stackpane {
        vbox {
            prefWidth = 800.0
            prefHeight = 600.0

            hbox {
                // imageview("tornado-fx-logo.png")
                hboxConstraints {
                    marginLeftRight(20.0)
                    marginTopBottom(20.0)
                }
            }.addClass(Styles.top)

            stackpane {
                vboxConstraints {
                    marginTopBottom(40.0)
                    marginLeftRight(40.0)
                }
                vbox {
                    label("Fetching")
                    progressindicator()
                    alignment = Pos.TOP_CENTER
                    spacing = 4.0
                    paddingTop = 10.0
                }
                console = listview {
                    items.add(consolePath)
                }
            }

            button("Upload your project.") {
                setOnAction {
                    chooseDirectory {
                        title = "Choose a TornadoFX Project"
                        initialDirectory = File(System.getProperty("user.home"))
                    }?.let {
                        console.items.clear()
                        console.items.add("SEARCHING FILES...")
                        controller.walk(it.absolutePath)
                    }
                }
                vboxConstraints {
                    marginLeft = 300.0
                    marginBottom = 40.0
                }
            }

        }.addClass(Styles.main)

        overlay = hbox {
            prefWidth = 800.0
            prefHeight = 600.0
            isMouseTransparent = true
        }.addClass(Styles.transparentLayer)
    }

    private fun writeFileToConsole(file: String, fileText: String) {
        console.items.add(consolePath + file)
        console.items.add("READING FILES...")
        console.items.add(fileText)
        console.items.add("===================================================================")
    }
}