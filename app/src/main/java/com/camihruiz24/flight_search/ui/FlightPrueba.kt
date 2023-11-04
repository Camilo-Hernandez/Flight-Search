package com.camihruiz24.flight_search.ui


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.AirplanemodeActive
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.camihruiz24.flight_search.R

@Composable
fun FlightCard(
    airport1Name: String,
    airport2Name: String,
    airport1Code: String,
    airport2Code: String,
    airport1Passengers: Int,
    airport2Passengers: Int,
    flightDuration: String,
    numStops: Int,
    isFavorite: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_high)),
        colors = CardDefaults.cardColors(containerColor = Color.White,),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary)

    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxWidth()
        ) {
            val (row1, row2, column1, column2, flightInfo,) = createRefs()

            val startGuide = createGuidelineFromStart(dimensionResource(id = R.dimen.padding_high))
            val endGuide = createGuidelineFromEnd(dimensionResource(id = R.dimen.padding_high))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = dimensionResource(id = R.dimen.padding_very_high),
                        start = dimensionResource(id = R.dimen.padding_very_high),
                        end = dimensionResource(id = R.dimen.padding_very_high),
                    )
                    .constrainAs(row1) {
                        bottom.linkTo(flightInfo.top, margin = 13.dp)
                    },
                Arrangement.SpaceBetween
            ) {
                Text(
                    text = airport1Name.uppercase(), style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = airport2Name.uppercase(), style = MaterialTheme.typography.bodyMedium
                )
            }

            val firstHorizontalBarrier = createBottomBarrier(row1)

            Column(
                modifier = Modifier
                    .constrainAs(column1) {
                        top.linkTo(firstHorizontalBarrier)
                        start.linkTo(startGuide)
                        bottom.linkTo(row2.top)
                    }
            ) {
                Text(text = airport1Code, style = MaterialTheme.typography.bodySmall)
                Text(text = airport1Passengers.toString(), style = MaterialTheme.typography.bodyLarge)
                Text(text = stringResource(id = R.string.passengers), style = MaterialTheme.typography.labelSmall)
            }

            Column(
                modifier = Modifier
                    .constrainAs(column2) {
                        top.linkTo(firstHorizontalBarrier)
                        end.linkTo(endGuide)
                        bottom.linkTo(row2.top)
                    },
                Arrangement.SpaceBetween,
                Alignment.End
            ) {
                Text(text = airport2Code, style = MaterialTheme.typography.bodySmall)
                Text(text = airport2Passengers.toString(), style = MaterialTheme.typography.bodyLarge)
                Text(text = stringResource(R.string.passengers), style = MaterialTheme.typography.labelSmall)
            }

            val secondHorizontalBarrier = createTopBarrier(row2)

            Column(
                modifier = Modifier
                    .height(70.dp)
                    .width(191.dp)
                    .constrainAs(flightInfo) {
                        top.linkTo(firstHorizontalBarrier)
                        bottom.linkTo(secondHorizontalBarrier)
                        start.linkTo(column1.end)
                        end.linkTo(column2.start)
                    },
                Arrangement.SpaceBetween,
                Alignment.CenterHorizontally,
            ) {
                Text(text = flightDuration, style = MaterialTheme.typography.labelLarge)

                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
//                        LinearProgressIndicator(Modifier.height(DividerDefaults.Thickness), MaterialTheme.colorScheme.tertiary)
                        Divider()
                        Icon(
                            imageVector = Icons.Rounded.AirplanemodeActive,
                            contentDescription = "Flight",
                            tint = Color(0xFF505050),
                            modifier = Modifier.rotate(90f)
                        )
                    }
                    Canvas(modifier = Modifier.fillMaxWidth()) {
                        translate(top = 0f, left = -size.width/2) {
                            drawCircle(Color(0xFF828282), radius = 4.dp.toPx())
                        }
                        translate(top = 0f, left = size.width/2) {
                            drawCircle(Color(0xFF828282), radius = 4.dp.toPx())
                        }
                    }
                }
                
                Text(text = "$numStops Stops", style = MaterialTheme.typography.labelLarge)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(24.dp)
                    .padding(
                        start = dimensionResource(id = R.dimen.padding_very_high),
                        end = dimensionResource(id = R.dimen.padding_very_high),
                        bottom = dimensionResource(id = R.dimen.padding_very_high),
                    )
                    .constrainAs(row2) {
                        top.linkTo(flightInfo.bottom, margin = 13.dp)
                    },
                Arrangement.SpaceBetween,
                Alignment.CenterVertically,
            ) {
                Row(modifier = Modifier, Arrangement.SpaceBetween, Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = stringResource(R.string.flight_info),
                        tint = Color(0xFFA5A5A5),
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_medium)))
                    Text(text = stringResource(R.string.flight_info), style = MaterialTheme.typography.labelMedium)
                }


                Icon(
                    imageVector = if (isFavorite) Icons.Rounded.Star else Icons.Rounded.StarBorder,
                    contentDescription = if (isFavorite) "Remove from Favorites" else "Add to Favorites",
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else Color.Black,
                    modifier = Modifier
                        .clickable { /** TODO */ }
                )

            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun FlightCardExample() {
    FlightCard(
        airport1Name = "Airport 1",
        airport2Name = "Airport 2",
        airport1Code = "ABC",
        airport2Code = "XYZ",
        airport1Passengers = 1000,
        airport2Passengers = 2000,
        flightDuration = "2h 30m",
        numStops = 1,
        isFavorite = false
    )
}
