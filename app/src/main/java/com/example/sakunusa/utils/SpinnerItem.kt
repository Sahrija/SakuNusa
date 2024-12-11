package com.example.sakunusa.utils

data class SpinnerItem(val id: Int, val name: String) {
    override fun toString(): String {
        return name
    }
}