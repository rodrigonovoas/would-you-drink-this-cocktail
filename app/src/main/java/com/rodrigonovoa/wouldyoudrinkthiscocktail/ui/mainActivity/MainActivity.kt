package com.rodrigonovoa.wouldyoudrinkthiscocktail.ui.mainActivity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rodrigonovoa.wouldyoudrinkthiscocktail.R
import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.api.DrinksResponse
import com.rodrigonovoa.wouldyoudrinkthiscocktail.repository.ApiResult
import com.rodrigonovoa.wouldyoudrinkthiscocktail.ui.StoredDrinksBottomSheet
import com.rodrigonovoa.wouldyoudrinkthiscocktail.ui.theme.WouldYouDrinkThisCocktailTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WouldYouDrinkThisCocktailTheme {
                BaseView()
            }
        }
    }
}

@Composable
 fun BaseView(viewModel: MainActivityViewModel = koinViewModel()) {
    val state = viewModel.drink.collectAsState(initial = ApiResult.loading()).value
    val dbDrinks = viewModel.drinks.collectAsState(initial = listOf()).value
    val showDrinkAddedDialog = remember { mutableStateOf(false) }

    LaunchedEffect(viewModel) {
        viewModel.drinkInserted.collect() {
            if (it) { showDrinkAddedDialog.value = true }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        var showSheet by remember { mutableStateOf(false) }

        if (showSheet) {
            StoredDrinksBottomSheet(
                dbDrinks,
                onDismiss = { showSheet = false},
                onDrinkSelected = { viewModel.loadDrink(it) }
            )
        }

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

                CocktailResponseManager(state)

                BottomButtons(
                    onDislikeButtonClick = { showSheet = true /* viewModel.getDrinkFromAPI() */ },
                    onLikeButtonClick = { viewModel.insertDrink(state.data?.drinks?.get(0)) }
                )
            }
        }

        if (showDrinkAddedDialog.value) {
            DrinkAddedAlertDialog(
                onDismissRequest = {
                    viewModel.resetDrinkInserted()
                    viewModel.getDrinkFromAPI()
                    showDrinkAddedDialog.value = false
                }
            )
        }
    }
}

@Composable
fun DrinkAddedAlertDialog(onDismissRequest: () -> Unit) {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(Color.Green, Color.Green, Color.Green)
    )

    Dialog(onDismissRequest = {}) {
        Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Drink saved",
                        textAlign = TextAlign.Center,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.width(16.dp))


                    Image(
                        painter = painterResource(R.drawable.ic_ok),
                        alignment = Alignment.Center,
                        contentDescription = "",
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        modifier = Modifier.width(128.dp),
                        onClick = { onDismissRequest.invoke() },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Green
                        ),
                        border = ButtonDefaults.outlinedBorder.copy(
                            width = 3.dp,
                            brush = gradientBrush
                        ),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Text("OK")
                    }
                }
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
fun CocktailResponseManager(state: ApiResult<DrinksResponse?>) {
    when (state.status) {
        ApiResult.Status.SUCCESS -> {
            state.data?.let {
                if (it.drinks.isNotEmpty()) {
                    Cocktail(it)
                } else {
                    Text("DRINK UNAVAILABLE")
                }
            }
        }
        ApiResult.Status.ERROR -> {
            Text(text = "Error: ${state.message}")
        }
        ApiResult.Status.LOADING -> {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun Cocktail(data: DrinksResponse) {
    val drink = data.drinks?.get(0)

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(drink?.strDrinkThumb)
            .crossfade(true)
            .build(),
        contentDescription = null,
        error = painterResource(R.drawable.placeholder),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .width(225.dp)
            .height(225.dp)
            .padding(top = 16.dp)
            .clip(CircleShape)
    )

    Text(
        text = "${drink?.strDrink}",
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        modifier = Modifier
            .padding(top = 16.dp)
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
        modifier = Modifier.padding(top = 8.dp)
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
fun BottomButtons(
    onDislikeButtonClick: () -> Unit,
    onLikeButtonClick: () -> Unit
) {
    Row(
        modifier = Modifier.padding(top = 24.dp)
    ){
        LikeButton(onLikeButtonClick)
        Spacer(modifier = Modifier.width(16.dp))
        DislikeButton(onDislikeButtonClick)
    }
}

@Composable
fun DislikeButton(onClick: () -> Unit) {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(Color.Red, Color.Red, Color.Red)
    )

    OutlinedButton(
        onClick = { onClick.invoke() },
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
fun LikeButton(onClick: () -> Unit) {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(Color.Green, Color.Green, Color.Green)
    )

    OutlinedButton(
        onClick = { onClick.invoke() },
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
    WouldYouDrinkThisCocktailTheme {
        BaseView()
    }
}