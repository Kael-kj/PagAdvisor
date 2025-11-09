package com.Kenji.pagadvisor.ui.components

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun MarkdownText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = LocalContentColor.current
) {
    val annotatedString = parseMarkdown(text, MaterialTheme.typography)
    Text(
        text = annotatedString,
        modifier = modifier,
        style = style,
        color = color
    )
}

/**
 * Um parser de Markdown mais robusto que lida com quebras de linha,
 * títulos (###) e negrito/itálico aninhados.
 */
private fun parseMarkdown(text: String, typography: Typography): AnnotatedString {
    return buildAnnotatedString {
        val lines = text.split('\n')

        // Regex única para capturar **negrito** (Grupo 2) ou *itálico* (Grupo 4)
        val inlineRegex = Regex("(\\*\\*(.*?)\\*\\*)|(\\*(.*?)\\*)")

        lines.forEachIndexed { index, line ->

            when {
                // Título (###)
                line.startsWith("### ") -> {
                    withStyle(style = typography.titleMedium.toSpanStyle().copy(fontWeight = FontWeight.Bold)) {
                        append(line.removePrefix("### ").trim())
                    }
                }

                // Texto normal com negrito/itálico
                else -> {
                    var currentIndex = 0
                    val matches = inlineRegex.findAll(line)

                    matches.forEach { matchResult ->
                        // 1. Adiciona o texto ANTES do match
                        if (matchResult.range.first > currentIndex) {
                            append(line.substring(currentIndex, matchResult.range.first))
                        }

                        // 2. Determina se é negrito ou itálico
                        val boldContent = matchResult.groupValues[2].ifEmpty { null }
                        val italicContent = matchResult.groupValues[4].ifEmpty { null }

                        when {
                            // É negrito (Grupo 2)
                            boldContent != null -> {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(boldContent)
                                }
                            }
                            // É itálico (Grupo 4)
                            italicContent != null -> {
                                withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                                    append(italicContent)
                                }
                            }
                        }
                        currentIndex = matchResult.range.last + 1
                    }

                    // 3. Adiciona o resto da linha (depois do último match)
                    if (currentIndex < line.length) {
                        append(line.substring(currentIndex, line.length))
                    }
                }
            }

            // Adiciona a quebra de linha de volta
            if (index < lines.size - 1) {
                append('\n')
            }
        }
    }
}