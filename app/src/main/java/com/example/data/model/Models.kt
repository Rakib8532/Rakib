package com.example.data.model

data class User(
  val id: String,
  val name: String,
  val username: String,
  val email: String,
  val avatarUrl: String = "",
  val bio: String = "Connecting, sharing & earning on Moodbook ✨",
  val isVerified: Boolean = false,
  val followersCount: Int = 128,
  val followingCount: Int = 45,
  val freeCoins: Long = 1000L,
  val paidCoins: Long = 100L,
  val referralCode: String = "MBK" + (1000..9999).random(),
  val referredBy: String? = null,
  val isBlocked: Boolean = false,
  val gender: String = "Not Specified",
  val birthDate: String = "2000-01-01",
  val address: String = "Dhaka, Bangladesh",
  val isAdmin: Boolean = false
)

enum class CoinType {
  FREE, PAID
}

enum class TransactionType {
  EARN_LIKE,
  EARN_COMMENT,
  EARN_SHARE,
  DAILY_CHECKIN,
  REFERRAL_BONUS,
  REWARDED_ADS,
  BOOST_POST,
  SUPER_BOOST,
  GIFT_SENT,
  GIFT_RECEIVED,
  BUY_COINS,
  WITHDRAWAL,
  RECHARGE,
  EXCHANGE
}

data class CoinTransaction(
  val id: String,
  val coinType: CoinType,
  val type: TransactionType,
  val amount: Long, // positive for earn/credit, negative for spend/debit
  val description: String,
  val timestamp: Long = System.currentTimeMillis(),
  val status: String = "Completed",
  val referenceId: String = "TXN-${System.currentTimeMillis() % 1000000}"
)

data class Post(
  val id: String,
  val authorId: String,
  val authorName: String,
  val authorUsername: String,
  val authorAvatar: String = "",
  val content: String,
  val imageUrl: String = "",
  val videoUrl: String = "",
  val isFlowerNote: Boolean = false,
  val audience: String = "Public",
  val isBoosted: Boolean = false,
  val isSuperBoosted: Boolean = false,
  val likesCount: Int = 0,
  val commentsCount: Int = 0,
  val sharesCount: Int = 0,
  val giftsCount: Int = 0,
  val viewsCount: Int = 42,
  val createdAt: Long = System.currentTimeMillis(),
  val isArchived: Boolean = false,
  val likedByMe: Boolean = false
)

data class Comment(
  val id: String,
  val postId: String,
  val authorId: String,
  val authorName: String,
  val authorAvatar: String = "",
  val text: String,
  val timestamp: Long = System.currentTimeMillis()
)

data class Message(
  val id: String,
  val conversationId: String,
  val senderId: String,
  val senderName: String,
  val text: String,
  val imageUrl: String = "",
  val isVoiceNote: Boolean = false,
  val timestamp: Long = System.currentTimeMillis(),
  val isRead: Boolean = true
)

data class Conversation(
  val id: String,
  val participantId: String,
  val participantName: String,
  val participantAvatar: String = "",
  val participantUsername: String,
  val lastMessage: String,
  val lastTimestamp: Long = System.currentTimeMillis(),
  val unreadCount: Int = 0,
  val isUnlocked: Boolean = true,
  val isMutualFollow: Boolean = false,
  val replyCountFromRecipient: Int = 0,
  val isAudioCallEnabled: Boolean = false
)

enum class NotificationType {
  LIKE, COMMENT, SHARE, FOLLOW, COIN_REWARD, GIFT, BOOST, ANNOUNCEMENT, SYSTEM
}

data class NotificationItem(
  val id: String,
  val title: String,
  val message: String,
  val type: NotificationType,
  val timestamp: Long = System.currentTimeMillis(),
  val isRead: Boolean = false,
  val targetPostId: String? = null,
  val targetUserId: String? = null
)

data class GiftItem(
  val id: String,
  val name: String,
  val nameBn: String,
  val emoji: String,
  val cost: Long,
  val coinType: CoinType,
  val description: String
)

data class WithdrawalRequest(
  val id: String,
  val userId: String,
  val userName: String,
  val mobileWallet: String,
  val method: String, // bKash, Nagad, Rocket
  val amountCoins: Long,
  val amountBdt: Long,
  val status: String = "Pending", // Pending, Approved, Rejected
  val createdAt: Long = System.currentTimeMillis()
)

data class MobileRecharge(
  val id: String,
  val userId: String,
  val operator: String,
  val mobileNumber: String,
  val amountBdt: Long,
  val status: String = "Success",
  val createdAt: Long = System.currentTimeMillis()
)

data class FeedbackReport(
  val id: String,
  val userId: String,
  val userName: String,
  val title: String,
  val description: String,
  val category: String,
  val status: String = "Open", // Open, In Review, Resolved
  val createdAt: Long = System.currentTimeMillis(),
  val adminReply: String? = null
)
