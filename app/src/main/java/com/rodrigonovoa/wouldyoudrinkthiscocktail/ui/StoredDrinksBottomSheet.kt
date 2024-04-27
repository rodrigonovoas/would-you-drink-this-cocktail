package com.rodrigonovoa.wouldyoudrinkthiscocktail.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rodrigonovoa.wouldyoudrinkthiscocktail.R
import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.db.Drink

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoredDrinksBottomSheet(
    drinks: List<Drink>,
    onDismiss: () -> Unit,
    onDrinkSelected: (Drink) -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Text(
            text = "My Drinks",
            style = TextStyle(fontSize = 24.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
                .fillMaxWidth()
        )

        LazyColumn(
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        ){
            itemsIndexed(drinks) { index, item ->
                Row (modifier = Modifier
                    .padding(16.dp)
                    .clickable { onDrinkSelected.invoke(item) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(item.thumb)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        placeholder = painterResource(R.drawable.loading_placeholder),
                        // error = painterResource(R.drawable.placeholder),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(32.dp)
                            .height(32.dp)
                            .clip(CircleShape)
                    )

                    Text(
                        text = item.name ?: "",
                        style = TextStyle(fontSize = 20.sp),
                        modifier = Modifier.padding(8.dp)
                    )
                }

                if (index != drinks.lastIndex) {
                    Divider(
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }
}