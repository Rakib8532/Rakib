package com.example.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Post
import com.example.data.model.User
import com.example.ui.components.PostCard
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
  user: User?,
  userPosts: List<Post>,
  lang: String,
  onOpenWallet: () -> Unit,
  onOpenCreatePost: () -> Unit,
  onUpdateProfile: (name: String, bio: String, address: String) -> Unit,
  onLikeClick: (String) -> Unit,
  onShareClick: (String) -> Unit,
  onGiftClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var showEditDialog by remember { mutableStateOf(false) }
  var editName by remember { mutableStateOf(user?.name ?: "") }
  var editBio by remember { mutableStateOf(user?.bio ?: "") }
  var editAddress by remember { mutableStateOf(user?.address ?: "") }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .testTag("profile_screen"),
    contentPadding = PaddingValues(bottom = 100.dp)
  ) {
    // Header Banner
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(130.dp)
          .background(
            Brush.linearGradient(
              listOf(SageGreenDark, SageGreenPrimary, AccentCyan)
            )
          )
      )
    }

    // Avatar & Core Info
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .offset(y = (-45).dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.Bottom
        ) {
          // Avatar
          Box(
            modifier = Modifier
              .size(90.dp)
              .clip(CircleShape)
              .background(SageGreenPrimary)
              .border(4.dp, MaterialTheme.colorScheme.surface, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = user?.name?.take(1)?.uppercase() ?: "M",
              color = Color.White,
              fontSize = 38.sp,
              fontWeight = FontWeight.Bold
            )
          }

          // Action Buttons: Edit Profile & Wallet
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
              onClick = {
                editName = user?.name ?: ""
                editBio = user?.bio ?: ""
                editAddress = user?.address ?: ""
                showEditDialog = true
              },
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.testTag("edit_profile_button")
            ) {
              Text(if (lang == "bn") "এডিট প্রোফাইল" else "Edit Profile", fontSize = 11.sp)
            }

            Button(
              onClick = onOpenWallet,
              colors = ButtonDefaults.buttonColors(containerColor = SageGreenPrimary),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.testTag("profile_wallet_button")
            ) {
              Text(if (lang == "bn") "ওয়ালেট" else "Wallet", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Name, Username, Verification Badge & User ID
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = user?.name ?: "Rakib Islam",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          if (user?.isVerified == true) {
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = "Verified",
              tint = AccentCyan,
              modifier = Modifier.size(18.dp)
            )
          }
          if (user?.isAdmin == true) {
            Spacer(modifier = Modifier.width(6.dp))
            Surface(color = GoldPaidCoinLight, shape = RoundedCornerShape(4.dp)) {
              Text(
                text = "ADMIN",
                color = Color(0xFF9E6500),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
              )
            }
          }
        }

        Text(
          text = "@${user?.username ?: "rakib_islam"} • ID: #${user?.referralCode ?: "MBK8522"}",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = user?.bio ?: "",
          fontSize = 13.sp,
          color = MaterialTheme.colorScheme.onSurface,
          lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Stats Card (Followers, Following, Posts, Free Coins, Paid Coins)
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceAround
          ) {
            StatItem(label = if (lang == "bn") "পোস্ট" else "Posts", value = "${userPosts.size}")
            StatItem(label = if (lang == "bn") "ফলোয়ার" else "Followers", value = "${user?.followersCount ?: 0}")
            StatItem(label = if (lang == "bn") "ফলোয়িং" else "Following", value = "${user?.followingCount ?: 0}")
            StatItem(label = "Free", value = "${user?.freeCoins ?: 0}", color = FreeCoinGreen)
            StatItem(label = "Paid", value = "${user?.paidCoins ?: 0}", color = Color(0xFF9E6500))
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Personal Posts Title & Quick Post Button
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = if (lang == "bn") "আমার পোস্টসমূহ (${userPosts.size})" else "My Posts (${userPosts.size})",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
          )
          TextButton(onClick = onOpenCreatePost) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(if (lang == "bn") "নতুন পোস্ট" else "New Post", fontSize = 12.sp)
          }
        }
      }
    }

    // User Posts Stream
    items(userPosts) { post ->
      PostCard(
        post = post,
        lang = lang,
        onLikeClick = { onLikeClick(post.id) },
        onCommentClick = {},
        onShareClick = { onShareClick(post.id) },
        onGiftClick = { onGiftClick(post.id) },
        onProfileClick = {},
        onReportClick = {},
        modifier = Modifier.offset(y = (-30).dp)
      )
    }

    if (userPosts.isEmpty()) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .offset(y = (-30).dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = if (lang == "bn") "এখনও কোনো পোস্ট করেননি। প্লাস (+) আইকনে চাপ দিয়ে পোস্ট করুন!"
            else "You have not published any posts yet. Tap (+) to create one!",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
  }

  // Edit Profile Dialog
  if (showEditDialog) {
    AlertDialog(
      onDismissRequest = { showEditDialog = false },
      title = { Text(if (lang == "bn") "প্রোফাইল আপডেট করুন" else "Edit Profile") },
      text = {
        Column {
          OutlinedTextField(
            value = editName,
            onValueChange = { editName = it },
            label = { Text(if (lang == "bn") "পূর্ণ নাম" else "Full Name") },
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedTextField(
            value = editBio,
            onValueChange = { editBio = it },
            label = { Text(if (lang == "bn") "বায়ো (Bio)" else "Bio") },
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedTextField(
            value = editAddress,
            onValueChange = { editAddress = it },
            label = { Text(if (lang == "bn") "ঠিকানা / শহর" else "Address / City") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            onUpdateProfile(editName, editBio, editAddress)
            showEditDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = SageGreenPrimary)
        ) {
          Text(if (lang == "bn") "সংরক্ষণ করুন" else "Save Changes")
        }
      },
      dismissButton = {
        TextButton(onClick = { showEditDialog = false }) {
          Text(if (lang == "bn") "বাতিল" else "Cancel")
        }
      }
    )
  }
}

@Composable
private fun StatItem(label: String, value: String, color: Color = Color.Unspecified) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
      text = value,
      fontWeight = FontWeight.Bold,
      fontSize = 15.sp,
      color = if (color != Color.Unspecified) color else MaterialTheme.colorScheme.onSurface
    )
    Text(
      text = label,
      fontSize = 10.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}
