package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.MoodbookRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class MainTab {
  HOME, INBOX, COIN_CENTER, PROFILE, SETTINGS
}

enum class SubScreen {
  NONE, NOTIFICATIONS, SEARCH, WALLET, CHAT_DETAIL, ADMIN_PANEL, REFERRAL, ABOUT, FEEDBACK
}

class MoodbookViewModel(
  val repository: MoodbookRepository = MoodbookRepository()
) : ViewModel() {

  // Current Main Tab
  private val _activeTab = MutableStateFlow(MainTab.HOME)
  val activeTab: StateFlow<MainTab> = _activeTab.asStateFlow()

  // Current Sub-Screen (BackHandler takes back to NONE)
  private val _activeSubScreen = MutableStateFlow(SubScreen.NONE)
  val activeSubScreen: StateFlow<SubScreen> = _activeSubScreen.asStateFlow()

  // Active chat conversation ID if inside CHAT_DETAIL
  private val _activeConversationId = MutableStateFlow<String?>(null)
  val activeConversationId: StateFlow<String?> = _activeConversationId.asStateFlow()

  // Active post for Gift Dialog
  private val _giftPostId = MutableStateFlow<String?>(null)
  val giftPostId: StateFlow<String?> = _giftPostId.asStateFlow()

  // Show Create Post Sheet
  private val _showCreatePostSheet = MutableStateFlow(false)
  val showCreatePostSheet: StateFlow<Boolean> = _showCreatePostSheet.asStateFlow()

  // Show Rewarded Ad simulation modal
  private val _showAdViewer = MutableStateFlow(false)
  val showAdViewer: StateFlow<Boolean> = _showAdViewer.asStateFlow()
  private val _adCountdown = MutableStateFlow(5)
  val adCountdown: StateFlow<Int> = _adCountdown.asStateFlow()
  private val _isAdRewardReady = MutableStateFlow(false)
  val isAdRewardReady: StateFlow<Boolean> = _isAdRewardReady.asStateFlow()

  // Audio Call Simulation Modal
  private val _showAudioCallModal = MutableStateFlow(false)
  val showAudioCallModal: StateFlow<Boolean> = _showAudioCallModal.asStateFlow()
  private val _callDurationSeconds = MutableStateFlow(0)
  val callDurationSeconds: StateFlow<Int> = _callDurationSeconds.asStateFlow()

  // Toast / Snackbar notification message
  private val _snackMessage = MutableStateFlow<String?>(null)
  val snackMessage: StateFlow<String?> = _snackMessage.asStateFlow()

  // Repository state exposures
  val currentUser = repository.currentUser
  val currentLanguage = repository.currentLanguage
  val isDarkMode = repository.isDarkMode
  val posts = repository.posts
  val comments = repository.comments
  val transactions = repository.transactions
  val conversations = repository.conversations
  val messages = repository.messages
  val notifications = repository.notifications
  val withdrawals = repository.withdrawals
  val mobileRecharges = repository.mobileRecharges
  val feedbackReports = repository.feedbackReports
  val paidAdProgress = repository.paidAdProgress
  val freeAdClaims = repository.freeAdClaims
  val allUsers = repository.allUsers
  val giftCatalog = repository.giftCatalog

  val unreadNotificationsCount: StateFlow<Int> = notifications.map { list ->
    list.count { !it.isRead }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

  fun selectTab(tab: MainTab) {
    _activeTab.value = tab
    _activeSubScreen.value = SubScreen.NONE
  }

  fun navigateTo(subScreen: SubScreen) {
    _activeSubScreen.value = subScreen
  }

  fun openChat(conversationId: String) {
    _activeConversationId.value = conversationId
    _activeSubScreen.value = SubScreen.CHAT_DETAIL
  }

  fun goBack(): Boolean {
    if (_activeSubScreen.value != SubScreen.NONE) {
      _activeSubScreen.value = SubScreen.NONE
      _activeConversationId.value = null
      return true
    }
    return false
  }

  fun openGiftDialog(postId: String) {
    _giftPostId.value = postId
  }

  fun closeGiftDialog() {
    _giftPostId.value = null
  }

  fun openCreatePostSheet() {
    _showCreatePostSheet.value = true
  }

  fun closeCreatePostSheet() {
    _showCreatePostSheet.value = false
  }

  fun showMessage(msg: String) {
    _snackMessage.value = msg
  }

  fun clearMessage() {
    _snackMessage.value = null
  }

  fun startRewardedAd() {
    _showAdViewer.value = true
    _adCountdown.value = 5
    _isAdRewardReady.value = false

    viewModelScope.launch {
      for (i in 5 downTo 1) {
        _adCountdown.value = i
        delay(1000)
      }
      _adCountdown.value = 0
      _isAdRewardReady.value = true
    }
  }

  fun claimWatchedAdReward() {
    _showAdViewer.value = false
    _isAdRewardReady.value = false
    val success = repository.completeRewardedAd()
    if (success) {
      showMessage(
        if (currentLanguage.value == "bn") "বিজ্ঞাপন সম্পন্ন! অগ্রগতির হিসেব আপডেট হয়েছে।"
        else "Ad completed! Task progress updated."
      )
    }
  }

  fun closeAdViewer() {
    _showAdViewer.value = false
    _isAdRewardReady.value = false
  }

  fun startAudioCall() {
    _showAudioCallModal.value = true
    _callDurationSeconds.value = 0
    viewModelScope.launch {
      while (_showAudioCallModal.value) {
        delay(1000)
        _callDurationSeconds.value += 1
      }
    }
  }

  fun endAudioCall() {
    _showAudioCallModal.value = false
    _callDurationSeconds.value = 0
    showMessage(
      if (currentLanguage.value == "bn") "অডিও কল শেষ হয়েছে"
      else "Audio call ended"
    )
  }

  fun toggleLanguage() {
    val newLang = if (currentLanguage.value == "bn") "en" else "bn"
    repository.setLanguage(newLang)
  }
}
