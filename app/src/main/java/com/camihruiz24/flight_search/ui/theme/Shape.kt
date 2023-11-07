package com.camihruiz24.flight_search.ui.theme

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    extraSmall = CutCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = CutCornerShape(topEnd = 16.dp, bottomStart = 16.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(20.dp),
)
