package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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
import com.example.data.model.User
import com.example.data.model.WithdrawalRequest
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
  currentUser: User?,
  allUsers: List<User>,
  withdrawals: List<WithdrawalRequest>,
  totalPostsCount: Int,
  lang: String,
  onBack: () -> Unit,
  onVerifyUser: (String) -> Unit,
  onToggleBanUser: (String) -> Unit,
  onAdjustCoins: (userId: String, free: Long, paid: Long, reason: String) -> Unit,
  onApproveWithdrawal: (String) -> Unit,
  onBroadcastAnnouncement: (title: String, message: String) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableStateOf(0) } // 0: Overview, 1: Users, 2: Withdrawals, 3: Broadcast
  var userSearchQuery by remember { mutableStateOf("") }

  // Adjust Coins Dialog
  var targetUserForCoins by remember { mutableStateOf<User?>(null) }
  var addFreeAmount by remember { mutableStateOf("100") }
  var addPaidAmount by remember { mutableStateOf("20") }
  var adjustReason by remember { mutableStateOf("Community Promotion") }

  // Announcement Dialog
  var announcementTitle by remember { mutableStateOf("Eid Bonus Announcement! 🌙") }
  var announcementMessage by remember { mutableStateOf("All active Moodbook users receive 200 Free Coins today! Check out Coin Center.") }

  val tabs = listOf(
    if (lang == "bn") "ওভারভিউ" else "Overview",
    if (lang == "bn") "ইউজার ম্যানেজমেন্ট" else "Users",
    if (lang == "bn") "উত্তোলন অনুমোদন" else "Withdrawals",
    if (lang == "bn") "ঘোষণা পাঠান" else "Broadcast"
  )

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              if (lang == "bn") "অ্যাডমিন প্যানেল" else "Admin Dashboard",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "islamrakib8522@gmail.com (Root Admin)",
              fontSize = 10.sp,
              color = Color(0xFF1E3827)
            )
          }
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("admin_back_button")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = SageBarColor,
          titleContentColor = Color(0xFF14291E)
        )
      )
    }
  ) { padding ->
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(padding)
        .background(MaterialTheme.colorScheme.background)
    ) {
      // Sub-tabs
      TabRow(selectedTabIndex = selectedTab) {
        tabs.forEachIndexed { index, title ->
          Tab(
            selected = selectedTab == index,
            onClick = { selectedTab = index },
            text = { Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
          )
        }
      }

      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp),
        contentPadding = PaddingValues(bottom = 60.dp)
      ) {
        // Tab 0: Overview Stats
        if (selectedTab == 0) {
          item {
            Text(
              text = if (lang == "bn") "📊 সিস্টেম ওভারভিউ ও মেটিক্স" else "📊 Platform Overview Metrics",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            val totalFreeInCirculation = allUsers.sumOf { it.freeCoins }
            val totalPaidInCirculation = allUsers.sumOf { it.paidCoins }
            val pendingWithdrawals = withdrawals.count { it.status == "Pending" }

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              MetricCard(
                title = if (lang == "bn") "মোট ইউজার" else "Total Users",
                value = "${allUsers.size}",
                modifier = Modifier.weight(1f)
              )
              MetricCard(
                title = if (lang == "bn") "ভেরিফায়েড" else "Verified",
                value = "${allUsers.count { it.isVerified }}",
                modifier = Modifier.weight(1f)
              )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              MetricCard(
                title = if (lang == "bn") "মোট পোস্ট" else "Total Posts",
                value = "$totalPostsCount",
                modifier = Modifier.weight(1f)
              )
              MetricCard(
                title = if (lang == "bn") "পেন্ডিং উইথড্র" else "Pending Withdraws",
                value = "$pendingWithdrawals",
                modifier = Modifier.weight(1f),
                color = if (pendingWithdrawals > 0) Color(0xFFE53935) else Color.Unspecified
              )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              MetricCard(
                title = "Circulating Free Coins",
                value = "$totalFreeInCirculation",
                color = FreeCoinGreen,
                modifier = Modifier.weight(1f)
              )
              MetricCard(
                title = "Circulating Paid Coins",
                value = "$totalPaidInCirculation",
                color = GoldPaidCoin,
                modifier = Modifier.weight(1f)
              )
            }
          }
        }

        // Tab 1: User Management
        if (selectedTab == 1) {
          item {
            OutlinedTextField(
              value = userSearchQuery,
              onValueChange = { userSearchQuery = it },
              placeholder = { Text(if (lang == "bn") "ইউজার খুঁজুন (নাম বা ইউজারনেম)" else "Search users...") },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("admin_user_search"),
              shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
          }

          val filteredUsers = allUsers.filter {
            it.name.contains(userSearchQuery, ignoreCase = true) ||
            it.username.contains(userSearchQuery, ignoreCase = true) ||
            it.email.contains(userSearchQuery, ignoreCase = true)
          }

          items(filteredUsers) { targetUser ->
            Card(
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                      modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(SageGreenPrimary),
                      contentAlignment = Alignment.Center
                    ) {
                      Text(targetUser.name.take(1), color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                      Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(targetUser.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        if (targetUser.isVerified) {
                          Spacer(modifier = Modifier.width(4.dp))
                          Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(14.dp))
                        }
                      }
                      Text("@${targetUser.username} • ${targetUser.email}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                  }

                  if (targetUser.isBlocked) {
                    Surface(color = Color(0xFFFFCDD2), shape = RoundedCornerShape(4.dp)) {
                      Text("BANNED", color = Color.Red, fontSize = 9.sp, modifier = Modifier.padding(4.dp), fontWeight = FontWeight.Bold)
                    }
                  }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "🌿 ${targetUser.freeCoins} Free  |  💰 ${targetUser.paidCoins} Paid",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                  )

                  Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    if (!targetUser.isVerified) {
                      OutlinedButton(
                        onClick = { onVerifyUser(targetUser.id) },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                      ) {
                        Text("Verify", fontSize = 10.sp)
                      }
                    }
                    Button(
                      onClick = { targetUserForCoins = targetUser },
                      colors = ButtonDefaults.buttonColors(containerColor = SageGreenPrimary),
                      contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                      Text("Coins ±", fontSize = 10.sp)
                    }
                    TextButton(
                      onClick = { onToggleBanUser(targetUser.id) },
                      contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                      Text(if (targetUser.isBlocked) "Unban" else "Ban", color = Color.Red, fontSize = 10.sp)
                    }
                  }
                }
              }
            }
          }
        }

        // Tab 2: Withdrawal Approvals
        if (selectedTab == 2) {
          items(withdrawals) { req ->
            Card(
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Column {
                    Text(text = "${req.userName} (${req.method})", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(text = "Wallet: ${req.mobileWallet}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                  }
                  Text(
                    text = "৳${req.amountBdt} (${req.amountCoins} Coins)",
                    color = SageGreenDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                  )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "Status: ${req.status}",
                    fontSize = 11.sp,
                    color = if (req.status == "Approved") FreeCoinGreen else Color(0xFFE53935),
                    fontWeight = FontWeight.Bold
                  )
                  if (req.status == "Pending") {
                    Button(
                      onClick = { onApproveWithdrawal(req.id) },
                      colors = ButtonDefaults.buttonColors(containerColor = FreeCoinGreen),
                      shape = RoundedCornerShape(8.dp),
                      contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                      modifier = Modifier.testTag("approve_withdrawal_${req.id}")
                    ) {
                      Text("Approve Payout", fontSize = 11.sp)
                    }
                  }
                }
              }
            }
          }
          if (withdrawals.isEmpty()) {
            item {
              Text(
                text = "No withdrawal requests found.",
                modifier = Modifier.padding(20.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }

        // Tab 3: Broadcast Announcement
        if (selectedTab == 3) {
          item {
            Card(
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Text(
                  text = if (lang == "bn") "সিস্টেম ঘোষণা ব্রডকাস্ট করুন" else "Broadcast System Announcement",
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                  value = announcementTitle,
                  onValueChange = { announcementTitle = it },
                  label = { Text("Title") },
                  modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                  value = announcementMessage,
                  onValueChange = { announcementMessage = it },
                  label = { Text("Message Body") },
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                )
                Spacer(modifier = Modifier.height(14.dp))
                Button(
                  onClick = {
                    onBroadcastAnnouncement(announcementTitle, announcementMessage)
                    announcementTitle = ""
                    announcementMessage = ""
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = SageGreenPrimary),
                  modifier = Modifier
                    .fillMaxWidth()
                    .testTag("send_broadcast_button")
                ) {
                  Icon(Icons.Default.Campaign, contentDescription = null)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(if (lang == "bn") "সবাইকে নোটিফিকেশন পাঠান" else "Broadcast to All Users")
                }
              }
            }
          }
        }
      }
    }
  }

  // Coin Adjustment Dialog
  targetUserForCoins?.let { user ->
    AlertDialog(
      onDismissRequest = { targetUserForCoins = null },
      title = { Text("Adjust Coins for @${user.username}") },
      text = {
        Column {
          OutlinedTextField(
            value = addFreeAmount,
            onValueChange = { addFreeAmount = it },
            label = { Text("Free Coins (+/-)") },
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedTextField(
            value = addPaidAmount,
            onValueChange = { addPaidAmount = it },
            label = { Text("Paid Coins (+/-)") },
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedTextField(
            value = adjustReason,
            onValueChange = { adjustReason = it },
            label = { Text("Audit Reason") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val free = addFreeAmount.toLongOrNull() ?: 0L
            val paid = addPaidAmount.toLongOrNull() ?: 0L
            onAdjustCoins(user.id, free, paid, adjustReason)
            targetUserForCoins = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = SageGreenPrimary)
        ) {
          Text("Apply Changes")
        }
      },
      dismissButton = {
        TextButton(onClick = { targetUserForCoins = null }) {
          Text("Cancel")
        }
      }
    )
  }
}

@Composable
private fun MetricCard(
  title: String,
  value: String,
  modifier: Modifier = Modifier,
  color: Color = Color.Unspecified
) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = modifier
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Text(
        text = value,
        fontWeight = FontWeight.Black,
        fontSize = 20.sp,
        color = if (color != Color.Unspecified) color else MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(text = title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}
