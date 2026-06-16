package dev.bluesheep.nanomirai.util

import net.minecraft.network.chat.Component
import net.minecraft.util.Mth
import java.util.Locale

object EnergyFormat {
    fun format(energy: Int): String {
        val units = arrayOf("", "k", "M", "G", "T", "P", "E", "Y", "Z")
        var value = energy.toDouble()
        var unitIndex = 0

        while (value >= 1000.0 && unitIndex < units.lastIndex) {
            value /= 1000.0
            unitIndex++
        }

        val truncatedValue = Mth.floor(value * 100.0) / 100.0
        val formattedValue = if (truncatedValue % 1.0 == 0.0) {
            truncatedValue.toInt().toString()
        } else {
            String.format(Locale.ROOT, "%.2f", truncatedValue)
        }

        return "$formattedValue${units[unitIndex]}FE"
    }

    fun tooltip(stored: Int, capacity: Int): Component {
        return Component.translatable(
            "nanomirai.tooltip.stored_energy",
            format(stored),
            format(capacity)
        )
    }
}
