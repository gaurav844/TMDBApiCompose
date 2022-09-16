package com.example.tmdbapicompose.presentation.ui.screens.movieDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tmdbapicompose.domain.models.Result

@Composable
fun MovieDetailScreen(result: Result?) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        result?.let {
            val url = "https://image.tmdb.org/t/p/w342/${it.poster_path}"
            Column {
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorScheme.primary)
                        .padding(horizontal = 16.dp, vertical =  30.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .shadow(2.dp, RoundedCornerShape(8.dp))
                    ) {
                        AsyncImage(
                            model = url,
                            contentDescription = it.title,
                            modifier = Modifier.size(140.dp, 230.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = it.title, color = colorScheme.onTertiary, fontSize = 18.sp)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 10.dp)
                        ) {
                            Row {
                                Text(
                                    text = "Language :  ",
                                    color = colorScheme.onTertiary,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = it.original_language,
                                    color = colorScheme.onTertiary,
                                    fontSize = 14.sp
                                )
                            }

                            Row {
                                Text(
                                    text = "Star Rating ",
                                    color = colorScheme.onTertiary,
                                    fontSize = 12.sp
                                )
                                Text(text = it.vote_average.toString(), color = colorScheme.onTertiary, fontSize = 14.sp)
                            }

                            Row {
                                Text(
                                    text = "Release Date :  ",
                                    color = colorScheme.onTertiary,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = it.release_date,
                                    color = colorScheme.onTertiary,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .background(colorScheme.onBackground)
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Synopsis",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(0.dp, 10.dp)
                    )
                    Text(text = it.overview, color = Color.Black, fontSize = 14.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun display() {
    MovieDetailScreen(null)
}