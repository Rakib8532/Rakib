package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class MoodbookRepository {

  // Current logged in user
  private val _currentUser = MutableStateFlow<User?>(
    User(
      id = "user-admin",
      name = "Rakib Islam",
      username = "rakib_islam",
      email = "islamrakib8522@gmail.com",
      avatarUrl = "",
      bio = "Tech enthusiast, creator & founder at Moodbook 🚀 #Dhaka",
      isVerified = true,
      followersCount = 842,
      followingCount = 194,
      freeCoins = 2450L,
      paidCoins = 350L,
      referralCode = "MBK8522",
      gender = "Male",
      address = "Mirpur, Dhaka, Bangladesh",
      isAdmin = true
    )
  )
  val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

  // App language state: "en" or "bn"
  private val _currentLanguage = MutableStateFlow("bn")
  val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

  // Dark mode override: null = follow system, true/false = manual
  private val _isDarkMode = MutableStateFlow(false)
  val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

  // Posts Feed
  private val _posts = MutableStateFlow<List<Post>>(emptyList())
  val posts: StateFlow<List<Post>> = _posts.asStateFlow()

  // Post comments map: PostId -> List<Comment>
  private val _comments = MutableStateFlow<Map<String, List<Comment>>>(emptyMap())
  val comments: StateFlow<Map<String, List<Comment>>> = _comments.asStateFlow()

  // Transactions ledger
  private val _transactions = MutableStateFlow<List<CoinTransaction>>(emptyList())
  val transactions: StateFlow<List<CoinTransaction>> = _transactions.asStateFlow()

  // Conversations
  private val _conversations = MutableStateFlow<List<Conversation>>(emptyList())
  val conversations: StateFlow<List<Conversation>> = _conversations.asStateFlow()

  // Messages: ConversationId -> List<Message>
  private val _messages = MutableStateFlow<Map<String, List<Message>>>(emptyMap())
  val messages: StateFlow<Map<String, List<Message>>> = _messages.asStateFlow()

  // Notifications
  private val _notifications = MutableStateFlow<List<NotificationItem>>(emptyList())
  val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

  // Withdrawal Requests
  private val _withdrawals = MutableStateFlow<List<WithdrawalRequest>>(emptyList())
  val withdrawals: StateFlow<List<WithdrawalRequest>> = _withdrawals.asStateFlow()

  // Mobile Recharges
  private val _mobileRecharges = MutableStateFlow<List<MobileRecharge>>(emptyList())
  val mobileRecharges: StateFlow<List<MobileRecharge>> = _mobileRecharges.asStateFlow()

  // Feedback Reports
  private val _feedbackReports = MutableStateFlow<List<FeedbackReport>>(emptyList())
  val feedbackReports: StateFlow<List<FeedbackReport>> = _feedbackReports.asStateFlow()

  // Rewarded ad progress for "Earn Paid Coins": (0..10)
  private val _paidAdProgress = MutableStateFlow(3)
  val paidAdProgress: StateFlow<Int> = _paidAdProgress.asStateFlow()

  // Free coin ad packages claimed today
  private val _freeAdClaims = MutableStateFlow(setOf<Int>())
  val freeAdClaims: StateFlow<Set<Int>> = _freeAdClaims.asStateFlow()

  // Daily Checkin status
  private val _lastCheckinTime = MutableStateFlow(0L)
  val lastCheckinTime: StateFlow<Long> = _lastCheckinTime.asStateFlow()

  // All Users (for admin & search)
  private val _allUsers = MutableStateFlow<List<User>>(emptyList())
  val allUsers: StateFlow<List<User>> = _allUsers.asStateFlow()

  // Gifts catalog
  val giftCatalog = listOf(
    GiftItem("g1", "Rose", "গোলাপ", "🌹", 50, CoinType.FREE, "Fresh crimson blossom"),
    GiftItem("g2", "Tulip", "টিউলিপ", "🌷", 100, CoinType.FREE, "Graceful garden tulip"),
    GiftItem("g3", "Sunflower", "সূর্যমুখী", "🌻", 200, CoinType.FREE, "Vibrant bright sunflower"),
    GiftItem("g4", "Golden Lotus", "স্বর্ণপদ্ম", "🪷", 20, CoinType.PAID, "Lustrous animated golden lotus"),
    GiftItem("g5", "Royal Orchid", "রাজকীয় অর্কিড", "🌸", 50, CoinType.PAID, "Exquisite royal orchid cluster"),
    GiftItem("g6", "Diamond Bouquet", "হীরা তোড়া", "💐", 100, CoinType.PAID, "Dazzling animated diamond bouquet")
  )

  init {
    initializeSeedData()
  }

  private fun initializeSeedData() {
    val adminUser = _currentUser.value!!

    val u1 = User(
      id = "u1",
      name = "Amina Rahman",
      username = "amina_r",
      email = "amina@example.com",
      bio = "Photographer & travel lover 📸 Sylhet | Dhaka",
      isVerified = true,
      followersCount = 420,
      followingCount = 110,
      gender = "Female",
      address = "Sylhet"
    )

    val u2 = User(
      id = "u2",
      name = "Tanvir Ahmed",
      username = "tanvir_pro",
      email = "tanvir@example.com",
      bio = "Video Editor & Motion Designer 🎬",
      isVerified = false,
      followersCount = 890,
      followingCount = 340,
      gender = "Male",
      address = "Dhaka"
    )

    val u3 = User(
      id = "u3",
      name = "Sadia Afrin",
      username = "sadia_afrin",
      email = "sadia@example.com",
      bio = "Student & active Moodbook member 🌸",
      isVerified = false,
      followersCount = 76,
      followingCount = 92,
      gender = "Female",
      address = "Chittagong"
    )

    val u4 = User(
      id = "u4",
      name = "Kamrul Hasan",
      username = "kamrul_h",
      email = "kamrul@example.com",
      bio = "Tech blogger & digital marketing enthusiast",
      isVerified = true,
      followersCount = 1240,
      followingCount = 530,
      gender = "Male",
      address = "Rajshahi"
    )

    _allUsers.value = listOf(adminUser, u1, u2, u3, u4)

    // Seed Posts matching the user's reference image
    val seedPosts = listOf(
      Post(
        id = "p1",
        authorId = u1.id,
        authorName = u1.name,
        authorUsername = u1.username,
        content = "Enjoying the peaceful hills and green tea gardens of Jaflong, Sylhet! 🌿☀️\n#NatureBangladesh #MoodbookMoments",
        imageUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=800&q=80",
        likesCount = 28,
        commentsCount = 6,
        sharesCount = 3,
        giftsCount = 5,
        viewsCount = 142,
        isBoosted = true,
        createdAt = System.currentTimeMillis() - 1000 * 60 * 45
      ),
      Post(
        id = "p2",
        authorId = u2.id,
        authorName = u2.name,
        authorUsername = u2.username,
        content = "Midnight video color-grading session in our studio! What do you guys think of this cinema look? 🎬✨",
        videoUrl = "https://example.com/video1.mp4",
        likesCount = 54,
        commentsCount = 12,
        sharesCount = 8,
        giftsCount = 2,
        viewsCount = 480,
        isSuperBoosted = true,
        createdAt = System.currentTimeMillis() - 1000 * 60 * 120
      ),
      Post(
        id = "p3",
        authorId = u3.id,
        authorName = u3.name,
        authorUsername = u3.username,
        content = "hello guys I am new here please help Eran coin 🪙🌸",
        isFlowerNote = true,
        likesCount = 89,
        commentsCount = 34,
        sharesCount = 15,
        giftsCount = 19,
        viewsCount = 650,
        createdAt = System.currentTimeMillis() - 1000 * 60 * 180
      ),
      Post(
        id = "p4",
        authorId = adminUser.id,
        authorName = adminUser.name,
        authorUsername = adminUser.username,
        content = "Welcome to Moodbook 0.2! Connect with friends, share your creative journey, and earn digital coins daily. 🇧🇩🚀\n#ConnectShareEarnEnjoy",
        likesCount = 142,
        commentsCount = 45,
        sharesCount = 31,
        giftsCount = 28,
        viewsCount = 1890,
        isSuperBoosted = true,
        createdAt = System.currentTimeMillis() - 1000 * 60 * 360
      )
    )
    _posts.value = seedPosts

    _comments.value = mapOf(
      "p3" to listOf(
        Comment(
          id = "c1",
          postId = "p3",
          authorId = adminUser.id,
          authorName = adminUser.name,
          text = "Welcome to Moodbook Sadia! Complete your daily check-in and share posts to earn Free Coins! 🌟"
        ),
        Comment(
          id = "c2",
          postId = "p3",
          authorId = u1.id,
          authorName = u1.name,
          text = "Sent you a rose gift 🌹 Enjoy your stay!"
        )
      )
    )

    // Seed conversations
    val c1 = Conversation(
      id = "conv1",
      participantId = u1.id,
      participantName = u1.name,
      participantUsername = u1.username,
      lastMessage = "Thanks for the gift! Have you tried the Coin Center today?",
      lastTimestamp = System.currentTimeMillis() - 1000 * 60 * 15,
      unreadCount = 1,
      isUnlocked = true,
      isMutualFollow = true,
      replyCountFromRecipient = 12,
      isAudioCallEnabled = true
    )
    val c2 = Conversation(
      id = "conv2",
      participantId = u3.id,
      participantName = u3.name,
      participantUsername = u3.username,
      lastMessage = "Assalamu alaikum brother, how can I earn more Paid Coins?",
      lastTimestamp = System.currentTimeMillis() - 1000 * 60 * 90,
      unreadCount = 0,
      isUnlocked = true,
      isMutualFollow = false,
      replyCountFromRecipient = 4,
      isAudioCallEnabled = false
    )
    _conversations.value = listOf(c1, c2)

    _messages.value = mapOf(
      "conv1" to listOf(
        Message(
          id = "m1",
          conversationId = "conv1",
          senderId = u1.id,
          senderName = u1.name,
          text = "Hello Rakib! Loved your new boosted announcement post."
        ),
        Message(
          id = "m2",
          conversationId = "conv1",
          senderId = adminUser.id,
          senderName = adminUser.name,
          text = "Thanks Amina! Feel free to check out the new rewarded ads section too."
        ),
        Message(
          id = "m3",
          conversationId = "conv1",
          senderId = u1.id,
          senderName = u1.name,
          text = "Thanks for the gift! Have you tried the Coin Center today?"
        )
      ),
      "conv2" to listOf(
        Message(
          id = "m4",
          conversationId = "conv2",
          senderId = u3.id,
          senderName = u3.name,
          text = "Assalamu alaikum brother, how can I earn more Paid Coins?"
        )
      )
    )

    // Seed transactions
    _transactions.value = listOf(
      CoinTransaction(
        id = "t1",
        coinType = CoinType.FREE,
        type = TransactionType.DAILY_CHECKIN,
        amount = 50,
        description = "Daily Check-in Bonus"
      ),
      CoinTransaction(
        id = "t2",
        coinType = CoinType.PAID,
        type = TransactionType.REWARDED_ADS,
        amount = 20,
        description = "Completed 10 Rewarded Ads Task"
      ),
      CoinTransaction(
        id = "t3",
        coinType = CoinType.FREE,
        type = TransactionType.REFERRAL_BONUS,
        amount = 500,
        description = "Referral Reward: Friend Joined"
      ),
      CoinTransaction(
        id = "t4",
        coinType = CoinType.PAID,
        type = TransactionType.REFERRAL_BONUS,
        amount = 10,
        description = "Referral Reward: 10 Paid Coins"
      )
    )

    // Seed notifications
    _notifications.value = listOf(
      NotificationItem(
        id = "n1",
        title = "Someone liked your post! 👍",
        message = "Amina Rahman liked your post. You earned +5 Free Coins!",
        type = NotificationType.LIKE,
        timestamp = System.currentTimeMillis() - 1000 * 60 * 30
      ),
      NotificationItem(
        id = "n2",
        title = "Gift Received! 🪷",
        message = "Tanvir Ahmed sent you a Golden Lotus gift!",
        type = NotificationType.GIFT,
        timestamp = System.currentTimeMillis() - 1000 * 60 * 75
      ),
      NotificationItem(
        id = "n3",
        title = "Super Boost Active 🚀",
        message = "Your post has been Super Boosted! Reaching 5,000+ eligible users.",
        type = NotificationType.BOOST,
        timestamp = System.currentTimeMillis() - 1000 * 60 * 180
      )
    )

    // Seed withdrawal requests
    _withdrawals.value = listOf(
      WithdrawalRequest(
        id = "w1",
        userId = "u2",
        userName = "Tanvir Ahmed",
        mobileWallet = "01711223344",
        method = "bKash",
        amountCoins = 6000,
        amountBdt = 1200,
        status = "Approved",
        createdAt = System.currentTimeMillis() - 1000 * 60 * 60 * 24
      )
    )

    // Seed feedback reports
    _feedbackReports.value = listOf(
      FeedbackReport(
        id = "fb1",
        userId = "u3",
        userName = "Sadia Afrin",
        title = "Coin Exchange question",
        description = "Exchange was super fast! Would love to see more flower gift varieties.",
        category = "Coin Problem",
        status = "Resolved",
        adminReply = "Thank you Sadia! We just added new flower gifts."
      )
    )
  }

  // Language & Theme
  fun setLanguage(lang: String) {
    _currentLanguage.value = lang
  }

  fun toggleDarkMode() {
    _isDarkMode.value = !_isDarkMode.value
  }

  // Authentication
  fun login(email: String, pass: String): Boolean {
    val existing = _allUsers.value.find { it.email.equals(email, ignoreCase = true) }
    if (existing != null) {
      _currentUser.value = existing
      return true
    }
    // Create session for user
    val newUser = User(
      id = "user-" + UUID.randomUUID().toString().take(6),
      name = email.substringBefore("@").replace(".", " ").capitalizeWords(),
      username = email.substringBefore("@").lowercase().replace(".", "_"),
      email = email,
      isVerified = true,
      freeCoins = 500,
      paidCoins = 10,
      isAdmin = email.equals("islamrakib8522@gmail.com", ignoreCase = true)
    )
    _allUsers.value = _allUsers.value + newUser
    _currentUser.value = newUser
    return true
  }

  fun register(
    name: String,
    email: String,
    birthDate: String,
    gender: String,
    referralCode: String?
  ): Boolean {
    val newId = "user-" + UUID.randomUUID().toString().take(6)
    val hasValidReferral = !referralCode.isNullOrBlank()
    val initialFree = if (hasValidReferral) 1000L else 500L
    val initialPaid = if (hasValidReferral) 20L else 10L

    val newUser = User(
      id = newId,
      name = name,
      username = email.substringBefore("@").lowercase().replace(".", "_"),
      email = email,
      birthDate = birthDate,
      gender = gender,
      freeCoins = initialFree,
      paidCoins = initialPaid,
      referredBy = referralCode?.takeIf { it.isNotBlank() },
      isVerified = false,
      isAdmin = email.equals("islamrakib8522@gmail.com", ignoreCase = true)
    )

    _allUsers.value = _allUsers.value + newUser
    _currentUser.value = newUser

    if (hasValidReferral) {
      addTransaction(
        CoinTransaction(
          id = UUID.randomUUID().toString(),
          coinType = CoinType.FREE,
          type = TransactionType.REFERRAL_BONUS,
          amount = 500,
          description = "Welcome bonus from referral code: $referralCode"
        )
      )
      addTransaction(
        CoinTransaction(
          id = UUID.randomUUID().toString(),
          coinType = CoinType.PAID,
          type = TransactionType.REFERRAL_BONUS,
          amount = 10,
          description = "Welcome bonus: 10 Paid Coins"
        )
      )
    }
    return true
  }

  fun logout() {
    _currentUser.value = null
  }

  fun verifyCurrentUser() {
    _currentUser.value = _currentUser.value?.copy(isVerified = true)
    _notifications.value = listOf(
      NotificationItem(
        id = UUID.randomUUID().toString(),
        title = "Account Verified! ✅",
        message = "Your Moodbook account is now fully verified. Financial features unlocked!",
        type = NotificationType.SYSTEM
      )
    ) + _notifications.value
  }

  fun updateProfile(name: String, bio: String, address: String) {
    _currentUser.value = _currentUser.value?.copy(
      name = name,
      bio = bio,
      address = address
    )
  }

  // Post Actions & Rewards
  fun createPost(
    content: String,
    imageUrl: String = "",
    videoUrl: String = "",
    isFlowerNote: Boolean = false,
    boostType: String // "FREE", "BOOST", "SUPER"
  ): Boolean {
    val user = _currentUser.value ?: return false

    var boostCost = 0L
    var isBoosted = false
    var isSuper = false

    when (boostType) {
      "BOOST" -> {
        boostCost = 100L
        if (user.freeCoins < boostCost) return false
        isBoosted = true
      }
      "SUPER" -> {
        boostCost = 500L
        if (user.freeCoins < boostCost) return false
        isSuper = true
      }
    }

    if (boostCost > 0) {
      _currentUser.value = user.copy(freeCoins = user.freeCoins - boostCost)
      addTransaction(
        CoinTransaction(
          id = UUID.randomUUID().toString(),
          coinType = CoinType.FREE,
          type = if (isSuper) TransactionType.SUPER_BOOST else TransactionType.BOOST_POST,
          amount = -boostCost,
          description = if (isSuper) "Super Boost Post (Priority distribution)" else "Boost Post (Public Feed)"
        )
      )
    }

    val newPost = Post(
      id = "p-" + UUID.randomUUID().toString().take(8),
      authorId = user.id,
      authorName = user.name,
      authorUsername = user.username,
      content = content,
      imageUrl = imageUrl,
      videoUrl = videoUrl,
      isFlowerNote = isFlowerNote,
      isBoosted = isBoosted,
      isSuperBoosted = isSuper,
      createdAt = System.currentTimeMillis()
    )

    _posts.value = listOf(newPost) + _posts.value

    if (isSuper) {
      _notifications.value = listOf(
        NotificationItem(
          id = UUID.randomUUID().toString(),
          title = "Super Boost Active! 🚀",
          message = "Your post has priority reach across the whole network.",
          type = NotificationType.BOOST
        )
      ) + _notifications.value
    }

    return true
  }

  fun likePost(postId: String) {
    val post = _posts.value.find { it.id == postId } ?: return
    val user = _currentUser.value ?: return

    val newLikedState = !post.likedByMe
    val diff = if (newLikedState) 1 else -1

    _posts.value = _posts.value.map {
      if (it.id == postId) {
        it.copy(
          likedByMe = newLikedState,
          likesCount = (it.likesCount + diff).coerceAtLeast(0)
        )
      } else it
    }

    // Award +5 Free Coins if liking (and not self-post)
    if (newLikedState && post.authorId != user.id) {
      _currentUser.value = user.copy(freeCoins = user.freeCoins + 5)
      addTransaction(
        CoinTransaction(
          id = UUID.randomUUID().toString(),
          coinType = CoinType.FREE,
          type = TransactionType.EARN_LIKE,
          amount = 5,
          description = "Earned for liking @${post.authorUsername}'s post"
        )
      )
    }
  }

  fun addComment(postId: String, text: String) {
    val user = _currentUser.value ?: return
    val newComment = Comment(
      id = UUID.randomUUID().toString(),
      postId = postId,
      authorId = user.id,
      authorName = user.name,
      text = text
    )

    val currentList = _comments.value[postId] ?: emptyList()
    val isFirstCommentFromUser = currentList.none { it.authorId == user.id }
    _comments.value = _comments.value + (postId to (currentList + newComment))

    _posts.value = _posts.value.map {
      if (it.id == postId) it.copy(commentsCount = it.commentsCount + 1) else it
    }

    // Rule: User earns +20 Free Coins for unique comment on post
    if (isFirstCommentFromUser) {
      _currentUser.value = user.copy(freeCoins = user.freeCoins + 20)
      addTransaction(
        CoinTransaction(
          id = UUID.randomUUID().toString(),
          coinType = CoinType.FREE,
          type = TransactionType.EARN_COMMENT,
          amount = 20,
          description = "Earned for commenting on post $postId"
        )
      )
    }
  }

  fun sharePost(postId: String) {
    val user = _currentUser.value ?: return
    _posts.value = _posts.value.map {
      if (it.id == postId) it.copy(sharesCount = it.sharesCount + 1) else it
    }

    // Rule: +20 Free Coins for sharing
    _currentUser.value = user.copy(freeCoins = user.freeCoins + 20)
    addTransaction(
      CoinTransaction(
        id = UUID.randomUUID().toString(),
        coinType = CoinType.FREE,
        type = TransactionType.EARN_SHARE,
        amount = 20,
        description = "Earned for sharing post to community"
      )
    )
  }

  fun sendGift(postId: String, gift: GiftItem): Boolean {
    val user = _currentUser.value ?: return false
    val post = _posts.value.find { it.id == postId } ?: return false

    if (gift.coinType == CoinType.FREE) {
      if (user.freeCoins < gift.cost) return false
      _currentUser.value = user.copy(freeCoins = user.freeCoins - gift.cost)
    } else {
      if (user.paidCoins < gift.cost) return false
      _currentUser.value = user.copy(paidCoins = user.paidCoins - gift.cost)
    }

    _posts.value = _posts.value.map {
      if (it.id == postId) it.copy(giftsCount = it.giftsCount + 1) else it
    }

    addTransaction(
      CoinTransaction(
        id = UUID.randomUUID().toString(),
        coinType = gift.coinType,
        type = TransactionType.GIFT_SENT,
        amount = -gift.cost,
        description = "Sent ${gift.name} gift ${gift.emoji} to @${post.authorUsername}"
      )
    )

    _notifications.value = listOf(
      NotificationItem(
        id = UUID.randomUUID().toString(),
        title = "Gift Sent! ${gift.emoji}",
        message = "You sent a ${gift.name} (${gift.cost} ${gift.coinType.name} Coins) to @${post.authorUsername}",
        type = NotificationType.GIFT
      )
    ) + _notifications.value

    return true
  }

  // Daily Check-in (+50 Free Coins)
  fun performDailyCheckin(): Boolean {
    val user = _currentUser.value ?: return false
    val now = System.currentTimeMillis()
    val elapsed = now - _lastCheckinTime.value
    if (elapsed < 24 * 60 * 60 * 1000 && _lastCheckinTime.value != 0L) {
      return false
    }

    _lastCheckinTime.value = now
    _currentUser.value = user.copy(freeCoins = user.freeCoins + 50)

    addTransaction(
      CoinTransaction(
        id = UUID.randomUUID().toString(),
        coinType = CoinType.FREE,
        type = TransactionType.DAILY_CHECKIN,
        amount = 50,
        description = "Daily 24-hour Check-in Reward"
      )
    )
    return true
  }

  // Rewarded Ads for Paid Coins: 10 completed = 20 Paid Coins
  fun completeRewardedAd(): Boolean {
    val current = _paidAdProgress.value + 1
    if (current >= 10) {
      _paidAdProgress.value = 0
      val user = _currentUser.value ?: return true
      _currentUser.value = user.copy(paidCoins = user.paidCoins + 20)
      addTransaction(
        CoinTransaction(
          id = UUID.randomUUID().toString(),
          coinType = CoinType.PAID,
          type = TransactionType.REWARDED_ADS,
          amount = 20,
          description = "Watched 10 Rewarded Ads Task Completion"
        )
      )
      _notifications.value = listOf(
        NotificationItem(
          id = UUID.randomUUID().toString(),
          title = "20 Paid Coins Earned! 💰",
          message = "Congratulations! You completed 10 rewarded ads task.",
          type = NotificationType.COIN_REWARD
        )
      ) + _notifications.value
    } else {
      _paidAdProgress.value = current
    }
    return true
  }

  // Free Coin Rewarded Ad packages
  fun claimFreeAdPackage(packageIndex: Int): Boolean {
    // 1 Ad = 20, 3 Ads = 100, 5 Ads = 200, 10 Ads = 500
    if (_freeAdClaims.value.contains(packageIndex)) return false
    val user = _currentUser.value ?: return false

    val reward = when (packageIndex) {
      1 -> 20L
      3 -> 100L
      5 -> 200L
      10 -> 500L
      else -> return false
    }

    _freeAdClaims.value = _freeAdClaims.value + packageIndex
    _currentUser.value = user.copy(freeCoins = user.freeCoins + reward)

    addTransaction(
      CoinTransaction(
        id = UUID.randomUUID().toString(),
        coinType = CoinType.FREE,
        type = TransactionType.REWARDED_ADS,
        amount = reward,
        description = "Claimed $packageIndex Ads Free Coin Package"
      )
    )
    return true
  }

  // Coin Exchange: 10 Paid Coins = 100 Free Coins
  fun exchangePaidForFree(paidCoinsAmount: Long): Boolean {
    val user = _currentUser.value ?: return false
    if (paidCoinsAmount <= 0 || user.paidCoins < paidCoinsAmount) return false
    if (paidCoinsAmount % 10 != 0L) return false

    val freeCoinsEarned = (paidCoinsAmount / 10) * 100

    _currentUser.value = user.copy(
      paidCoins = user.paidCoins - paidCoinsAmount,
      freeCoins = user.freeCoins + freeCoinsEarned
    )

    addTransaction(
      CoinTransaction(
        id = UUID.randomUUID().toString(),
        coinType = CoinType.PAID,
        type = TransactionType.EXCHANGE,
        amount = -paidCoinsAmount,
        description = "Exchanged $paidCoinsAmount Paid Coins -> $freeCoinsEarned Free Coins"
      )
    )
    addTransaction(
      CoinTransaction(
        id = UUID.randomUUID().toString(),
        coinType = CoinType.FREE,
        type = TransactionType.EXCHANGE,
        amount = freeCoinsEarned,
        description = "Received from Paid Coins Exchange"
      )
    )
    return true
  }

  // Deposit / Buy Paid Coins: ৳1,000 = 5,000 Paid Coins
  fun depositBuyCoins(bdtAmount: Long, method: String, walletNumber: String, trxId: String): Boolean {
    val user = _currentUser.value ?: return false
    if (!user.isVerified) return false

    val coinsToCredit = (bdtAmount * 5) // ৳1,000 -> 5,000 coins
    _currentUser.value = user.copy(paidCoins = user.paidCoins + coinsToCredit)

    addTransaction(
      CoinTransaction(
        id = UUID.randomUUID().toString(),
        coinType = CoinType.PAID,
        type = TransactionType.BUY_COINS,
        amount = coinsToCredit,
        description = "Purchased $coinsToCredit Paid Coins via $method (৳$bdtAmount). TrxID: $trxId"
      )
    )

    _notifications.value = listOf(
      NotificationItem(
        id = UUID.randomUUID().toString(),
        title = "Coins Purchased Successfully! ৳$bdtAmount",
        message = "Added $coinsToCredit Paid Coins to your wallet via $method.",
        type = NotificationType.COIN_REWARD
      )
    ) + _notifications.value

    return true
  }

  // Withdrawal: 6,000 Paid Coins = ৳1,200
  fun requestWithdrawal(method: String, walletNumber: String, coinsAmount: Long): Boolean {
    val user = _currentUser.value ?: return false
    if (!user.isVerified) return false
    if (coinsAmount < 6000L || user.paidCoins < coinsAmount) return false

    val bdtAmount = (coinsAmount / 6000L) * 1200L

    _currentUser.value = user.copy(paidCoins = user.paidCoins - coinsAmount)

    val req = WithdrawalRequest(
      id = "w-" + UUID.randomUUID().toString().take(6),
      userId = user.id,
      userName = user.name,
      mobileWallet = walletNumber,
      method = method,
      amountCoins = coinsAmount,
      amountBdt = bdtAmount,
      status = "Pending"
    )

    _withdrawals.value = listOf(req) + _withdrawals.value

    addTransaction(
      CoinTransaction(
        id = UUID.randomUUID().toString(),
        coinType = CoinType.PAID,
        type = TransactionType.WITHDRAWAL,
        amount = -coinsAmount,
        description = "Withdrawal request of ৳$bdtAmount to $method ($walletNumber)"
      )
    )
    return true
  }

  // Mobile Recharge: ৳20 to ৳100 (using Free or Paid coins, e.g. 100 Paid Coins = ৳20)
  fun requestMobileRecharge(operator: String, mobileNumber: String, amountBdt: Long): Boolean {
    val user = _currentUser.value ?: return false
    if (!user.isVerified) return false
    if (amountBdt < 20 || amountBdt >= 100) return false

    // Cost: ৳20 = 100 Paid Coins
    val costCoins = amountBdt * 5
    if (user.paidCoins < costCoins) return false

    _currentUser.value = user.copy(paidCoins = user.paidCoins - costCoins)

    val recharge = MobileRecharge(
      id = "rec-" + UUID.randomUUID().toString().take(6),
      userId = user.id,
      operator = operator,
      mobileNumber = mobileNumber,
      amountBdt = amountBdt,
      status = "Success"
    )

    _mobileRecharges.value = listOf(recharge) + _mobileRecharges.value

    addTransaction(
      CoinTransaction(
        id = UUID.randomUUID().toString(),
        coinType = CoinType.PAID,
        type = TransactionType.RECHARGE,
        amount = -costCoins,
        description = "Mobile Recharge ৳$amountBdt to $operator ($mobileNumber)"
      )
    )
    return true
  }

  // Messaging & Unlocking
  fun unlockInbox(conversationId: String, cost: Long): Boolean {
    val user = _currentUser.value ?: return false
    if (user.paidCoins < cost) return false

    _currentUser.value = user.copy(paidCoins = user.paidCoins - cost)

    _conversations.value = _conversations.value.map {
      if (it.id == conversationId) it.copy(isUnlocked = true) else it
    }

    addTransaction(
      CoinTransaction(
        id = UUID.randomUUID().toString(),
        coinType = CoinType.PAID,
        type = TransactionType.GIFT_SENT,
        amount = -cost,
        description = "Unlocked inbox conversation ($cost Paid Coins)"
      )
    )
    return true
  }

  fun sendMessage(conversationId: String, text: String) {
    val user = _currentUser.value ?: return
    val msg = Message(
      id = UUID.randomUUID().toString(),
      conversationId = conversationId,
      senderId = user.id,
      senderName = user.name,
      text = text
    )

    val current = _messages.value[conversationId] ?: emptyList()
    _messages.value = _messages.value + (conversationId to (current + msg))

    _conversations.value = _conversations.value.map {
      if (it.id == conversationId) {
        val newReplies = it.replyCountFromRecipient + 1
        val audioEnabled = newReplies >= 10
        it.copy(
          lastMessage = text,
          lastTimestamp = System.currentTimeMillis(),
          replyCountFromRecipient = newReplies,
          isAudioCallEnabled = audioEnabled
        )
      } else it
    }
  }

  // Referral code claim from Invite & Earn section
  fun claimReferralCode(code: String): Boolean {
    val user = _currentUser.value ?: return false
    if (user.referredBy != null) return false
    if (code.equals(user.referralCode, ignoreCase = true)) return false // No self-referral

    _currentUser.value = user.copy(
      referredBy = code,
      freeCoins = user.freeCoins + 500,
      paidCoins = user.paidCoins + 10
    )

    addTransaction(
      CoinTransaction(
        id = UUID.randomUUID().toString(),
        coinType = CoinType.FREE,
        type = TransactionType.REFERRAL_BONUS,
        amount = 500,
        description = "Claimed Referral Code: $code"
      )
    )
    addTransaction(
      CoinTransaction(
        id = UUID.randomUUID().toString(),
        coinType = CoinType.PAID,
        type = TransactionType.REFERRAL_BONUS,
        amount = 10,
        description = "Claimed Referral: 10 Paid Coins"
      )
    )
    return true
  }

  // Feedback Submission
  fun submitFeedback(title: String, description: String, category: String) {
    val user = _currentUser.value ?: return
    val report = FeedbackReport(
      id = "fb-" + UUID.randomUUID().toString().take(6),
      userId = user.id,
      userName = user.name,
      title = title,
      description = description,
      category = category
    )
    _feedbackReports.value = listOf(report) + _feedbackReports.value
  }

  // Admin Features
  fun adminAdjustCoins(userId: String, freeDelta: Long, paidDelta: Long, reason: String) {
    _allUsers.value = _allUsers.value.map {
      if (it.id == userId) {
        it.copy(
          freeCoins = (it.freeCoins + freeDelta).coerceAtLeast(0),
          paidCoins = (it.paidCoins + paidDelta).coerceAtLeast(0)
        )
      } else it
    }
    if (_currentUser.value?.id == userId) {
      _currentUser.value = _currentUser.value?.copy(
        freeCoins = (_currentUser.value!!.freeCoins + freeDelta).coerceAtLeast(0),
        paidCoins = (_currentUser.value!!.paidCoins + paidDelta).coerceAtLeast(0)
      )
    }
  }

  fun adminVerifyUser(userId: String) {
    _allUsers.value = _allUsers.value.map {
      if (it.id == userId) it.copy(isVerified = true) else it
    }
    if (_currentUser.value?.id == userId) {
      _currentUser.value = _currentUser.value?.copy(isVerified = true)
    }
  }

  fun adminToggleBanUser(userId: String) {
    _allUsers.value = _allUsers.value.map {
      if (it.id == userId) it.copy(isBlocked = !it.isBlocked) else it
    }
  }

  fun adminApproveWithdrawal(reqId: String) {
    _withdrawals.value = _withdrawals.value.map {
      if (it.id == reqId) it.copy(status = "Approved") else it
    }
  }

  fun adminBroadcastNotification(title: String, message: String) {
    val notif = NotificationItem(
      id = UUID.randomUUID().toString(),
      title = title,
      message = message,
      type = NotificationType.ANNOUNCEMENT
    )
    _notifications.value = listOf(notif) + _notifications.value
  }

  fun markAllNotificationsRead() {
    _notifications.value = _notifications.value.map { it.copy(isRead = true) }
  }

  private fun addTransaction(txn: CoinTransaction) {
    _transactions.value = listOf(txn) + _transactions.value
  }

  private fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
}
