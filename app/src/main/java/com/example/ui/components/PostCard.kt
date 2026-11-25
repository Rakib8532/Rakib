package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Post
import com.example.ui.theme.*

@Composable
fun PostCard(
  post: Post,
  lang: String,
  onLikeClick: () -> Unit,
  onCommentClick: () -> Unit,
  onShareClick: () -> Unit,
  onGiftClick: () -> Unit,
  onProfileClick: () -> Unit,
  onReportClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showMenu by remember { mutableStateOf(false) }

  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 12.dp, vertical = 6.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
    ) {
      // Header: Author Avatar on left + Name/Handle + 3-Dot Menu
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Author Avatar
        Box(
          modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(
              Brush.linearGradient(
                listOf(SageGreenPrimary, AccentCyan)
              )
            )
            .border(2.dp, Color.White, CircleShape)
            .clickable { onProfileClick() },
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = post.authorName.take(1).uppercase(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
          )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Author Details
        Column(
          modifier = Modifier
            .weight(1f)
            .clickable { onProfileClick() }
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = post.authorName,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = "Verified",
              tint = AccentCyan,
              modifier = Modifier.size(15.dp)
            )
          }
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "@${post.authorUsername}",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
              text = " • 12m",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        // Boost Status Badges
        if (post.isSuperBoosted) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Brush.horizontalGradient(listOf(GoldPaidCoin, Color(0xFFFF8A00))))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "🚀 Super",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
          Spacer(modifier = Modifier.width(4.dp))
        } else if (post.isBoosted) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Brush.horizontalGradient(listOf(AccentCyan, SageGreenPrimary)))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text(
              text = "⚡ Boost",
              color = Color.White,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
          Spacer(modifier = Modifier.width(4.dp))
        }

        // Three-Dot Menu
        Box {
          IconButton(
            onClick = { showMenu = true },
            modifier = Modifier
              .testTag("post_three_dot_${post.id}")
              .size(36.dp)
          ) {
            Icon(
              imageVector = Icons.Default.MoreVert,
              contentDescription = "Post Options",
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
          ) {
            DropdownMenuItem(
              text = { Text(if (lang == "bn") "প্রোফাইল দেখুন" else "See Profile") },
              leadingIcon = { Icon(Icons.Outlined.Person, null) },
              onClick = {
                showMenu = false
                onProfileClick()
              }
            )
            DropdownMenuItem(
              text = { Text(if (lang == "bn") "উপহার দিন" else "Gift Coins") },
              leadingIcon = { Icon(Icons.Outlined.CardGiftcard, null) },
              onClick = {
                showMenu = false
                onGiftClick()
              }
            )
            DropdownMenuItem(
              text = { Text(if (lang == "bn") "পোস্ট রিপোর্ট করুন" else "Report Post") },
              leadingIcon = { Icon(Icons.Outlined.Report, null) },
              onClick = {
                showMenu = false
                onReportClick()
              }
            )
            DropdownMenuItem(
              text = { Text(if (lang == "bn") "শেয়ার করুন" else "Share Link") },
              leadingIcon = { Icon(Icons.Outlined.Share, null) },
              onClick = {
                showMenu = false
                onShareClick()
              }
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Post Content: Text, Image, Video, or Flower Note
      if (post.isFlowerNote) {
        // Signature Pink Rounded Card as in Reference Image!
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFFFDFE6))
            .border(2.dp, Color(0xFFFFB3C6), RoundedCornerShape(20.dp))
            .padding(16.dp)
        ) {
          Row(verticalAlignment = Alignment.Top) {
            Text(
              text = "🌸",
              fontSize = 24.sp,
              modifier = Modifier.padding(end = 8.dp)
            )
            Column {
              Text(
                text = post.content,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF6B1D36),
                lineHeight = 22.sp
              )
            }
          }
        }
      } else {
        // Standard Content
        if (post.content.isNotBlank()) {
          Text(
            text = post.content,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 20.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
          )
          Spacer(modifier = Modifier.height(8.dp))
        }

        // Image Card (if image attached)
        if (post.imageUrl.isNotBlank()) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(190.dp)
              .clip(RoundedCornerShape(14.dp))
              .background(Brush.verticalGradient(listOf(Color(0xFF86A870), Color(0xFF55828B))))
              .border(3.dp, Color(0xFFB4C5B8), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(
                imageVector = Icons.Default.Landscape,
                contentDescription = "Scenic Image",
                tint = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.size(54.dp)
              )
              Text(
                text = "Sylhet Green Countryside",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
              )
            }
          }
          Spacer(modifier = Modifier.height(8.dp))
        }

        // Video Card (if video attached)
        if (post.videoUrl.isNotBlank()) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(190.dp)
              .clip(RoundedCornerShape(14.dp))
              .background(Brush.verticalGradient(listOf(Color(0xFF1E2822), Color(0xFF0F1612)))),
            contentAlignment = Alignment.Center
          ) {
            Box(
              modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.85f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Play Video",
                tint = Color(0xFF162B1F),
                modifier = Modifier.size(42.dp)
              )
            }
            Text(
              text = "01:45 • HD",
              color = Color.White.copy(alpha = 0.7f),
              fontSize = 11.sp,
              modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
            )
          }
          Spacer(modifier = Modifier.height(8.dp))
        }
      }

      Spacer(modifier = Modifier.height(4.dp))

      // Interaction Action Bar (Like, Comment, Share, Gift)
      Divider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 0.8.dp)
      Spacer(modifier = Modifier.height(4.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Like (+5 Free Coins)
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .testTag("post_like_button_${post.id}")
            .clip(RoundedCornerShape(8.dp))
            .clickable { onLikeClick() }
            .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
          Icon(
            imageVector = if (post.likedByMe) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
            contentDescription = "Like",
            tint = if (post.likedByMe) SageGreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(5.dp))
          Text(
            text = "${post.likesCount}",
            fontSize = 13.sp,
            fontWeight = if (post.likedByMe) FontWeight.Bold else FontWeight.Normal,
            color = if (post.likedByMe) SageGreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        // Comment (+20 Free Coins)
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .testTag("post_comment_button_${post.id}")
            .clip(RoundedCornerShape(8.dp))
            .clickable { onCommentClick() }
            .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
          Icon(
            imageVector = Icons.Outlined.ChatBubbleOutline,
            contentDescription = "Comment",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(5.dp))
          Text(
            text = "${post.commentsCount}",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        // Share (+20 Free Coins)
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .testTag("post_share_button_${post.id}")
            .clip(RoundedCornerShape(8.dp))
            .clickable { onShareClick() }
            .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
          Icon(
            imageVector = Icons.Outlined.Share,
            contentDescription = "Share",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(5.dp))
          Text(
            text = "${post.sharesCount}",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        // Gift Button (Flower)
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .testTag("post_gift_button_${post.id}")
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFFFF0F5))
            .clickable { onGiftClick() }
            .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
          Text(
            text = "🌸",
            fontSize = 16.sp
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = if (lang == "bn") "উপহার" else "Gift",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = AccentPink
          )
        }
      }
    }
  }
}
