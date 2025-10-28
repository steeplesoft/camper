package com.steeplesoft.camper.demo.models

import com.steeplesoft.camper.fields.PickerValue

data class Country(
    val code: String,
    val name: String
): PickerValue() {
    override fun searchFilter(query: String): Boolean {
        return this.name.startsWith(query)
    }
}
