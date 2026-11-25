package com.example.ui.screens.coin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.model.User
import com.example.ui.theme.*

@Composable
fun CoinCenterScreen(
  user: User?,
  paidAdProgress: Int,
  freeAdClaims: Set<Int>,
  lang: String,
  onDailyCheckin: () -> Unit,
  onWatchPaidAd: () -> Unit,
  onClaimFreeAdPackage: (Int) -> Unit,
  onExchangePaidCoins: (Long) -> Unit,
  onOpenWallet: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showExchangeDialog by remember { mutableStateOf(false) }
  var exchangeAmountInput by remember { mutableStateOf("10") }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .testTag("coin_center_screen"),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp)
  ) {
    item {
      Text(
        text = if (lang == "bn") "🪙 মুডবুক কয়েন সেন্টার" else "🪙 Moodbook Coin Center",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = if (lang == "bn") "সোশ্যাল অ্যাক্টিভিটি, বিজ্ঞাপন ও রেফারেল থেকে কয়েন আয় করুন"
        else "Earn digital coins from interactions, rewarded ads & referrals",
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      Spacer(modifier = Modifier.height(14.dp))
    }

    // Dual Balances Cards: FREE COINS & PAID COINS
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Free Coins Card
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = FreeCoinGreenLight),
          border = androidx.compose.foundation.BorderStroke(1.5.dp, FreeCoinGreen),
          modifier = Modifier
            .weight(1f)
            .testTag("free_coins_balance_card")
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("🌿", fontSize = 18.sp)
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (lang == "bn") "ফ্রি কয়েন" else "FREE COINS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = FreeCoinGreen
              )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "${user?.freeCoins ?: 0}",
              fontSize = 22.sp,
              fontWeight = FontWeight.Black,
              color = FreeCoinGreen
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = if (lang == "bn") "পোস্ট বুস্ট ও গিফটে প্রযোজ্য" else "For boosts & normal gifts",
              fontSize = 10.sp,
              color = Color(0xFF26592C)
            )
          }
        }

        // Paid Coins Card
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = GoldPaidCoinLight),
          border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldPaidCoin),
          modifier = Modifier
            .weight(1f)
            .testTag("paid_coins_balance_card")
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("💰", fontSize = 18.sp)
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (lang == "bn") "পেইড কয়েন" else "PAID COINS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF9E6500)
              )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "${user?.paidCoins ?: 0}",
              fontSize = 22.sp,
              fontWeight = FontWeight.Black,
              color = Color(0xFF9E6500)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = if (lang == "bn") "উইথড্র ও রিচার্জে যোগ্য" else "Withdrawable / Rechargeable",
              fontSize = 10.sp,
              color = Color(0xFF7A4E00)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
    }

    // Quick Actions Row: Exchange & Open Wallet
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Button(
          onClick = { showExchangeDialog = true },
          colors = ButtonDefaults.buttonColors(containerColor = SageGreenPrimary),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .weight(1f)
            .testTag("exchange_coins_button")
        ) {
          Icon(Icons.Default.SwapHoriz, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = if (lang == "bn") "কয়েন রূপান্তর" else "Exchange", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        OutlinedButton(
          onClick = onOpenWallet,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .weight(1f)
            .testTag("open_wallet_button")
        ) {
          Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = if (lang == "bn") "ওয়ালেট ও উত্তোলন" else "Wallet & BDT", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      }
      Spacer(modifier = Modifier.height(18.dp))
    }

    // Daily Check-in Card (+50 Free Coins)
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(SageGreenContainer),
            contentAlignment = Alignment.Center
          ) {
            Text("📅", fontSize = 22.sp)
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = if (lang == "bn") "দৈনিক চেক-ইন (Daily Check-in)" else "Daily Check-in",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
            Text(
              text = if (lang == "bn") "+৫০ ফ্রি কয়েন (প্রতি ২৪ ঘণ্টায়)" else "+50 Free Coins (Every 24 hours)",
              fontSize = 12.sp,
              color = SageGreenDark
            )
          }
          Button(
            onClick = onDailyCheckin,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SageGreenPrimary),
            modifier = Modifier.testTag("claim_daily_checkin_button")
          ) {
            Text(text = if (lang == "bn") "ক্লেইম" else "Claim", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
      Spacer(modifier = Modifier.height(18.dp))
    }

    // Rewarded Ads for Paid Coins: 10 Ads = 20 Paid Coins!
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("📺", fontSize = 20.sp)
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = if (lang == "bn") "পেইড কয়েন বিজ্ঞাপন টাস্ক" else "Earn Paid Coins via Ads",
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp
                )
                Text(
                  text = if (lang == "bn") "১০টি বিজ্ঞাপন = ২০ পেইড কয়েন" else "10 Completed Ads = 20 Paid Coins",
                  fontSize = 11.sp,
                  color = Color(0xFF9E6500)
                )
              }
            }

            Surface(
              color = GoldPaidCoinLight,
              shape = RoundedCornerShape(8.dp)
            ) {
              Text(
                text = "$paidAdProgress/10",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF9E6500),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          LinearProgressIndicator(
            progress = { paidAdProgress / 10f },
            modifier = Modifier
              .fillMaxWidth()
              .height(8.dp)
              .clip(RoundedCornerShape(4.dp)),
            color = GoldPaidCoin,
            trackColor = Color(0xFFF1E4C3)
          )

          Spacer(modifier = Modifier.height(12.dp))

          Button(
            onClick = onWatchPaidAd,
            colors = ButtonDefaults.buttonColors(containerColor = GoldPaidCoin),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("watch_paid_ad_button")
          ) {
            Icon(Icons.Default.PlayCircle, contentDescription = null, tint = Color.Black)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = if (lang == "bn") "বিজ্ঞাপন দেখুন ($paidAdProgress/10)" else "Watch Ad ($paidAdProgress/10)",
              color = Color.Black,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
      Spacer(modifier = Modifier.height(18.dp))
    }

    // Free Coin Rewarded Ad Packages (1 Ad = 20, 3 Ads = 100, 5 Ads = 200, 10 Ads = 500)
    item {
      Text(
        text = if (lang == "bn") "🎁 ফ্রি কয়েন ডেইলি অ্যাড প্যাকেজ" else "🎁 Free Coin Daily Ad Packages",
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(8.dp))
    }

    val adPackages = listOf(
      Triple(1, 20L, "1 Ad = 20 Free Coins"),
      Triple(3, 100L, "3 Ads = 100 Free Coins"),
      Triple(5, 200L, "5 Ads = 200 Free Coins"),
      Triple(10, 500L, "10 Ads = 500 Free Coins")
    )

    items(adPackages.size) { idx ->
      val (count, reward, label) = adPackages[idx]
      val isClaimed = freeAdClaims.contains(count)

      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
          containerColor = if (isClaimed) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
        ),
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
            Text("🎁", fontSize = 18.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(text = label, fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Text(
                text = if (lang == "bn") "প্রতিদিন একবার ক্লেইমযোগ্য" else "Claimable once daily",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Button(
            onClick = { onClaimFreeAdPackage(count) },
            enabled = !isClaimed,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SageGreenPrimary),
            modifier = Modifier.testTag("claim_ad_package_$count")
          ) {
            Text(
              text = if (isClaimed) (if (lang == "bn") "ক্লেইমড" else "Claimed") else (if (lang == "bn") "ক্লেইম" else "Claim"),
              fontSize = 11.sp
            )
          }
        }
      }
    }
  }

  // Coin Exchange Dialog: 10 Paid Coins = 100 Free Coins
  if (showExchangeDialog) {
    AlertDialog(
      onDismissRequest = { showExchangeDialog = false },
      title = {
        Text(if (lang == "bn") "কয়েন রূপান্তর (Coin Exchange)" else "Exchange Paid Coins")
      },
      text = {
        Column {
          Text(
            text = if (lang == "bn") "নিয়ম: ১০ পেইড কয়েন = ১০০ ফ্রি কয়েন\nআপনার পেইড কয়েন ব্যালেন্স: ${user?.paidCoins ?: 0}"
            else "Rate: 10 Paid Coins = 100 Free Coins\nYour Paid Coins: ${user?.paidCoins ?: 0}",
            fontSize = 13.sp
          )
          Spacer(modifier = Modifier.height(12.dp))
          OutlinedTextField(
            value = exchangeAmountInput,
            onValueChange = { exchangeAmountInput = it },
            label = { Text(if (lang == "bn") "পেইড কয়েনের পরিমাণ (১০ এর গুণিতক)" else "Paid Coins (Multiples of 10)") },
            modifier = Modifier.fillMaxWidth()
          )
          val entered = exchangeAmountInput.toLongOrNull() ?: 0L
          val willReceive = (entered / 10) * 100
          if (entered > 0) {
            Text(
              text = if (lang == "bn") "আপনি পাবেন: $willReceive ফ্রি কয়েন" else "You will receive: $willReceive Free Coins",
              color = SageGreenDark,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              modifier = Modifier.padding(top = 6.dp)
            )
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val amount = exchangeAmountInput.toLongOrNull() ?: 0L
            if (amount > 0 && amount % 10 == 0L && (user?.paidCoins ?: 0) >= amount) {
              onExchangePaidCoins(amount)
              showExchangeDialog = false
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = SageGreenPrimary)
        ) {
          Text(if (lang == "bn") "নিশ্চিত করুন" else "Confirm Exchange")
        }
      },
      dismissButton = {
        TextButton(onClick = { showExchangeDialog = false }) {
          Text(if (lang == "bn") "বাতিল" else "Cancel")
        }
      }
    )
  }
}
