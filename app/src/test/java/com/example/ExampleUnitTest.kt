package com.example

import com.example.data.repository.MoodbookRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun `verify coin logic in repository`() {
    val repo = MoodbookRepository()
    val initialFree = repo.currentUser.value?.freeCoins ?: 0L

    // Liking a post awards +5 Free Coins
    val firstPost = repo.posts.value.first { it.authorId != repo.currentUser.value?.id }
    repo.likePost(firstPost.id)
    assertEquals(initialFree + 5L, repo.currentUser.value?.freeCoins)

    // Daily check-in awards +50 Free Coins
    val beforeCheckin = repo.currentUser.value?.freeCoins ?: 0L
    val successCheckin = repo.performDailyCheckin()
    assertTrue(successCheckin)
    assertEquals(beforeCheckin + 50L, repo.currentUser.value?.freeCoins)
  }

  @Test
  fun `verify coin exchange logic`() {
    val repo = MoodbookRepository()
    val initialPaid = repo.currentUser.value?.paidCoins ?: 0L
    val initialFree = repo.currentUser.value?.freeCoins ?: 0L

    // Exchange 10 Paid Coins -> 100 Free Coins
    val ok = repo.exchangePaidForFree(10)
    assertTrue(ok)
    assertEquals(initialPaid - 10L, repo.currentUser.value?.paidCoins)
    assertEquals(initialFree + 100L, repo.currentUser.value?.freeCoins)
  }
}
