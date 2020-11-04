package com.example.imageloadmoreview
import com.google.gson.annotations.SerializedName

data class LoadModel(
    @SerializedName("Items")
    val items: List<Item?>? = listOf(),
    @SerializedName("lastBuildDate")
    val lastBuildDate: String? = "",
    @SerializedName("title")
    val title: String? = ""
) {
    data class Item(
        @SerializedName("Item")
        val item: Item? = Item(),
        val isLoadingView: Boolean = false

    ) {
        data class Item(
            @SerializedName("affiliateRate")
            val affiliateRate: String? = "",
            @SerializedName("affiliateUrl")
            val affiliateUrl: String? = "",
            @SerializedName("asurakuArea")
            val asurakuArea: String? = "",
            @SerializedName("asurakuClosingTime")
            val asurakuClosingTime: String? = "",
            @SerializedName("asurakuFlag")
            val asurakuFlag: Int? = 0,
            @SerializedName("availability")
            val availability: Int? = 0,
            @SerializedName("carrier")
            val carrier: Int? = 0,
            @SerializedName("catchcopy")
            val catchcopy: String? = "",
            @SerializedName("creditCardFlag")
            val creditCardFlag: Int? = 0,
            @SerializedName("endTime")
            val endTime: String? = "",
            @SerializedName("genreId")
            val genreId: String? = "",
            @SerializedName("imageFlag")
            val imageFlag: Int? = 0,
            @SerializedName("itemCaption")
            val itemCaption: String? = "",
            @SerializedName("itemCode")
            val itemCode: String? = "",
            @SerializedName("itemName")
            val itemName: String? = "",
            @SerializedName("itemPrice")
            val itemPrice: String? = "",
            @SerializedName("itemUrl")
            val itemUrl: String? = "",
            @SerializedName("mediumImageUrls")
            val mediumImageUrls: List<MediumImageUrl?>? = listOf(),
            @SerializedName("pointRate")
            val pointRate: Int? = 0,
            @SerializedName("pointRateEndTime")
            val pointRateEndTime: String? = "",
            @SerializedName("pointRateStartTime")
            val pointRateStartTime: String? = "",
            @SerializedName("postageFlag")
            val postageFlag: Int? = 0,
            @SerializedName("rank")
            val rank: Int? = 0,
            @SerializedName("reviewAverage")
            val reviewAverage: String? = "",
            @SerializedName("reviewCount")
            val reviewCount: Int? = 0,
            @SerializedName("shipOverseasArea")
            val shipOverseasArea: String? = "",
            @SerializedName("shipOverseasFlag")
            val shipOverseasFlag: Int? = 0,
            @SerializedName("shopAffiliateUrl")
            val shopAffiliateUrl: String? = "",
            @SerializedName("shopCode")
            val shopCode: String? = "",
            @SerializedName("shopName")
            val shopName: String? = "",
            @SerializedName("shopOfTheYearFlag")
            val shopOfTheYearFlag: Int? = 0,
            @SerializedName("shopUrl")
            val shopUrl: String? = "",
            @SerializedName("smallImageUrls")
            val smallImageUrls: List<SmallImageUrl?>? = listOf(),
            @SerializedName("startTime")
            val startTime: String? = "",
            @SerializedName("taxFlag")
            val taxFlag: Int? = 0
        ) {
            data class MediumImageUrl(
                @SerializedName("imageUrl")
                val imageUrl: String? = ""
            )

            data class SmallImageUrl(
                @SerializedName("imageUrl")
                val imageUrl: String? = ""
            )
        }
    }
}