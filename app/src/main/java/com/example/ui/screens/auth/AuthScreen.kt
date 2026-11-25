package com.example.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun AuthScreen(
  lang: String,
  onLogin: (email: String, pass: String) -> Unit,
  onRegister: (name: String, email: String, birthDate: String, gender: String, referral: String?) -> Unit,
  modifier: Modifier = Modifier
) {
  var isRegistering by remember { mutableStateOf(false) }

  // Login Fields
  var loginEmail by remember { mutableStateOf("islamrakib8522@gmail.com") }
  var loginPassword by remember { mutableStateOf("password123") }
  var showPassword by remember { mutableStateOf(false) }
  var rememberMe by remember { mutableStateOf(true) }

  // Register Fields
  var regName by remember { mutableStateOf("") }
  var regEmail by remember { mutableStateOf("") }
  var regPassword by remember { mutableStateOf("") }
  var regConfirmPassword by remember { mutableStateOf("") }
  var regBirthDate by remember { mutableStateOf("2000-01-01") }
  var regGender by remember { mutableStateOf("Male") }
  var regReferralCode by remember { mutableStateOf("") }

  var showForgotPasswordDialog by remember { mutableStateOf(false) }
  var forgotEmail by remember { mutableStateOf("") }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          listOf(SageBarColor, MaterialTheme.colorScheme.background)
        )
      )
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      contentPadding = PaddingValues(top = 40.dp, bottom = 40.dp)
    ) {
      // Moodbook Brand Logo & Tagline
      item {
        Box(
          modifier = Modifier
            .size(76.dp)
            .clip(CircleShape)
            .background(Color.White)
            .border(3.dp, SageGreenDark, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text("🌿", fontSize = 38.sp)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = "moodbook",
          fontSize = 32.sp,
          fontWeight = FontWeight.Black,
          color = Color(0xFF14291E),
          letterSpacing = (-1).sp
        )
        Text(
          text = if (lang == "bn") "কানেক্ট • শেয়ার • আর্ন • এনজয়" else "Connect. Share. Earn. Enjoy.",
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          color = Color(0xFF264733)
        )
        Spacer(modifier = Modifier.height(24.dp))
      }

      // Card Container
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(20.dp)) {
            // Tab Switcher (Login / Register)
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(4.dp)
            ) {
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(10.dp))
                  .background(if (!isRegistering) SageGreenPrimary else Color.Transparent)
                  .clickable { isRegistering = false }
                  .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = if (lang == "bn") "লগইন" else "Login",
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = if (!isRegistering) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(10.dp))
                  .background(if (isRegistering) SageGreenPrimary else Color.Transparent)
                  .clickable { isRegistering = true }
                  .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = if (lang == "bn") "নতুন অ্যাকাউন্ট" else "Register",
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = if (isRegistering) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Spacer(modifier = Modifier.height(18.dp))

            if (!isRegistering) {
              // LOGIN FORM
              OutlinedTextField(
                value = loginEmail,
                onValueChange = { loginEmail = it },
                label = { Text(if (lang == "bn") "ইমেইল এড্রেস" else "Email Address") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("login_email_input")
              )

              Spacer(modifier = Modifier.height(12.dp))

              OutlinedTextField(
                value = loginPassword,
                onValueChange = { loginPassword = it },
                label = { Text(if (lang == "bn") "পাসওয়ার্ড" else "Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                  IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                      imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                      contentDescription = "Toggle Password"
                    )
                  }
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("login_password_input")
              )

              Spacer(modifier = Modifier.height(8.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it }
                  )
                  Text(
                    text = if (lang == "bn") "মনে রাখুন" else "Remember me",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
                TextButton(onClick = { showForgotPasswordDialog = true }) {
                  Text(
                    text = if (lang == "bn") "পাসওয়ার্ড ভুলে গেছেন?" else "Forgot Password?",
                    fontSize = 12.sp,
                    color = SageGreenPrimary
                  )
                }
              }

              Spacer(modifier = Modifier.height(14.dp))

              Button(
                onClick = { onLogin(loginEmail, loginPassword) },
                colors = ButtonDefaults.buttonColors(containerColor = SageGreenPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp)
                  .testTag("login_submit_button")
              ) {
                Text(
                  text = if (lang == "bn") "লগইন করুন" else "Sign In",
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp
                )
              }
            } else {
              // REGISTRATION FORM
              OutlinedTextField(
                value = regName,
                onValueChange = { regName = it },
                label = { Text(if (lang == "bn") "পূর্ণ নাম" else "Full Name") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("reg_name_input")
              )

              Spacer(modifier = Modifier.height(8.dp))

              OutlinedTextField(
                value = regEmail,
                onValueChange = { regEmail = it },
                label = { Text(if (lang == "bn") "ইমেইল এড্রেস" else "Email Address") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("reg_email_input")
              )

              Spacer(modifier = Modifier.height(8.dp))

              OutlinedTextField(
                value = regPassword,
                onValueChange = { regPassword = it },
                label = { Text(if (lang == "bn") "পাসওয়ার্ড" else "Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
              )

              Spacer(modifier = Modifier.height(8.dp))

              OutlinedTextField(
                value = regBirthDate,
                onValueChange = { regBirthDate = it },
                label = { Text(if (lang == "bn") "জন্ম তারিখ (YYYY-MM-DD)" else "Date of Birth") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
              )

              Spacer(modifier = Modifier.height(8.dp))

              // Gender selection chips
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                listOf("Male", "Female", "Other").forEach { g ->
                  FilterChip(
                    selected = regGender == g,
                    onClick = { regGender = g },
                    label = { Text(g) }
                  )
                }
              }

              Spacer(modifier = Modifier.height(8.dp))

              // Optional Referral Code
              OutlinedTextField(
                value = regReferralCode,
                onValueChange = { regReferralCode = it.uppercase() },
                label = { Text(if (lang == "bn") "রেফারেল কোড (ঐচ্ছিক - দিলে ৫০০ ফ্রি কয়েন)" else "Referral Code (Optional - get 500 Coins)") },
                leadingIcon = { Icon(Icons.Default.CardGiftcard, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("reg_referral_input")
              )

              Spacer(modifier = Modifier.height(14.dp))

              Button(
                onClick = {
                  if (regName.isNotBlank() && regEmail.isNotBlank()) {
                    onRegister(regName, regEmail, regBirthDate, regGender, regReferralCode.ifBlank { null })
                  }
                },
                enabled = regName.isNotBlank() && regEmail.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = SageGreenPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp)
                  .testTag("register_submit_button")
              ) {
                Text(
                  text = if (lang == "bn") "অ্যাকাউন্ট তৈরি করুন" else "Create Account",
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp
                )
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Google Sign-In Button
            OutlinedButton(
              onClick = {
                onLogin("islamrakib8522@gmail.com", "google_pass")
              },
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .testTag("google_signin_button")
            ) {
              Text("G ", fontWeight = FontWeight.Black, color = Color(0xFF4285F4), fontSize = 16.sp)
              Text(
                text = if (lang == "bn") "Google দিয়ে সাইন ইন করুন" else "Continue with Google",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
      }

      // Quick Test Profiles Row
      item {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
          text = if (lang == "bn") "⚡ দ্রুত প্রিভিউ প্রোফাইল নির্বাচন:" else "⚡ Quick Switch / Demo Accounts:",
          fontSize = 12.sp,
          color = Color(0xFF1E3827),
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedButton(
            onClick = {
              onLogin("islamrakib8522@gmail.com", "pass")
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("quick_admin_login")
          ) {
            Text("Admin (Rakib Islam)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }

          OutlinedButton(
            onClick = {
              onLogin("sadia@example.com", "pass")
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("quick_user_login")
          ) {
            Text("User (Sadia Afrin)", fontSize = 11.sp)
          }
        }
      }
    }
  }

  // Forgot password dialog
  if (showForgotPasswordDialog) {
    AlertDialog(
      onDismissRequest = { showForgotPasswordDialog = false },
      title = { Text(if (lang == "bn") "পাসওয়ার্ড রিসেট" else "Reset Password") },
      text = {
        Column {
          Text(
            text = if (lang == "bn") "আপনার নিবন্ধিত ইমেইলে পাসওয়ার্ড রিসেট লিংক পাঠানো হবে।"
            else "A password recovery email link will be sent to your registered email.",
            fontSize = 13.sp
          )
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedTextField(
            value = forgotEmail,
            onValueChange = { forgotEmail = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = { showForgotPasswordDialog = false },
          colors = ButtonDefaults.buttonColors(containerColor = SageGreenPrimary)
        ) {
          Text(if (lang == "bn") "লিংক পাঠান" else "Send Reset Link")
        }
      },
      dismissButton = {
        TextButton(onClick = { showForgotPasswordDialog = false }) {
          Text(if (lang == "bn") "বাতিল" else "Cancel")
        }
      }
    )
  }
}
