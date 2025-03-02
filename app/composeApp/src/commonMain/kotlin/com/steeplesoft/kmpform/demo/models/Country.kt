package com.steeplesoft.kmpform.demo.models

import com.steeplesoft.kmpform.fields.PickerValue

data class Country(
    val code: String,
    val name: String
): PickerValue() {
    override fun searchFilter(query: String): Boolean {
        return this.name.startsWith(query)
    }
}
