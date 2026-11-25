package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainTab
import com.example.ui.theme.SageGreenPrimary

@Composable
fun MoodbookBottomNav(
  currentTab: MainTab,
  onTabSelected: (MainTab) -> Unit,
  lang: String,
  modifier: Modifier = Modifier
) {
  Surface(
    color = Color(0xFF6B8B75),
    shadowElevation = 8.dp,
    modifier = modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .navigationBarsPadding()
        .padding(vertical = 6.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      NavItem(
        label = if (lang == "bn") "হোম" else "Home",
        selected = currentTab == MainTab.HOME,
        icon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        tag = "nav_home",
        onClick = { onTabSelected(MainTab.HOME) }
      )
      NavItem(
        label = if (lang == "bn") "ইনবক্স" else "Inbox",
        selected = currentTab == MainTab.INBOX,
        icon = Icons.Filled.ChatBubble,
        unselectedIcon = Icons.Outlined.ChatBubbleOutline,
        tag = "nav_inbox",
        onClick = { onTabSelected(MainTab.INBOX) }
      )
      NavItem(
        label = if (lang == "bn") "কয়েন সেন্টার" else "Coin Center",
        selected = currentTab == MainTab.COIN_CENTER,
        icon = Icons.Filled.MonetizationOn,
        unselectedIcon = Icons.Outlined.MonetizationOn,
        tag = "nav_coins",
        onClick = { onTabSelected(MainTab.COIN_CENTER) }
      )
      NavItem(
        label = if (lang == "bn") "প্রোফাইল" else "Profile",
        selected = currentTab == MainTab.PROFILE,
        icon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle,
        tag = "nav_profile",
        onClick = { onTabSelected(MainTab.PROFILE) }
      )
      NavItem(
        label = if (lang == "bn") "মেনু" else "More",
        selected = currentTab == MainTab.SETTINGS,
        icon = Icons.Filled.MenuOpen,
        unselectedIcon = Icons.Outlined.MoreHoriz,
        tag = "nav_more",
        onClick = { onTabSelected(MainTab.SETTINGS) }
      )
    }
  }
}

@Composable
private fun NavItem(
  label: String,
  selected: Boolean,
  icon: ImageVector,
  unselectedIcon: ImageVector,
  tag: String,
  onClick: () -> Unit
) {
  val activeColor = Color(0xFF14291E)
  val inactiveColor = Color(0xFF2E4738)

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .testTag(tag)
      .clip(RoundedCornerShape(12.dp))
      .clickable { onClick() }
      .padding(horizontal = 10.dp, vertical = 4.dp)
  ) {
    Box(
      modifier = Modifier
        .clip(RoundedCornerShape(16.dp))
        .background(if (selected) Color(0xFF88AA93) else Color.Transparent)
        .padding(horizontal = 12.dp, vertical = 4.dp),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = if (selected) icon else unselectedIcon,
        contentDescription = label,
        tint = if (selected) activeColor else inactiveColor,
        modifier = Modifier.size(26.dp)
      )
    }
    Text(
      text = label,
      fontSize = 11.sp,
      fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
      color = if (selected) activeColor else inactiveColor
    )
  }
}
