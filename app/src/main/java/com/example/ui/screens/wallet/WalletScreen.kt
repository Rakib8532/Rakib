package com.example.ui.screens.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.data.model.CoinTransaction
import com.example.data.model.CoinType
import com.example.data.model.User
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
  user: User?,
  transactions: List<CoinTransaction>,
  lang: String,
  onBack: () -> Unit,
  onVerifyUser: () -> Unit,
  onDeposit: (bdt: Long, method: String, wallet: String, trxId: String) -> Unit,
  onWithdraw: (method: String, wallet: String, coins: Long) -> Unit,
  onMobileRecharge: (operator: String, number: String, bdt: Long) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableStateOf(0) } // 0: Overview/History, 1: Deposit, 2: Withdraw, 3: Recharge

  // Deposit Form State
  var depositBdt by remember { mutableStateOf("1000") }
  var depositMethod by remember { mutableStateOf("bKash") }
  var depositWallet by remember { mutableStateOf("01712345678") }
  var depositTrxId by remember { mutableStateOf("TRX" + (100000..999999).random()) }

  // Withdraw Form State
  var withdrawCoins by remember { mutableStateOf("6000") }
  var withdrawMethod by remember { mutableStateOf("bKash") }
  var withdrawWallet by remember { mutableStateOf("") }

  // Recharge Form State
  var rechargeOperator by remember { mutableStateOf("Grameenphone") }
  var rechargeNumber by remember { mutableStateOf("") }
  var rechargeAmount by remember { mutableStateOf("50") }

  val tabs = if (lang == "bn") {
    listOf("লেনদেন হিস্ট্রি", "ডিপোজিট (কয়েন ক্রয়)", "উত্তোলন (Withdraw)", "মোবাইল রিচার্জ")
  } else {
    listOf("History", "Buy Coins", "Withdraw BDT", "Mobile Recharge")
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            if (lang == "bn") "মুডবুক ওয়ালেট (BDT ৳)" else "Moodbook Wallet (BDT ৳)",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
          )
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("wallet_back_button")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = SageBarColor,
          titleContentColor = Color(0xFF14291E),
          navigationIconContentColor = Color(0xFF14291E)
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
      // Verification Alert Banner if unverified
      if (user?.isVerified == false) {
        item {
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3CD)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFEEBA)),
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 14.dp)
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⚠️", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = if (lang == "bn") "অ্যাকাউন্ট ভেরিফিকেশন প্রয়োজন" else "Account Verification Required",
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF856404),
                  fontSize = 13.sp
                )
              }
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = if (lang == "bn") "ডিপোজিট, উত্তোলন ও রিচার্জ করতে ইমেইল ভেরিফাই করুন।"
                else "Verify your email to unlock deposits, withdrawals & mobile recharge.",
                fontSize = 11.sp,
                color = Color(0xFF856404)
              )
              Spacer(modifier = Modifier.height(8.dp))
              Button(
                onClick = onVerifyUser,
                colors = ButtonDefaults.buttonColors(containerColor = SageGreenPrimary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("verify_now_button")
              ) {
                Text(
                  text = if (lang == "bn") "এখনই ভেরিফাই করুন (Verify Now)" else "Verify Account Now",
                  fontSize = 11.sp
                )
              }
            }
          }
        }
      }

      // Wallet Header with Balances
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = SageGreenDark),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = if (lang == "bn") "মোট কয়েন ব্যালেন্স" else "Total Coin Balances",
              color = SageGreenLight,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column {
                Text("🌿 Free Coins", color = Color(0xFFA5D6A7), fontSize = 11.sp)
                Text(
                  text = "${user?.freeCoins ?: 0}",
                  color = Color.White,
                  fontSize = 20.sp,
                  fontWeight = FontWeight.Bold
                )
              }
              Column {
                Text("💰 Paid Coins", color = GoldPaidCoin, fontSize = 11.sp)
                Text(
                  text = "${user?.paidCoins ?: 0}",
                  color = GoldPaidCoin,
                  fontSize = 20.sp,
                  fontWeight = FontWeight.Bold
                )
              }
              Column {
                Text("৳ Approx BDT", color = Color(0xFF80DEEA), fontSize = 11.sp)
                val approxBdt = ((user?.paidCoins ?: 0) / 6000f) * 1200f
                Text(
                  text = String.format("৳%.1f", approxBdt),
                  color = Color.White,
                  fontSize = 20.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }
        Spacer(modifier = Modifier.height(14.dp))
      }

      // Section Navigation Tabs
      item {
        ScrollableTabRow(
          selectedTabIndex = selectedTab,
          edgePadding = 0.dp,
          containerColor = Color.Transparent,
          divider = {}
        ) {
          tabs.forEachIndexed { index, title ->
            Tab(
              selected = selectedTab == index,
              onClick = { selectedTab = index },
              text = {
                Text(
                  text = title,
                  fontSize = 12.sp,
                  fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                )
              }
            )
          }
        }
        Spacer(modifier = Modifier.height(14.dp))
      }

      // Tab 0: Transaction History
      if (selectedTab == 0) {
        item {
          Text(
            text = if (lang == "bn") "লেনদেন রেকর্ড (${transactions.size})" else "Transactions (${transactions.size})",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
          Spacer(modifier = Modifier.height(8.dp))
        }

        items(transactions) { txn ->
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(if (txn.coinType == CoinType.PAID) GoldPaidCoinLight else FreeCoinGreenLight),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = if (txn.coinType == CoinType.PAID) "💰" else "🌿",
                    fontSize = 18.sp
                  )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(text = txn.description, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                  val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(txn.timestamp))
                  Text(text = "$dateStr • ${txn.status}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
              }

              val sign = if (txn.amount > 0) "+" else ""
              val color = if (txn.amount > 0) FreeCoinGreen else Color(0xFFD32F2F)
              Text(
                text = "$sign${txn.amount} ${txn.coinType.name}",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = color
              )
            }
          }
        }
      }

      // Tab 1: Deposit / Buy Paid Coins
      if (selectedTab == 1) {
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                text = if (lang == "bn") "পেইড কয়েন ক্রয় করুন (৳১,০০০ = ৫,০০০ কয়েন)"
                else "Buy Paid Coins (৳1,000 = 5,000 Paid Coins)",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
              )
              Spacer(modifier = Modifier.height(10.dp))

              // BDT Options: ৳200, ৳500, ৳1000, ৳2000
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                listOf("200", "500", "1000", "2000").forEach { amount ->
                  FilterChip(
                    selected = depositBdt == amount,
                    onClick = { depositBdt = amount },
                    label = { Text("৳$amount") }
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              // Method: bKash, Nagad, Rocket, Bank
              Text(text = if (lang == "bn") "পেমেন্ট মাধ্যম:" else "Payment Method:", fontSize = 12.sp)
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                listOf("bKash", "Nagad", "Rocket").forEach { m ->
                  FilterChip(
                    selected = depositMethod == m,
                    onClick = { depositMethod = m },
                    label = { Text(m) }
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              OutlinedTextField(
                value = depositWallet,
                onValueChange = { depositWallet = it },
                label = { Text(if (lang == "bn") "মোবাইল ওয়ালেট নম্বর" else "Wallet Number") },
                modifier = Modifier.fillMaxWidth()
              )

              Spacer(modifier = Modifier.height(10.dp))

              OutlinedTextField(
                value = depositTrxId,
                onValueChange = { depositTrxId = it },
                label = { Text(if (lang == "bn") "পেমেন্ট Transaction ID" else "Payment TrxID") },
                modifier = Modifier.fillMaxWidth()
              )

              Spacer(modifier = Modifier.height(14.dp))

              val bdt = depositBdt.toLongOrNull() ?: 1000L
              val willGetCoins = bdt * 5

              Button(
                onClick = {
                  onDeposit(bdt, depositMethod, depositWallet, depositTrxId)
                  selectedTab = 0
                },
                enabled = user?.isVerified == true && depositWallet.isNotBlank() && depositTrxId.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = GoldPaidCoin),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("submit_deposit_button")
              ) {
                Text(
                  text = if (lang == "bn") "নিশ্চিত করুন (পাবেন: $willGetCoins কয়েন)"
                  else "Confirm Purchase (Get: $willGetCoins Coins)",
                  color = Color.Black,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }
      }

      // Tab 2: Withdraw (6,000 Paid Coins = ৳1,200)
      if (selectedTab == 2) {
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                text = if (lang == "bn") "উত্তোলন (৬,০০০ পেইড কয়েন = ৳১,২০০)"
                else "Withdrawal (6,000 Paid Coins = ৳1,200)",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
              )
              Spacer(modifier = Modifier.height(10.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                listOf("bKash", "Nagad", "Rocket").forEach { m ->
                  FilterChip(
                    selected = withdrawMethod == m,
                    onClick = { withdrawMethod = m },
                    label = { Text(m) }
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              OutlinedTextField(
                value = withdrawWallet,
                onValueChange = { withdrawWallet = it },
                placeholder = { Text("01XXXXXXXXX") },
                label = { Text(if (lang == "bn") "প্রাপক মোবাইল নম্বর" else "Recipient Number") },
                modifier = Modifier.fillMaxWidth()
              )

              Spacer(modifier = Modifier.height(10.dp))

              OutlinedTextField(
                value = withdrawCoins,
                onValueChange = { withdrawCoins = it },
                label = { Text(if (lang == "bn") "পেইড কয়েনের পরিমাণ (ন্যূনতম ৬,০০০)" else "Paid Coins (Min 6,000)") },
                modifier = Modifier.fillMaxWidth()
              )

              val coins = withdrawCoins.toLongOrNull() ?: 0L
              val bdt = (coins / 6000L) * 1200L

              if (coins >= 6000) {
                Text(
                  text = if (lang == "bn") "আপনি পাবেন: ৳$bdt" else "You will receive: ৳$bdt",
                  color = SageGreenDark,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp,
                  modifier = Modifier.padding(top = 4.dp)
                )
              }

              Spacer(modifier = Modifier.height(14.dp))

              Button(
                onClick = {
                  onWithdraw(withdrawMethod, withdrawWallet, coins)
                  selectedTab = 0
                },
                enabled = user?.isVerified == true && withdrawWallet.isNotBlank() && coins >= 6000 && (user?.paidCoins ?: 0) >= coins,
                colors = ButtonDefaults.buttonColors(containerColor = SageGreenPrimary),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("submit_withdraw_button")
              ) {
                Text(
                  text = if (lang == "bn") "উত্তোলন রিকোয়েস্ট পাঠান" else "Submit Withdrawal Request",
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }
      }

      // Tab 3: Mobile Recharge (৳20 to ৳100)
      if (selectedTab == 3) {
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                text = if (lang == "bn") "মোবাইল রিচার্জ (৳২০ - ৳১০০)"
                else "Mobile Recharge (৳20 - ৳100)",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
              )
              Spacer(modifier = Modifier.height(10.dp))

              // Operators: GP, Robi, Airtel, Banglalink, Teletalk
              Text(text = if (lang == "bn") "অপারেটর নির্বাচন করুন:" else "Select Operator:", fontSize = 12.sp)
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                listOf("Grameenphone", "Robi", "Banglalink").forEach { op ->
                  FilterChip(
                    selected = rechargeOperator == op,
                    onClick = { rechargeOperator = op },
                    label = { Text(op.take(6), fontSize = 10.sp) }
                  )
                }
              }
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                listOf("Airtel", "Teletalk").forEach { op ->
                  FilterChip(
                    selected = rechargeOperator == op,
                    onClick = { rechargeOperator = op },
                    label = { Text(op, fontSize = 10.sp) }
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              OutlinedTextField(
                value = rechargeNumber,
                onValueChange = { rechargeNumber = it },
                placeholder = { Text("01XXXXXXXXX") },
                label = { Text(if (lang == "bn") "মোবাইল নম্বর" else "Mobile Number") },
                modifier = Modifier.fillMaxWidth()
              )

              Spacer(modifier = Modifier.height(10.dp))

              // Amount chip selection
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                listOf("20", "30", "50", "80", "99").forEach { a ->
                  FilterChip(
                    selected = rechargeAmount == a,
                    onClick = { rechargeAmount = a },
                    label = { Text("৳$a") }
                  )
                }
              }

              Spacer(modifier = Modifier.height(14.dp))

              val amount = rechargeAmount.toLongOrNull() ?: 50L
              val costPaidCoins = amount * 5

              Button(
                onClick = {
                  onMobileRecharge(rechargeOperator, rechargeNumber, amount)
                  selectedTab = 0
                },
                enabled = user?.isVerified == true && rechargeNumber.length >= 11 && (user?.paidCoins ?: 0) >= costPaidCoins,
                colors = ButtonDefaults.buttonColors(containerColor = SageGreenPrimary),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("submit_recharge_button")
              ) {
                Text(
                  text = if (lang == "bn") "রিচার্জ নিশ্চিত করুন ($costPaidCoins পেইড কয়েন)"
                  else "Confirm Recharge ($costPaidCoins Paid Coins)",
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }
      }
    }
  }
}
