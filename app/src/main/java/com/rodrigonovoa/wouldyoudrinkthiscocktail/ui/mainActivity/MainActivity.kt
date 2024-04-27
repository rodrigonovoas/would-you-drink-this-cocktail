package com.rodrigonovoa.wouldyoudrinkthiscocktail.ui.mainActivity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.List
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
import com.airbnb.lottie.compose.*
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
    val detailMode = viewModel.detailMode.collectAsState(initial = false).value
    val dbDrinks = viewModel.drinks.collectAsState(initial = listOf()).value
    val showDrinkAddedDialog = remember { mutableStateOf(false) }

    // lottie animations
    var isPlaying by remember { mutableStateOf(false) }
    var amount by remember { mutableStateOf(0) }

    // observe when drink has been inserted
    LaunchedEffect(viewModel) {
        viewModel.drinkInserted.collect {
            if (it) {
                amount = amount + 1
                isPlaying = true
            }
        }
    }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        var showMyDrinksSheet by remember { mutableStateOf(false) }

        if (showMyDrinksSheet) {
            StoredDrinksBottomSheet(
                dbDrinks,
                onDismiss = { showMyDrinksSheet = false },
                onDrinkSelected = { viewModel.loadDrink(it) }
            )
        }

        CocktailDetail(
            detailMode = detailMode,
            viewModel = viewModel,
            state = state,
            showSheet = { showMyDrinksSheet = true }
        )

        LottieAnimationManager(
            isPlaying = isPlaying,
            amount = amount,
            showDrinkAddedDialog = showDrinkAddedDialog,
            stopAnimation = {  isPlaying = false }
        )

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
fun CocktailDetail(
    detailMode: Boolean,
    viewModel: MainActivityViewModel,
    state: ApiResult<DrinksResponse?>,
    showSheet: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (detailMode) {
                DetailHeader(viewModel = viewModel)
            } else {
                Title()
            }

            CocktailBody(state)

            Spacer(modifier = Modifier.weight(1f))

            if (!detailMode) {
                BottomButtons(
                    onDislikeButtonClick = { viewModel.getDrinkFromAPI() },
                    onLikeButtonClick = {
                        viewModel.insertDrink(state.data?.drinks?.get(0))
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // set MyDrinks button to the right side of the screen
            Row {
                Spacer(modifier = Modifier.weight(1f))
                MyDrinksButton({ showSheet.invoke() })
            }
        }
    }
}

@Composable
fun DetailHeader(viewModel: MainActivityViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(onClick = {
            viewModel.closeDetailMode()
            viewModel.loadLastDrink()
        }) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Close"
            )
        }
    }
}


@Composable
fun LottieAnimationManager(
    isPlaying: Boolean,
    amount: Int,
    showDrinkAddedDialog: MutableState<Boolean>,
    stopAnimation: () -> Unit
) {
    key(amount) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.like_anim)
        )

        val progress by animateLottieCompositionAsState(
            composition,
            isPlaying = isPlaying,
            speed = 1.2f,
            restartOnPlay = false,
            cancellationBehavior = LottieCancellationBehavior.OnIterationFinish
        )

        LaunchedEffect(progress) {
            if (progress >= 0.8f && isPlaying) {
                stopAnimation.invoke()
                showDrinkAddedDialog.value = true
            }
        }

        if (isPlaying) {
            LottieAnimation(composition = composition, progress = progress)
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
    val style = TextStyle(
        textAlign = TextAlign.Center,
        fontSize = 32.sp
    )

    Text(
        text = "Would you drink this cocktail?",
        style = style,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun CocktailBody(state: ApiResult<DrinksResponse?>) {
    // manage the response from api
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

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(drink?.strDrinkThumb)
                .crossfade(true)
                .build(),
            contentDescription = null,
            placeholder = painterResource(R.drawable.loading_placeholder),
            // error = painterResource(R.drawable.loading_placeholder),
            modifier = Modifier
                .fillMaxWidth()
                .height(225.dp)
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomEnd = 20.dp,
                        bottomStart = 20.dp
                    )
                ) ,
            contentScale = ContentScale.Crop,
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            DrinkProperty(text = drink?.strCategory)

            DrinkProperty(text = drink?.strAlcoholic)
        }
    }

    Text(
        text = "${drink?.strDrink}",
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        modifier = Modifier
            .padding(top = 16.dp)
    )

    DrinkInstructions(drink?.strInstructions)
}

@Composable
fun DrinkProperty(text: String?) {
    Text(
        text = text ?: "",
        style = TextStyle(fontWeight = FontWeight.Bold),
        fontSize = 16.sp,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun DrinkInstructions(instructions: String?) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .background(Color(0xFFF5D37F), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Instructions",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                fontSize = 20.sp
            )

            Text(
                text = instructions ?: "",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun BottomButtons(
    onDislikeButtonClick: () -> Unit,
    onLikeButtonClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(top = 24.dp)
    ){
        LikeButton(onLikeButtonClick)
        Spacer(modifier = Modifier.width(42.dp))
        DislikeButton(onDislikeButtonClick)
    }
}

@Composable
fun DislikeButton(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }

    Image(
        painter = painterResource(R.drawable.ic_dislike),
        alignment = Alignment.Center,
        contentDescription = "",
        modifier = Modifier
            .size(56.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { onClick.invoke() }
            )
    )
}

@Composable
fun LikeButton(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }

    Image(
        painter = painterResource(R.drawable.ic_like),
        alignment = Alignment.Center,
        contentDescription = "",
        modifier = Modifier
            .size(56.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { onClick.invoke() }
            )

    )
}

@Composable
fun MyDrinksButton(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val customBlue = Color(0xFF7FDAF5)

    IconButton(
        onClick = { onClick.invoke() },
        interactionSource = interactionSource,
        modifier = Modifier
            .size(56.dp)
            .background(color = customBlue, shape = CircleShape)
    ) {
        Icon(
            imageVector = Icons.Filled.List,
            contentDescription = "Favorite",
            tint = Color.White
        )
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