package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.CoinType
import com.example.data.model.GiftItem
import com.example.ui.theme.*

@Composable
fun GiftDialog(
  giftCatalog: List<GiftItem>,
  userFreeCoins: Long,
  userPaidCoins: Long,
  lang: String,
  onDismiss: () -> Unit,
  onSendGift: (GiftItem) -> Unit
) {
  var selectedGift by remember { mutableStateOf<GiftItem?>(giftCatalog.firstOrNull()) }
  var showCelebration by remember { mutableStateOf(false) }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Top Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text("🌸", fontSize = 24.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = if (lang == "bn") "ফ্লাওয়ার গিফট পাঠান" else "Send Flower Gift",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
          IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Balance Badges
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          Surface(
            color = FreeCoinGreenLight,
            shape = RoundedCornerShape(12.dp)
          ) {
            Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)) {
              Text("🌿 Free: ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = FreeCoinGreen)
              Text("$userFreeCoins", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
          }

          Surface(
            color = GoldPaidCoinLight,
            shape = RoundedCornerShape(12.dp)
          ) {
            Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)) {
              Text("💰 Paid: ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB57C00))
              Text("$userPaidCoins", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Gift Grid
        LazyVerticalGrid(
          columns = GridCells.Fixed(3),
          verticalArrangement = Arrangement.spacedBy(10.dp),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          modifier = Modifier.height(190.dp)
        ) {
          items(giftCatalog) { gift ->
            val isSelected = selectedGift?.id == gift.id
            val isPaid = gift.coinType == CoinType.PAID
            val canAfford = if (isPaid) userPaidCoins >= gift.cost else userFreeCoins >= gift.cost

            Card(
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(
                containerColor = if (isSelected) {
                  if (isPaid) GoldPaidCoinLight else SageGreenContainer
                } else MaterialTheme.colorScheme.surfaceVariant
              ),
              border = if (isSelected) {
                androidx.compose.foundation.BorderStroke(2.dp, if (isPaid) GoldPaidCoin else SageGreenPrimary)
              } else null,
              modifier = Modifier
                .clickable { selectedGift = gift }
                .testTag("gift_item_${gift.id}")
            ) {
              Column(
                modifier = Modifier
                  .padding(8.dp)
                  .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text(text = gift.emoji, fontSize = 28.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = if (lang == "bn") gift.nameBn else gift.name,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "${gift.cost} ${if (isPaid) "Paid" else "Free"}",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = if (isPaid) Color(0xFFB57C00) else FreeCoinGreen
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Send Gift Button
        selectedGift?.let { gift ->
          val isPaid = gift.coinType == CoinType.PAID
          val canAfford = if (isPaid) userPaidCoins >= gift.cost else userFreeCoins >= gift.cost

          Button(
            onClick = {
              if (canAfford) {
                onSendGift(gift)
                onDismiss()
              }
            },
            enabled = canAfford,
            colors = ButtonDefaults.buttonColors(
              containerColor = if (isPaid) GoldPaidCoin else AccentPink
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(46.dp)
              .testTag("send_gift_action_button")
          ) {
            Text(
              text = if (canAfford) {
                if (lang == "bn") "${gift.emoji} উপহার পাঠান (${gift.cost} কয়েন)"
                else "Send ${gift.name} ${gift.emoji} (${gift.cost} Coins)"
              } else {
                if (lang == "bn") "পর্যাপ্ত ব্যালেন্স নেই" else "Insufficient Coins"
              },
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }
      }
    }
  }
}
