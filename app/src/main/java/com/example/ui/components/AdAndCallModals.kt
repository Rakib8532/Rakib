package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.*

@Composable
fun AdViewerModal(
  countdown: Int,
  isReady: Boolean,
  lang: String,
  onClaim: () -> Unit,
  onClose: () -> Unit
) {
  Dialog(
    onDismissRequest = { if (isReady) onClose() },
    properties = DialogProperties(dismissOnBackPress = isReady, dismissOnClickOutside = false)
  ) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color(0xFF161F1A)),
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Top status
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            color = Color(0xFF24332B),
            shape = RoundedCornerShape(8.dp)
          ) {
            Text(
              text = "Rewarded Ad • Moodbook Partner",
              color = SageGreenLight,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }

          if (isReady) {
            IconButton(onClick = onClose, modifier = Modifier.size(28.dp)) {
              Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
          } else {
            Text(
              text = "Reward in: ${countdown}s",
              color = GoldPaidCoin,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Ad Creative Video Simulation
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
              Brush.verticalGradient(
                listOf(Color(0xFF22362C), Color(0xFF0D1712))
              )
            )
            .border(2.dp, SageGreenDark, RoundedCornerShape(14.dp)),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
              modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(SageGreenPrimary),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                Icons.Default.MonetizationOn,
                contentDescription = null,
                tint = GoldPaidCoin,
                modifier = Modifier.size(36.dp)
              )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = "Moodbook Digital Rewards",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp
            )
            Text(
              text = "Watch 10 Rewarded Ads to earn 20 Paid Coins!",
              color = Color(0xFFB5C4BB),
              fontSize = 12.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (isReady) {
          Button(
            onClick = onClaim,
            colors = ButtonDefaults.buttonColors(containerColor = GoldPaidCoin),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("claim_ad_reward_button")
          ) {
            Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (lang == "bn") "পুরস্কার দাবি করুন (Claim Reward)" else "Claim Reward Now",
              color = Color.Black,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            )
          }
        } else {
          LinearProgressIndicator(
            progress = { (5 - countdown) / 5f },
            modifier = Modifier
              .fillMaxWidth()
              .height(8.dp)
              .clip(RoundedCornerShape(4.dp)),
            color = SageGreenPrimary,
            trackColor = Color(0xFF2A3D32)
          )
        }
      }
    }
  }
}

@Composable
fun AudioCallModal(
  participantName: String,
  callDuration: Int,
  lang: String,
  onEndCall: () -> Unit
) {
  var isMuted by remember { mutableStateOf(false) }
  var isSpeaker by remember { mutableStateOf(false) }

  Dialog(onDismissRequest = {}) {
    Card(
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = Color(0xFF14211A)),
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = if (lang == "bn") "মুডবুক অডিও কল" else "Moodbook Audio Call",
          color = SageGreenLight,
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Avatar
        Box(
          modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(SageGreenPrimary)
            .border(3.dp, SageGreenLight, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = participantName.take(1).uppercase(),
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = participantName,
          color = Color.White,
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp
        )

        val minutes = callDuration / 60
        val seconds = callDuration % 60
        Text(
          text = String.format("%02d:%02d", minutes, seconds),
          color = Color(0xFFA5B8AD),
          fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        // In-call buttons: Mute, Speaker, End Call
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = { isMuted = !isMuted },
            modifier = Modifier
              .size(50.dp)
              .clip(CircleShape)
              .background(if (isMuted) Color.White else Color(0xFF263B2F))
          ) {
            Icon(
              imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
              contentDescription = "Mute",
              tint = if (isMuted) Color.Black else Color.White
            )
          }

          IconButton(
            onClick = onEndCall,
            modifier = Modifier
              .size(60.dp)
              .clip(CircleShape)
              .background(Color(0xFFE53935))
              .testTag("end_audio_call_button")
          ) {
            Icon(
              imageVector = Icons.Default.CallEnd,
              contentDescription = "End Call",
              tint = Color.White,
              modifier = Modifier.size(32.dp)
            )
          }

          IconButton(
            onClick = { isSpeaker = !isSpeaker },
            modifier = Modifier
              .size(50.dp)
              .clip(CircleShape)
              .background(if (isSpeaker) Color.White else Color(0xFF263B2F))
          ) {
            Icon(
              imageVector = if (isSpeaker) Icons.Default.VolumeUp else Icons.Default.VolumeDown,
              contentDescription = "Speaker",
              tint = if (isSpeaker) Color.Black else Color.White
            )
          }
        }
      }
    }
  }
}
