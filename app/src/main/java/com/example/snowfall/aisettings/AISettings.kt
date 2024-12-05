package com.example.snowfall.aisettings

data class AISettings(
    val temperature: Float = 0.7f,
    val maxTokens: Int = 1000,
    val topP: Float = 1.0f,
    val frequencyPenalty: Float = 0.0f,
    val presencePenalty: Float = 0.0f,
    val stopSequences: List<String> = emptyList()
) {
    companion object {
        val DEFAULT = AISettings()
    }
} 