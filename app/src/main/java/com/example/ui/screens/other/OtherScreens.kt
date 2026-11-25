package com.example.ui.screens.other

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FeedbackReport
import com.example.data.model.NotificationItem
import com.example.data.model.Post
import com.example.data.model.User
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
  notifications: List<NotificationItem>,
  lang: String,
  onBack: () -> Unit,
  onMarkAllRead: () -> Unit,
  modifier: Modifier = Modifier
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(if (lang == "bn") "নোটিফিকেশন সেন্টার" else "Notification Center", fontWeight = FontWeight.Bold) },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("notification_back_button")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        actions = {
          TextButton(onClick = onMarkAllRead, modifier = Modifier.testTag("mark_all_read_button")) {
            Text(if (lang == "bn") "সব পঠিত" else "Mark all read", color = Color(0xFF14291E), fontWeight = FontWeight.Bold)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = SageBarColor,
          titleContentColor = Color(0xFF14291E)
        )
      )
    }
  ) { padding ->
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .padding(padding)
        .background(MaterialTheme.colorScheme.background),
      contentPadding = PaddingValues(16.dp)
    ) {
      items(notifications, key = { it.id }) { notif ->
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (!notif.isRead) SageGreenContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
          ),
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(SageGreenPrimary),
              contentAlignment = Alignment.Center
            ) {
              Text("🔔", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(text = notif.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Text(text = notif.message, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(notif.timestamp))
              Text(text = dateStr, fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
            }
          }
        }
      }
      if (notifications.isEmpty()) {
        item {
          Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
            Text(if (lang == "bn") "কোনো নোটিফিকেশন নেই" else "No notifications yet", color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
  allUsers: List<User>,
  posts: List<Post>,
  lang: String,
  onBack: () -> Unit,
  onUserClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }
  val trendingTags = listOf("#Bangladesh", "#MoodbookRewards", "#FreeCoins", "#DhakaVibes", "#SylhetTrip", "#EarnMoney")

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(if (lang == "bn") "ইউজার বা পোস্ট খুঁজুন..." else "Search Moodbook...") },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("global_search_input"),
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = MaterialTheme.colorScheme.surface,
              unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
          )
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("search_back_button")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = SageBarColor
        )
      )
    }
  ) { padding ->
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .padding(padding)
        .background(MaterialTheme.colorScheme.background),
      contentPadding = PaddingValues(16.dp)
    ) {
      // Trending Hashtags
      item {
        Text(
          text = if (lang == "bn") "🔥 ট্রেন্ডিং হ্যাশট্যাগ" else "🔥 Trending Topics",
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(trendingTags) { tag ->
            Surface(
              shape = RoundedCornerShape(16.dp),
              color = SageGreenContainer,
              modifier = Modifier.clickable { searchQuery = tag }
            ) {
              Text(
                text = tag,
                color = SageGreenDark,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
              )
            }
          }
        }
        Spacer(modifier = Modifier.height(18.dp))
      }

      // Matching Users
      item {
        Text(
          text = if (lang == "bn") "ব্যবহারকারী (Users)" else "Users",
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
      }

      val matchedUsers = allUsers.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
        it.username.contains(searchQuery, ignoreCase = true) ||
        it.referralCode.contains(searchQuery, ignoreCase = true)
      }

      items(matchedUsers) { user ->
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onUserClick(user.id) }
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(40.dp)
                  .clip(CircleShape)
                  .background(SageGreenPrimary),
                contentAlignment = Alignment.Center
              ) {
                Text(user.name.take(1), color = Color.White, fontWeight = FontWeight.Bold)
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(user.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("@${user.username} • ${user.address}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }

            Button(
              onClick = {},
              colors = ButtonDefaults.buttonColors(containerColor = SageGreenPrimary),
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
              Text("Follow", fontSize = 11.sp)
            }
          }
        }
      }
    }
  }
}

@Composable
fun SettingsScreen(
  currentUser: User?,
  isDarkMode: Boolean,
  lang: String,
  onToggleDarkMode: () -> Unit,
  onToggleLanguage: () -> Unit,
  onNavigateToReferral: () -> Unit,
  onNavigateToAbout: () -> Unit,
  onNavigateToFeedback: () -> Unit,
  onNavigateToAdmin: () -> Unit,
  onLogout: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showLogoutConfirm by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .testTag("settings_screen"),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp)
  ) {
    item {
      Text(
        text = if (lang == "bn") "⚙️ সেটিংস ও মেনু" else "⚙️ Settings & Menu",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(14.dp))
    }

    // Admin Panel Banner (If user is Admin)
    if (currentUser?.isAdmin == true) {
      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = GoldPaidCoinLight),
          border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldPaidCoin),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigateToAdmin() }
            .testTag("open_admin_panel_button")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("👑", fontSize = 24.sp)
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = if (lang == "bn") "অ্যাডমিন ড্যাশবোর্ড" else "Admin Dashboard",
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  color = Color(0xFF9E6500)
                )
                Text(
                  text = "User moderation, coins, withdrawals",
                  fontSize = 11.sp,
                  color = Color(0xFF7A4E00)
                )
              }
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFF9E6500))
          }
        }
        Spacer(modifier = Modifier.height(14.dp))
      }
    }

    // Theme & Language Settings
    item {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          // Dark Mode Toggle
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.DarkMode, contentDescription = null, tint = SageGreenPrimary)
              Spacer(modifier = Modifier.width(10.dp))
              Text(if (lang == "bn") "ডার্ক মোড (Dark Mode)" else "Dark Theme", fontWeight = FontWeight.SemiBold)
            }
            Switch(checked = isDarkMode, onCheckedChange = { onToggleDarkMode() })
          }

          Divider(modifier = Modifier.padding(vertical = 10.dp), color = MaterialTheme.colorScheme.surfaceVariant)

          // Language Toggle
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Language, contentDescription = null, tint = SageGreenPrimary)
              Spacer(modifier = Modifier.width(10.dp))
              Text(if (lang == "bn") "ভাষা পরিবর্তন (Language)" else "App Language", fontWeight = FontWeight.SemiBold)
            }
            TextButton(onClick = onToggleLanguage) {
              Text(
                text = if (lang == "bn") "বাংলা (BN)" else "English (EN)",
                fontWeight = FontWeight.Bold,
                color = SageGreenDark
              )
            }
          }
        }
      }
      Spacer(modifier = Modifier.height(14.dp))
    }

    // Navigation Links: Invite & Earn, About, Feedback, Help
    item {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(8.dp)) {
          SettingsLinkRow(
            icon = Icons.Default.CardGiftcard,
            title = if (lang == "bn") "আমন্ত্রণ ও আয় (Invite & Earn)" else "Invite & Earn",
            onClick = onNavigateToReferral
          )
          SettingsLinkRow(
            icon = Icons.Default.Info,
            title = if (lang == "bn") "মুডবুক সম্পর্কে (About Moodbook)" else "About Moodbook",
            onClick = onNavigateToAbout
          )
          SettingsLinkRow(
            icon = Icons.Default.Feedback,
            title = if (lang == "bn") "মতামত ও অভিযোগ (Give Feedback)" else "Give Feedback",
            onClick = onNavigateToFeedback
          )
          SettingsLinkRow(
            icon = Icons.Default.Lock,
            title = if (lang == "bn") "অ্যাকাউন্ট সিকিউরিটি ও প্রাইভেসি" else "Security & Privacy",
            onClick = {}
          )
        }
      }
      Spacer(modifier = Modifier.height(16.dp))
    }

    // Logout Button
    item {
      Button(
        onClick = { showLogoutConfirm = true },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("logout_button")
      ) {
        Icon(Icons.Default.ExitToApp, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(if (lang == "bn") "লগআউট করুন (Logout)" else "Log Out", fontWeight = FontWeight.Bold)
      }
    }
  }

  if (showLogoutConfirm) {
    AlertDialog(
      onDismissRequest = { showLogoutConfirm = false },
      title = { Text(if (lang == "bn") "লগআউট নিশ্চিতকরণ" else "Confirm Logout") },
      text = {
        Text(
          if (lang == "bn") "আপনি কি নিশ্চিতভাবে আপনার অ্যাকাউন্ট থেকে লগআউট করতে চান?"
          else "Are you sure you want to securely log out of your Moodbook account?"
        )
      },
      confirmButton = {
        Button(
          onClick = {
            showLogoutConfirm = false
            onLogout()
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
        ) {
          Text(if (lang == "bn") "হ্যাঁ, লগআউট" else "Yes, Log Out")
        }
      },
      dismissButton = {
        TextButton(onClick = { showLogoutConfirm = false }) {
          Text(if (lang == "bn") "বাতিল" else "Cancel")
        }
      }
    )
  }
}

@Composable
private fun SettingsLinkRow(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .clickable { onClick() }
      .padding(horizontal = 12.dp, vertical = 10.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(icon, contentDescription = null, tint = SageGreenPrimary, modifier = Modifier.size(20.dp))
      Spacer(modifier = Modifier.width(12.dp))
      Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
  lang: String,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(if (lang == "bn") "মুডবুক পরিচিতি" else "About Moodbook", fontWeight = FontWeight.Bold) },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = SageBarColor)
      )
    }
  ) { padding ->
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .padding(padding)
        .background(MaterialTheme.colorScheme.background),
      contentPadding = PaddingValues(16.dp)
    ) {
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Moodbook 0.2", fontSize = 18.sp, fontWeight = FontWeight.Black, color = SageGreenDark)
            Text(text = "Tagline: Connect. Share. Earn. Enjoy.", fontSize = 12.sp, color = SageGreenPrimary)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = if (lang == "bn") "মুডবুক একটি আধুনিক সোশ্যাল নেটওয়ার্কিং ও ডিজিটাল রিওয়ার্ড প্ল্যাটফর্ম। বাংলাদেশের ব্যবহারকারীদের জন্য সহজে সংযোগ স্থাপন এবং সামাজিক পোস্টের মাধ্যমে কয়েন উপার্জনের চমৎকার সুযোগ।"
              else "Moodbook is a premier modern social networking & digital reward platform engineered for seamless engagement and reward monetization.",
              fontSize = 13.sp,
              lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Coin Rules (কয়েন অর্জনের নিয়ম):", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(text = "• Like on Post: +5 Free Coins\n• Unique Comment: +20 Free Coins\n• Share Post: +20 Free Coins\n• Daily Check-in: +50 Free Coins every 24h\n• 10 Rewarded Ads = 20 Paid Coins\n• Free Coin Exchange: 10 Paid Coins = 100 Free Coins\n• Withdrawal: 6,000 Paid Coins = ৳1,200 (bKash/Nagad/Rocket)", fontSize = 12.sp, lineHeight = 18.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Contact: islamrakib8522@gmail.com", fontSize = 12.sp, color = SageGreenDark, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(
  feedbackList: List<FeedbackReport>,
  lang: String,
  onBack: () -> Unit,
  onSubmitFeedback: (title: String, desc: String, category: String) -> Unit,
  modifier: Modifier = Modifier
) {
  var title by remember { mutableStateOf("") }
  var desc by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("Coin Problem") }

  val categories = listOf("Coin Problem", "Login Problem", "Payment Problem", "Post Problem", "Technical Issue", "Other")

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(if (lang == "bn") "মতামত ও অভিযোগ" else "Give Feedback", fontWeight = FontWeight.Bold) },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = SageBarColor)
      )
    }
  ) { padding ->
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .padding(padding)
        .background(MaterialTheme.colorScheme.background),
      contentPadding = PaddingValues(16.dp)
    ) {
      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(text = if (lang == "bn") "নতুন সমস্যা বা পরামর্শ জমা দিন" else "Submit Feedback or Report Bug", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
              value = title,
              onValueChange = { title = it },
              label = { Text("Title") },
              modifier = Modifier.fillMaxWidth().testTag("feedback_title_input")
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Category Chips
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              items(categories) { cat ->
                FilterChip(
                  selected = category == cat,
                  onClick = { category = cat },
                  label = { Text(cat, fontSize = 11.sp) }
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
              value = desc,
              onValueChange = { desc = it },
              label = { Text("Description") },
              modifier = Modifier.fillMaxWidth().height(90.dp).testTag("feedback_desc_input")
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
              onClick = {
                if (title.isNotBlank() && desc.isNotBlank()) {
                  onSubmitFeedback(title, desc, category)
                  title = ""
                  desc = ""
                }
              },
              enabled = title.isNotBlank() && desc.isNotBlank(),
              colors = ButtonDefaults.buttonColors(containerColor = SageGreenPrimary),
              modifier = Modifier.fillMaxWidth().testTag("submit_feedback_button")
            ) {
              Text(if (lang == "bn") "জমা দিন" else "Submit Report", fontWeight = FontWeight.Bold)
            }
          }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = if (lang == "bn") "পূর্বে দাখিলকৃত রিপোর্ট" else "Your Reports History", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
      }

      items(feedbackList) { report ->
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text(text = report.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Surface(
                color = if (report.status == "Resolved") FreeCoinGreenLight else Color(0xFFFFF3CD),
                shape = RoundedCornerShape(6.dp)
              ) {
                Text(
                  text = report.status,
                  color = if (report.status == "Resolved") FreeCoinGreen else Color(0xFF856404),
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }
            Text(text = report.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (report.adminReply != null) {
              Spacer(modifier = Modifier.height(4.dp))
              Text(text = "Admin: ${report.adminReply}", fontSize = 11.sp, color = SageGreenDark, fontWeight = FontWeight.SemiBold)
            }
          }
        }
      }
    }
  }
}
