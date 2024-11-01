package org.tawakal.composemphelloworld

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello Boos Artan, ${platform.name}!"
    }
}