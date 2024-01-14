package com.rodrigonovoa.wouldyoudrinkthiscocktail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodrigonovoa.wouldyoudrinkthiscocktail.ui.theme.WouldYouDrinkThisCocktailTheme

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
private fun BaseView() {
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
            Cocktail()
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
fun Cocktail() {
    Image(
        painter = painterResource(id = R.drawable.placeholder),
        contentDescription = null,
        modifier = Modifier
            .width(150.dp)
            .height(150.dp)
            .padding(top = 16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WouldYouDrinkThisCocktailTheme {
        BaseView()
    }
}