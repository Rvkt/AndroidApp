package com.rvyknt.androidapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rvyknt.androidapp.ui.theme.AndroidAppTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.rvyknt.androidapp.data.api.ApiInterface
import com.rvyknt.androidapp.data.api.RetrofitInstance
import com.rvyknt.androidapp.domain.UserModel
import com.rvyknt.androidapp.presetation.UserCard

class MainActivity : ComponentActivity() {

    // Access the API interface from RetrofitInstance
    private val apiInterface: ApiInterface by lazy {
        RetrofitInstance.apiInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enabling edge-to-edge layout for modern Android design
        enableEdgeToEdge()

        // Setting the content of the activity
        setContent {
            AndroidAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainContent(
                        modifier = Modifier.padding(innerPadding),
                        apiInterface = apiInterface // Pass API interface to the composable
                    )
                }
            }
        }
    }

}

@Composable
fun MainContent(
    modifier: Modifier = Modifier, // Modifier for customizing the layout
    apiInterface: ApiInterface // The API interface used to fetch data
) {
    // Holds the list of users fetched from the API
    var users by remember { mutableStateOf<List<UserModel>?>(null) }
    // Tracks whether the data is still loading
    var isLoading by remember { mutableStateOf(true) }

    // Automatically runs when this composable is loaded
    LaunchedEffect(Unit) {
        // Fetch the list of users from the API
        fetchUsers(
            apiInterface,
            { userList -> // Success callback
                users = userList // Set the fetched users
                isLoading = false // Mark loading as complete
            },
            { error -> // Error callback
                Log.e("MainContent", "API Error: $error") // Log the error
                isLoading = false // Mark loading as complete even if there's an error
            }
        )
    }

    // Main layout of the screen
    Column(
        modifier = modifier
            .fillMaxSize() // Make the column fill the entire screen
            .padding(16.dp), // Add padding around the content
        verticalArrangement = Arrangement.Top // Align items at the top
    ) {
        // Show the header text
        Text(
            text = "Users List",
            style = MaterialTheme.typography.headlineSmall.copy(textAlign = TextAlign.Center), // Use headline style for the text
            modifier = Modifier.padding(bottom = 16.dp) // Add space below the header
        )

        // Show a loading spinner while the data is being fetched
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally) // Center the spinner
            )
        } else {
            // If data is loaded, show the list of users
            users?.let { userList ->
                // Use LazyColumn to display a scrollable list
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    // Display each user using the UserCard composable
                    items(userList) { user ->
                        UserCard(user = user)
                    }
                }
            } ?: Text(
                // If no users are found, show a message
                text = "No users found.",
                style = MaterialTheme.typography.bodyLarge // Use body style for the text
            )
        }
    }
}


/**
 * Fetches a list of users from the API.
 *
 * @param apiInterface The API interface used to make network requests.
 * @param onSuccess A callback function to handle the successful API response with the list of users.
 * @param onError A callback function to handle errors, either from the API response or the network call.
 */
fun fetchUsers(
    apiInterface: ApiInterface,
    onSuccess: (List<UserModel>) -> Unit,
    onError: (String) -> Unit
) {
    // Initiates a network call to fetch users using the provided ApiInterface.
    apiInterface.getUsers().enqueue(object : Callback<List<UserModel>> {

        // Called when the API responds successfully.
        override fun onResponse(call: Call<List<UserModel>>, response: Response<List<UserModel>>) {
            // Checks if the response is successful (status code 200-299).
            if (response.isSuccessful) {
                // If the response body is not null, passes the list of users to the onSuccess callback.
                response.body()?.let { users -> onSuccess(users) }
            } else {
                // If the response is not successful, passes an error message with the response code to the onError callback.
                onError("API Error: ${response.code()}")
            }
        }

        // Called when the API call fails, such as due to network issues or timeouts.
        override fun onFailure(call: Call<List<UserModel>>, t: Throwable) {
            // Passes the error message to the onError callback.
            onError("API Call Failed: ${t.message}")
        }
    })
}


