package com.example.ui.screens.inbox

import androidx.compose.foundation.background
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
import com.example.data.model.Conversation
import com.example.data.model.Message
import com.example.data.model.User
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun InboxScreen(
  conversations: List<Conversation>,
  lang: String,
  onOpenConversation: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .testTag("inbox_screen"),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp)
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = if (lang == "bn") "💬 মুডবুক ইনবক্স" else "💬 Moodbook Inbox",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = if (lang == "bn") "সরাসরি বার্তা ও অডিও কল" else "Direct messaging & audio calls",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Surface(
          color = SageGreenContainer,
          shape = RoundedCornerShape(12.dp)
        ) {
          Text(
            text = "${conversations.size} Chats",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = SageGreenDark,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }
      Spacer(modifier = Modifier.height(14.dp))
    }

    // Rules banner (Mutual follow = Free, 10 replies = Audio Call + 300 Paid Coins)
    item {
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 12.dp)
      ) {
        Row(
          modifier = Modifier.padding(10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("💡", fontSize = 16.sp)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (lang == "bn") "পারস্পরিক ফলোতে ইনবক্স ফ্রি! ১০টি রিপ্লাই পূর্ণ হলে অডিও কল এবং ৩০০ পেইড কয়েন রিওয়ার্ড আনলক হয়।"
            else "Mutual follow unlocks inbox for free! 10 qualifying replies unlock Audio Calls & 300 Paid Coins reward.",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    items(conversations, key = { it.id }) { conv ->
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp)
          .clickable { onOpenConversation(conv.id) }
          .testTag("conversation_item_${conv.id}")
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Participant Avatar
          Box(
            modifier = Modifier
              .size(48.dp)
              .clip(CircleShape)
              .background(SageGreenPrimary),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = conv.participantName.take(1).uppercase(),
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 20.sp
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = conv.participantName,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
              )
              val timeStr = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(conv.lastTimestamp))
              Text(
                text = timeStr,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
              text = conv.lastMessage,
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Status badges: Replies count / Audio call ready
            Row(verticalAlignment = Alignment.CenterVertically) {
              if (conv.isAudioCallEnabled) {
                Surface(color = FreeCoinGreenLight, shape = RoundedCornerShape(6.dp)) {
                  Text(
                    text = "📞 Audio Call Ready",
                    fontSize = 9.sp,
                    color = FreeCoinGreen,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              } else {
                Surface(color = SageGreenContainer, shape = RoundedCornerShape(6.dp)) {
                  Text(
                    text = "${conv.replyCountFromRecipient}/10 Replies",
                    fontSize = 9.sp,
                    color = SageGreenDark,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
  conversation: Conversation?,
  messages: List<Message>,
  currentUser: User?,
  lang: String,
  onBack: () -> Unit,
  onSendMessage: (String) -> Unit,
  onStartAudioCall: () -> Unit,
  onUnlockInbox: (Long) -> Unit,
  modifier: Modifier = Modifier
) {
  var messageInput by remember { mutableStateOf("") }

  if (conversation == null) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      Text("Conversation not found")
    }
    return
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(SageGreenDark),
              contentAlignment = Alignment.Center
            ) {
              Text(conversation.participantName.take(1), color = Color.White, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(conversation.participantName, fontSize = 14.sp, fontWeight = FontWeight.Bold)
              Text(
                text = if (conversation.isAudioCallEnabled) "Online • Audio Call Ready" else "Active now",
                fontSize = 10.sp,
                color = Color(0xFF1E3827)
              )
            }
          }
        },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        actions = {
          IconButton(
            onClick = onStartAudioCall,
            enabled = conversation.isAudioCallEnabled,
            modifier = Modifier.testTag("audio_call_action_button")
          ) {
            Icon(
              imageVector = Icons.Default.Phone,
              contentDescription = "Audio Call",
              tint = if (conversation.isAudioCallEnabled) Color(0xFF162B1F) else Color.Gray.copy(alpha = 0.5f)
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = SageBarColor,
          titleContentColor = Color(0xFF14291E)
        )
      )
    }
  ) { padding ->
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(padding)
        .background(MaterialTheme.colorScheme.background)
    ) {
      // Unlocked Check
      if (!conversation.isUnlocked) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
          contentAlignment = Alignment.Center
        ) {
          Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(20.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text("🔒", fontSize = 36.sp)
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = if (lang == "bn") "ইনবক্স লক করা রয়েছে" else "Inbox is Locked",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
              )
              Text(
                text = if (lang == "bn") "পারস্পরিক ফলো থাকলে বিনামূল্যে আনলক হয়, অথবা ৫০০ পেইড কয়েন দিয়ে আনলক করুন।"
                else "Unlocks free upon mutual follow, or unlock using 500 Paid Coins.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Spacer(modifier = Modifier.height(16.dp))
              Button(
                onClick = { onUnlockInbox(500) },
                colors = ButtonDefaults.buttonColors(containerColor = GoldPaidCoin)
              ) {
                Text(
                  text = if (lang == "bn") "৫০০ পেইড কয়েনে আনলক করুন" else "Unlock for 500 Paid Coins",
                  color = Color.Black,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }
        return@Scaffold
      }

      // Messages Stream
      LazyColumn(
        modifier = Modifier
          .weight(1f)
          .padding(horizontal = 14.dp, vertical = 8.dp),
        reverseLayout = false
      ) {
        items(messages) { msg ->
          val isMe = msg.senderId == currentUser?.id
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp),
            horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
          ) {
            Box(
              modifier = Modifier
                .widthIn(max = 260.dp)
                .clip(
                  RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isMe) 16.dp else 4.dp,
                    bottomEnd = if (isMe) 4.dp else 16.dp
                  )
                )
                .background(if (isMe) SageGreenPrimary else MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
              Column {
                Text(
                  text = msg.text,
                  color = if (isMe) Color.White else MaterialTheme.colorScheme.onSurface,
                  fontSize = 13.sp
                )
                val timeStr = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(msg.timestamp))
                Text(
                  text = timeStr,
                  fontSize = 9.sp,
                  color = if (isMe) Color.White.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.align(Alignment.End)
                )
              }
            }
          }
        }
      }

      // Message Input Row
      Divider(color = MaterialTheme.colorScheme.surfaceVariant)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(8.dp)
          .navigationBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedTextField(
          value = messageInput,
          onValueChange = { messageInput = it },
          placeholder = { Text(if (lang == "bn") "বার্তা লিখুন..." else "Type message...") },
          modifier = Modifier
            .weight(1f)
            .testTag("chat_message_input"),
          shape = RoundedCornerShape(24.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        IconButton(
          onClick = {
            if (messageInput.isNotBlank()) {
              onSendMessage(messageInput)
              messageInput = ""
            }
          },
          modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(SageGreenPrimary)
            .testTag("chat_send_button")
        ) {
          Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
        }
      }
    }
  }
}
