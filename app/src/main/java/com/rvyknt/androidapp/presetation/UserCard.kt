package com.rvyknt.androidapp.presetation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rvyknt.androidapp.domain.UserModel

// Composable to display individual user information in a card
@Composable
fun UserCard(user: UserModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), // Adds space between cards
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Adds shadow effect
    ) {
        // Display the user details inside the card
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.url) // Image URL
                    .crossfade(true)
                    .build(),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .fillMaxWidth() // Set size for the image
                    .padding(16.dp)
            )
            Text(text = "Album ID: ${user.albumID}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "ID: ${user.id}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Title: ${user.title}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "URL: ${user.url}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}