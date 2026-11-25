package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostSheet(
  userFreeCoins: Long,
  lang: String,
  onDismiss: () -> Unit,
  onSubmit: (content: String, imageUrl: String, videoUrl: String, isFlower: Boolean, boostType: String) -> Unit
) {
  var selectedTab by remember { mutableStateOf("POST_NOW") } // POST_NOW, VIDEO, LINK, PICTURE
  var postText by remember { mutableStateOf("") }
  var isFlowerNote by remember { mutableStateOf(false) }
  var selectedBoost by remember { mutableStateOf("FREE") } // FREE, BOOST, SUPER
  var hasImage by remember { mutableStateOf(false) }
  var hasVideo by remember { mutableStateOf(false) }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    containerColor = MaterialTheme.colorScheme.surface,
    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 8.dp)
        .navigationBarsPadding()
    ) {
      Text(
        text = if (lang == "bn") "নতুন পোস্ট তৈরি করুন" else "Create New Post",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Four Options: Post Now, Add Video, Add Link, Add Picture
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        OptionItem(
          title = if (lang == "bn") "পোস্ট করুন" else "Post Now",
          icon = Icons.Default.Edit,
          selected = selectedTab == "POST_NOW",
          onClick = {
            selectedTab = "POST_NOW"
            hasImage = false
            hasVideo = false
          }
        )
        OptionItem(
          title = if (lang == "bn") "ভিডিও যোগ" else "Add Video",
          icon = Icons.Default.Videocam,
          selected = selectedTab == "VIDEO",
          onClick = {
            selectedTab = "VIDEO"
            hasVideo = true
            hasImage = false
          }
        )
        OptionItem(
          title = if (lang == "bn") "লিংক যোগ" else "Add Link",
          icon = Icons.Default.Link,
          selected = selectedTab == "LINK",
          onClick = { selectedTab = "LINK" }
        )
        OptionItem(
          title = if (lang == "bn") "ছবি যোগ" else "Add Picture",
          icon = Icons.Default.PhotoLibrary,
          selected = selectedTab == "PICTURE",
          onClick = {
            selectedTab = "PICTURE"
            hasImage = true
            hasVideo = false
          }
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Text Input Field
      OutlinedTextField(
        value = postText,
        onValueChange = { postText = it },
        placeholder = {
          Text(
            if (lang == "bn") "আপনার মনের কথা বা ভাবনা শেয়ার করুন..."
            else "What's on your mind? Share with Moodbook..."
          )
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(110.dp)
          .testTag("create_post_input"),
        shape = RoundedCornerShape(12.dp)
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Flower Card Note Toggle
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(if (isFlowerNote) Color(0xFFFFEBF0) else MaterialTheme.colorScheme.surfaceVariant)
          .clickable { isFlowerNote = !isFlowerNote }
          .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text("🌸", fontSize = 20.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = if (lang == "bn") "গোলাপি ফ্লাওয়ার কার্ডে সাজান" else "Style as Pink Flower Note Card",
          fontSize = 13.sp,
          fontWeight = FontWeight.Medium,
          color = if (isFlowerNote) AccentPink else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.weight(1f))
        Switch(
          checked = isFlowerNote,
          onCheckedChange = { isFlowerNote = it }
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Distribution / Boost Options:
      // A. Free Post (profile only)
      // B. Boost Post (100 Free Coins)
      // C. Super Boost (500 Free Coins)
      Text(
        text = if (lang == "bn") "পোস্ট ডিস্ট্রিবিউশন ও বুস্ট অপশন" else "Distribution & Boost Plan",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        BoostChoiceCard(
          title = if (lang == "bn") "ফ্রি পোস্ট" else "Free Post",
          cost = "0 Coins",
          desc = if (lang == "bn") "প্রোফাইলে থাকবে" else "Profile Only",
          selected = selectedBoost == "FREE",
          modifier = Modifier.weight(1f),
          onClick = { selectedBoost = "FREE" }
        )
        BoostChoiceCard(
          title = if (lang == "bn") "বুস্ট পোস্ট" else "Boost Post",
          cost = "100 Free",
          desc = if (lang == "bn") "হোম ফিডে যাবে" else "Public Feed",
          selected = selectedBoost == "BOOST",
          modifier = Modifier.weight(1f),
          onClick = { selectedBoost = "BOOST" }
        )
        BoostChoiceCard(
          title = if (lang == "bn") "সুপার বুস্ট" else "Super Boost",
          cost = "500 Free",
          desc = if (lang == "bn") "অগ্রাধিকার ও নোটিফ" else "Priority Blast",
          selected = selectedBoost == "SUPER",
          modifier = Modifier.weight(1f),
          onClick = { selectedBoost = "SUPER" }
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Coins balance reminder
      val requiredCoins = when (selectedBoost) {
        "BOOST" -> 100L
        "SUPER" -> 500L
        else -> 0L
      }
      val canAfford = userFreeCoins >= requiredCoins

      if (!canAfford) {
        Text(
          text = if (lang == "bn") "⚠️ আপনার কাছে পর্যাপ্ত ফ্রি কয়েন নেই (প্রয়োজন: $requiredCoins, আছে: $userFreeCoins)"
          else "⚠️ Insufficient Free Coins (Need: $requiredCoins, Have: $userFreeCoins)",
          color = MaterialTheme.colorScheme.error,
          fontSize = 12.sp,
          modifier = Modifier.padding(horizontal = 4.dp)
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Publish Button
      Button(
        onClick = {
          if (postText.isNotBlank() && canAfford) {
            val img = if (hasImage) "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=800&q=80" else ""
            val vid = if (hasVideo) "https://example.com/video.mp4" else ""
            onSubmit(postText, img, vid, isFlowerNote, selectedBoost)
            onDismiss()
          }
        },
        enabled = postText.isNotBlank() && canAfford,
        colors = ButtonDefaults.buttonColors(containerColor = SageGreenPrimary),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("publish_post_button")
      ) {
        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = if (lang == "bn") "প্রকাশ করুন (Publish)" else "Publish Post",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold
        )
      }
      Spacer(modifier = Modifier.height(10.dp))
    }
  }
}

@Composable
private fun OptionItem(
  title: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  selected: Boolean,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .clip(RoundedCornerShape(12.dp))
      .clickable { onClick() }
      .padding(horizontal = 6.dp, vertical = 4.dp)
  ) {
    Box(
      modifier = Modifier
        .size(46.dp)
        .clip(CircleShape)
        .background(if (selected) SageGreenPrimary else SageGreenContainer),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = title,
        tint = if (selected) Color.White else SageGreenDark,
        modifier = Modifier.size(24.dp)
      )
    }
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = title,
      fontSize = 11.sp,
      fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
      color = if (selected) SageGreenDark else MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}

@Composable
private fun BoostChoiceCard(
  title: String,
  cost: String,
  desc: String,
  selected: Boolean,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (selected) SageGreenContainer else MaterialTheme.colorScheme.surfaceVariant
    ),
    border = if (selected) androidx.compose.foundation.BorderStroke(2.dp, SageGreenPrimary) else null,
    modifier = modifier.clickable { onClick() }
  ) {
    Column(
      modifier = Modifier.padding(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold)
      Text(text = cost, fontSize = 11.sp, color = SageGreenDark, fontWeight = FontWeight.SemiBold)
      Text(text = desc, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}
