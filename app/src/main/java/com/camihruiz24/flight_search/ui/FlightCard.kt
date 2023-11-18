package com.camihruiz24.flight_search.ui


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.AirplanemodeActive
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.camihruiz24.flight_search.R
import com.camihruiz24.flight_search.data.model.Flight
import com.camihruiz24.flight_search.data.repository.fake.FakeFlightDatasource

@Composable
fun FlightCard(
    flight: Flight,
    onFavoriteChanged: (Flight) -> Unit,
    modifier: Modifier = Modifier
) {
    val flightDuration: String = stringResource(
        R.string.flight_duration,
        (0..14).random(),
        (0..60).random(),
    )
    val stops: Int = if (flightDuration.substringBefore('h').toInt() < 5)
        0
    else if (flightDuration.substringBefore('h').toInt() < 9)
        2
    else
        3

    val numStops = stringResource(id = R.string.numStops, stops)

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
//        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            Modifier.padding(dimensionResource(id = R.dimen.padding_high)),
            Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_high))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                Arrangement.SpaceBetween
            ) {
                Text(
                    text = flight.departureName.uppercase(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    softWrap = true,
                    textAlign = TextAlign.Start
                )

                Text(
                    text = flight.destinationName.uppercase(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    softWrap = true,
                    textAlign = TextAlign.End
                )
            }

            FlightDetails(
                flight,
                flightDuration,
                numStops,
                Modifier
                    .fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(24.dp),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically,
            ) {
                Row(modifier = Modifier, Arrangement.SpaceBetween, Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = stringResource(R.string.flight_info),
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_medium)))
                    Text(text = stringResource(R.string.flight_info), style = MaterialTheme.typography.labelMedium)
                }

                Icon(
                    imageVector = if (flight.isFavorite) Icons.Rounded.Star else Icons.Rounded.StarBorder,
                    contentDescription = if (flight.isFavorite) stringResource(R.string.remove_from_favorites)
                    else stringResource(R.string.add_to_favorites),
                    tint = if (flight.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .clickable(onClick = { onFavoriteChanged(flight) })
                )

            }
        }
    }
}

@Composable
private fun FlightDetails(
    flight: Flight,
    flightDuration: String,
    numStops: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier,
        Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ) {

        Column(
            Modifier.weight(1f),
            Arrangement.Center,
            Alignment.Start,
        ) {
            Text(text = flight.departureCode, style = MaterialTheme.typography.bodySmall)
            Text(text = flight.departurePassengers.toString(), style = MaterialTheme.typography.bodyLarge)
            Text(text = stringResource(id = R.string.passengers), style = MaterialTheme.typography.labelSmall)
        }

        Column(
            Modifier.weight(2f),
            Arrangement.SpaceBetween,
            Alignment.CenterHorizontally,
        ) {
            Text(text = flightDuration, style = MaterialTheme.typography.labelLarge)

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Divider()
                    Icon(
                        imageVector = Icons.Rounded.AirplanemodeActive,
                        contentDescription = "Flight",
                        tint = Color(0xFF505050),
                        modifier = Modifier.rotate(90f)
                    )
                }
                Canvas(modifier = Modifier.fillMaxWidth()) {
                    translate(top = 0f, left = -size.width / 2) {
                        drawCircle(Color(0xFF828282), radius = 4.dp.toPx())
                    }
                    translate(top = 0f, left = size.width / 2) {
                        drawCircle(Color(0xFF828282), radius = 4.dp.toPx())
                    }
                }
            }

            Text(text = numStops, style = MaterialTheme.typography.labelLarge)
        }

        Column(
            Modifier.weight(1f),
            Arrangement.Center,
            Alignment.End,
        ) {
            Text(text = flight.destinationCode, style = MaterialTheme.typography.bodySmall)
            Text(text = flight.destinationPassengers.toString(), style = MaterialTheme.typography.bodyLarge)
            Text(text = stringResource(R.string.passengers), style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FlightCardPreview() {
    val testFlight: Flight = FakeFlightDatasource.flight1
    FlightCard(
        flight = testFlight,
        onFavoriteChanged = {},
    )
}
