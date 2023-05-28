package org.kindcensor.core

/**
 * The utility object for data-masking operations.
 */
object DataMasker {

    /**
     * The default maximum number of symbols to open
     */
    const val DEFAULT_MAX_OPEN = 2

    /**
     *  The default minimum number of hidden symbols
     */
    const val DEFAULT_MIN_HIDDEN = 4

    /**
     * The default mask char
     */
    const val DEFAULT_MASK = '*'

    /**
     * The default length for hidden value
     */
    const val DEFAULT_HIDDEN_LENGTH = 8

    /**
     * Default string for hidden value
     */
    val DEFAULT_HIDDEN_STRING = DEFAULT_MASK.toString().repeat(DEFAULT_HIDDEN_LENGTH)

    /**
     * Masks string. Hides at least [minHidden] symbols from the beginning of result string and opens no more than
     * [maxOpen] symbols in the end of string. If result string is shorter than [minHidden] mask will be extended to
     * meet [minHidden] length. If result string is longer than [minHidden] + [maxOpen] then the rest of elements will be
     * masked
     *
     * Examples:
     *
     *      assertThat(maskBeginning("Vladimir")).isEqualTo("******ir")
     *      assertThat(maskBeginning("Vladimir", 3, 3)).isEqualTo("*****mir")
     *      assertThat(maskBeginning("Vl", 3, 3)).isEqualTo("***")
     *      assertThat(maskBeginning("Vladimir", 3, 3, '#')).isEqualTo("#####mir")
     *
     * Will always return `null` for `null` input
     * @param subject The input object
     * @param maxOpen The maximum number of symbols to open
     * @param minHidden The minimum number of hidden symbols
     * @param mask The mask char
     */
    @JvmStatic
    @JvmOverloads
    fun maskBeginning(
        subject: Any?,
        maxOpen: Int = DEFAULT_MAX_OPEN,
        minHidden: Int = DEFAULT_MIN_HIDDEN,
        mask: Char = DEFAULT_MASK
    ): String? {
        if (subject == null) {
            return null
        }

        val s = subject.toString()
        if (s.length < minHidden) {
            return mask.toString().repeat(minHidden)
        }

        val openFrom = kotlin.math.max(s.length - maxOpen, minHidden)
        return buildString(s.length) {
            repeat(openFrom) { append(mask) }
            append(s.subSequence(openFrom, s.length))
        }
    }

    /**
     * Masks string. Hides at least [minHidden] symbols from the ending of result string and opens no more than
     * [maxOpen] symbols in the beginning of the string. If result string is shorter than [minHidden] mask will be
     * extended to  meet [minHidden] length. If result string is longer than [minHidden] + [maxOpen] then the rest of
     * elements will be masked
     *
     * Examples:
     *
     *      assertThat(maskEnding("Vladimir")).isEqualTo("Vl******")
     *      assertThat(maskEnding("Vladimir", 3, 3)).isEqualTo("Vla*****")
     *      assertThat(maskEnding("Vl", 3, 3)).isEqualTo("***")
     *      assertThat(maskEnding("Vladimir", 3, 3, '#')).isEqualTo("Vla#####")
     *
     * Will always return `null` for `null` input
     * @param subject The input object
     * @param maxOpen The maximum number of symbols to open
     * @param minHidden The minimum number of hidden symbols
     * @param mask The mask char
     */
    @JvmStatic
    @JvmOverloads
    fun maskEnding(
        subject: Any?,
        maxOpen: Int = DEFAULT_MAX_OPEN,
        minHidden: Int = DEFAULT_MIN_HIDDEN,
        mask: Char = DEFAULT_MASK
    ): String? {
        if (subject == null) {
            return null
        }

        val s = subject.toString()
        if (s.length < minHidden) {
            return mask.toString().repeat(minHidden)
        }

        val hiddenFromPosition = kotlin.math.min(s.length - minHidden, maxOpen)
        return buildString {
            append(s.subSequence(0, hiddenFromPosition))
            repeat(s.length - hiddenFromPosition) { append(mask) }
        }
    }

    /**
     * Masks email address. It will split value in to 2 parts by '@' symbols. [maskBeginning] will be applied to the
     * first part. The second part will be split by '.' symbol and [maskEnding] will be applied to each part except
     * the very last.
     *
     * Examples:
     *
     *    assertThat(maskEmail(null)).isNull()
     *    assertThat(maskEmail("")).isEqualTo("****")
     *    assertThat(maskEmail("a.korovkov@fff.org")).isEqualTo("********ov@f**.org")
     *    assertThat(maskEmail("a.korovkov@gmail.com")).isEqualTo("********ov@g****.com")
     *    assertThat(maskEmail("a.korovkov@fff.org", mask = '#')).isEqualTo("########ov@f##.org")
     *
     * @param email The email value
     * @param mask The mask char
     */
    @JvmStatic
    @JvmOverloads
    fun maskEmail(email: Any?, mask: Char = '*'): String? {
        if (email == null) {
            return null
        }

        val parts = email.toString().split(delimiters = arrayOf("@"), ignoreCase = false, limit = 2)
        if (parts.size < 2) {
            return maskEnding(email, mask = mask)
        }

        val beforeAt = parts[0]
        val afterAt = parts[1]
        return buildString {
            append(maskBeginning(beforeAt, mask = mask))
            append("@")

            val domainParts = afterAt.split(".")
            if (domainParts.size < 2) {
                append(maskEnding(afterAt, mask = mask))
            } else {
                domainParts.dropLast(1).forEach { append(maskEnding(it, 1, 1, mask = mask)) }
                append(".")
                append(domainParts.last())
            }
        }
    }

    /**
     * Reduces string to first not blank symbol if any
     *
     * Examples:
     *
     *      assertThat(initial(null)).isNull()
     *      assertThat(initial("  ")).isEqualTo("")
     *      assertThat(initial("Vladimir")).isEqualTo("V.")
     *      assertThat(initial("Vladimir", withPeriodEnding = false)).isEqualTo("V")
     *
     * @param subject The subject of transformation
     * @param withPeriodEnding Whether to include '.' symbol in the end of the string
     */
    @JvmStatic
    @JvmOverloads
    fun initial(subject: Any?, withPeriodEnding: Boolean = true): String? {
        if (subject == null) {
            return null
        }
        val s = subject.toString().trim()
        if (s.isEmpty()) {
            return s
        }
        return if (withPeriodEnding) "${s.first()}." else s.substring(0, 1)
    }

    /**
     * Hides value of object
     *
     * @param subject The subject of transformation
     * @param preserveLength Whether to save length of original [toString] result or replace with fixed
     * [DEFAULT_HIDDEN_LENGTH] length
     * @param preserveNull Whether to return `null` if [subject] is null or replace it with default string with
     * [DEFAULT_HIDDEN_LENGTH] length
     * @param mask The mask char
     */
    @JvmStatic
    @JvmOverloads
    fun hide(
        subject: Any?,
        preserveLength: Boolean = false,
        preserveNull: Boolean = true,
        mask: Char = DEFAULT_MASK
    ): String? =
        if (subject != null) {
            when {
                preserveLength -> mask.toString().repeat(subject.toString().length)
                mask == DEFAULT_MASK -> DEFAULT_HIDDEN_STRING
                else -> mask.toString().repeat(DEFAULT_HIDDEN_LENGTH)
            }
        } else {
            when {
                preserveNull -> null
                mask == DEFAULT_MASK -> DEFAULT_HIDDEN_STRING
                else -> mask.toString().repeat(DEFAULT_HIDDEN_LENGTH)
            }
        }
}