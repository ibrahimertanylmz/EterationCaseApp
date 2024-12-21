package com.eteration.data.remote.model

import com.google.gson.annotations.SerializedName


data class ProductResponse(
    @SerializedName("createdAt") var createdAt: String,
    @SerializedName("name") var name: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("price") var price: Double? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("model") var model: String? = null,
    @SerializedName("brand") var brand: String? = null,
    @SerializedName("id") var id:  String? = null
)