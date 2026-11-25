package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.*
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.auth.AuthScreen
import com.example.ui.screens.coin.CoinCenterScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.inbox.ChatDetailScreen
import com.example.ui.screens.inbox.InboxScreen
import com.example.ui.screens.other.*
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.referral.ReferralScreen
import com.example.ui.screens.wallet.WalletScreen
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun MoodbookApp(
  viewModel: MoodbookViewModel = viewModel()
) {
  val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
  val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()
  val activeSubScreen by viewModel.activeSubScreen.collectAsStateWithLifecycle()
  val activeConversationId by viewModel.activeConversationId.collectAsStateWithLifecycle()
  val unreadNotificationsCount by viewModel.unreadNotificationsCount.collectAsStateWithLifecycle()
  val snackMessage by viewModel.snackMessage.collectAsStateWithLifecycle()
  val showCreatePostSheet by viewModel.showCreatePostSheet.collectAsStateWithLifecycle()
  val giftPostId by viewModel.giftPostId.collectAsStateWithLifecycle()
  val showAdViewer by viewModel.showAdViewer.collectAsStateWithLifecycle()
  val adCountdown by viewModel.adCountdown.collectAsStateWithLifecycle()
  val isAdRewardReady by viewModel.isAdRewardReady.collectAsStateWithLifecycle()
  val showAudioCallModal by viewModel.showAudioCallModal.collectAsStateWithLifecycle()
  val callDurationSeconds by viewModel.callDurationSeconds.collectAsStateWithLifecycle()

  val posts by viewModel.posts.collectAsStateWithLifecycle()
  val comments by viewModel.comments.collectAsStateWithLifecycle()
  val transactions by viewModel.transactions.collectAsStateWithLifecycle()
  val conversations by viewModel.conversations.collectAsStateWithLifecycle()
  val messagesMap by viewModel.messages.collectAsStateWithLifecycle()
  val notifications by viewModel.notifications.collectAsStateWithLifecycle()
  val withdrawals by viewModel.withdrawals.collectAsStateWithLifecycle()
  val feedbackReports by viewModel.feedbackReports.collectAsStateWithLifecycle()
  val paidAdProgress by viewModel.paidAdProgress.collectAsStateWithLifecycle()
  val freeAdClaims by viewModel.freeAdClaims.collectAsStateWithLifecycle()
  val allUsers by viewModel.allUsers.collectAsStateWithLifecycle()
  val giftCatalog = viewModel.giftCatalog
  val lang by viewModel.currentLanguage.collectAsStateWithLifecycle()
  val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()

  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val scope = rememberCoroutineScope()
  val snackbarHostState = remember { SnackbarHostState() }

  // Show snackbar message when snackMessage updates
  LaunchedEffect(snackMessage) {
    snackMessage?.let { msg ->
      snackbarHostState.showSnackbar(msg)
      viewModel.clearMessage()
    }
  }

  // Handle Android back button
  BackHandler(enabled = activeSubScreen != SubScreen.NONE) {
    viewModel.goBack()
  }

  MoodbookTheme(darkTheme = isDarkMode) {
    if (currentUser == null) {
      AuthScreen(
        lang = lang,
        onLogin = { email, pass ->
          viewModel.repository.login(email, pass)
          viewModel.showMessage(if (lang == "bn") "স্বাগতম মুডবুকে!" else "Welcome to Moodbook!")
        },
        onRegister = { name, email, birth, gender, referral ->
          viewModel.repository.register(name, email, birth, gender, referral)
          viewModel.showMessage(if (lang == "bn") "অ্যাকাউন্ট সফলভাবে তৈরি হয়েছে!" else "Account registered successfully!")
        }
      )
    } else {
      ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
          ModalDrawerSheet(
            drawerContainerColor = MaterialTheme.colorScheme.surface,
            drawerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
          ) {
            Column(
              modifier = Modifier
                .width(280.dp)
                .fillMaxHeight()
                .padding(20.dp)
            ) {
              // Drawer User Card
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(SageGreenPrimary),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = currentUser?.name?.take(1)?.uppercase() ?: "M",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  Text(text = currentUser?.name ?: "", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                  Text(text = "@${currentUser?.username}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
              }

              Spacer(modifier = Modifier.height(16.dp))

              // Balances Quick Glance
              Surface(
                color = SageGreenContainer,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(
                  modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceAround
                ) {
                  Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🌿 Free", fontSize = 11.sp, color = SageGreenDark, fontWeight = FontWeight.SemiBold)
                    Text("${currentUser?.freeCoins}", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                  }
                  Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("💰 Paid", fontSize = 11.sp, color = Color(0xFF9E6500), fontWeight = FontWeight.SemiBold)
                    Text("${currentUser?.paidCoins}", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                  }
                }
              }

              Spacer(modifier = Modifier.height(20.dp))
              Divider(color = MaterialTheme.colorScheme.surfaceVariant)
              Spacer(modifier = Modifier.height(12.dp))

              // Drawer Nav Items
              DrawerItem(
                icon = Icons.Default.MonetizationOn,
                label = if (lang == "bn") "পেইড কয়েন আয় (Rewarded Ads)" else "Earn Paid Coins",
                onClick = {
                  scope.launch { drawerState.close() }
                  viewModel.startRewardedAd()
                }
              )
              DrawerItem(
                icon = Icons.Default.AccountBalanceWallet,
                label = if (lang == "bn") "ওয়ালেট ও উত্তোলন (Wallet)" else "Wallet & BDT",
                onClick = {
                  scope.launch { drawerState.close() }
                  viewModel.navigateTo(SubScreen.WALLET)
                }
              )
              DrawerItem(
                icon = Icons.Default.CardGiftcard,
                label = if (lang == "bn") "রেফারেল ও উপহার (Invite & Earn)" else "Invite & Earn",
                onClick = {
                  scope.launch { drawerState.close() }
                  viewModel.navigateTo(SubScreen.REFERRAL)
                }
              )
              DrawerItem(
                icon = Icons.Default.Feedback,
                label = if (lang == "bn") "ফিডব্যাক ও রিপোর্ট" else "Give Feedback",
                onClick = {
                  scope.launch { drawerState.close() }
                  viewModel.navigateTo(SubScreen.FEEDBACK)
                }
              )
              DrawerItem(
                icon = Icons.Default.Info,
                label = if (lang == "bn") "মুডবুক পরিচিতি" else "About Moodbook",
                onClick = {
                  scope.launch { drawerState.close() }
                  viewModel.navigateTo(SubScreen.ABOUT)
                }
              )

              if (currentUser?.isAdmin == true) {
                DrawerItem(
                  icon = Icons.Default.AdminPanelSettings,
                  label = if (lang == "bn") "অ্যাডমিন ড্যাশবোর্ড" else "Admin Dashboard",
                  color = GoldPaidCoin,
                  onClick = {
                    scope.launch { drawerState.close() }
                    viewModel.navigateTo(SubScreen.ADMIN_PANEL)
                  }
                )
              }

              Spacer(modifier = Modifier.weight(1f))

              Button(
                onClick = {
                  scope.launch { drawerState.close() }
                  viewModel.repository.logout()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                modifier = Modifier.fillMaxWidth()
              ) {
                Text(if (lang == "bn") "লগআউট" else "Logout")
              }
            }
          }
        }
      ) {
        Scaffold(
          snackbarHost = { SnackbarHost(snackbarHostState) },
          topBar = {
            if (activeSubScreen == SubScreen.NONE) {
              MoodbookTopBar(
                unreadCount = unreadNotificationsCount,
                onMenuClick = { scope.launch { drawerState.open() } },
                onPlusClick = { viewModel.openCreatePostSheet() },
                onNotificationClick = { viewModel.navigateTo(SubScreen.NOTIFICATIONS) },
                onSearchClick = { viewModel.navigateTo(SubScreen.SEARCH) }
              )
            }
          },
          bottomBar = {
            if (activeSubScreen == SubScreen.NONE) {
              MoodbookBottomNav(
                currentTab = activeTab,
                onTabSelected = { viewModel.selectTab(it) },
                lang = lang
              )
            }
          }
        ) { paddingValues ->
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(paddingValues)
          ) {
            // Main sub-screen routing or Tab routing
            when (activeSubScreen) {
              SubScreen.NOTIFICATIONS -> {
                NotificationScreen(
                  notifications = notifications,
                  lang = lang,
                  onBack = { viewModel.goBack() },
                  onMarkAllRead = { viewModel.repository.markAllNotificationsRead() }
                )
              }
              SubScreen.SEARCH -> {
                SearchScreen(
                  allUsers = allUsers,
                  posts = posts,
                  lang = lang,
                  onBack = { viewModel.goBack() },
                  onUserClick = {}
                )
              }
              SubScreen.WALLET -> {
                WalletScreen(
                  user = currentUser,
                  transactions = transactions,
                  lang = lang,
                  onBack = { viewModel.goBack() },
                  onVerifyUser = {
                    viewModel.repository.verifyCurrentUser()
                    viewModel.showMessage(if (lang == "bn") "অ্যাকাউন্ট ভেরিফাইড হয়েছে! ✅" else "Account Verified! ✅")
                  },
                  onDeposit = { bdt, method, wallet, trx ->
                    val ok = viewModel.repository.depositBuyCoins(bdt, method, wallet, trx)
                    if (ok) viewModel.showMessage(if (lang == "bn") "পেইড কয়েন কেনা সম্পন্ন হয়েছে!" else "Coins purchased successfully!")
                  },
                  onWithdraw = { method, wallet, coins ->
                    val ok = viewModel.repository.requestWithdrawal(method, wallet, coins)
                    if (ok) viewModel.showMessage(if (lang == "bn") "উত্তোলন রিকোয়েস্ট সফলভাবে দাখিল হয়েছে!" else "Withdrawal submitted!")
                  },
                  onMobileRecharge = { operator, number, bdt ->
                    val ok = viewModel.repository.requestMobileRecharge(operator, number, bdt)
                    if (ok) viewModel.showMessage(if (lang == "bn") "মোবাইল রিচার্জ সফল হয়েছে! ৳$bdt" else "Mobile Recharge success! ৳$bdt")
                  }
                )
              }
              SubScreen.CHAT_DETAIL -> {
                val conv = conversations.find { it.id == activeConversationId }
                val messages = messagesMap[activeConversationId] ?: emptyList()
                ChatDetailScreen(
                  conversation = conv,
                  messages = messages,
                  currentUser = currentUser,
                  lang = lang,
                  onBack = { viewModel.goBack() },
                  onSendMessage = { text ->
                    activeConversationId?.let { cid -> viewModel.repository.sendMessage(cid, text) }
                  },
                  onStartAudioCall = { viewModel.startAudioCall() },
                  onUnlockInbox = { cost ->
                    activeConversationId?.let { cid ->
                      val ok = viewModel.repository.unlockInbox(cid, cost)
                      if (ok) viewModel.showMessage(if (lang == "bn") "ইনবক্স আনলক হয়েছে!" else "Inbox unlocked!")
                    }
                  }
                )
              }
              SubScreen.ADMIN_PANEL -> {
                AdminDashboardScreen(
                  currentUser = currentUser,
                  allUsers = allUsers,
                  withdrawals = withdrawals,
                  totalPostsCount = posts.size,
                  lang = lang,
                  onBack = { viewModel.goBack() },
                  onVerifyUser = { uid -> viewModel.repository.adminVerifyUser(uid) },
                  onToggleBanUser = { uid -> viewModel.repository.adminToggleBanUser(uid) },
                  onAdjustCoins = { uid, free, paid, reason ->
                    viewModel.repository.adminAdjustCoins(uid, free, paid, reason)
                    viewModel.showMessage("Coins updated for user.")
                  },
                  onApproveWithdrawal = { wid ->
                    viewModel.repository.adminApproveWithdrawal(wid)
                    viewModel.showMessage("Withdrawal approved!")
                  },
                  onBroadcastAnnouncement = { title, msg ->
                    viewModel.repository.adminBroadcastNotification(title, msg)
                    viewModel.showMessage("Announcement broadcasted!")
                  }
                )
              }
              SubScreen.REFERRAL -> {
                ReferralScreen(
                  user = currentUser,
                  lang = lang,
                  onBack = { viewModel.goBack() },
                  onClaimReferralCode = { code ->
                    val ok = viewModel.repository.claimReferralCode(code)
                    if (ok) viewModel.showMessage(if (lang == "bn") "অভিনন্দন! ৫০০ ফ্রি + ১০ পেইড কয়েন যোগ হয়েছে!" else "Claimed 500 Free + 10 Paid Coins!")
                    else viewModel.showMessage(if (lang == "bn") "রেফারেল কোড ব্যর্থ হয়েছে" else "Invalid referral code")
                  },
                  onShowMessage = { viewModel.showMessage(it) }
                )
              }
              SubScreen.ABOUT -> {
                AboutScreen(
                  lang = lang,
                  onBack = { viewModel.goBack() }
                )
              }
              SubScreen.FEEDBACK -> {
                FeedbackScreen(
                  feedbackList = feedbackReports,
                  lang = lang,
                  onBack = { viewModel.goBack() },
                  onSubmitFeedback = { title, desc, cat ->
                    viewModel.repository.submitFeedback(title, desc, cat)
                    viewModel.showMessage(if (lang == "bn") "রিপোর্ট দাখিল হয়েছে!" else "Feedback submitted successfully!")
                  }
                )
              }
              SubScreen.NONE -> {
                when (activeTab) {
                  MainTab.HOME -> {
                    HomeScreen(
                      posts = posts,
                      commentsMap = comments,
                      lang = lang,
                      onLikeClick = { pid ->
                        viewModel.repository.likePost(pid)
                        viewModel.showMessage(if (lang == "bn") "+৫ ফ্রি কয়েন আয় হয়েছে! 👍" else "+5 Free Coins earned! 👍")
                      },
                      onCommentSubmit = { pid, text ->
                        viewModel.repository.addComment(pid, text)
                        viewModel.showMessage(if (lang == "bn") "+২০ ফ্রি কয়েন আয় হয়েছে! 💬" else "+20 Free Coins earned! 💬")
                      },
                      onShareClick = { pid ->
                        viewModel.repository.sharePost(pid)
                        viewModel.showMessage(if (lang == "bn") "+২০ ফ্রি কয়েন আয় হয়েছে! 🚀" else "+20 Free Coins earned! 🚀")
                      },
                      onGiftClick = { pid -> viewModel.openGiftDialog(pid) },
                      onProfileClick = { _ -> viewModel.selectTab(MainTab.PROFILE) },
                      onReportClick = { _ -> viewModel.showMessage(if (lang == "bn") "পোস্টটি রিভিউয়ের জন্য জমা দেওয়া হয়েছে।" else "Post reported for review.") }
                    )
                  }
                  MainTab.INBOX -> {
                    InboxScreen(
                      conversations = conversations,
                      lang = lang,
                      onOpenConversation = { cid -> viewModel.openChat(cid) }
                    )
                  }
                  MainTab.COIN_CENTER -> {
                    CoinCenterScreen(
                      user = currentUser,
                      paidAdProgress = paidAdProgress,
                      freeAdClaims = freeAdClaims,
                      lang = lang,
                      onDailyCheckin = {
                        val ok = viewModel.repository.performDailyCheckin()
                        if (ok) viewModel.showMessage(if (lang == "bn") "+৫০ ফ্রি কয়েন পেয়েছেন! 📅" else "+50 Free Coins claimed! 📅")
                        else viewModel.showMessage(if (lang == "bn") "আজকের চেক-ইন ইতোমধ্যে করা হয়েছে।" else "Already checked in today.")
                      },
                      onWatchPaidAd = { viewModel.startRewardedAd() },
                      onClaimFreeAdPackage = { pkg ->
                        val ok = viewModel.repository.claimFreeAdPackage(pkg)
                        if (ok) viewModel.showMessage(if (lang == "bn") "প্যাকেজ রিওয়ার্ড যোগ হয়েছে!" else "Free coin ad package claimed!")
                      },
                      onExchangePaidCoins = { paidAmount ->
                        val ok = viewModel.repository.exchangePaidForFree(paidAmount)
                        if (ok) viewModel.showMessage(if (lang == "bn") "কয়েন রূপান্তর সফল হয়েছে!" else "Exchanged to Free Coins!")
                      },
                      onOpenWallet = { viewModel.navigateTo(SubScreen.WALLET) }
                    )
                  }
                  MainTab.PROFILE -> {
                    val myPosts = posts.filter { it.authorId == currentUser?.id }
                    ProfileScreen(
                      user = currentUser,
                      userPosts = myPosts,
                      lang = lang,
                      onOpenWallet = { viewModel.navigateTo(SubScreen.WALLET) },
                      onOpenCreatePost = { viewModel.openCreatePostSheet() },
                      onUpdateProfile = { n, b, a ->
                        viewModel.repository.updateProfile(n, b, a)
                        viewModel.showMessage(if (lang == "bn") "প্রোফাইল আপডেট হয়েছে!" else "Profile updated!")
                      },
                      onLikeClick = { pid -> viewModel.repository.likePost(pid) },
                      onShareClick = { pid -> viewModel.repository.sharePost(pid) },
                      onGiftClick = { pid -> viewModel.openGiftDialog(pid) }
                    )
                  }
                  MainTab.SETTINGS -> {
                    SettingsScreen(
                      currentUser = currentUser,
                      isDarkMode = isDarkMode,
                      lang = lang,
                      onToggleDarkMode = { viewModel.repository.toggleDarkMode() },
                      onToggleLanguage = { viewModel.toggleLanguage() },
                      onNavigateToReferral = { viewModel.navigateTo(SubScreen.REFERRAL) },
                      onNavigateToAbout = { viewModel.navigateTo(SubScreen.ABOUT) },
                      onNavigateToFeedback = { viewModel.navigateTo(SubScreen.FEEDBACK) },
                      onNavigateToAdmin = { viewModel.navigateTo(SubScreen.ADMIN_PANEL) },
                      onLogout = { viewModel.repository.logout() }
                    )
                  }
                }
              }
            }
          }
        }
      }

      // Create Post Bottom Sheet
      if (showCreatePostSheet) {
        CreatePostSheet(
          userFreeCoins = currentUser?.freeCoins ?: 0,
          lang = lang,
          onDismiss = { viewModel.closeCreatePostSheet() },
          onSubmit = { content, img, vid, isFlower, boostType ->
            val ok = viewModel.repository.createPost(content, img, vid, isFlower, boostType)
            if (ok) {
              viewModel.showMessage(
                if (boostType == "SUPER") (if (lang == "bn") "পোস্ট সুপার বুস্ট হয়েছে! 🚀" else "Post Super Boosted! 🚀")
                else if (boostType == "BOOST") (if (lang == "bn") "পোস্ট বুস্ট হয়েছে! ⚡" else "Post Boosted! ⚡")
                else (if (lang == "bn") "পোস্ট প্রকাশিত হয়েছে!" else "Post published!")
              )
            } else {
              viewModel.showMessage(if (lang == "bn") "পর্যাপ্ত ফ্রি কয়েন নেই!" else "Insufficient Free Coins!")
            }
          }
        )
      }

      // Gift Dialog
      giftPostId?.let { pid ->
        GiftDialog(
          giftCatalog = giftCatalog,
          userFreeCoins = currentUser?.freeCoins ?: 0,
          userPaidCoins = currentUser?.paidCoins ?: 0,
          lang = lang,
          onDismiss = { viewModel.closeGiftDialog() },
          onSendGift = { gift ->
            val ok = viewModel.repository.sendGift(pid, gift)
            if (ok) viewModel.showMessage(if (lang == "bn") "${gift.name} উপহার পাঠানো হয়েছে! ${gift.emoji}" else "Gift ${gift.name} ${gift.emoji} sent!")
          }
        )
      }

      // Rewarded Ad Simulation Modal
      if (showAdViewer) {
        AdViewerModal(
          countdown = adCountdown,
          isReady = isAdRewardReady,
          lang = lang,
          onClaim = { viewModel.claimWatchedAdReward() },
          onClose = { viewModel.closeAdViewer() }
        )
      }

      // Audio Call Modal
      if (showAudioCallModal) {
        val conv = conversations.find { it.id == activeConversationId }
        AudioCallModal(
          participantName = conv?.participantName ?: "Moodbook User",
          callDuration = callDurationSeconds,
          lang = lang,
          onEndCall = { viewModel.endAudioCall() }
        )
      }
    }
  }
}

@Composable
private fun DrawerItem(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  label: String,
  color: Color = Color.Unspecified,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .clickable { onClick() }
      .padding(horizontal = 12.dp, vertical = 10.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      icon,
      contentDescription = null,
      tint = if (color != Color.Unspecified) color else SageGreenPrimary,
      modifier = Modifier.size(22.dp)
    )
    Spacer(modifier = Modifier.width(12.dp))
    Text(
      text = label,
      fontSize = 13.sp,
      fontWeight = FontWeight.Medium,
      color = if (color != Color.Unspecified) color else MaterialTheme.colorScheme.onSurface
    )
  }
}
