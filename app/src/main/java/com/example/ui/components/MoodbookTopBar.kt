package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SageBarColor
import com.example.ui.theme.SageGreenDark

@Composable
fun MoodbookTopBar(
  unreadCount: Int,
  onMenuClick: () -> Unit,
  onPlusClick: () -> Unit,
  onNotificationClick: () -> Unit,
  onSearchClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    color = SageBarColor,
    shadowElevation = 4.dp,
    modifier = modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .padding(horizontal = 8.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Menu Icon (Leftmost three horizontal lines)
      IconButton(
        onClick = onMenuClick,
        modifier = Modifier
          .testTag("top_menu_button")
          .size(44.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Menu,
          contentDescription = "Menu Drawer",
          tint = Color(0xFF1E3326),
          modifier = Modifier.size(28.dp)
        )
      }

      // Plus (+) Icon (for Post Now / Add Media)
      IconButton(
        onClick = onPlusClick,
        modifier = Modifier
          .testTag("top_plus_button")
          .size(44.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "Create Post",
          tint = Color(0xFF162B1F),
          modifier = Modifier.size(32.dp)
        )
      }

      Spacer(modifier = Modifier.width(4.dp))

      // Moodbook Logo in Center (Lowercase bold font as in reference image)
      Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "moodbook",
          fontSize = 24.sp,
          fontWeight = FontWeight.Black,
          color = Color(0xFF14291E),
          letterSpacing = (-0.5).sp,
          modifier = Modifier.testTag("app_logo_title")
        )
        Text(
          text = " 0.2",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF2E5339)
        )
      }

      // Notification Bell with Badge
      Box(contentAlignment = Alignment.TopEnd) {
        IconButton(
          onClick = onNotificationClick,
          modifier = Modifier
            .testTag("top_notification_button")
            .size(44.dp)
        ) {
          Icon(
            imageVector = Icons.Outlined.Notifications,
            contentDescription = "Notifications",
            tint = Color(0xFF1E3326),
            modifier = Modifier.size(28.dp)
          )
        }
        if (unreadCount > 0) {
          Box(
            modifier = Modifier
              .padding(top = 6.dp, end = 6.dp)
              .size(18.dp)
              .clip(CircleShape)
              .background(Color(0xFFE53935)),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = if (unreadCount > 9) "9+" else unreadCount.toString(),
              color = Color.White,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      // Search Icon
      IconButton(
        onClick = onSearchClick,
        modifier = Modifier
          .testTag("top_search_button")
          .size(44.dp)
      ) {
        Icon(
          imageVector = Icons.Outlined.Search,
          contentDescription = "Search",
          tint = Color(0xFF1E3326),
          modifier = Modifier.size(28.dp)
        )
      }
    }
  }
}
