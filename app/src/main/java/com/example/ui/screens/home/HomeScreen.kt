package com.example.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
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
import com.example.data.model.Comment
import com.example.data.model.Post
import com.example.ui.components.PostCard
import com.example.ui.theme.SageBarColor
import com.example.ui.theme.SageFeedBackground
import com.example.ui.theme.SageGreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  posts: List<Post>,
  commentsMap: Map<String, List<Comment>>,
  lang: String,
  onLikeClick: (String) -> Unit,
  onCommentSubmit: (String, String) -> Unit,
  onShareClick: (String) -> Unit,
  onGiftClick: (String) -> Unit,
  onProfileClick: (String) -> Unit,
  onReportClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedFeedTab by remember { mutableStateOf(0) }
  var commentSheetPostId by remember { mutableStateOf<String?>(null) }
  var commentInputText by remember { mutableStateOf("") }

  val tabs = if (lang == "bn") {
    listOf("হোম ফিড", "ট্রেন্ডিং", "সর্বশেষ", "ফলোয়িং", "সুপার বুস্ট")
  } else {
    listOf("Public Feed", "Trending", "Latest", "Following", "Super Boost")
  }

  // Filter posts based on selected tab
  val displayedPosts = remember(posts, selectedFeedTab) {
    when (selectedFeedTab) {
      1 -> posts.sortedByDescending { it.likesCount + it.commentsCount }
      2 -> posts.sortedByDescending { it.createdAt }
      3 -> posts.filter { it.isBoosted || it.authorId.startsWith("u") }
      4 -> posts.filter { it.isSuperBoosted }
      else -> posts.filter { !it.isArchived }
    }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(SageFeedBackground) // Sage green background as in user's reference image!
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .testTag("home_feed_list"),
      contentPadding = PaddingValues(bottom = 90.dp)
    ) {
      // Feed Filter Tabs Header
      item {
        LazyRow(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          items(tabs.size) { index ->
            val isSelected = selectedFeedTab == index
            Surface(
              shape = RoundedCornerShape(20.dp),
              color = if (isSelected) Color(0xFF1E3827) else Color(0xFF6B8B75),
              modifier = Modifier
                .clickable { selectedFeedTab = index }
                .testTag("feed_tab_$index")
            ) {
              Text(
                text = tabs[index],
                color = if (isSelected) Color.White else Color(0xFF14291E),
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
              )
            }
          }
        }
      }

      // Posts Stream
      items(displayedPosts, key = { it.id }) { post ->
        PostCard(
          post = post,
          lang = lang,
          onLikeClick = { onLikeClick(post.id) },
          onCommentClick = { commentSheetPostId = post.id },
          onShareClick = { onShareClick(post.id) },
          onGiftClick = { onGiftClick(post.id) },
          onProfileClick = { onProfileClick(post.authorId) },
          onReportClick = { onReportClick(post.id) }
        )
      }

      if (displayedPosts.isEmpty()) {
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(40.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = if (lang == "bn") "কোন পোস্ট খুঁজে পাওয়া যায়নি" else "No posts found in this tab",
              color = Color.White,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }
    }

    // Comment Modal BottomSheet
    commentSheetPostId?.let { postId ->
      val postComments = commentsMap[postId] ?: emptyList()

      ModalBottomSheet(
        onDismissRequest = { commentSheetPostId = null },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .navigationBarsPadding()
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = if (lang == "bn") "মন্তব্য (${postComments.size})" else "Comments (${postComments.size})",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = if (lang == "bn") "প্রথম মন্তব্যে +২০ ফ্রি কয়েন!" else "+20 Free Coins for 1st comment!",
              fontSize = 11.sp,
              color = SageGreenPrimary,
              fontWeight = FontWeight.Bold
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          LazyColumn(
            modifier = Modifier
              .fillMaxWidth()
              .heightIn(max = 240.dp)
          ) {
            items(postComments) { comment ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 6.dp),
                verticalAlignment = Alignment.Top
              ) {
                Box(
                  modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(SageGreenPrimary),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = comment.authorName.take(1).uppercase(),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                  modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(8.dp)
                ) {
                  Text(text = comment.authorName, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                  Text(text = comment.text, fontSize = 13.sp)
                }
              }
            }
            if (postComments.isEmpty()) {
              item {
                Text(
                  text = if (lang == "bn") "এখনও কোনো মন্তব্য নেই। প্রথম মন্তব্য করুন!" else "No comments yet. Be the first to comment!",
                  fontSize = 12.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.padding(vertical = 12.dp)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Comment Input Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            OutlinedTextField(
              value = commentInputText,
              onValueChange = { commentInputText = it },
              placeholder = { Text(if (lang == "bn") "একটি মন্তব্য লিখুন..." else "Write a comment...") },
              modifier = Modifier
                .weight(1f)
                .testTag("comment_input_field"),
              shape = RoundedCornerShape(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
              onClick = {
                if (commentInputText.isNotBlank()) {
                  onCommentSubmit(postId, commentInputText)
                  commentInputText = ""
                }
              },
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(SageGreenPrimary)
                .testTag("submit_comment_button")
            ) {
              Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
            }
          }
        }
      }
    }
  }
}
