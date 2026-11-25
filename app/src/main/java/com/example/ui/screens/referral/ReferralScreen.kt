package com.example.ui.screens.referral

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.User
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReferralScreen(
  user: User?,
  lang: String,
  onBack: () -> Unit,
  onClaimReferralCode: (String) -> Unit,
  onShowMessage: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var enteredCode by remember { mutableStateOf("") }
  val referralLink = "https://sites.google.com/view/freerefer/home"
  val code = user?.referralCode ?: "MBK8522"

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            if (lang == "bn") "রেফারেল ও উপহার (Invite & Earn)" else "Invite & Earn",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
          )
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("referral_back_button")) {
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
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .padding(padding)
        .background(MaterialTheme.colorScheme.background),
      contentPadding = PaddingValues(16.dp)
    ) {
      // Header Card
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = SageGreenDark),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Box(
              modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(GoldPaidCoin),
              contentAlignment = Alignment.Center
            ) {
              Text("🎁", fontSize = 28.sp)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = if (lang == "bn") "বন্ধুদের আমন্ত্রণ জানান ও কয়েন আয় করুন!" else "Invite Friends & Earn Rewards!",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = if (lang == "bn") "প্রতিটি সফল আমন্ত্রণে:\nআপনি পাবেন ৫০০ ফ্রি কয়েন + ১০ পেইড কয়েন\nআপনার বন্ধুও পাবেন ৫০০ ফ্রি কয়েন + ১০ পেইড কয়েন!"
              else "For each eligible referral:\nReferrer receives: 500 Free Coins + 10 Paid Coins\nFriend receives: 500 Free Coins + 10 Paid Coins!",
              color = Color(0xFFD6E3DB),
              fontSize = 12.sp,
              lineHeight = 18.sp
            )
          }
        }
        Spacer(modifier = Modifier.height(16.dp))
      }

      // My Unique Referral Code Card
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = if (lang == "bn") "আপনার রেফারেল কোড:" else "Your Unique Referral Code:",
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 14.dp, vertical = 10.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = code,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = SageGreenPrimary,
                letterSpacing = 1.sp
              )
              IconButton(
                onClick = {
                  val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                  clipboard.setPrimaryClip(ClipData.newPlainText("Moodbook Code", code))
                  onShowMessage(if (lang == "bn") "কোড কপি করা হয়েছে!" else "Code copied to clipboard!")
                },
                modifier = Modifier.testTag("copy_referral_code_button")
              ) {
                Icon(Icons.Default.ContentCopy, contentDescription = "Copy Code", tint = SageGreenDark)
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Share buttons
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Button(
                onClick = {
                  val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                      Intent.EXTRA_TEXT,
                      "Join me on Moodbook 0.2! Use my referral code: $code to get 500 Free Coins + 10 Paid Coins!\nVisit: $referralLink"
                    )
                    type = "text/plain"
                  }
                  context.startActivity(Intent.createChooser(sendIntent, "Share Moodbook Referral"))
                },
                colors = ButtonDefaults.buttonColors(containerColor = SageGreenPrimary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                  .weight(1f)
                  .testTag("share_referral_link_button")
              ) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (lang == "bn") "শেয়ার লিংক" else "Share Link", fontSize = 12.sp)
              }

              OutlinedButton(
                onClick = {
                  val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                  clipboard.setPrimaryClip(ClipData.newPlainText("Moodbook Link", "$referralLink?ref=$code"))
                  onShowMessage(if (lang == "bn") "ওয়েবসাইট লিংক কপি হয়েছে!" else "Referral website link copied!")
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
              ) {
                Text(if (lang == "bn") "ওয়েবসাইট লিংক" else "Copy URL", fontSize = 12.sp)
              }
            }
          }
        }
        Spacer(modifier = Modifier.height(16.dp))
      }

      // Enter Friend's Referral Code Section (for users who skipped during registration)
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = if (lang == "bn") "বন্ধুর রেফারেল কোড দিন:" else "Have a Friend's Referral Code?",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
            Text(
              text = if (lang == "bn") "আপনি যদি রেজিস্ট্রেশনের সময় কোড না দিয়ে থাকেন, তবে এখানে কোড দিয়ে বোনাস সংগ্রহ করুন।"
              else "If you skipped during registration, claim your 500 Free + 10 Paid Coins reward here.",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (user?.referredBy != null) {
              Surface(
                color = FreeCoinGreenLight,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(
                  modifier = Modifier.padding(12.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text("✅", fontSize = 16.sp)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = if (lang == "bn") "আপনি ইতোমধ্যে রেফারেল বোনাস সংগ্রহ করেছেন (${user.referredBy})"
                    else "Referral reward claimed using code: ${user.referredBy}",
                    color = FreeCoinGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                  )
                }
              }
            } else {
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
              ) {
                OutlinedTextField(
                  value = enteredCode,
                  onValueChange = { enteredCode = it.uppercase() },
                  placeholder = { Text("MBKXXXX") },
                  modifier = Modifier
                    .weight(1f)
                    .testTag("enter_referral_code_input"),
                  shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                  onClick = {
                    if (enteredCode.isNotBlank()) {
                      onClaimReferralCode(enteredCode)
                      enteredCode = ""
                    }
                  },
                  enabled = enteredCode.isNotBlank(),
                  colors = ButtonDefaults.buttonColors(containerColor = SageGreenPrimary),
                  shape = RoundedCornerShape(12.dp),
                  modifier = Modifier.testTag("claim_referral_button")
                ) {
                  Text(if (lang == "bn") "ক্লেইম" else "Claim", fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }
      }
    }
  }
}
