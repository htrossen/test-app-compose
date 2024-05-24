package com.example

import com.example.testappcompose.common.CarouselItem
import com.example.testappcompose.core.net.IngredientResponse

object DataMocks {

    val ingredientMock = IngredientResponse(
        id = "1",
        name = "Vodka",
        abv = "40",
        description = "Vodka is a distilled beverage composed primarily of water and ethanol, sometimes with traces of impurities and flavorings."
    )

    val cocktailsMock = listOf(
        CarouselItem(
            id = "15346",
            title = "155 Belmont",
            imageUrl = "https://www.thecocktaildb.com/images/media/drink/yqvvqs1475667388.jpg"
        ),
        CarouselItem(
            id = "17105",
            title = "501 Blue",
            imageUrl = "https://www.thecocktaildb.com/images/media/drink/ywxwqs1461867097.jpg"
        )
    )
}
