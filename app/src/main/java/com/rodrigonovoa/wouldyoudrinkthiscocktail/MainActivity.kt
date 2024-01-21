package com.rodrigonovoa.wouldyoudrinkthiscocktail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.DrinksResponse
import com.rodrigonovoa.wouldyoudrinkthiscocktail.ui.theme.WouldYouDrinkThisCocktailTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel = MainActivityViewModel()

        super.onCreate(savedInstanceState)
        setContent {
            WouldYouDrinkThisCocktailTheme {
                BaseView(
                    data = viewModel.drink
                )
            }
        }
    }
}

@Composable
private fun BaseView(data: State<DrinksResponse?>) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Title()
            Cocktail(data)
        }
    }
}

@Composable
fun Title() {
    Text(
        text = "Would you drink this cocktail?",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun Cocktail(data: State<DrinksResponse?>) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(data.value?.drinks?.get(0)?.strDrinkThumb)
            .crossfade(true)
            .build(),
        contentDescription = null,
        error = painterResource(R.drawable.placeholder),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .width(200.dp)
            .height(200.dp)
            .padding(top = 16.dp)
    )

    Text(
        text = "${data.value?.drinks?.get(0)?.strDrink}",
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(top = 8.dp)
    )

    Text(
        text = "Category: ${data.value?.drinks?.get(0)?.strCategory}",
        modifier = Modifier
            .padding(top = 8.dp)
    )

    Text(
        text = "Type: ${data.value?.drinks?.get(0)?.strAlcoholic}",
        modifier = Modifier
            .padding(top = 8.dp)
    )

    Text(
        text = "Instructions",
        style = TextStyle(textDecoration = TextDecoration.Underline),
        modifier = Modifier
            .padding(top = 8.dp)
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "${data.value?.drinks?.get(0)?.strInstructions}",
            modifier = Modifier
                .padding(top = 4.dp)
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    val data: MutableState<DrinksResponse?> = mutableStateOf(null)

    WouldYouDrinkThisCocktailTheme {
        BaseView(data)
    }
}