package com.rodrigonovoa.wouldyoudrinkthiscocktail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Title()
                Cocktail(data)
                BottomButtons()
            }
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
    val drink = data.value?.drinks?.get(0)

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(drink?.strDrinkThumb)
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
        text = "${drink?.strDrink}",
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        modifier = Modifier
            .padding(top = 8.dp)
    )

    DrinkProperty(label = "Category", text = drink?.strCategory)

    DrinkProperty(label = "Type", text = drink?.strAlcoholic)

    DrinkInstructions(drink?.strInstructions)
}

@Composable
fun DrinkProperty(label: String? = null, text: String?, isTitle: Boolean = false) {
    val style = if (isTitle) TextStyle(fontWeight = FontWeight.Bold) else TextStyle.Default
    val labelText = if (label != null) "$label: " else ""

    Text(
        text = "$labelText$text",
        style = style,
        fontSize = 16.sp,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
fun DrinkInstructions(instructions: String?) {
    Text(
        text = "Instructions",
        style = TextStyle(textDecoration = TextDecoration.Underline),
        fontSize = 16.sp,
        modifier = Modifier.padding(top = 16.dp)
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Text(
            text = instructions ?: "",
            fontSize = 16.sp
        )
    }
}

@Composable
fun BottomButtons() {
    Row(
        modifier = Modifier.padding(top = 32.dp)
    ){
        DislikeButton()
        Spacer(modifier = Modifier.width(16.dp))
        LikeButton()
    }
}

@Composable
fun DislikeButton() {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(Color.Red, Color.Red, Color.Red)
    )

    OutlinedButton(
        onClick = { },
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color.Red
        ),
        border = ButtonDefaults.outlinedBorder.copy(
            width = 3.dp,
            brush = gradientBrush
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .defaultMinSize(minWidth = 125.dp)
            .padding(8.dp)
    ) {
        Text("DISLIKE")
    }
}

@Composable
fun LikeButton() {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(Color.Green, Color.Green, Color.Green)
    )

    OutlinedButton(
        onClick = { },
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color.Green
        ),
        border = ButtonDefaults.outlinedBorder.copy(
            width = 3.dp,
            brush = gradientBrush
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .defaultMinSize(minWidth = 125.dp)
            .padding(8.dp)
    ) {
        Text("LIKE")
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